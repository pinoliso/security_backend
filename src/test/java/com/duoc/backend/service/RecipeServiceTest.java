package com.duoc.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.duoc.backend.model.Recipe;
import com.duoc.backend.repository.RecipeRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void testGetAllRecipes() {
        // Simular datos
        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        recipe1.setName("Recipe 1");

        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);
        recipe2.setName("Recipe 2");

        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);

        // Configurar el mock
        when(recipeRepository.findAll()).thenReturn(recipes);

        // Llamar al método del servicio
        List<Recipe> result = recipeService.getAllRecipes();

        // Verificar resultados
        assertEquals(2, result.size());
        assertEquals("Recipe 1", result.get(0).getName());
        assertEquals("Recipe 2", result.get(1).getName());

        // Verificar interacción con el mock
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void testGetRecipeById_Success() {
        // Simular datos
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Recipe 1");

        // Configurar el mock
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        // Llamar al método del servicio
        Optional<Recipe> result = recipeService.getRecipeById(1L);

        // Verificar resultados
        assertTrue(result.isPresent());
        assertEquals("Recipe 1", result.get().getName());

        // Verificar interacción con el mock
        verify(recipeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRecipeById_NotFound() {
        // Configurar el mock para devolver un Optional vacío
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        // Llamar al método del servicio
        Optional<Recipe> result = recipeService.getRecipeById(1L);

        // Verificar resultados
        assertFalse(result.isPresent());

        // Verificar interacción con el mock
        verify(recipeRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveRecipe() {
        // Simular datos
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Recipe 1");

        // Configurar el mock
        when(recipeRepository.save(recipe)).thenReturn(recipe);

        // Llamar al método del servicio
        Recipe result = recipeService.saveRecipe(recipe);

        // Verificar resultados
        assertEquals("Recipe 1", result.getName());
        assertEquals(1L, result.getId());

        // Verificar interacción con el mock
        verify(recipeRepository, times(1)).save(recipe);
    }

    @Test
    void testDeleteRecipe() {
        // Llamar al método del servicio
        recipeService.deleteRecipe(1L);

        // Verificar interacción con el mock
        verify(recipeRepository, times(1)).deleteById(1L);
    }
}
