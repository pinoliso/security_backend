package com.duoc.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.duoc.backend.model.Comment;
import com.duoc.backend.repository.CommentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void testFindAll() {
        // Simular datos
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setContent("First comment");

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setContent("Second comment");

        List<Comment> comments = Arrays.asList(comment1, comment2);

        // Configurar el mock
        when(commentRepository.findAll()).thenReturn(comments);

        // Llamar al método del servicio
        List<Comment> result = commentService.findAll();

        // Verificar resultados
        assertEquals(2, result.size());
        assertEquals("First comment", result.get(0).getContent());
        assertEquals("Second comment", result.get(1).getContent());

        // Verificar interacción con el mock
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        // Simular datos
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("New comment");

        // Configurar el mock
        when(commentRepository.save(comment)).thenReturn(comment);

        // Llamar al método del servicio
        Comment result = commentService.save(comment);

        // Verificar resultados
        assertEquals("New comment", result.getContent());
        assertEquals(1L, result.getId());

        // Verificar interacción con el mock
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testDeleteById() {
        // Llamar al método del servicio
        commentService.deleteById(1L);

        // Verificar interacción con el mock
        verify(commentRepository, times(1)).deleteById(1L);
    }
}
