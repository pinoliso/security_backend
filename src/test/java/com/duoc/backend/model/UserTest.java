package com.duoc.backend.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserTest {
    @Test
    void testUserGettersAndSetters() {
        User user = new User();
        user.setUsername("testuser");
        user.setRole("USER");

        assertEquals("testuser", user.getUsername());
        assertEquals("USER", user.getRole());
    }

    @Test
    public void testGetAuthorities() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEnabled(true);
        user.setPassword("password");
        user.setUsername("testUser");
        user.setRole("ROLE_ADMIN,ROLE_USER");

        // Act
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testIsCredentialsNonExpiredTrue() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEnabled(true);
        user.setPassword("password");
        user.setUsername("testUser");
        user.setRole("ROLE_ADMIN,ROLE_USER");

        // Act
        boolean result = user.isCredentialsNonExpired();

        // Assert
        assertTrue(result, "Credentials should be non-expired");
    }

    @Test
    public void testIsAccountNonLockedTrue() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEnabled(true);
        user.setPassword("password");
        user.setUsername("testUser");
        user.setRole("ROLE_ADMIN,ROLE_USER");

        // Act
        boolean result = user.isAccountNonLocked();

        // Assert
        assertTrue(result, "Account should be non-locked");
    }

    @Test
    public void testIsAccountNonExpiredTrue() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEnabled(true);
        user.setPassword("password");
        user.setUsername("testUser");
        user.setRole("ROLE_ADMIN,ROLE_USER");

        // Act
        boolean result = user.isAccountNonExpired();

        // Assert
        assertTrue(result, "Account should be non-expired");
    }

    @Test
    public void testIsEnabledTrue() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEnabled(true);
        user.setPassword("password");
        user.setUsername("testUser");
        user.setRole("ROLE_ADMIN,ROLE_USER");

        // Act
        boolean result = user.isEnabled();

        // Assert
        assertTrue(result, "User should be enabled");
    }
}
