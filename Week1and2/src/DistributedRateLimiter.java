import java.util.concurrent.ConcurrentHashMap;

class RateLimiterApp {

    // TokenBucket class
    static class TokenBucket {
        private int tokens;
        private final int maxTokens;
        private final double refillRate;
        private long lastRefillTime;

        public TokenBucket(int maxTokens, double refillRate) {
            this.maxTokens = maxTokens;
            this.refillRate = refillRate;
            this.tokens = maxTokens;
            this.lastRefillTime = System.currentTimeMillis();
        }

        private void refill() {
            long now = System.currentTimeMillis();
            double tokensToAdd = (now - lastRefillTime) / 1000.0 * refillRate;

            if (tokensToAdd > 0) {
                tokens = Math.min(maxTokens, tokens + (int) tokensToAdd);
                lastRefillTime = now;
            }
        }

        public synchronized boolean allowRequest() {
            refill();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }

        public synchronized int getRemainingTokens() {
            refill();
            return tokens;
        }
    }

    // RateLimiter class
    static class RateLimiter {

        private static final int MAX_REQUESTS = 1000;
        private static final double REFILL_RATE = 1000.0 / 3600.0;

        private ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

        public String checkRateLimit(String clientId) {

            TokenBucket bucket = buckets.computeIfAbsent(
                    clientId,
                    id -> new TokenBucket(MAX_REQUESTS, REFILL_RATE)
            );

            boolean allowed = bucket.allowRequest();

            if (allowed) {
                int remaining = bucket.getRemainingTokens();
                return "Allowed (" + remaining + " requests remaining)";
            } else {
                return "Denied (0 requests remaining, retry later)";
            }
        }

        public String getRateLimitStatus(String clientId) {

            TokenBucket bucket = buckets.get(clientId);

            if (bucket == null) {
                return "{used:0, limit:1000}";
            }

            int remaining = bucket.getRemainingTokens();
            int used = MAX_REQUESTS - remaining;

            return "{used:" + used + ", limit:" + MAX_REQUESTS + "}";
        }
    }

    // Main method
    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();
        String clientId = "abc123";

        for (int i = 0; i < 5; i++) {
            System.out.println(limiter.checkRateLimit(clientId));
        }

        System.out.println(limiter.getRateLimitStatus(clientId));
    }
}
