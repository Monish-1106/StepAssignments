import java.util.*;

class Client {
    String name;
    int riskScore;
    double accountBalance;

    Client(String name, int riskScore, double accountBalance) {
        this.name = name;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }

    public String toString() {
        return name + ":" + riskScore;
    }
}

public class ClientRisk {

    // 🔹 Bubble Sort (Ascending riskScore)
    public static void bubbleSort(Client[] arr) {
        int n = arr.length;
        int swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].riskScore > arr[j + 1].riskScore) {
                    // swap
                    Client temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;

                    swaps++;
                    swapped = true;

                    // Visualization of swap
                    System.out.println("Swap: " + Arrays.toString(arr));
                }
            }

            if (!swapped) break; // optimization
        }

        System.out.println("Bubble Sorted (Ascending): " + Arrays.toString(arr));
        System.out.println("Total Swaps: " + swaps);
    }

    // 🔹 Insertion Sort (Descending riskScore + accountBalance)
    public static void insertionSort(Client[] arr) {
        int n = arr.length;

        for (int i = 1; i < n; i++) {
            Client key = arr[i];
            int j = i - 1;

            while (j >= 0 && compare(arr[j], key) < 0) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }

        System.out.println("Insertion Sorted (Descending): " + Arrays.toString(arr));
    }

    // 🔹 Comparator (DESC riskScore, if equal then accountBalance)
    private static int compare(Client c1, Client c2) {
        if (c1.riskScore != c2.riskScore)
            return Integer.compare(c1.riskScore, c2.riskScore);
        else
            return Double.compare(c1.accountBalance, c2.accountBalance);
    }

    // 🔹 Top 10 Highest Risk Clients
    public static void topRiskClients(Client[] arr) {
        System.out.print("Top Risk Clients: ");

        int limit = Math.min(10, arr.length);

        for (int i = 0; i < limit; i++) {
            System.out.print(arr[i].name + "(" + arr[i].riskScore + ") ");
        }
        System.out.println();
    }

    // 🔹 Main Method
    public static void main(String[] args) {

        Client[] clients = {
                new Client("clientC", 80, 5000),
                new Client("clientA", 20, 2000),
                new Client("clientB", 50, 3000)
        };

        // Bubble Sort (Ascending)
        Client[] bubbleArr = clients.clone();
        bubbleSort(bubbleArr);

        // Insertion Sort (Descending)
        Client[] insertionArr = clients.clone();
        insertionSort(insertionArr);

        // Top Risk Clients
        topRiskClients(insertionArr);
    }
}