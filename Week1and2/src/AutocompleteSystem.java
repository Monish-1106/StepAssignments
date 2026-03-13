import java.util.*;

public class AutocompleteSystem {

    // Trie Node
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        List<String> queries = new ArrayList<>();
    }

    // Global frequency map
    private Map<String, Integer> frequencyMap = new HashMap<>();
    private TrieNode root = new TrieNode();

    // Insert query into Trie
    public void insert(String query) {
        TrieNode node = root;

        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            if (!node.queries.contains(query)) {
                node.queries.add(query);
            }
        }

        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);
    }

    // Search top 10 suggestions
    public List<String> search(String prefix) {
        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }

        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> frequencyMap.get(b) - frequencyMap.get(a)
        );

        pq.addAll(node.queries);

        List<String> result = new ArrayList<>();
        int k = 10;

        while (!pq.isEmpty() && k-- > 0) {
            result.add(pq.poll() + " (" + frequencyMap.get(result.size() < node.queries.size() ? node.queries.get(result.size()) : "") + ")");
        }

        return result;
    }

    // Update frequency
    public void updateFrequency(String query) {
        insert(query);
    }

    // Main test
    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.insert("java tutorial");
        system.insert("javascript");
        system.insert("java download");
        system.insert("java tutorial");
        system.insert("java tutorial");
        system.insert("java 21 features");

        System.out.println("Search results for 'jav':");

        List<String> suggestions = system.search("jav");

        int rank = 1;
        for (String s : suggestions) {
            System.out.println(rank++ + ". " + s);
        }

        System.out.println("\nUpdating frequency...");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");

        System.out.println("Updated search results:");

        suggestions = system.search("jav");

        rank = 1;
        for (String s : suggestions) {
            System.out.println(rank++ + ". " + s);
        }
    }
}
