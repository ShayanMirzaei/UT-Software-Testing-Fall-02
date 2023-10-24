package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

    private Comment comment;

    @BeforeEach
    public void setUp() {
        comment = new Comment(1, "Ali@ut.com", "Ali Alii", 123, "This is a test comment. :)");
    }

    @Test
    public void testInitialization() {
        assertEquals(1, comment.getId());
        assertEquals("Ali@ut.com", comment.getUserEmail());
        assertEquals("Ali Alii", comment.getUsername());
        assertEquals(123, comment.getCommodityId());
        assertEquals("This is a test comment. :)", comment.getText());
        assertNotNull(comment.getDate());
        assertEquals(0, comment.getLike());
        assertEquals(0, comment.getDislike());
        assertTrue(comment.getUserVote().isEmpty());
    }

    @Test
    public void testAddUserVote() {
        comment.addUserVote("Alice", "like");
        comment.addUserVote("Bob", "dislike");

        Map<String, String> userVotes = comment.getUserVote();
        assertEquals(2, userVotes.size());
        assertEquals("like", userVotes.get("Alice"));
        assertEquals("dislike", userVotes.get("Bob"));

        assertEquals(1, comment.getLike());
        assertEquals(1, comment.getDislike());
    }
}
