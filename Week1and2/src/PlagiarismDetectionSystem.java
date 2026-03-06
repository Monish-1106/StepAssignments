import java.util.*;

class PlagiarismDetector {

    static int N = 5; // n-gram size

    // n-gram -> documents
    static HashMap<String, Set<String>> index = new HashMap<>();


    // Extract n-grams from text
    public static List<String> generateNGrams(String text) {

        String[] words = text.toLowerCase().split("\\s+");
        List<String> ngrams = new ArrayList<>();

        for(int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for(int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }


    // Add document to database
    public static void addDocument(String docId, String text) {

        List<String> ngrams = generateNGrams(text);

        for(String gram : ngrams) {

            index.putIfAbsent(gram, new HashSet<>());
            index.get(gram).add(docId);
        }
    }


    // Analyze document for plagiarism
    public static void analyzeDocument(String docId, String text) {

        List<String> ngrams = generateNGrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for(String gram : ngrams) {

            if(index.containsKey(gram)) {

                for(String otherDoc : index.get(gram)) {

                    matchCount.put(otherDoc,
                            matchCount.getOrDefault(otherDoc, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for(String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);
            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with \"" + doc + "\"");

            System.out.println("Similarity: " +
                    String.format("%.1f", similarity) + "%");

            if(similarity > 50) {
                System.out.println("⚠ PLAGIARISM DETECTED");
            }

            System.out.println();
        }
    }


    public static void main(String[] args) {

        // Existing documents
        addDocument("essay_089.txt",
                "data structures and algorithms are important for programming");

        addDocument("essay_092.txt",
                "data structures and algorithms are important for programming and learning");


        // New submission
        String newEssay =
                "data structures and algorithms are important for programming";

        analyzeDocument("essay_123.txt", newEssay);
    }
}
