package com.infirmary.backend.configuration.impl;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;
import static com.infirmary.backend.shared.utility.FunctionUtil.isValidPassword;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.Exception.UserAlreadyExists;
import com.infirmary.backend.configuration.dto.AdDTO;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.dto.JwtResponse;
import com.infirmary.backend.configuration.dto.LoginRequestDTO;
import com.infirmary.backend.configuration.dto.PasswordChangeDTO;
import com.infirmary.backend.configuration.dto.PatientReqDTO;
import com.infirmary.backend.configuration.jwt.JwtUtils;
import com.infirmary.backend.configuration.model.AD;
import com.infirmary.backend.configuration.model.Conformation;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Location;
import com.infirmary.backend.configuration.model.PasswordChange;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.repository.AdRepository;
import com.infirmary.backend.configuration.repository.ConformationRepository;
import com.infirmary.backend.configuration.repository.DoctorRepository;
import com.infirmary.backend.configuration.repository.LocationRepository;
import com.infirmary.backend.configuration.repository.PasswordChangeRepository;
import com.infirmary.backend.configuration.repository.PatientRepository;
import com.infirmary.backend.configuration.securityimpl.UserDetailsImpl;
import com.infirmary.backend.configuration.service.AuthService;
import com.infirmary.backend.shared.utility.FunctionUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{
    
    private AuthenticationManager authenticationManager;
    private PatientRepository patientRepository;
    private PasswordEncoder encoder;
    private JwtUtils jwtUtils;
    private AdRepository adRepository;
    private DoctorRepository doctorRepository;
    private LocationRepository locationRepository;
    private ConformationRepository conformationRepository;
    private JavaMailSender javaMailSender;
    private PasswordChangeRepository passwordChangeRepository;
    
    @Override
    public ResponseEntity<?> loginServicePat(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.genrateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        if(!roles.get(0).equals("ROLE_PATIENT")) throw new SecurityException("Unauthorized Not Patient");
        if(conformationRepository.existsByPatient_Email(loginRequestDTO.getEmail())) throw new IllegalArgumentException("Please Verify Email");
        return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getUsername(),roles));
    }
    
    @Override
    public ResponseEntity<?> signUpPat(PatientReqDTO patientDTO) throws UserAlreadyExists, IOException, MessagingException {
            if(patientRepository.existsByEmail(patientDTO.getEmail())){
                throw new UserAlreadyExists("User Already Exists");
            }

            if(!patientDTO.getSchool().equals("Guest")){
                if(patientRepository.existsBySapId(patientDTO.getSapID())){
                    throw new UserAlreadyExists("User With SapId Already Exists");
                }
            }

            if(!(isValidPassword(patientDTO.getPassword()))) throw new IllegalArgumentException("Password must satisfy conditions");

            patientDTO.setPassword(encoder.encode(patientDTO.getPassword()));
    
            Patient patient = new Patient(
                patientDTO
            );

            if(patientDTO.getImg() != null){

                byte[] data = Base64.getDecoder().decode(patientDTO.getImg().substring(patientDTO.getImg().indexOf(",")+1));
                
                String type = patientDTO.getImg().split(";")[0].split("/")[1];
                
                String fileName = patientDTO.getEmail().replace(".", "_");
                
                String filePath = "./build/resources/main/static/Profile/"+fileName+"."+type;
                String flpth = "./src/main/resources/static/Profile/"+fileName+"."+type;
                
                try{
                    FileUtils.writeByteArrayToFile(new File(filePath), data);
                    FileUtils.writeByteArrayToFile(new File(flpth), data);
                }catch (Exception e){
                    throw new IOException(e);
                }
                
                patient.setImageUrl("Profile/"+fileName+"."+type);
            }

            patient = patientRepository.save(patient);

            Conformation conformation = new Conformation();
            conformation.setPatient(patient);
            conformation.setTimestamp(System.currentTimeMillis());

            conformation = conformationRepository.save(conformation);

            String toAddress = patient.getEmail();
            String fromAddress = "infirmarytest@gmail.com";
            String senderName = "UPES UHS";
            String subject = "Please verify your registration";
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to verify your registration:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Thank you,<br>"
                    + "UPES UHS.";
            
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            
            content = content.replace("[[name]]", patient.getName());
            String verifyURL = "http://ec2-13-201-227-93.ap-south-1.compute.amazonaws.com/api/auth/user" + "/verify?code=" + conformation.getConformationToken();
            
            content = content.replace("[[URL]]", verifyURL);
            
            helper.setText(content, true);
            
            javaMailSender.send(message);

            return createSuccessResponse("Patient Created, Please verify email by clicking on the link sent to your email");
        }

        @Override
        public ResponseEntity<?> signUpDat(DoctorDTO doctorDTO) throws UserAlreadyExists, MessagingException, UnsupportedEncodingException{

            if(doctorRepository.existsByDoctorEmail(doctorDTO.getDoctorEmail())){
                throw new UserAlreadyExists("Doctor Already Exists");
            }

            if(!(isValidPassword(doctorDTO.getPassword()))) throw new IllegalArgumentException("Password must satisfy conditions");

            doctorDTO.setPassword(encoder.encode(doctorDTO.getPassword()));

            Doctor doctor = new Doctor(
                doctorDTO
            );

            doctor = doctorRepository.save(doctor);

            Conformation conformation = new Conformation();
            conformation.setDoctor(doctor);
            conformation.setTimestamp(System.currentTimeMillis());

            conformation = conformationRepository.save(conformation);

            String toAddress = doctor.getDoctorEmail();
            String fromAddress = "infirmarytest@gmail.com";
            String senderName = "UPES UHS";
            String subject = "Please verify your registration";
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to verify your registration:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Thank you,<br>"
                    + "UPES UHS.";
            
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            
            content = content.replace("[[name]]", doctor.getName());
            String verifyURL = "http://ec2-13-201-227-93.ap-south-1.compute.amazonaws.com/api/auth/user" + "/verify?code=" + conformation.getConformationToken();
            
            content = content.replace("[[URL]]", verifyURL);
            
            helper.setText(content, true);
            
            javaMailSender.send(message);

            return createSuccessResponse("Doctor Created Please verify email by clicking on the link sent to the email");
        }

        @Override
        public ResponseEntity<?> signUpAD(AdDTO adDTO) throws UserAlreadyExists, UnsupportedEncodingException, MessagingException{
            if(adRepository.existsById(adDTO.getEmail())){
                throw new UserAlreadyExists("AD Already Exists");
            }

            if(!(isValidPassword(adDTO.getPassword()))) throw new IllegalArgumentException("Password must satisfy conditions");

            adDTO.setPassword(encoder.encode(adDTO.getPassword()));

            AD ad = new AD(adDTO);

            ad = adRepository.save(ad);

            Conformation conformation = new Conformation();
            conformation.setAd(ad);
            conformation.setTimestamp(System.currentTimeMillis());

            conformation = conformationRepository.save(conformation);

            String toAddress = ad.getAdEmail();
            String fromAddress = "infirmarytest@gmail.com";
            String senderName = "UPES UHS";
            String subject = "Please verify your registration";
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to verify your registration:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Thank you,<br>"
                    + "UPES UHS.";
            
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            
            content = content.replace("[[name]]", ad.getName());
            String verifyURL = "http://ec2-13-201-227-93.ap-south-1.compute.amazonaws.com/api/auth/user" + "/verify?code=" + conformation.getConformationToken();
            
            content = content.replace("[[URL]]", verifyURL);
            
            helper.setText(content, true);
            
            javaMailSender.send(message);

            return createSuccessResponse("AD created Please verify email by clicking on the link sent to the email");
        }

        @Override
        public ResponseEntity<?> loginServiceAd(LoginRequestDTO loginRequestDTO,Double latitude, Double longitude) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.genrateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

            if(!roles.get(0).equals("ROLE_AD")) throw new SecurityException("Unauthorized Not AD");

            Location presentLocation = null;

            List<Location> locations = locationRepository.findAll();

            for(Location location:locations){
                if(FunctionUtil.IsWithinRadius(location.getLatitude(), location.getLongitude(), latitude, longitude)){
                    presentLocation = location;
                    break;
                }
            }

            if(presentLocation == null) throw new IllegalArgumentException("Be Available at the Infirmary before Logging In");

            AD ad = adRepository.findByAdEmail(loginRequestDTO.getEmail()).orElseThrow(()->new ResourceNotFoundException("No User Found"));

            if(conformationRepository.existsByAd_AdEmail(loginRequestDTO.getEmail())) throw new IllegalArgumentException("Please Verify Email");

            ad.setLocation(presentLocation);

            adRepository.save(ad);

            return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getUsername(),roles));
        }

        @Override
        public ResponseEntity<?> loginServiceDat(LoginRequestDTO loginRequestDTO) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.genrateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

            if(!roles.get(0).equals("ROLE_DOCTOR")) throw new SecurityException("Unauthorized Not A doctor");

            if(conformationRepository.existsByDoctor_DoctorEmail(loginRequestDTO.getEmail())) throw new IllegalArgumentException("Please Verify Email");

            return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getUsername(),roles));
        }

        @Override
        public ResponseEntity<?> loginServiceAdmin(LoginRequestDTO loginRequestDTO){
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),loginRequestDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.genrateJwtToken(authentication);

            UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetailsImpl.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

            if(!roles.get(0).equals("ROLE_ADMIN")) throw new SecurityException("Unauthorized Not a Admin");

            return createSuccessResponse(new JwtResponse(jwt,userDetailsImpl.getUsername(),roles));

        }

        @Override
        public String verifyUser(UUID code) {
            Conformation conformation = conformationRepository.findById(code).orElseThrow(()-> new ResourceNotFoundException("Invalid Code"));
            conformationRepository.delete(conformation);
            return "Email Verified";
        }

        @Override
        public String forgetPassPat(String email) throws UnsupportedEncodingException, MessagingException {
            email = email.substring(0,email.indexOf("@")).concat(email.substring(email.indexOf("@")).replaceAll(",", "."));

            Patient patient = patientRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("No User Found"));
            Optional<PasswordChange> passRecord = passwordChangeRepository.findByPatient(patient);

            if(passRecord.isPresent()){
                passwordChangeRepository.delete(passRecord.get());
            }

            PasswordChange newPassChange = new PasswordChange();
            newPassChange.setPatient(patient);

            newPassChange = passwordChangeRepository.save(newPassChange);

            String toAddress = patient.getEmail();
            String fromAddress = "infirmarytest@gmail.com";
            String senderName = "UPES UHS";
            String subject = "Password Change";
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to change your password:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">PASSWORD CHANGE</a></h3>"
                    + "Thank you,<br>"
                    + "UPES UHS.";
            
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            
            content = content.replace("[[name]]", patient.getName());
            String verifyURL = "http://ec2-13-232-83-190.ap-south-1.compute.amazonaws.com/pass-change" + "?code=" + newPassChange.getChangeCode() + "&role=patient";
            
            content = content.replace("[[URL]]", verifyURL);
            
            helper.setText(content, true);
            
            javaMailSender.send(message);

            return "Please check the email for password change";
        }

        @Override
        public String forgetPassDoc(String email) throws UnsupportedEncodingException, MessagingException{

            email = email.substring(0,email.indexOf("@")).concat(email.substring(email.indexOf("@")).replaceAll(",", "."));


            Doctor doctor = doctorRepository.findByDoctorEmail(email).orElseThrow(()->new ResourceNotFoundException("No Doctor Found"));
            Optional<PasswordChange> passRecord = passwordChangeRepository.findByDoctor(doctor);

            if(passRecord.isPresent()){
                passwordChangeRepository.delete(passRecord.get());
            }

            PasswordChange newPassChange = new PasswordChange();
            newPassChange.setDoctor(doctor);

            newPassChange = passwordChangeRepository.save(newPassChange);

            String toAddress = doctor.getDoctorEmail();
            String fromAddress = "infirmarytest@gmail.com";
            String senderName = "UPES UHS";
            String subject = "Password Change";
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to change your password:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">PASSWORD CHANGE</a></h3>"
                    + "Thank you,<br>"
                    + "UPES UHS.";
            
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            
            content = content.replace("[[name]]", doctor.getName());
            String verifyURL = "http://ec2-13-232-83-190.ap-south-1.compute.amazonaws.com/pass-change" + "?code=" + newPassChange.getChangeCode() + "&role=doctor";
            
            content = content.replace("[[URL]]", verifyURL);
            
            helper.setText(content, true);
            
            javaMailSender.send(message);

            return "Please check the email for password change";
        }

        @Override
        public String forgetPassAd(String email) throws UnsupportedEncodingException, MessagingException{
            
            email = email.substring(0,email.indexOf("@")).concat(email.substring(email.indexOf("@")).replaceAll(",", "."));

            
            AD ad = adRepository.findByAdEmail(email).orElseThrow(()->new ResourceNotFoundException("No AD Found"));
            Optional<PasswordChange> passRecord = passwordChangeRepository.findByAd(ad);

            if(passRecord.isPresent()){
                passwordChangeRepository.delete(passRecord.get());
            }

            PasswordChange newPassChange = new PasswordChange();
            newPassChange.setAd(ad);

            newPassChange = passwordChangeRepository.save(newPassChange);

            String toAddress = ad.getAdEmail();
            String fromAddress = "infirmarytest@gmail.com";
            String senderName = "UPES UHS";
            String subject = "Password Change";
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to change your password:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">PASSWORD CHANGE</a></h3>"
                    + "Thank you,<br>"
                    + "UPES UHS.";
            
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            
            content = content.replace("[[name]]", ad.getName());
            String verifyURL = "http://ec2-13-232-83-190.ap-south-1.compute.amazonaws.com/pass-change" + "?code=" + newPassChange.getChangeCode() + "&role=ad";
            
            content = content.replace("[[URL]]", verifyURL);
            
            helper.setText(content, true);
            
            javaMailSender.send(message);

            return "Please check the email for password change";
        }

        @Override
        public String changePassPat(UUID code, PasswordChangeDTO passwordChangeDTO) throws UnsupportedEncodingException, MessagingException {
            PasswordChange passwordChange = passwordChangeRepository.findById(code).orElseThrow(()-> new ResourceNotFoundException("Invalid Code"));

            if(passwordChange.getPatient() == null) throw new IllegalArgumentException("Invalid Code");

            if(!(passwordChangeDTO.getNewPass().equals(passwordChangeDTO.getRepeatPassword()))) throw new IllegalArgumentException("Password does not match with repeat password");
            
            if(!(isValidPassword(passwordChangeDTO.getNewPass()))) throw new IllegalArgumentException("Pasword must contain atleast 8 characters including uppercase, lowercase, number and a special character");
            
            Patient patient = passwordChange.getPatient();
            patient.setPassword(encoder.encode(passwordChangeDTO.getNewPass()));
            patientRepository.save(patient);

            passwordChangeRepository.delete(passwordChange);

            return "Password Successfully changed";
        }

        @Override
        public String changePassDoc(UUID code, PasswordChangeDTO passwordChangeDTO) throws UnsupportedEncodingException, MessagingException {
            PasswordChange passwordChange = passwordChangeRepository.findById(code).orElseThrow(()-> new ResourceNotFoundException("Invalid Code"));

            if(passwordChange.getDoctor() == null) throw new IllegalArgumentException("Invalid Code");

            if(!(passwordChangeDTO.getNewPass().equals(passwordChangeDTO.getRepeatPassword()))) throw new IllegalArgumentException("Password does not match with repeat password");

            if(!(isValidPassword(passwordChangeDTO.getNewPass()))) throw new IllegalArgumentException("Pasword must contain atleast 8 characters including uppercase, lowercase, number and a special character");

            Doctor doctor = passwordChange.getDoctor();
            doctor.setPassword(encoder.encode(passwordChangeDTO.getNewPass()));
            doctorRepository.save(doctor);

            passwordChangeRepository.delete(passwordChange);

            return "Password Successfully changed";
        }

        @Override
        public String changePassAd(UUID code, PasswordChangeDTO passwordChangeDTO) throws UnsupportedEncodingException, MessagingException {
            PasswordChange passwordChange = passwordChangeRepository.findById(code).orElseThrow(()-> new ResourceNotFoundException("Invalid Code"));

            if(passwordChange.getAd() == null) throw new IllegalArgumentException("Invalid Code");

            if(!(passwordChangeDTO.getNewPass().equals(passwordChangeDTO.getRepeatPassword()))) throw new IllegalArgumentException("Password does not match with repeat password");

            if(!(isValidPassword(passwordChangeDTO.getNewPass()))) throw new IllegalArgumentException("Pasword must contain atleast 8 characters including uppercase, lowercase, number and a special character");

            AD ad = passwordChange.getAd();
            ad.setPassword(encoder.encode(passwordChangeDTO.getNewPass()));
            adRepository.save(ad);

            passwordChangeRepository.delete(passwordChange);

            return "Password Successfully changed";
        }

}
