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
        boolean prevTabPressed = false;
        while (true) {
            int key = System.in.read();

            // Handle Enter key
            if (key == 10) {
                System.out.println();
                return input.toString();
            }
            // Handle Tab key
            else if (key == 9) {
                ArrayList<String> completion = findCompletion(input.toString());
                if (completion.size() == 1) {
                    clearCurrentLine(input.length());
                    input = new StringBuilder(completion.get(0));
                    input.append(" ");
                    System.out.printf("%s ", completion.get(0));
                } else if (completion.size() > 1) {
                    if (!prevTabPressed) {
                        System.out.print("\u0007"); // sending alert if first tab
                        prevTabPressed = true;
                        continue;
                    } else {
                        System.out.println();
                        for (int i = 0; i < completion.size(); i++) {
                            System.out.printf("%s  ", completion.get(i));
                        }
                        System.out.println();
                        System.out.printf("$ %s", input.toString());
                    }
                } else {
                    System.out.print("\u0007"); // sending alert if not found
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
            prevTabPressed = false;
        }
    }

    private ArrayList<String> findCompletion(String prefix) {
        HashSet<String> cmds = new HashSet<>();

        for (int i = 0; i < strings.size(); i++) {
            String cmd = strings.get(i);
            if (cmd.startsWith(prefix))
                cmds.add(cmd);
        }

        findUserProgram(prefix, cmds);

        ArrayList<String> uniqueCmds = new ArrayList<>(cmds);
        Collections.sort(uniqueCmds);
        return uniqueCmds;
    }

    private void findUserProgram(String prefix, HashSet<String> cmds) {
        String PATH = System.getenv("PATH");
        String pathSep = Shell.getEnvPathSep();
        String[] paths = PATH.split(pathSep);

        for (String path : paths) {
            File folder = new File(path);
            if (!Shell.isDirectoryExists(path))
                continue;

            File[] files = folder.listFiles(File::isFile);
            if (files == null)
                continue;

            for (File file : files) {
                String fileName = file.getName();

                if (fileName.startsWith(prefix)) {
                    cmds.add(fileName);
                }
            }
        }
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
