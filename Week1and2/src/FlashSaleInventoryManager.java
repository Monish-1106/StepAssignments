import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class FlashSaleInventoryManager {
    // productId -> stock
    static HashMap<String, Integer> inventory = new HashMap<>();

    // productId -> waiting queue
    static HashMap<String, Queue<Integer>> waitingList = new HashMap<>();


    // Check stock availability
    public static int checkStock(String productId) {
        return inventory.getOrDefault(productId, 0);
    }


    // Purchase item (thread-safe)
    public synchronized static String purchaseItem(String productId, int userId) {

        int stock = inventory.getOrDefault(productId, 0);

        if(stock > 0) {
            inventory.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        }

        // add to waiting list
        waitingList.putIfAbsent(productId, new LinkedList<>());
        waitingList.get(productId).add(userId);

        int position = waitingList.get(productId).size();

        return "Added to waiting list, position #" + position;
    }


    public static void main(String[] args) {

        // initial stock
        inventory.put("IPHONE15_256GB", 100);

        System.out.println("Stock: " + checkStock("IPHONE15_256GB") + " units available");

        System.out.println(purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(purchaseItem("IPHONE15_256GB", 67890));

        // simulate stock finished
        inventory.put("IPHONE15_256GB", 0);

        System.out.println(purchaseItem("IPHONE15_256GB", 99999));
    }
}

