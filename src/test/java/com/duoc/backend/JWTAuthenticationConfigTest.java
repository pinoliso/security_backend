package com.duoc.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class JWTAuthenticationConfigTest {
    @Test
    public void testGetJWTToken() {
        // Arrange
        JWTAuthenticationConfig jwtConfig = new JWTAuthenticationConfig();
        String username = "testUser";

        // Act
        String token = jwtConfig.getJWTToken(username);
        token = token.replace("Bearer ", "").trim();

        // Assert
        assertNotNull(token, "Token should not be null");

        // // Decode the token to validate its content
        // Claims claims = Jwts.parserBuilder()
        //         .setSigningKey("mySecureSecretKey1234567890123456".getBytes()) // Same secret key
        //         .build() // Build the parser
        //         .parseClaimsJws(token)
        //         .getBody();

        // // assertEquals(username, claims.getSubject(), "Token subject should match the username");
        // assertTrue(claims.getExpiration().after(new Date()), "Token expiration should be in the future");
    }
}
