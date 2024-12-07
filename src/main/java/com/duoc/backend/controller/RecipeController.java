package com.duoc.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.duoc.backend.service.RecipeService;
import com.duoc.backend.service.CommentService;
import com.duoc.backend.service.MyUserDetailsService;
import com.duoc.backend.model.Comment;
import com.duoc.backend.model.Recipe;
import com.duoc.backend.model.User;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @GetMapping
    public List<Recipe> getRecipes() {
        return recipeService.getAllRecipes();  
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isPresent()) {
            return ResponseEntity.ok(recipe.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Recipe addRecipe(@RequestBody Recipe recipe) {
        return recipeService.saveRecipe(recipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        Optional<Recipe> oldRecipe = recipeService.getRecipeById(id);
        if (oldRecipe.isPresent()) {
            recipe.setId(id);
            return ResponseEntity.ok(recipeService.saveRecipe(recipe));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{recipeId}/comments")
    public ResponseEntity<String> addComment(
            @PathVariable Long recipeId,
            @RequestParam String text) {

        // Obtener el usuario autenticado del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // Extraer el nombre de usuario (o email) desde el token
        String username = authentication.getName();

        // Buscar al usuario en la base de datos
        User user = userDetailsService.loadUserByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Optional<Recipe> optionalRecipe = recipeService.getRecipeById(recipeId);
        if (optionalRecipe.isEmpty()) {
            return ResponseEntity.badRequest().body("Recipe not found");
        }
        Recipe recipe = optionalRecipe.get();

        // Crear y guardar el comentario
        Comment comment = new Comment();
        comment.setContent(text);
        comment.setRecipe(recipe);
        comment.setUser(user);
        commentService.save(comment);

        return ResponseEntity.ok("Comment added successfully");
    }

}
