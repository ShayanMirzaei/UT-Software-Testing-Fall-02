package controllers;

import exceptions.NotExistentCommodity;
import exceptions.NotExistentUser;
import helper.Helper;
import model.Comment;
import model.Commodity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.Baloot;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class CommoditiesControllerTest {
    private CommoditiesController controller;
    private Baloot balootMock;

    @BeforeEach
    void setUp() {
        balootMock = Mockito.mock(Baloot.class);
        controller = new CommoditiesController();
        controller.setBaloot(balootMock);
    }

    @Test
    void GivenCommoditiesInDatabase_ShouldReturnCommodities_WhenGetCommoditiesIsCalled() {
        var commodities = Helper.createCommodities(5);

        when(balootMock.getCommodities()).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = controller.getCommodities();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (int i = 0; i < commodities.size(); i++) {
            assertEquals(commodities.get(i).getId(), response.getBody().get(i).getId());
        }
    }

    @Test
    void GivenNoCommoditiesInDatabase_ShouldReturnEmptyList_WhenGetCommoditiesIsCalled() {
        when(balootMock.getCommodities()).thenReturn(new ArrayList<>());

        ResponseEntity<ArrayList<Commodity>> response = controller.getCommodities();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void GivenCommodityWithIdExistsInDatabase_ShouldReturnCommodity_WhenGetCommodityWithIdIsCalled() throws Exception {
        var commodity = Helper.createCommodity("id");

        when(balootMock.getCommodityById(commodity.getId())).thenReturn(commodity);

        ResponseEntity<Commodity> response = controller.getCommodity(commodity.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(commodity.getId(), response.getBody().getId());
    }

    @Test
    void GivenCommodityWithIdDoesNotExistInDatabase_ShouldReturnNotFound_WhenGetCommodityWithIdIsCalled() throws Exception {
        var nonExistentId = "id";

        doThrow(new NotExistentCommodity()).when(balootMock).getCommodityById(nonExistentId);

        ResponseEntity<Commodity> response = controller.getCommodity(nonExistentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void GivenCommodityExistsInDatabase_ShouldReturnOk_WhenUserIsRatingCommodity() throws Exception {
        var commodity = Helper.createCommodity("id");
        var input = Helper.createRateCommodityInput("5");

        when(balootMock.getCommodityById(commodity.getId())).thenReturn(commodity);

        ResponseEntity<String> response = controller.rateCommodity(commodity.getId(), input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("rate added successfully!", response.getBody());
    }

    @Test
    void GivenCommodityExistsInDatabase_ShouldReturnBadRequest_WhenUserIsRatingCommodityWithNonNumberRate() throws Exception {
        var commodity = Helper.createCommodity("id");
        var input = Helper.createRateCommodityInput("invalid rate");

        when(balootMock.getCommodityById(commodity.getId())).thenReturn(commodity);

        ResponseEntity<String> response = controller.rateCommodity(commodity.getId(), input);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void GivenCommodityDoesNotExistInDatabase_ShouldReturnNotFound_WhenUserIsRatingCommodity() throws Exception {
        var input = Helper.createRateCommodityInput("5");
        var nonExistentId = "id";

        doThrow(new NotExistentCommodity()).when(balootMock).getCommodityById(nonExistentId);

        ResponseEntity<String> response = controller.rateCommodity(nonExistentId, input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void GivenUserExistsInDatabase_ShouldReturnOk_WhenUserIsCommentingOnCommodity() throws Exception {
        var input = Helper.createCommentOnCommodityInput("username", "comment");
        var user = Helper.createUser("username");
        var newCommentId = 3;
        var comment = new Comment(newCommentId, "email", "username", 1, "comment");

        when(balootMock.generateCommentId()).thenReturn(newCommentId);
        when(balootMock.getUserById(user.getUsername())).thenReturn(user);
        doNothing().when(balootMock).addComment(argThat(candidate -> Helper.commentsAreEqual(candidate, comment)));

        ResponseEntity<String> response = controller.addCommodityComment("1", input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("comment added successfully!", response.getBody());
    }

    @Test
    void GivenUserDoesNotExistInDatabase_ShouldReturnNotFound_WhenUserIsCommentingOnCommodity() throws Exception {
        var input = Helper.createCommentOnCommodityInput("username", "comment");
        var user = Helper.createUser("username");
        var newCommentId = 3;
        var comment = new Comment(newCommentId, "email", "username", 1, "comment");

        when(balootMock.generateCommentId()).thenReturn(newCommentId);
        doThrow(new NotExistentUser()).when(balootMock).getUserById(user.getUsername());

        ResponseEntity<String> response = controller.addCommodityComment("1", input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User does not exist.", response.getBody());
    }

    @Test
    void GivenCommodityHasComments_ShouldReturnComments_WhenGetCommentsIsCalled() throws Exception {
        var commodityId = "1";
        var comments = Helper.createComments(5);

        when(balootMock.getCommentsForCommodity(Integer.parseInt(commodityId))).thenReturn(comments);

        ResponseEntity<ArrayList<Comment>> response = controller.getCommodityComment(commodityId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (int i = 0; i < comments.size(); i++) {
            assertEquals(comments.get(i).getId(), response.getBody().get(i).getId());
        }
    }

    @Test
    void GivenCommodityHasNoComments_ShouldReturnEmpty_WhenGetCommentsIsCalled() throws Exception {
        var commodityId = "1";
        var comments = Helper.createComments(0);

        when(balootMock.getCommentsForCommodity(Integer.parseInt(commodityId))).thenReturn(comments);

        ResponseEntity<ArrayList<Comment>> response = controller.getCommodityComment(commodityId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void GivenCommoditiesWithNameExistInDatabase_ShouldReturnSaidCommodities_WhenSearchByNameIsDone() {
        var input = Helper.createSearchCommoditiesInput("name", "nameValue");
        var commodities = Helper.createCommodities(5);

        when(balootMock.filterCommoditiesByName("nameValue")).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = controller.searchCommodities(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (int i = 0; i < commodities.size(); i++) {
            assertEquals(commodities.get(i).getId(), response.getBody().get(i).getId());
        }
    }

    @Test
    void GivenCommoditiesWithNameDontExistInDatabase_ShouldReturnEmptyList_WhenSearchByNameIsDone() {
        var input = Helper.createSearchCommoditiesInput("name", "nameValue");

        when(balootMock.filterCommoditiesByName("nameValue")).thenReturn(new ArrayList<>());

        ResponseEntity<ArrayList<Commodity>> response = controller.searchCommodities(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void GivenCommoditiesWithCategoryExistInDatabase_ShouldReturnSaidCommodities_WhenSearchByCategoryIsDone() {
        var input = Helper.createSearchCommoditiesInput("category", "categoryValue");
        var commodities = Helper.createCommodities(5);

        when(balootMock.filterCommoditiesByCategory("categoryValue")).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = controller.searchCommodities(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (int i = 0; i < commodities.size(); i++) {
            assertEquals(commodities.get(i).getId(), response.getBody().get(i).getId());
        }
    }

    @Test
    void GivenCommoditiesWithCategoryDontExistInDatabase_ShouldReturnEmptyList_WhenSearchByCategoryIsDone() {
        var input = Helper.createSearchCommoditiesInput("category", "categoryValue");

        when(balootMock.filterCommoditiesByCategory("categoryValue")).thenReturn(new ArrayList<>());

        ResponseEntity<ArrayList<Commodity>> response = controller.searchCommodities(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }


    @Test
    void GivenCommoditiesWithProviderExistInDatabase_ShouldReturnSaidCommodities_WhenSearchByProviderIsDone() {
        var input = Helper.createSearchCommoditiesInput("provider", "providerValue");
        var commodities = Helper.createCommodities(5);

        when(balootMock.filterCommoditiesByProviderName("providerValue")).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = controller.searchCommodities(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (int i = 0; i < commodities.size(); i++) {
            assertEquals(commodities.get(i).getId(), response.getBody().get(i).getId());
        }
    }

    @Test
    void GivenCommoditiesWithProviderDontExistInDatabase_ShouldReturnEmptyList_WhenSearchByProviderIsDone() {
        var input = Helper.createSearchCommoditiesInput("provider", "providerValue");

        when(balootMock.filterCommoditiesByProviderName("providerValue")).thenReturn(new ArrayList<>());

        ResponseEntity<ArrayList<Commodity>> response = controller.searchCommodities(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void GivenOkDatabase_ShouldReturnEmptyList_WhenSearchOptionIsInvalid() {
        var input = Helper.createSearchCommoditiesInput("invalid search option", "value");

        ResponseEntity<ArrayList<Commodity>> response = controller.searchCommodities(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void GivenCommodityExistsWithSimilarCommodities_ShouldReturnSuggestions_WhenGetSuggestionsIsCalled() throws Exception {
        var commodityId = "id";
        var commodities = Helper.createCommodities(5);

        when(balootMock.getCommodityById(commodityId)).thenReturn(Helper.createCommodity(commodityId));
        when(balootMock.suggestSimilarCommodities(argThat(
                commodity -> "id".equals(commodity.getId())))).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = controller.getSuggestedCommodities(commodityId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (int i = 0; i < commodities.size(); i++) {
            assertEquals(commodities.get(i).getId(), response.getBody().get(i).getId());
        }
    }

    @Test
    void GivenCommodityExistsWithNoSimilarCommodities_ShouldReturnEmpty_WhenGetSuggestionsIsCalled() throws Exception {
        var commodityId = "id";

        when(balootMock.getCommodityById(commodityId)).thenReturn(Helper.createCommodity(commodityId));
        when(balootMock.suggestSimilarCommodities(argThat(
                commodity -> "id".equals(commodity.getId())))).thenReturn(new ArrayList<>());

        ResponseEntity<ArrayList<Commodity>> response = controller.getSuggestedCommodities(commodityId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void GivenCommodityDoesNotExist_ShouldReturnNotFound_WhenGetSuggestionsIsCalled() throws Exception {
        var nonExistentCommodityId = "id";

        doThrow(new NotExistentCommodity()).when(balootMock).getCommodityById(nonExistentCommodityId);

        ResponseEntity<ArrayList<Commodity>> response = controller.getSuggestedCommodities(nonExistentCommodityId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
