package com.duoc.backend.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class RecipeTest {
    @Test
    void testRecipeGettersAndSetters() {
        Recipe recipe = new Recipe();
        recipe.setName("Ajo con ají");
        recipe.setCousine("griega");

        assertEquals("Ajo con ají", recipe.getName());
        assertEquals("griega", recipe.getCousine());
    }
}
