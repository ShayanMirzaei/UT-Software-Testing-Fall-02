package application;

import controllers.CommoditiesController;
import model.Commodity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import service.Baloot;
import utility.Helper;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CommoditiesController.class)
public class CommoditiesControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    public void setUpFixture() {
        Baloot.getInstance().fetchAndStoreData();
    }

    @Test
    public void GivenCommoditiesExistInDatabase_ShouldReturnCorrectJsonAndStatusCode_WhenGetCommoditiesIsCalled() throws Exception {
        var expectedCommodities = Helper.getCommoditiesInDatabase();

        mockMvc.perform(get("/commodities"))
                .andExpect(status().isOk())
                .andExpect(content().string(Helper.asJsonString(expectedCommodities)));
    }


    @Test
    public void GivenCommodityWithIdExists_ShouldReturnOkStatusCode_WhenGetCommodityIsCalled() throws Exception {
        var expectedCommodity = Helper.getCommodityInDatabaseById("1");

        mockMvc.perform(get("/commodities/{id}", expectedCommodity.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(Helper.asJsonString(expectedCommodity)));
    }

    @Test
    public void GivenCommodityWithIdDoesNotExist_ShouldReturnNotFound_WhenGetCommodityIsCalled() throws Exception {

        mockMvc.perform(get("/commodities/non-existent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void GivenCommodityAndUserExist_ShouldReturnOk_WhenRateCommodityIsCalledWithValidRate() throws Exception {

        var existingUser = Helper.getUserInDatabaseWithUsername("ali");
        var exsitingCommodity = Helper.getCommodityInDatabaseById("1");
        var body = Helper.createRateCommodityBodyWithString(existingUser.getUsername(), 5);

        mockMvc.perform(post("/commodities/{id}/rate", exsitingCommodity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("rate added successfully!"));
    }

    @Test
    public void GivenCommodityDoesNotExist_ShouldReturnNotFound_WhenRateCommodityIsCalledWithValidRate() throws Exception {

        var existingUser = Helper.getUserInDatabaseWithUsername("ali");
        var body = Helper.createRateCommodityBodyWithString(existingUser.getUsername(), 5);

        mockMvc.perform(post("/commodities/non-existent-commodity/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Commodity does not exist."));
    }

    @Test
    public void GivenCommodityExists_ShouldReturnBadRequest_WhenRateCommodityIsCalledWithInvalidRate() throws Exception {

        var existingUser = Helper.getUserInDatabaseWithUsername("ali");
        var exsitingCommodity = Helper.getCommodityInDatabaseById("1");
        var body = Helper.createRateCommodityBodyWithInvalidRate(existingUser.getUsername());

        mockMvc.perform(post("/commodities/{id}/rate", exsitingCommodity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("For input string: \"invalid_rate\""));
    }

    @Test
    public void GivenCommodityAndUserExist_ShouldReturnOk_WhenAddCommentIsCalled() throws Exception {

        var existingUser = Helper.getUserInDatabaseWithUsername("ali");
        var exsitingCommodity = Helper.getCommodityInDatabaseById("1");
        var body = Helper.createAddCommentBody(existingUser.getUsername());

        mockMvc.perform(post("/commodities/{id}/comment", exsitingCommodity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("comment added successfully!"));
    }

    @Test
    public void GivenUserDoesNotExist_ShouldReturnNotFound_WhenAddCommentIsCalled() throws Exception {

        var exsitingCommodity = Helper.getCommodityInDatabaseById("1");

        var body = Helper.createAddCommentBody("non-existent-username");

        mockMvc.perform(post("/commodities/{id}/comment", exsitingCommodity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User does not exist."));
    }

    @Test
    public void GivenCommodityExists_ShouldReturnOk_WhenGetCommentIsCalled() throws Exception {

        var exsitingCommodity = Helper.getCommodityInDatabaseById("1");
        var comments = Helper.getCommodityCommentsInDatabase("1");

        mockMvc.perform(get("/commodities/{id}/comment", exsitingCommodity.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(Helper.asJsonString(comments)));
    }

    @Test
    public void GivenCommoditiesExist_ShouldReturnOk_WhenSearchWithCorrectNameIsCalled() throws Exception {

        var exsitingCommodity = Helper.getCommodityInDatabaseById("1");
        var body = Helper.createSearchCommoditiesBody(exsitingCommodity.getName(), "name");

        var expectedResult = new ArrayList<Commodity>();
        expectedResult.add(exsitingCommodity);

        mockMvc.perform(post("/commodities/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(Helper.asJsonString(expectedResult)));
    }

    @Test
    public void GivenCommoditiesExist_ShouldReturnOk_WhenSearchWithCorrectCategoryIsCalled() throws Exception {

        var existingCommodities = Helper.getCommoditiesInDatabase();
        var body = Helper.createSearchCommoditiesBody(existingCommodities.get(0).getCategories().get(0), "category");

        mockMvc.perform(post("/commodities/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(Helper.asJsonString(existingCommodities)));
    }

    @Test
    public void GivenCommoditiesExist_ShouldReturnOk_WhenSearchWithCorrectProviderIsCalled() throws Exception {

        var exsitingCommodity = Helper.getCommodityInDatabaseById("1");
        var body = Helper.createSearchCommoditiesBody("apple", "provider");

        var expectedResult = new ArrayList<Commodity>();
        expectedResult.add(exsitingCommodity);

        mockMvc.perform(post("/commodities/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(Helper.asJsonString(expectedResult)));
    }

    @Test
    public void GivenCommoditiesExist_ShouldReturnEmpty_WhenSearchWithAnotherNameIsCalled() throws Exception {

        var body = Helper.createSearchCommoditiesBody("non-existent-name", "name");

        var expectedResult = new ArrayList<Commodity>();

        mockMvc.perform(post("/commodities/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(Helper.asJsonString(expectedResult)));
    }

    @Test
    public void GivenCommoditiesExist_ShouldReturnEmpty_WhenSearchWithAnotherCategoryIsCalled() throws Exception {

        var body = Helper.createSearchCommoditiesBody("non-existent-category", "category");

        var expectedResult = new ArrayList<Commodity>();

        mockMvc.perform(post("/commodities/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(Helper.asJsonString(expectedResult)));
    }

    @Test
    public void GivenCommoditiesExist_ShouldReturnEmpty_WhenSearchWithAnotherProviderIsCalled() throws Exception {

        var body = Helper.createSearchCommoditiesBody("non-existent-provider", "provider");

        var expectedResult = new ArrayList<Commodity>();

        mockMvc.perform(post("/commodities/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(Helper.asJsonString(expectedResult)));
    }

    @Test
    public void GivenCommoditiesExist_ShouldReturnOkSuggestedCommodities_WhenGetSuggestionsIsCalled() throws Exception {

        var exsitingCommodity = Helper.getCommodityInDatabaseById("1");
        var expectedSuggestedCommodity = Helper.getCommodityInDatabaseById("2");

        var expectedResult = new ArrayList<Commodity>();
        expectedResult.add(expectedSuggestedCommodity);

        mockMvc.perform(get("/commodities/{id}/suggested", exsitingCommodity.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(Helper.asJsonString(expectedResult)));
    }

    @Test
    public void GivenCommoditiesExist_ShouldReturnNotFound_WhenGetSuggestionsForANonExistentCommodityIsCalled() throws Exception {

        mockMvc.perform(get("/commodities/non-existent-commodity/suggested"))
                .andExpect(status().isNotFound());
    }
}
