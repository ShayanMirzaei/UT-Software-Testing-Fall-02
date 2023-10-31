package controllers;

import exceptions.IncorrectPassword;
import exceptions.NotExistentUser;
import exceptions.UsernameAlreadyTaken;
import helper.Helper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.Baloot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {
    private AuthenticationController controller;
    private Baloot balootMock;

    @BeforeEach
    void setUp() {
        balootMock = Mockito.mock(Baloot.class);
        controller = new AuthenticationController();
        controller.setBaloot(balootMock);
    }

    @Test
    void GivenUserExists_ShouldLoginSuccessfully_WhenCorrectUsernameAndPasswordIsGiven() throws Exception {
        var input = Helper.createLoginInput("username", "password");

        doNothing().when(balootMock).login("username","password");

        ResponseEntity<String> response = controller.login(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("login successfully!", response.getBody());
    }

    @Test
    void GivenUserExists_ShouldReturnUnauthorized_WhenIncorrectPasswordIsGiven() throws Exception {
        var input = Helper.createLoginInput("username", "password");

        doThrow(new IncorrectPassword()).when(balootMock).login("username","password");

        ResponseEntity<String> response = controller.login(input);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Incorrect password.", response.getBody());
    }

    @Test
    void GivenUserDoesNotExist_ShouldReturnNotFound_WhenLoginIsTried() throws Exception {
        var input = Helper.createLoginInput("username", "password");

        doThrow(new NotExistentUser()).when(balootMock).login("username","password");

        ResponseEntity<String> response = controller.login(input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User does not exist.", response.getBody());
    }

    @Test
    void GivenOkayUserRepo_ShouldReturnOk_WhenSignupIsAttempted() throws Exception {
        var input = Helper.createSignupInput("username");
        var user = Helper.createUser("username");

        doNothing().when(balootMock).addUser(user);

        ResponseEntity<String> response = controller.signup(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("signup successfully!", response.getBody());
    }


    @Test
    void GivenUserWithUsernameExists_ShouldReturnBadRequest_WhenSameUsernameIsTryingToSignup() throws Exception {
        var input = Helper.createSignupInput("username");
        var user = Helper.createUser("username");

        doThrow(new UsernameAlreadyTaken()).when(balootMock).addUser(
                argThat(candidate -> Helper.usersAreEqual(candidate, user)));

        ResponseEntity<String> response = controller.signup(input);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The username is already taken.", response.getBody());
    }
}
