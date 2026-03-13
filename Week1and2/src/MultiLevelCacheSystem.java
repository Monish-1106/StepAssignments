import java.util.*;

public class MultiLevelCacheSystem {

    // Video data
    static class Video {
        String id;
        String data;

        Video(String id, String data) {
            this.id = id;
            this.data = data;
        }
    }

    // L1 Cache (Memory) using LRU
    static class LRUCache<K, V> extends LinkedHashMap<K, V> {

        private int capacity;

        LRUCache(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    private LRUCache<String, Video> L1;
    private LRUCache<String, Video> L2;
    private Map<String, Video> L3;

    private int L1Hits = 0;
    private int L2Hits = 0;
    private int L3Hits = 0;

    public MultiLevelCacheSystem() {

        L1 = new LRUCache<>(10000);
        L2 = new LRUCache<>(100000);
        L3 = new HashMap<>();

        // Simulated database videos
        L3.put("video_123", new Video("video_123", "Video Data 123"));
        L3.put("video_999", new Video("video_999", "Video Data 999"));
    }

    public Video getVideo(String videoId) {

        // L1 check
        if (L1.containsKey(videoId)) {
            L1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 check
        if (L2.containsKey(videoId)) {

            L2Hits++;
            System.out.println("L2 Cache HIT (5ms)");

            Video v = L2.get(videoId);

            // Promote to L1
            L1.put(videoId, v);
            System.out.println("Promoted to L1");

            return v;
        }

        System.out.println("L2 Cache MISS");

        // L3 database
        if (L3.containsKey(videoId)) {

            L3Hits++;
            System.out.println("L3 Database HIT (150ms)");

            Video v = L3.get(videoId);

            // Add to L2
            L2.put(videoId, v);
            System.out.println("Added to L2");

            return v;
        }

        System.out.println("Video not found");
        return null;
    }

    // Cache statistics
    public void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        double l1Rate = total == 0 ? 0 : (L1Hits * 100.0 / total);
        double l2Rate = total == 0 ? 0 : (L2Hits * 100.0 / total);
        double l3Rate = total == 0 ? 0 : (L3Hits * 100.0 / total);

        System.out.println("\nCache Statistics:");
        System.out.println("L1 Hit Rate: " + String.format("%.2f", l1Rate) + "%");
        System.out.println("L2 Hit Rate: " + String.format("%.2f", l2Rate) + "%");
        System.out.println("L3 Hit Rate: " + String.format("%.2f", l3Rate) + "%");
    }

    public static void main(String[] args) {

        MultiLevelCacheSystem cache = new MultiLevelCacheSystem();

        System.out.println("Request 1:");
        cache.getVideo("video_123");

        System.out.println("\nRequest 2:");
        cache.getVideo("video_123");

        System.out.println("\nRequest 3:");
        cache.getVideo("video_999");

        cache.getStatistics();
    }
}
