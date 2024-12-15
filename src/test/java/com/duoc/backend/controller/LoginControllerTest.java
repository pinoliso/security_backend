package com.duoc.backend.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.duoc.backend.JWTAuthenticationConfig;
import com.duoc.backend.service.CommentService;
import com.duoc.backend.service.MyUserDetailsService;
import com.duoc.backend.service.RecipeService;
// import org.springframework.security.core.userdetails.User;
import com.duoc.backend.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyUserDetailsService userDetailsService;

    @MockBean
    private JWTAuthenticationConfig jwtAuthenticationConfig;

    @InjectMocks
    private LoginController loginController;

    @Test
    void testLogin_Success() throws Exception {
        // Configurar mocks
        String username = "test";
        String encryptedPass = "correctPass";
        String mockToken = "mockToken";

        User user = new User();
        // when(user.getUsername()).thenReturn(username);
        // when(user.getPassword()).thenReturn(encryptedPass);
        user.setUsername(username);
        user.setId(1L);
        user.setPassword(encryptedPass);

        when(userDetailsService.loadUserByUsername(username))
            .thenReturn(user);
        when(jwtAuthenticationConfig.getJWTToken(username)).thenReturn(mockToken);

        // Ejecutar solicitud y verificar resultado
        mockMvc.perform(post("/login")
                .param("user", username)
                .param("encryptedPass", encryptedPass))
                .andExpect(status().isOk())
                .andExpect(content().string(mockToken));
    }

    private UserDetails createMockUserDetails(String username, String password) {
        return new org.springframework.security.core.userdetails.User(
            username, password, new ArrayList<>());
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        // Datos simulados
        String username = "epino";
        String encryptedPass = "12345678qwerEfsfq$";
        String wrongPass = "wrongPass";

        User user = new User();
        // when(user.getUsername()).thenReturn(username);
        // when(user.getPassword()).thenReturn(encryptedPass);
        user.setUsername(username);
        user.setId(1L);
        user.setPassword(encryptedPass);

        // Configurar el mock
        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(post("/login")
                .param("user", username)
                .param("encryptedPass", wrongPass))
                .andExpect(status().is5xxServerError());

        // Verificar interacción con mocks
        // verify(userDetailsService, times(1)).loadUserByUsername(username);
        // verify(jwtAuthenticationConfig, times(0)).getJWTToken(anyString());
    }
}