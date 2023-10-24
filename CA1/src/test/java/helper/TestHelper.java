package helper;

import model.Commodity;
import model.CommodityTest;
import model.User;

import java.util.HashMap;

public class TestHelper {
    public static User createAnonymousUser() {
        return new User("username", "password", "email", "date", "address");
    }

    public static User createUserWithCredit(float credit) {
        var user = createAnonymousUser();
        user.setCredit(credit);
        return user;
    }

    public static User createUserWithItemInBuyList(String commodityId, int quantity) {
        var user = createAnonymousUser();
        user.setBuyList(new HashMap<String, Integer>() {{
            put(commodityId, quantity);
        }});
        return user;
    }

    public static User createUserWithItemInPurchasedList(String commodityId, int quantity) {
        var user = createAnonymousUser();
        user.setPurchasedList(new HashMap<>() {{
            put(commodityId, quantity);
        }});
        return user;
    }

    public static Commodity createAnonymousCommodity() {
        var commodity = new Commodity();
        commodity.setId("id");
        return commodity;
    }

    public static Commodity createCommodityWithStock(int stock) {
        var commodity = createAnonymousCommodity();
        commodity.setInStock(stock);
        return commodity;
    }

    public static Commodity createCommodityWithUserRating(String username, int rating) {
        var commodity = createAnonymousCommodity();
        commodity.setUserRate(new HashMap<>(){{
            put(username, rating);
        }});
        return commodity;
    }

    public static Commodity createCommodityWithInitialRating(int initialRating) {
        var commodity = createAnonymousCommodity();
        commodity.setInitRate(initialRating);
        return commodity;
    }
}
