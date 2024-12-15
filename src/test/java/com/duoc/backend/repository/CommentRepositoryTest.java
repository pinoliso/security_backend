package com.duoc.backend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.duoc.backend.model.Comment;
import com.duoc.backend.model.Recipe;
import com.duoc.backend.model.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindComment() {

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

        Comment comment = new Comment();
        comment.setContent("Test Comment");
        comment.setUser(user);
        comment.setRecipe(savedRecipe);
        Comment savedComment = commentRepository.save(comment);

        assertNotNull(savedComment.getId());
        assertEquals("Test Comment", commentRepository.findById(savedComment.getId()).orElseThrow().getContent());
    }
}
