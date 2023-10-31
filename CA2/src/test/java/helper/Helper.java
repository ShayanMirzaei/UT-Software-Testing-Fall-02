package helper;

import model.Comment;
import model.Commodity;
import model.User;

import java.util.*;

public class Helper {

    public static Map<String, String> createLoginInput(String username, String password){
        Map<String, String> input = new HashMap<>();
        input.put("username", username);
        input.put("password", password);
        return input;
    }

    public static Map<String, String> createSignupInput(String username){
        Map<String, String> input = new HashMap<>();
        input.put("username", username);
        input.put("password", "password");
        input.put("address", "address");
        input.put("birthDate", "birthDate");
        input.put("email", "email");
        return input;
    }

    public static User createUser(String username) {
        return new User(username, "password", "email", "birthDate", "address");
    }

    public static boolean usersAreEqual(User u1, User u2) {
        return u2.getUsername().equals(u1.getUsername()) &&
                u2.getPassword().equals(u1.getPassword()) &&
                u2.getEmail().equals(u1.getEmail()) &&
                u2.getBirthDate().equals(u1.getBirthDate()) &&
                u2.getAddress().equals(u1.getAddress());
    }

    public static Map<String, String> createCommentInput(){
        Map<String, String> input = new HashMap<>();
        input.put("username", "username");
        return input;
    }

    public static Comment createComment(int id){
        return new Comment(id, "email", "username", 1, "text");
    }

    public static Commodity createCommodity(String id) {
        var commodity = new Commodity();
        commodity.setId(id);
        return commodity;
    }

    public static ArrayList<Commodity> createCommodities(int n) {
        ArrayList<Commodity> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(createCommodity("id" + n));
        }

        return list;
    }

    public static Map<String, String> createRateCommodityInput(String rate) {
        Map<String, String> input = new HashMap<>();
        input.put("username", "username");
        input.put("rate", rate);
        return input;
    }

    public static Map<String, String> createCommentOnCommodityInput(String username, String comment) {
        Map<String, String> input = new HashMap<>();
        input.put("username", username);
        input.put("comment", comment);
        return input;
    }


    public static boolean commentsAreEqual(Comment c1, Comment c2) {
        return c1.getId() == (c2.getId()) &&
                c1.getText().equals(c2.getText()) &&
                c1.getCommodityId() == (c2.getCommodityId()) &&
                c1.getUsername().equals(c2.getUsername()) &&
                c1.getUserEmail().equals(c2.getUserEmail());
    }

    public static ArrayList<Comment> createComments(int n) {
        ArrayList<Comment> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(createComment(i));
        }

        return list;
    }

    public static Map<String, String> createSearchCommoditiesInput(String searchOption, String searchValue) {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", searchOption);
        input.put("searchValue", searchValue);
        return input;
    }
}

