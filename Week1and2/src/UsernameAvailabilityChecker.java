import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsernameAvailabilityChecker {
    // username -> userId
    static HashMap<String, Integer> users = new HashMap<>();

    // username -> attempt count
    static HashMap<String, Integer> attempts = new HashMap<>();

    // Check availability
    public static boolean checkAvailability(String username) {

        // update attempt count
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);

        // check if username exists
        return !users.containsKey(username);
    }

    // Suggest alternative usernames
    public static List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for(int i = 1; i <= 3; i++) {
            String suggestion = username + i;

            if(!users.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        suggestions.add(username.replace("_", "."));

        return suggestions;
    }

    // Find most attempted username
    public static String getMostAttempted() {

        String result = "";
        int max = 0;

        for(String name : attempts.keySet()) {

            if(attempts.get(name) > max) {
                max = attempts.get(name);
                result = name;
            }
        }

        return result + " (" + max + " attempts)";
    }

    public static void main(String[] args) {

        // existing users
        users.put("john_doe", 1);
        users.put("admin", 2);

        System.out.println("john_doe available? " + checkAvailability("john_doe"));
        System.out.println("jane_smith available? " + checkAvailability("jane_smith"));

        System.out.println("Suggestions: " + suggestAlternatives("john_doe"));

        System.out.println("Most Attempted: " + getMostAttempted());
    }
}

