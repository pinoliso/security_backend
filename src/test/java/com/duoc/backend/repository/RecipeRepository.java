package com.duoc.backend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.duoc.backend.model.Recipe;
import com.duoc.backend.model.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindRecipe() {

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("12341234");
        user.setEnabled(true);
        user.setRole("USER");
        user.setId(1L);
        user = userRepository.save(user);

        Recipe recipe = new Recipe();
        recipe.setName("Test Recipe");
        recipe.setUser(user);
        Recipe savedRecipe = recipeRepository.save(recipe);

        assertNotNull(savedRecipe.getId());
        assertEquals("Test Recipe", recipeRepository.findById(savedRecipe.getId()).orElseThrow().getName());
    }
}
