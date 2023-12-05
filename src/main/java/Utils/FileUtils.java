package Utils;

import com.deque.html.axecore.results.Rule;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class FileUtils {
    public static void writeToFile(String filePath, List<Rule> content) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResults = "";
        try {
            // Convert the results object to JSON format
            jsonResults = objectMapper.writeValueAsString(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create a new file with the specified path
        File file = new File(filePath);
        try {
            // Use FileWriter to write content to the file
            java.io.FileWriter writer = new java.io.FileWriter(file);
            writer.write(jsonResults);
            System.out.println("Results stored in " + filePath);
            // Close the writer to release resources
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeToFile(String filePath, Object content) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResults = "";
        try {
            // Convert the results object to JSON format
            jsonResults = objectMapper.writeValueAsString(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create a new file with the specified path
        File file = new File(filePath);
        try {
            // Use FileWriter to write content to the file
            java.io.FileWriter writer = new java.io.FileWriter(file);
            writer.write(jsonResults);
            System.out.println("Results stored in " + filePath);
            // Close the writer to release resources
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + fileName);
                return properties;
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
