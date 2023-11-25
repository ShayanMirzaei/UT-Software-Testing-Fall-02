package utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import org.json.JSONString;

import java.util.HashMap;
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

    private static String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
