import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class Shell {
    String[] instructions;

    public void repl() {
        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            System.out.print("$ ");
            input = scanner.nextLine();
            instructions = input.split(" ");
            String cmd = instructions[0];

            switch (cmd) {
                case "exit":
                    exit();
                    scanner.close();
                    break;

                case "echo":
                    echo();
                    break;

                case "type":
                    type();
                    break;

                default:
                    String path = getPath(cmd);
                    if (path == null) {
                        System.out.println(input + ": command not found");
                        break;
                    }
                    runProgram();
                    break;
            }

        } while (true);
    }

    void exit() {
        System.exit(0);
    }

    void echo() {
        for (int i = 1; i < instructions.length; i++) {
            System.out.printf("%s ", instructions[i]);
        }
        System.out.println();
    }

    void type() {
        if (instructions.length < 2) {
            System.out.println("usage type <command>");
            return;
        }
        String cmdName = instructions[1];
        if (checkBuiltins(cmdName)) {
            System.out.printf("%s is a shell builtin\n", cmdName);
            return;
        }
        String path = getPath(cmdName);
        if (path != null) {
            System.out.printf("%s is %s\n", cmdName, path);
            return;
        }

        System.out.printf("%s: not found\n", cmdName);

    }

    void runProgram() {
        List<String> cmd = Arrays.asList(instructions);

        ProcessBuilder processBuilder = new ProcessBuilder(instructions);
        try {
            Process process = processBuilder.start();

            // read output from the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // wait untill process completes
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean checkBuiltins(String cmdName) {
        String[] builtins = { "exit", "echo", "type" };
        return Arrays.asList(builtins).contains(cmdName);
    }

    private String getPath(String cmdName) {
        String PATH = System.getenv("PATH");
        String pathSep = getPathSep();
        String[] paths = PATH.split(pathSep);

        for (String path : paths) {
            File folder = new File(path);
            if (!folder.exists() || !folder.isDirectory())
                continue;

            File[] files = folder.listFiles(File::isFile);
            if (files == null)
                continue;

            for (File file : files) {
                String fileName = file.getName();

                if (fileName.equals(cmdName))
                    return file.getPath();
            }

        }
        return null;
    }

    private String getPathSep() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win"))
            return ";";

        return ":";
    }

}
