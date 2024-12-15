package com.duoc.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.duoc.backend.JWTAuthorizationFilter;
import com.duoc.backend.model.Comment;
import com.duoc.backend.model.Recipe;
import com.duoc.backend.model.User;
import com.duoc.backend.service.CommentService;
import com.duoc.backend.service.MyUserDetailsService;
import com.duoc.backend.service.RecipeService;
import com.duoc.backend.controller.RecipeController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private CommentService commentService;

    // @MockBean
    // private MyUserDetailsService userDetailsService;

    // @Mock
    // private SecurityContext securityContext;

    // @Mock
    // private Authentication authentication;

    // @MockBean
    // private JWTAuthorizationFilter jwtAuthorizationFilter;

    // @BeforeEach
    // public void setup() {
    //     MockitoAnnotations.openMocks(this);
    //     SecurityContextHolder.setContext(securityContext);
    // }

    @Test
    void testGetRecipes() throws Exception {
        // Datos simulados
        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        recipe1.setName("Pizza Margherita");

        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);
        recipe2.setName("Spaghetti Carbonara");

        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);

        // Configurar el mock
        when(recipeService.getAllRecipes()).thenReturn(recipes);

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(get("/api/recipes")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Pizza Margherita"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Spaghetti Carbonara"));
    }

    @Test
    void testAddRecipe() throws Exception {
        // Crear un objeto Recipe simulado
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("New Recipe");

        // Configurar el mock para el servicio
        when(recipeService.saveRecipe(any(Recipe.class))).thenReturn(recipe);

        // JSON de entrada
        String recipeJson = """
                {
                    "name": "New Recipe"
                }
                """;

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(recipeJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Recipe"));
    }

    @Test
    void testGetRecipe_Success() throws Exception {
        // Simulación del objeto Recipe que será devuelto
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Pizza Margherita");

        // Configurar el mock del servicio
        when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(recipe));

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(get("/api/recipes/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pizza Margherita"));
    }

    @Test
    void testGetRecipe_NotFound() throws Exception {
        // Configurar el mock para devolver un Optional vacío
        when(recipeService.getRecipeById(1L)).thenReturn(Optional.empty());

        // Realizar la solicitud y verificar que devuelve un 404
        mockMvc.perform(get("/api/recipes/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRecipe_Success() throws Exception {
        // No se necesita configurar el mock, ya que deleteRecipe no devuelve nada
        doNothing().when(recipeService).deleteRecipe(1L);

        // Realizar la solicitud DELETE y verificar la respuesta
        mockMvc.perform(delete("/api/recipes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateRecipe_Success() throws Exception {
        // Datos simulados para la receta existente y la actualizada
        Recipe existingRecipe = new Recipe();
        existingRecipe.setId(1L);
        existingRecipe.setName("Old Recipe");

        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setId(1L);
        updatedRecipe.setName("Updated Recipe");

        // Configuración del mock
        when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(existingRecipe));
        when(recipeService.saveRecipe(any(Recipe.class))).thenReturn(updatedRecipe);

        // JSON de entrada para la receta actualizada
        String updatedRecipeJson = """
                {
                    "name": "Updated Recipe"
                }
                """;

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(put("/api/recipes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedRecipeJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Recipe"));
    }

    @Test
    void testUpdateRecipe_NotFound() throws Exception {
        // Configurar el mock para devolver un Optional vacío
        when(recipeService.getRecipeById(1L)).thenReturn(Optional.empty());

        // JSON de entrada para la receta actualizada
        String updatedRecipeJson = """
                {
                    "name": "Updated Recipe"
                }
                """;

        // Realizar la solicitud y verificar que devuelve un 404
        mockMvc.perform(put("/api/recipes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedRecipeJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "epino", roles = "USER")
    void testAddComment_Success() throws Exception {
        // Datos simulados
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Pizza Margherita");

        User user = new User();
        user.setUsername("epino");

        // Configurar los mocks
        when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(recipe));
        // doNothing().when(commentService).save(any(Comment.class));

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(post("/api/recipes/1/comments")
                .param("text", "This is a comment"))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment added successfully"));
    }

    @Test
    @WithMockUser(username = "epino", roles = "USER")
    void testAddComment_RecipeNotFound() throws Exception {
        // Configurar el mock para devolver Optional vacío
        when(recipeService.getRecipeById(1L)).thenReturn(Optional.empty());

        // Realizar la solicitud y verificar que devuelve 400
        mockMvc.perform(post("/api/recipes/1/comments")
                .param("text", "This is a comment"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Recipe not found"));
    }

    @Test
    void testAddComment_Unauthenticated() throws Exception {
        // Realizar la solicitud sin usuario autenticado
        mockMvc.perform(post("/api/recipes/1/comments")
                .param("text", "This is a comment"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test", roles = "USER")
    void testAddComment_UserNotFound() throws Exception {
        // Datos simulados
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Pizza Margherita");

        // Configurar los mocks
        when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(recipe));

        // Realizar la solicitud y verificar que devuelve 400
        mockMvc.perform(post("/api/recipes/1/comments")
                .param("text", "This is a comment"))
                .andExpect(status().is5xxServerError());
    }

    
}
