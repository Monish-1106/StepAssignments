import java.util.LinkedHashMap;
import java.util.Map;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    // Constructor
    DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
    }

    // Check if entry expired
    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

class DNSCacheSystem {

    static final int MAX_CACHE_SIZE = 5;

    // LRU Cache using LinkedHashMap
    static LinkedHashMap<String, DNSEntry> cache =
            new LinkedHashMap<String, DNSEntry>(MAX_CACHE_SIZE, 0.75f, true) {

                protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                    return size() > MAX_CACHE_SIZE;
                }
            };

    static int hits = 0;
    static int misses = 0;

    // Simulated upstream DNS lookup
    public static String queryUpstreamDNS(String domain) {

        if(domain.equals("google.com"))
            return "172.217.14.206";

        if(domain.equals("facebook.com"))
            return "157.240.22.35";

        return "192.168.1.1";
    }

    // Resolve domain
    public static String resolve(String domain) {

        long start = System.nanoTime();

        if(cache.containsKey(domain)) {

            DNSEntry entry = cache.get(domain);

            if(!entry.isExpired()) {
                hits++;

                long time = System.nanoTime() - start;

                return "Cache HIT → " + entry.ipAddress +
                        " (retrieved in " + time / 1_000_000.0 + " ms)";
            }

            // remove expired entry
            cache.remove(domain);
        }

        misses++;

        String ip = queryUpstreamDNS(domain);

        DNSEntry entry = new DNSEntry(domain, ip, 300);
        cache.put(domain, entry);

        return "Cache MISS → Query upstream → " + ip + " (TTL: 300s)";
    }

    // Cache statistics
    public static void getCacheStats() {

        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0) / total;

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    // Main method
    public static void main(String[] args) {

        System.out.println(resolve("google.com"));
        System.out.println(resolve("google.com"));
        System.out.println(resolve("facebook.com"));

        getCacheStats();
    }
}
