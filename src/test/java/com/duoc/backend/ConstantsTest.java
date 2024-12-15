package com.duoc.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import org.springframework.security.crypto.codec.Base64;
import java.security.Key;

import org.junit.jupiter.api.Test;

public class ConstantsTest {
    

    @Test
    public void testConstantsValues() {
        // Verify constant values
        assertEquals("/login", Constants.LOGIN_URL);
        assertEquals("Authorization", Constants.HEADER_AUTHORIZACION_KEY);
        assertEquals("Bearer ", Constants.TOKEN_BEARER_PREFIX);
        assertEquals("https://www.duocuc.cl/", Constants.ISSUER_INFO);
        assertEquals(864_000_000, Constants.TOKEN_EXPIRATION_TIME);

        // Check if SUPER_SECRET_KEY is valid Base64
        assertTrue(Base64.isBase64(Constants.SUPER_SECRET_KEY.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testGetSigningKeyB64() {
        // Arrange
        String secret = Constants.SUPER_SECRET_KEY;

        // Act
        Key signingKey = Constants.getSigningKeyB64(secret);

        // Assert
        assertNotNull(signingKey);
        assertEquals("HmacSHA512", signingKey.getAlgorithm());
    }

    @Test
    public void testGetSigningKey() {
        // Arrange
        String secret = "mySecureSecretKey1234567890123456";

        // Act
        Key signingKey = Constants.getSigningKey(secret);

        // Assert
        assertNotNull(signingKey);
        // assertEquals("HmacSHA512", signingKey.getAlgorithm());
    }
}
