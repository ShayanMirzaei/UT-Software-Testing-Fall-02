package utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Comment;
import model.Commodity;
import model.User;
import org.json.JSONString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Helper {

    public static User getUserInDatabaseWithUsername(String username) {
        if (Objects.equals(username, "ali")) {
            var user = new User("ali", "123", "ali@gmail.com", "2000-01-01", "Tehran, Iran");
            user.setCredit(1000);
            return user;
        } else if (Objects.equals(username, "amin")) {
            var user = new User("amin", "456", "amin@gmail.com", "2000-01-01", "Tehran, Iran");
            user.setCredit(5000);
            return user;
        }
        return new User();
    }

    public static String createAddCreditBody(double credit) throws Exception {
        var map = new HashMap<String, String>();
        map.put("credit", String.valueOf(credit));
        return asJsonString(map);
    }

    public static String createAddCreditBodyWithString(String credit) throws Exception {
        var map = new HashMap<String, String>();
        map.put("credit", credit);
        return asJsonString(map);
    }

    public static String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }

    public static Commodity getCommodityInDatabaseById(String id) {
        var categories = new ArrayList<String>();
        categories.add("phone");
        categories.add("tech");
        if (Objects.equals(id, "1")) {
            var commodity1 = new Commodity();
            commodity1.setId("1");
            commodity1.setName("iPhone");
            commodity1.setProviderId("1");
            commodity1.setPrice(100);
            commodity1.setCategories(categories);
            commodity1.setInStock(100);
            commodity1.setImage("");
            commodity1.setRating(9.8F);
            return commodity1;
        } else if (Objects.equals(id, "2")) {
            var commodity2 = new Commodity();
            commodity2.setId("2");
            commodity2.setName("Galaxy");
            commodity2.setProviderId("2");
            commodity2.setPrice(100);
            commodity2.setCategories(categories);
            commodity2.setInStock(100);
            commodity2.setImage("");
            commodity2.setRating(8.8F);
            return commodity2;
        }
        return new Commodity();
    }

    public static List<Commodity> getCommoditiesInDatabase() {
        var commodity1 = getCommodityInDatabaseById("1");

        var commodity2 = getCommodityInDatabaseById("2");

        var commodities = new ArrayList<Commodity>();
        commodities.add(commodity1);
        commodities.add(commodity2);

        return commodities;
    }

    public static String createRateCommodityBodyWithString(String username, int rate) throws Exception {
        var map = new HashMap<String, String>();
        map.put("username", username);
        map.put("rate", String.valueOf(rate));
        return asJsonString(map);
    }

    public static String createRateCommodityBodyWithInvalidRate(String username) throws Exception {
        var map = new HashMap<String, String>();
        map.put("username", username);
        map.put("rate", "invalid_rate");
        return asJsonString(map);
    }

    public static String createAddCommentBody(String username) throws Exception {
        var map = new HashMap<String, String>();
        map.put("username", username);
        map.put("comment", "comment");
        return asJsonString(map);
    }

    public static List<Comment> getCommodityCommentsInDatabase(String commodityId) {
        var comments = new ArrayList<Comment>();
        var comment = new Comment();
        if (Objects.equals(commodityId, "1")) {
            comment.setCommodityId(1);
            comment.setText("good");
            comment.setId(0);
            comment.setUserEmail("amin@gmail.com");
            comment.setUsername("amin");
            comment.setDate("2023-01-01");
        }
        else {
            comment.setCommodityId(2);
            comment.setText("not bad");
            comment.setId(1);
            comment.setUserEmail("ali@gmail.com");
            comment.setUsername("ali");
            comment.setDate("2023-02-01");
        }
        comments.add(comment);
        return comments;
    }

    public static String createSearchCommoditiesBody(String searchValue, String searchOption) throws Exception {
        var map = new HashMap<String, String>();
        map.put("searchOption", searchOption);
        map.put("searchValue", searchValue);
        return asJsonString(map);
    }
}

