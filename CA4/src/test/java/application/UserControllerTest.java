package application;

import controllers.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utility.Helper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void GivenUserWithUsernameExists_ShouldReturnCorrectJsonAndStatusCode_WhenGetUserIsCalled() throws Exception {
        var expectedUser = Helper.getUserInDatabaseWithUsername("ali");

        mockMvc.perform(get("/users/{id}", expectedUser.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(expectedUser.getUsername()))
                .andExpect(jsonPath("$.password").value(expectedUser.getPassword()))
                .andExpect(jsonPath("$.email").value(expectedUser.getEmail()))
                .andExpect(jsonPath("$.birthDate").value(expectedUser.getBirthDate()))
                .andExpect(jsonPath("$.address").value(expectedUser.getAddress()))
                .andExpect(jsonPath("$.credit").value(expectedUser.getCredit()))
                .andExpect(jsonPath("$.commoditiesRates").value(expectedUser.getCommoditiesRates()))
                .andExpect(jsonPath("$.buyList").value(expectedUser.getBuyList()))
                .andExpect(jsonPath("$.purchasedList").value(expectedUser.getPurchasedList()));
    }


    @Test
    public void GivenUserWithUsernameDoesNotExist_ShouldReturnNotFoundStatusCode_WhenGetUserIsCalled() throws Exception {

        mockMvc.perform(get("/users/non-existent-username"))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(doubles = { 1000.5, 1.0, 0.0 })
    public void GivenUserWithUsernameExists_ShouldReturnOk_WhenAddCreditWithValidValueIsCalled(double validCredit) throws Exception {

        var body = Helper.createAddCreditBody(validCredit);

        mockMvc.perform(post("/users/{id}/credit", "ali")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("credit added successfully!"));
    }

    @ParameterizedTest
    @ValueSource(doubles = { -1.0, -1000 })
    public void GivenUserWithUsernameExists_ShouldReturnBadRequest_WhenAddCreditWithInvalidValueIsCalled(double validCredit) throws Exception {

        var body = Helper.createAddCreditBody(validCredit);

        mockMvc.perform(post("/users/{id}/credit", "ali")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Credit value must be a positive float"));
    }

    @Test
    public void GivenUserWithUsernameExists_ShouldReturnBadRequest_WhenAddCreditWithInvalidNumberIsCalled() throws Exception {

        var invalidCreditNumber = "noNumber";
        var body = Helper.createAddCreditBodyWithString(invalidCreditNumber);

        mockMvc.perform(post("/users/{id}/credit", "ali")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Please enter a valid number for the credit amount."));
    }

    @Test
    public void GivenUserWithUsernameDoesNotExist_ShouldReturnNotFound_WhenAddCreditIsCalled() throws Exception {

        var body = Helper.createAddCreditBody(1000);

        mockMvc.perform(post("/users/{id}/credit", "non-existent-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User does not exist."));
    }

}
