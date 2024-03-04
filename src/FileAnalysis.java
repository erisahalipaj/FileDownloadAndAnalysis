import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FileAnalysis {
    // Constants for the URL of the file to be downloaded and the path where it will be saved locally
    private static final String FILE_URL = "https://www.w3.org/TR/2003/REC-PNG-20031110/iso_8859-1.txt";
    private static final String DOWNLOAD_PATH = "downloaded_file.txt";

    public static void main(String[] args) {
        // Main method it calls downloadFile method to read from the URL and write to the file with the specified path locally 
        //and calles analyzeFile method to print each line and  count the occurance of every character and print them on reveresed order 
        try {
            downloadFile(FILE_URL, DOWNLOAD_PATH);
            analyzeFile(DOWNLOAD_PATH);
        } catch (IOException e) {
            // Catching and printing any I/O errors that might occur during the process
            e.printStackTrace();
        }
    }

    // Method to download a file from the specified URL and save it to a local path
    private static void downloadFile(String fileUrl, String downloadPath) throws IOException {
        URL url = new URL(fileUrl); // Creating a URL object from the string URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Opening a connection to the URL
        try (InputStream inputStream = connection.getInputStream(); // Getting the input stream from the connection
             FileOutputStream outputStream = new FileOutputStream(downloadPath)) { // Opening a file output stream to the download path
            byte[] buffer = new byte[1024]; // Buffer for reading data
            int bytesRead;
            // Reading from the input stream and writing to the file output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("File downloaded: " + downloadPath); //Just a print statement confirming file download
        } finally {
            connection.disconnect(); // Cloding connection
        }
    }

    // Method to analyze the downloaded file by printing lines and counting character occurrences
    private static void analyzeFile(String filePath) throws IOException {
        Map<Character, Integer> charOccurrences = new HashMap<>(); // Map to track character occurrences
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) { // Opening the file for reading
            String line;
            int lineNum = 0; // Line counter
            // Reading the file line by line
            while ((line = reader.readLine()) != null) {
                System.out.println("Line " + (++lineNum) + ": " + line); // Printing each line with its number
                // Counting occurrences of each character in the line
                for (char c : line.toCharArray()) {
                    charOccurrences.merge(c, 1, Integer::sum); // Updating the occurrence count for each character
                }
            }
        }

        // Sorting the character occurrence map by value  in descending order
        Map<Character, Integer> sortedMap = charOccurrences.entrySet()
                .stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new)); // Collecting the results in a LinkedHashMap to preserve order

        System.out.println("\nCharacter occurrences, sorted by frequency:");
        sortedMap.forEach((character, count) -> System.out.println(character + ": " + count)); // Printing each character and its count
    }
}
