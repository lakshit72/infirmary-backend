package com.infirmary.backend.configuration.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.infirmary.backend.configuration.securityimpl.AdDetailsImpl;
import com.infirmary.backend.configuration.securityimpl.AdminDetailsImpl;
import com.infirmary.backend.configuration.securityimpl.AnalyticsDetailsImpl;
import com.infirmary.backend.configuration.securityimpl.DoctorDetailsImpl;
import com.infirmary.backend.configuration.securityimpl.PatientDetailsImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthTokenFilter extends OncePerRequestFilter{
    private JwtUtils jwtUtils;
    private PatientDetailsImpl patientDetailsImpl;
    private AdminDetailsImpl adminDetailsImpl;
    private AdDetailsImpl adDetailsImpl;
    private DoctorDetailsImpl doctorDetailsImpl;
    private AnalyticsDetailsImpl analyticsDetailsImpl;

    public AuthTokenFilter(JwtUtils jwtUtils, PatientDetailsImpl patientDetailsImpl, AdminDetailsImpl adminDetailsImpl, AdDetailsImpl adDetailsImpl, DoctorDetailsImpl doctorDetailsImpl, AnalyticsDetailsImpl analyticsDetailsImpl){
        this.jwtUtils = jwtUtils;
        this.patientDetailsImpl = patientDetailsImpl;
        this.adDetailsImpl = adDetailsImpl;
        this.adminDetailsImpl = adminDetailsImpl;
        this.doctorDetailsImpl = doctorDetailsImpl;
        this.analyticsDetailsImpl = analyticsDetailsImpl;
    }

    private String parseJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");

        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")){
            return headerAuth.substring(7);
        }

        return null;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException{
        String jwt = parseJwt(request);

        if(jwt != null && jwtUtils.validateToken(jwt)){
            String username = jwtUtils.getUserNameFromJwtToken(jwt);

            String requestURI = request.getRequestURI();

            UserDetails userDetails = null;
            
            if ((requestURI.startsWith("/api/patient/")) || (requestURI.startsWith("/api/auth/patient/"))) {
                userDetails = patientDetailsImpl.loadUserByUsername(username);
            } else if ((requestURI.startsWith("/api/doctor/")) || (requestURI.startsWith("/api/auth/doctor/"))) {
                userDetails = doctorDetailsImpl.loadUserByUsername(username);
            } else if ((requestURI.startsWith("/api/AD/")) || (requestURI.startsWith("/api/auth/ad/"))) {
                userDetails = adDetailsImpl.loadUserByUsername(username);
            } else if (requestURI.startsWith("/api/admin/")) {
                userDetails = adminDetailsImpl.loadUserByUsername(username);
            }else if (requestURI.startsWith("/api/analytics/")) {
                userDetails = analyticsDetailsImpl.loadUserByUsername(username);
            }

            if(userDetails == null) throw new IllegalArgumentException("No valid Endpoint exists");

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }
        filterChain.doFilter(request, response);
    }

}