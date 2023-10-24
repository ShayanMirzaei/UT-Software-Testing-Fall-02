package model;

import exceptions.NotInStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommodityTest {

    private Commodity commodity;

    @BeforeEach
    public void setUp() {
        commodity = new Commodity();
        commodity.setId("123");
        commodity.setName("Sample Commodity");
        commodity.setProviderId("456");
        commodity.setPrice(100);
        commodity.setCategories(new ArrayList<>(Arrays.asList("Category1", "Category2")));
        commodity.setRating(0);
        commodity.setInStock(50);
        commodity.setImage("sample.jpg");
        commodity.setInitRate(3.5f);
        commodity.setUserRate(new HashMap<>());
    }

    @Test
    public void testUpdateInStock() throws NotInStock {
        commodity.updateInStock(-10);
        assertEquals(40, commodity.getInStock());
    }

    @Test
    public void testUpdateInStockThrowsException() {
        assertThrows(NotInStock.class, () -> {
            commodity.updateInStock(-60);
        });
    }

    @Test
    public void testAddRateAndCalcRating() {
        commodity.addRate("user1", 4);
        commodity.addRate("user2", 5);

        assertEquals(2, commodity.getUserRate().size());
        assertEquals(4, commodity.getUserRate().get("user1"));
        assertEquals(5, commodity.getUserRate().get("user2"));

        commodity.calcRating();
        assertEquals(4.25, commodity.getRating(), 0.01);
    }
}
