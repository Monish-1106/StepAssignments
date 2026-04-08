import java.util.*;

class Trade {
    String id;
    int volume;

    Trade(String id, int volume) {
        this.id = id;
        this.volume = volume;
    }

    public String toString() {
        return id + ":" + volume;
    }
}

public class HistoricalTradeVolumeAnalysis {

    // 🔹 Merge Sort (Ascending - Stable)
    public static void mergeSort(Trade[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    private static void merge(Trade[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Trade[] L = new Trade[n1];
        Trade[] R = new Trade[n2];

        for (int i = 0; i < n1; i++)
            L[i] = arr[left + i];
        for (int j = 0; j < n2; j++)
            R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (L[i].volume <= R[j].volume) { // Stable
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }

        while (i < n1)
            arr[k++] = L[i++];

        while (j < n2)
            arr[k++] = R[j++];
    }

    // 🔹 Quick Sort (Descending)
    public static void quickSort(Trade[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);

            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(Trade[] arr, int low, int high) {
        int pivot = arr[high].volume; // Lomuto pivot
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j].volume > pivot) { // DESC
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(Trade[] arr, int i, int j) {
        Trade temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // 🔹 Merge Two Sorted Lists (Ascending)
    public static Trade[] mergeTwoSorted(Trade[] a, Trade[] b) {
        int i = 0, j = 0, k = 0;
        Trade[] result = new Trade[a.length + b.length];

        while (i < a.length && j < b.length) {
            if (a[i].volume <= b[j].volume) {
                result[k++] = a[i++];
            } else {
                result[k++] = b[j++];
            }
        }

        while (i < a.length)
            result[k++] = a[i++];

        while (j < b.length)
            result[k++] = b[j++];

        return result;
    }

    // 🔹 Total Volume
    public static int totalVolume(Trade[] arr) {
        int sum = 0;
        for (Trade t : arr) {
            sum += t.volume;
        }
        return sum;
    }

    // 🔹 Main Method
    public static void main(String[] args) {

        Trade[] trades = {
                new Trade("trade3", 500),
                new Trade("trade1", 100),
                new Trade("trade2", 300)
        };

        // Merge Sort (Ascending)
        Trade[] mergeArr = trades.clone();
        mergeSort(mergeArr, 0, mergeArr.length - 1);
        System.out.println("Merge Sort (Ascending): " + Arrays.toString(mergeArr));

        // Quick Sort (Descending)
        Trade[] quickArr = trades.clone();
        quickSort(quickArr, 0, quickArr.length - 1);
        System.out.println("Quick Sort (Descending): " + Arrays.toString(quickArr));

        // Merge two sorted lists (example: morning + afternoon)
        Trade[] morning = {
                new Trade("t1", 100),
                new Trade("t2", 300)
        };

        Trade[] afternoon = {
                new Trade("t3", 200),
                new Trade("t4", 400)
        };

        mergeSort(morning, 0, morning.length - 1);
        mergeSort(afternoon, 0, afternoon.length - 1);

        Trade[] merged = mergeTwoSorted(morning, afternoon);
        System.out.println("Merged Trades: " + Arrays.toString(merged));

        // Total volume
        int total = totalVolume(merged);
        System.out.println("Total Volume: " + total);
    }
}