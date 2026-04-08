import java.util.*;

public class RiskThresholdBinaryLookup {

    static int linearComparisons = 0;
    static int binaryComparisons = 0;

    // 🔹 Linear Search (unsorted)
    public static int linearSearch(int[] arr, int target) {
        linearComparisons = 0;

        for (int i = 0; i < arr.length; i++) {
            linearComparisons++;
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    // 🔹 Binary Search (find insertion point / lower_bound)
    public static int lowerBound(int[] arr, int target) {
        int low = 0, high = arr.length;
        binaryComparisons = 0;

        while (low < high) {
            int mid = (low + high) / 2;
            binaryComparisons++;

            if (arr[mid] < target)
                low = mid + 1;
            else
                high = mid;
        }
        return low; // insertion index
    }

    // 🔹 Upper Bound (first > target)
    public static int upperBound(int[] arr, int target) {
        int low = 0, high = arr.length;

        while (low < high) {
            int mid = (low + high) / 2;

            if (arr[mid] <= target)
                low = mid + 1;
            else
                high = mid;
        }
        return low;
    }

    // 🔹 Floor (largest ≤ target)
    public static Integer floor(int[] arr, int target) {
        int idx = lowerBound(arr, target);

        if (idx < arr.length && arr[idx] == target)
            return arr[idx];
        if (idx == 0)
            return null;

        return arr[idx - 1];
    }

    // 🔹 Ceiling (smallest ≥ target)
    public static Integer ceiling(int[] arr, int target) {
        int idx = lowerBound(arr, target);

        if (idx == arr.length)
            return null;

        return arr[idx];
    }

    // 🔹 Main Method
    public static void main(String[] args) {

        int[] risks = {10, 25, 50, 100}; // sorted

        // Linear Search (unsorted case simulation)
        int linearIndex = linearSearch(risks, 30);
        System.out.println("Linear Search (30): " +
                (linearIndex == -1 ? "Not found" : "Found at " + linearIndex));
        System.out.println("Linear Comparisons: " + linearComparisons);

        // Binary Search (insertion point)
        int insertPos = lowerBound(risks, 30);
        System.out.println("Insertion Position for 30: " + insertPos);
        System.out.println("Binary Comparisons: " + binaryComparisons);

        // Floor & Ceiling
        Integer floorVal = floor(risks, 30);
        Integer ceilVal = ceiling(risks, 30);

        System.out.println("Floor (≤30): " + floorVal);
        System.out.println("Ceiling (≥30): " + ceilVal);
    }
}