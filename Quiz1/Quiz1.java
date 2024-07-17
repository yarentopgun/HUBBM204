import java.util.*;
import java.io.*;
public class Quiz1 {

    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.ENGLISH);

        String inputFile = args[0]; // Assuming first argument is the input file path
        List<String> titles = new ArrayList<>();
        Set<String> ignoreWords = new HashSet<>();
        boolean readingIgnoreWords = true;


        FileInputStream file = new FileInputStream(inputFile);
        Scanner scanner = new Scanner(file);


        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.equals("...")) {
                readingIgnoreWords = false; // Switch to reading titles
                continue;
            }
            if (readingIgnoreWords) {
                ignoreWords.add(line.toLowerCase());
            } else {
                titles.add(line);
            }
        }


        // Process titles to generate KWIC index
        List<String> kwicIndex = generateKWICIndex(titles, ignoreWords);

        // Output the result
        for (String title : kwicIndex) {
            System.out.println(title);
        }

    }

    private static List<String> generateKWICIndex(List<String> titles, Set<String> ignoreWords) {
        // Temporary storage for keywords, original title index, and the modified title.
        List<String[]> tempStorage = new ArrayList<>();

        for (int titleIndex = 0; titleIndex < titles.size(); titleIndex++) {
            String originalTitle = titles.get(titleIndex).trim(); // Trim to remove leading and trailing spaces
            String[] words = originalTitle.split("\\s+"); // Split by one or more spaces

            for (int i = 0; i < words.length; i++) {
                String wordLowerCase = words[i].toLowerCase();
                // Check if the word is not in the ignore list and not empty
                if (!ignoreWords.contains(wordLowerCase) && !words[i].isEmpty()) {
                    StringBuilder modifiedTitle = new StringBuilder();
                    for (int j = 0; j < words.length; j++) {
                        if (j > 0) modifiedTitle.append(" ");
                        modifiedTitle.append(j == i ? words[j].toUpperCase() : words[j].toLowerCase());
                    }
                    // Store the keyword for sorting, the original title's index for maintaining initial order, and the modified title
                    tempStorage.add(new String[]{wordLowerCase, String.valueOf(titleIndex), modifiedTitle.toString()});
                }
            }
        }

        // Sort by keyword, then by original order of titles when keywords match
        tempStorage.sort((entry1, entry2) -> {
            int keywordComparison = entry1[0].compareTo(entry2[0]);
            if (keywordComparison == 0) {
                return Integer.compare(Integer.parseInt(entry1[1]), Integer.parseInt(entry2[1]));
            }
            return keywordComparison;
        });

        // Extract the modified titles in sorted order
        List<String> sortedTitles = new ArrayList<>();
        for (String[] entry : tempStorage) {
            sortedTitles.add(entry[2]);
        }

        return sortedTitles;
    }
}
