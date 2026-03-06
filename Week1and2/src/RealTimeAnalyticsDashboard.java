import java.util.*;

class PageEvent {
    String url;
    String userId;
    String source;

    PageEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

class RealTimeAnalytics {

    // page → visit count
    static HashMap<String, Integer> pageViews = new HashMap<>();

    // page → unique users
    static HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // source → count
    static HashMap<String, Integer> trafficSources = new HashMap<>();


    // Process incoming event
    public static void processEvent(PageEvent event) {

        // update page views
        pageViews.put(event.url,
                pageViews.getOrDefault(event.url, 0) + 1);

        // update unique visitors
        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

        // update traffic source
        trafficSources.put(event.source,
                trafficSources.getOrDefault(event.source, 0) + 1);
    }


    // Get top 10 pages
    public static List<Map.Entry<String, Integer>> getTopPages() {

        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        return list.subList(0, Math.min(10, list.size()));
    }


    // Display dashboard
    public static void getDashboard() {

        System.out.println("Top Pages:");

        int rank = 1;

        for(Map.Entry<String, Integer> entry : getTopPages()) {

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url +
                    " - " + views + " views (" +
                    unique + " unique)");

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        int total = trafficSources.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        for(String source : trafficSources.keySet()) {

            int count = trafficSources.get(source);
            double percent = (count * 100.0) / total;

            System.out.println(source + ": " +
                    String.format("%.1f", percent) + "%");
        }
    }


    public static void main(String[] args) {

        processEvent(new PageEvent("/article/breaking-news","user_123","Google"));
        processEvent(new PageEvent("/article/breaking-news","user_456","Facebook"));
        processEvent(new PageEvent("/sports/championship","user_123","Direct"));
        processEvent(new PageEvent("/sports/championship","user_789","Google"));
        processEvent(new PageEvent("/article/breaking-news","user_789","Google"));

        getDashboard();
    }
}
