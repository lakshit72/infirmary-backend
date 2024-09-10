package com.infirmary.backend.configuration.jwt;

import java.security.Key;
import java.util.Date;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.infirmary.backend.configuration.securityimpl.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtils {
    // private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${infirmary.backend.jwtSecret}")
    private String jwString;

    @Value("${infirmary.backend.jwtExpirationMs}")
    private int jwtExpiration;

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwString));
    }

    public String getUserNameFromJwtToken(String token){
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String authToken) throws MalformedJwtException,ExpiredJwtException,UnsupportedJwtException,IllegalArgumentException{
        Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
        return true;
    }

    public String genrateJwtToken(Authentication authentication){
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder().setSubject(userPrincipal.getUsername()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+jwtExpiration)).signWith(key(),SignatureAlgorithm.HS256).compact();
    }

}
