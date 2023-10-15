import java.io.*;
import java.util.*;

public class DirectoryAnalyzer {

    public static void main(String[] args) throws IOException {
        String directoryPath = "C:\\Users\\Lenovo\\Desktop\\JavaProject11";
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Invalid directory path.");
            System.exit(1);
        }

        List<File> textFiles = findTextFiles(directory);
        long totalSize = calculateTotalSize(textFiles);
        Map<String, Integer> wordFrequency = countWordFrequency(textFiles);

        System.out.println("List of .txt files:");
        textFiles.forEach(file -> System.out.println(file.getName()));
        System.out.println("Total size of .txt files: " + totalSize + " bytes");

        System.out.println("\nTop 10 most frequent words:");
        wordFrequency.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue() + " occurrences"));
    }

    private static List<File> findTextFiles(File directory) {
        List<File> textFiles = new ArrayList<>();
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                textFiles.addAll(findTextFiles(file));
            } else if (file.getName().toLowerCase().endsWith(".txt")) {
                textFiles.add(file);
            }
        }
        return textFiles;
    }

    private static long calculateTotalSize(List<File> files) {
        return files.stream().mapToLong(File::length).sum();
    }

    private static Map<String, Integer> countWordFrequency(List<File> files) throws IOException {
        Map<String, Integer> wordFrequency = new HashMap<>();
        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        word = word.toLowerCase();
                        word = word.replaceAll("[^a-zA-Z]", "");
                        if (!word.isEmpty()) {
                            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                        }
                    }
                }
            }
        }
        return wordFrequency;
    }
}