package model;

import exceptions.InvalidScore;
import exceptions.NotInStock;
import helper.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommodityTest {

    @Test
    public void GivenCommodityWithStock_ShouldHaveCorrectStock_WhenStockIsAdded() throws NotInStock {
        var commodity = TestHelper.createCommodityWithStock(10);

        var oldStock = commodity.getInStock();
        commodity.updateInStock(10);

        assertEquals(oldStock + 10, commodity.getInStock());
    }

    @Test
    public void GivenCommodityWithStock_ShouldHaveCorrectStock_WhenStockIsDecreased() throws NotInStock {
        var commodity = TestHelper.createCommodityWithStock(10);

        var oldStock = commodity.getInStock();
        commodity.updateInStock(-10);

        assertEquals(oldStock - 10, commodity.getInStock());
    }

    @Test
    public void GivenCommodityWithLowStock_ShouldThrowNotInStock_WhenStockIsDecreasedTooMuch() {
        var commodity = TestHelper.createCommodityWithStock(1);

        assertThrows(NotInStock.class, () -> commodity.updateInStock(-50));
    }


    @Test
    public void GivenCommodity_ShouldHaveCorrectUserRating_WhenRateIsAdded() throws InvalidScore {
        var commodity = TestHelper.createAnonymousCommodity();

        commodity.addRate("username", 4);

        assertEquals(4, commodity.getUserRate().get("username"));
    }

    @Test
    public void GivenCommodityHasUserRating_ShouldUpdateRatingCorrectly_WhenRateIsAdded() throws InvalidScore {
        var commodity = TestHelper.createCommodityWithUserRating("username", 5);

        commodity.addRate("username", 4);

        assertEquals(4, commodity.getUserRate().get("username"));
    }

    @Test
    public void GivenCommodity_ShouldThrowInvalidScore_WhenRateUnderZeroIsAdded() {
        var commodity = TestHelper.createAnonymousCommodity();


        assertThrows(InvalidScore.class, () -> commodity.addRate("username", -1));
    }

    @Test
    public void GivenCommodity_ShouldThrowInvalidScore_WhenRateAboveTenIsAdded() {
        var commodity = TestHelper.createAnonymousCommodity();


        assertThrows(InvalidScore.class, () -> commodity.addRate("username", 11));
    }

    @Test
    public void GivenCommodityWithInitialRating_ShouldHaveCorrectRating_WhenRateIsAdded() throws InvalidScore {
        var commodity = TestHelper.createCommodityWithInitialRating(5);

        commodity.addRate("u1", 4);
        commodity.addRate("u2", 9);

        assertEquals(6, commodity.getRating());
    }
}
