package controllers;

import exceptions.NotExistentComment;
import helper.Helper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.Baloot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CommentControllerTest {
    private CommentController controller;
    private Baloot balootMock;

    @BeforeEach
    void setUp() {
        balootMock = Mockito.mock(Baloot.class);
        controller = new CommentController();
        controller.setBaloot(balootMock);
    }

    @Test
    void GivenCommentExists_ShouldReturnOK_WhenCommentIsLiked() throws Exception {
        var input = Helper.createCommentInput();
        var commentId = 1;

        when(balootMock.getCommentById(commentId)).thenReturn(Helper.createComment(commentId));

        ResponseEntity<String> response = controller.likeComment(String.valueOf(commentId), input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The comment was successfully liked!", response.getBody());
    }

    @Test
    void GivenCommentDoesNotExist_ShouldReturnNotFound_WhenCommentIsLiked() throws Exception {
        var input = Helper.createCommentInput();
        var commentId = 1;

        doThrow(new NotExistentComment()).when(balootMock).getCommentById(commentId);

        ResponseEntity<String> response = controller.likeComment(String.valueOf(commentId), input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Comment does not exist.", response.getBody());
    }

    @Test
    void GivenCommentExists_ShouldReturnOK_WhenCommentIsDisliked() throws Exception {
        var input = Helper.createCommentInput();
        var commentId = 1;

        when(balootMock.getCommentById(commentId)).thenReturn(Helper.createComment(commentId));

        ResponseEntity<String> response = controller.dislikeComment(String.valueOf(commentId), input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The comment was successfully disliked!", response.getBody());
    }

    @Test
    void GivenCommentDoesNotExist_ShouldReturnNotFound_WhenCommentIsDisliked() throws Exception {
        var input = Helper.createCommentInput();
        var commentId = 1;

        doThrow(new NotExistentComment()).when(balootMock).getCommentById(commentId);

        ResponseEntity<String> response = controller.dislikeComment(String.valueOf(commentId), input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Comment does not exist.", response.getBody());
    }
}
