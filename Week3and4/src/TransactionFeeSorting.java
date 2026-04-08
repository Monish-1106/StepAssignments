import java.util.*;

class Transaction {
    String id;
    double fee;
    String timestamp;

    Transaction(String id, double fee, String timestamp) {
        this.id = id;
        this.fee = fee;
        this.timestamp = timestamp;
    }

    public String toString() {
        return id + ":" + fee + "@" + timestamp;
    }
}

public class TransactionFeeSorting {

    // 🔹 Bubble Sort (by fee)
    public static void bubbleSortByFee(ArrayList<Transaction> list) {
        int n = list.size();
        int swaps = 0, passes = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            passes++;

            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).fee > list.get(j + 1).fee) {
                    Transaction temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swaps++;
                    swapped = true;
                }
            }

            if (!swapped) break; // Early stop
        }

        System.out.println("Bubble Sort Result: " + list);
        System.out.println("Passes: " + passes + ", Swaps: " + swaps);
    }

    // 🔹 Insertion Sort (by fee + timestamp)
    public static void insertionSort(ArrayList<Transaction> list) {
        int n = list.size();

        for (int i = 1; i < n; i++) {
            Transaction key = list.get(i);
            int j = i - 1;

            while (j >= 0 && compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }

            list.set(j + 1, key);
        }

        System.out.println("Insertion Sort Result: " + list);
    }

    // 🔹 Comparator (fee first, then timestamp)
    private static int compare(Transaction t1, Transaction t2) {
        if (t1.fee != t2.fee)
            return Double.compare(t1.fee, t2.fee);
        else
            return t1.timestamp.compareTo(t2.timestamp);
    }

    // 🔹 Outlier Detection (>50)
    public static void findHighFee(ArrayList<Transaction> list) {
        System.out.print("High-fee outliers: ");
        boolean found = false;

        for (Transaction t : list) {
            if (t.fee > 50) {
                System.out.print(t + " ");
                found = true;
            }
        }

        if (!found) {
            System.out.print("none");
        }
        System.out.println();
    }

    // 🔹 Main Method
    public static void main(String[] args) {

        ArrayList<Transaction> transactions = new ArrayList<>();

        // Sample Input
        transactions.add(new Transaction("id1", 10.5, "10:00"));
        transactions.add(new Transaction("id2", 25.0, "09:30"));
        transactions.add(new Transaction("id3", 5.0, "10:15"));

        // Bubble Sort (small batch)
        ArrayList<Transaction> bubbleList = new ArrayList<>(transactions);
        bubbleSortByFee(bubbleList);

        // Insertion Sort (medium batch)
        ArrayList<Transaction> insertionList = new ArrayList<>(transactions);
        insertionSort(insertionList);

        // Outlier Detection
        findHighFee(transactions);
    }
}