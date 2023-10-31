package helper;

import model.User;

import java.util.HashMap;
import java.util.Map;

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
}
