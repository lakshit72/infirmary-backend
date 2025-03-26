package com.infirmary.backend.configuration.security;

import static com.infirmary.backend.shared.utility.FunctionUtil.passwordEncoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.infirmary.backend.configuration.jwt.AuthEntryPointJwt;
import com.infirmary.backend.configuration.jwt.AuthTokenFilter;
import com.infirmary.backend.configuration.jwt.JwtUtils;
import com.infirmary.backend.configuration.securityimpl.AdDetailsImpl;
import com.infirmary.backend.configuration.securityimpl.AdminDetailsImpl;
import com.infirmary.backend.configuration.securityimpl.AnalyticsDetailsImpl;
import com.infirmary.backend.configuration.securityimpl.DoctorDetailsImpl;
import com.infirmary.backend.configuration.securityimpl.PatientDetailsImpl;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    private PatientDetailsImpl patientDetailsImpl;
    private DoctorDetailsImpl doctorDetailsImpl;
    private AdDetailsImpl adDetailsImpl;
    private AdminDetailsImpl adminDetailsImpl;
    private AuthEntryPointJwt unauthorizedHandler;
    private JwtUtils jwtUtils;
    private AnalyticsDetailsImpl analyticsDetailsImpl;

    public WebSecurityConfig(AuthEntryPointJwt authEntryPointJwt,JwtUtils jwtUtils, PatientDetailsImpl patientDetailsImpl, DoctorDetailsImpl doctorDetailsImpl, AdDetailsImpl adDetailsImpl, AdminDetailsImpl adminDetailsImpl,AnalyticsDetailsImpl analyticsDetailsImpl){
        this.unauthorizedHandler = authEntryPointJwt;
        this.jwtUtils = jwtUtils;
        this.adDetailsImpl = adDetailsImpl;
        this.adminDetailsImpl = adminDetailsImpl;
        this.doctorDetailsImpl = doctorDetailsImpl;
        this.patientDetailsImpl = patientDetailsImpl;
        this.analyticsDetailsImpl = analyticsDetailsImpl;
    }

    @Bean
    public AuthTokenFilter authenticatioTokenFilterPatient(){
        return new AuthTokenFilter(jwtUtils,patientDetailsImpl,adminDetailsImpl,adDetailsImpl,doctorDetailsImpl,analyticsDetailsImpl);
    }

    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsImpl){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsImpl);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = List.of(
            authenticationProvider(patientDetailsImpl),
            authenticationProvider(doctorDetailsImpl),
            authenticationProvider(adDetailsImpl),
            authenticationProvider(adminDetailsImpl),
            authenticationProvider(analyticsDetailsImpl)
        );

        return authentication -> {
            // Get the current request from the SecurityContextHolder
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                throw new AuthenticationServiceException("Unable to determine request");
            }

            HttpServletRequest request = attributes.getRequest();
            String requestURI = request.getRequestURI();

            // Select the appropriate provider based on the request URI
            AuthenticationProvider selectedProvider = null;
            if ((requestURI.startsWith("/api/patient/")) || (requestURI.startsWith("/api/auth/patient/"))) {
                selectedProvider = providers.stream().toList().get(0);
            } else if ((requestURI.startsWith("/api/doctor/")) || (requestURI.startsWith("/api/auth/doctor/"))) {
                selectedProvider = providers.stream().toList().get(1);
            } else if ((requestURI.startsWith("/api/AD/")) || (requestURI.startsWith("/api/auth/ad/"))) {
                selectedProvider = providers.stream().toList().get(2);
            } else if (requestURI.startsWith("/api/admin/") || requestURI.startsWith("/api/auth/admin/")) {
                selectedProvider = providers.stream().toList().get(3);
            }else if (requestURI.startsWith("/api/analytics/")) {
                selectedProvider = providers.stream().toList().get(4);
            }
            // If no matching provider is found, throw an exception
            if (selectedProvider == null) {
                throw new ProviderNotFoundException("No authentication provider found for the request");
            }

            // Authenticate using the selected provider
            return selectedProvider.authenticate(authentication);
        };
    }

    @Order(1)
    @Bean
     public SecurityFilterChain globalSecurityFilterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers
                .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                .contentSecurityPolicy(csp -> csp.policyDirectives("script-src 'self'"))
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable());
    
        return http.build();
    }


    @Order(2)
    @Bean
    public SecurityFilterChain doctorSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/doctor/**", "/api/auth/doctor/**")
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/doctor/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(authenticatioTokenFilterPatient(), UsernamePasswordAuthenticationFilter.class);

            return http.build();
    }

    @Order(3)
    @Bean
    public SecurityFilterChain AdSecurityFilterChain(HttpSecurity http) throws Exception {
         http
            .securityMatcher("/api/AD/**", "/api/auth/ad/**")
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/AD/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(authenticatioTokenFilterPatient(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Order(4)
    @Bean
    public SecurityFilterChain AdminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/admin/**", "/api/auth/admin/**")
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/admin/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(authenticatioTokenFilterPatient(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Order(5)
    @Bean
    public SecurityFilterChain PatientSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/patient/**", "/api/auth/patient/**")
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/patient/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(authenticatioTokenFilterPatient(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Order(6)
    @Bean
    public SecurityFilterChain analyticsSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/analytics/**")
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated()
            )
            .addFilterBefore(authenticatioTokenFilterPatient(), UsernamePasswordAuthenticationFilter.class);

            return http.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/api/location/*").permitAll().requestMatchers("/Profile/*").permitAll().anyRequest().denyAll());
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(CorsConfiguration.ALL);
        configuration.setAllowedMethods(Arrays.asList(CorsConfiguration.ALL));
        configuration.setAllowedHeaders(Arrays.asList(CorsConfiguration.ALL));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}