package com.infirmary.backend.configuration.security;

import static com.infirmary.backend.shared.utility.FunctionUtil.passwordEncoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.infirmary.backend.configuration.jwt.AuthEntryPointJwt;
import com.infirmary.backend.configuration.jwt.AuthTokenFilter;
import com.infirmary.backend.configuration.jwt.JwtUtils;
import com.infirmary.backend.configuration.securityimpl.UserDetailsServiceImpl;

@Order(2)
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    private UserDetailsServiceImpl userDetailsServiceImpl;
    private AuthEntryPointJwt unauthorizedHandler;
    private JwtUtils jwtUtils;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl,AuthEntryPointJwt authEntryPointJwt,JwtUtils jwtUtils){
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.unauthorizedHandler = authEntryPointJwt;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public AuthTokenFilter authenticationJwTokenFilter(){
        return new AuthTokenFilter(jwtUtils, userDetailsServiceImpl);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable()).exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated());

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}