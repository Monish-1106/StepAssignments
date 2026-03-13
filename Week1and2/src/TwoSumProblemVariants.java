import java.util.*;

class FinancialTransactionSystem {

    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        int time; // minutes (example: 10:30 -> 630)

        Transaction(int id, int amount, String merchant, String account, int time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }
    }

    // Classic Two Sum
    public static List<String> findTwoSum(List<Transaction> list, int target) {

        Map<Integer, Transaction> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : list) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction other = map.get(complement);
                result.add("(id:" + other.id + ", id:" + t.id + ")");
            }

            map.put(t.amount, t);
        }

        return result;
    }

    // Two Sum within 1 hour
    public static List<String> findTwoSumTimeWindow(List<Transaction> list, int target) {

        List<String> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {

            for (int j = i + 1; j < list.size(); j++) {

                Transaction a = list.get(i);
                Transaction b = list.get(j);

                if (a.amount + b.amount == target && Math.abs(a.time - b.time) <= 60) {

                    result.add("(id:" + a.id + ", id:" + b.id + ")");
                }
            }
        }

        return result;
    }

    // K Sum (Recursive)
    public static void findKSum(List<Transaction> list, int start, int k, int target,
                                List<Integer> path, List<List<Integer>> result) {

        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(path));
            return;
        }

        if (k == 0) return;

        for (int i = start; i < list.size(); i++) {

            path.add(list.get(i).id);

            findKSum(list, i + 1, k - 1, target - list.get(i).amount, path, result);

            path.remove(path.size() - 1);
        }
    }

    // Duplicate detection
    public static List<String> detectDuplicates(List<Transaction> list) {

        Map<String, List<String>> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : list) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t.account);
        }

        for (String key : map.keySet()) {

            List<String> accounts = map.get(key);

            if (accounts.size() > 1) {

                result.add("{amount-merchant:" + key + ", accounts:" + accounts + "}");
            }
        }

        return result;
    }

    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, 500, "Store A", "acc1", 600));
        transactions.add(new Transaction(2, 300, "Store B", "acc2", 615));
        transactions.add(new Transaction(3, 200, "Store C", "acc3", 630));
        transactions.add(new Transaction(4, 500, "Store A", "acc4", 640));

        System.out.println("Two Sum (target=500):");
        System.out.println(findTwoSum(transactions, 500));

        System.out.println("\nTwo Sum within 1 hour:");
        System.out.println(findTwoSumTimeWindow(transactions, 500));

        List<List<Integer>> ksumResult = new ArrayList<>();
        findKSum(transactions, 0, 3, 1000, new ArrayList<>(), ksumResult);

        System.out.println("\nK-Sum (k=3 target=1000):");
        System.out.println(ksumResult);

        System.out.println("\nDuplicate Transactions:");
        System.out.println(detectDuplicates(transactions));
    }
}
