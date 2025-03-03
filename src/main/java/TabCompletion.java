import java.io.*;
import java.util.*;

public class TabCompletion {
    private String originalTerminalSettings;
    private List<String> strings;

    public TabCompletion(List<String> words) {
        strings = words;
        try {
            initializeTerminal();
        } catch (Exception e) {
            handleError("An error occurred: ", e);
        }
    }

    String readLine() throws IOException {
        StringBuilder input = new StringBuilder();

        while (true) {
            int key = System.in.read();

            // Handle Enter key
            if (key == 10) {
                System.out.println();
                return input.toString();
            }
            // Handle Tab key
            else if (key == 9) {
                String completion = findCompletion(input.toString());
                if (completion != null) {
                    clearCurrentLine(input.length());
                    input = new StringBuilder(completion);
                    System.out.printf("%s ", completion);
                }
            }
            // Handle Backspace (127 in most Unix shells, 8 in others)
            else if (key == 127 || key == 8) {
                if (input.length() > 0) {
                    input.deleteCharAt(input.length() - 1);
                    System.out.print("\b \b"); // Erase last character
                }
            }
            // Regular characters
            else if (key >= 32 && key <= 126) { // Printable ASCII range
                input.append((char) key);
                System.out.print((char) key);
            }
        }
    }

    private String findCompletion(String prefix) {
        return strings.stream()
                .filter(word -> word.startsWith(prefix))
                .findFirst()
                .orElse(null);
    }

    private void clearCurrentLine(int length) {
        for (int i = 0; i < length; i++) {
            System.out.print("\b \b"); // Backspace and clear each character
        }
    }

    private void initializeTerminal() throws IOException, InterruptedException {
        // Save original terminal settings
        originalTerminalSettings = stty("--save");

        // Set up terminal
        stty("-echo"); // Disable echo
        stty("-icanon"); // Disable buffered input

        // Add shutdown hook to restore terminal
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                stty(originalTerminalSettings);
            } catch (Exception e) {
                handleError("Failed to restore terminal: ", e);
            }
        }));
    }

    private String stty(String args) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("sh", "-c", "stty " + args + " < /dev/tty").start();
        return readOutput(process);
    }

    private String readOutput(Process process) throws IOException {
        try (InputStream in = process.getInputStream()) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString().trim();
        }
    }

    private void handleError(String message, Exception e) {
        System.err.println(message + e.getMessage());
        e.printStackTrace();
    }
}
