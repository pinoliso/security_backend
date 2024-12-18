package com.duoc.backend.controller;

import com.duoc.backend.JWTAuthenticationConfig;
import com.duoc.backend.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.backend.service.MyUserDetailsService;

@RestController
public class LoginController {

    @Autowired
    JWTAuthenticationConfig jwtAuthtenticationConfig;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("login")
    public String login(
            @RequestParam("user") String username,
            @RequestParam("encryptedPass") String encryptedPass) {
        
        final User user = userDetailsService.loadUserByUsername(username);

        if (!user.getPassword().equals(encryptedPass)) {
            throw new RuntimeException("Credenciales Inválidas");
        }

        String token = jwtAuthtenticationConfig.getJWTToken(username);

        return token;

    }

}
