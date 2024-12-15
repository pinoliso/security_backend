package com.duoc.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CommentTest {
    @Test
    void testCommentGettersAndSetters() {
        Comment comment = new Comment();
        comment.setContent("asdf qwer sdfg");

        assertEquals("asdf qwer sdfg", comment.getContent());
    }
}
