package com.duoc.backend;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import static com.duoc.backend.Constants.*;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    public Claims setSigningKey(HttpServletRequest request) {
        String jwtToken = request.
                getHeader(HEADER_AUTHORIZACION_KEY).
                replace(TOKEN_BEARER_PREFIX, "");

                // return Jwts.parser()
                // .verifyWith((SecretKey) getSigningKey(SUPER_SECRET_KEY))
                // .build()
                // .parseSignedClaims(jwtToken)
                // .getPayload();

                jwtToken = jwtToken.replace("Bearer ", "").trim();

                return Jwts.parserBuilder()
                .setSigningKey((SecretKey) getSigningKey(SUPER_SECRET_KEY)) // Set the signing key
                .build() // Build the parser
                .parseClaimsJws(jwtToken) // Parse and validate the token
                .getBody(); // Extract claims

    }

    public void setAuthentication(Claims claims) {

        Object authoritiesClaim = claims.get("authorities");
        List<GrantedAuthority> authorities = null;

        if (authoritiesClaim instanceof List<?>) {
            authorities = ((List<?>) authoritiesClaim).stream()
                .filter(authority -> authority instanceof String)  
                .map(authority -> new SimpleGrantedAuthority((String) authority))
                .collect(Collectors.toList());
        }

        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                claims.getSubject(), 
                null,
                authorities
            );

        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    public boolean isJWTValid(HttpServletRequest request, HttpServletResponse res) {
        String authenticationHeader = request.getHeader(HEADER_AUTHORIZACION_KEY);
        if (authenticationHeader == null || !authenticationHeader.startsWith(TOKEN_BEARER_PREFIX))
            return false;
        return true;
    }

    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request, @SuppressWarnings("null") HttpServletResponse response, @SuppressWarnings("null") FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isJWTValid(request, response)) {
                Claims claims = setSigningKey(request);
                if (claims.get("authorities") != null) {
                    setAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return;
        }
    }

}
