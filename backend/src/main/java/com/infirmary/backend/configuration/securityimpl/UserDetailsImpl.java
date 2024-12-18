package com.infirmary.backend.configuration.securityimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.infirmary.backend.configuration.enums.EnumRoles;
import com.infirmary.backend.configuration.model.AD;
import com.infirmary.backend.configuration.model.Admin;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Patient;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    
    public static UserDetailsImpl build(AD ad){
        // Initiate Doctor roles

        List<EnumRoles> roles = new ArrayList<>();
        roles.add(EnumRoles.ROLE_AD);

        List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());

        return new UserDetailsImpl(
            ad.getAdEmail(),
            ad.getPassword(),
            authorities
        );
    }



    public static UserDetailsImpl build(Patient patient){
        // Initiate Patient Roles
        List<EnumRoles> roles = new ArrayList<>();
        roles.add(EnumRoles.ROLE_PATIENT);

        List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
        return new UserDetailsImpl(
            patient.getEmail(),
            patient.getPassword(),
            authorities
        );
    }

    public static UserDetailsImpl build(Doctor doctor){
        // Initiate Doctor roles
        List<EnumRoles> roles = new ArrayList<>();
        roles.add(EnumRoles.ROLE_DOCTOR);
        List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
        return new UserDetailsImpl(
            doctor.getDoctorEmail(),
            doctor.getPassword(),
            authorities
        );
    }

    public static UserDetailsImpl build(Admin admin){
        //Initiate Admin roles
        List<EnumRoles> roles = new ArrayList<>();
        roles.add(EnumRoles.ROLE_ADMIN);
        List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());

        return new UserDetailsImpl(admin.getAdminEmail(), admin.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
