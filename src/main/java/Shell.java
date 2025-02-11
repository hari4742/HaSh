import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Shell {
    private Map<String, Runnable> cmds;
    private Path currentDirectory;
    String[] instructions;
    Scanner scanner;

    Shell() {
        cmds = new HashMap<>();

        cmds.put("exit", () -> exit());
        cmds.put("echo", () -> echo());
        cmds.put("type", () -> type());
        cmds.put("pwd", () -> pwd());
        cmds.put("cd", () -> cd());
        cmds.put("default", () -> userPrograms());

        currentDirectory = Paths.get("").toAbsolutePath();

    }

    public void repl() {
        scanner = new Scanner(System.in);
        String input;

        do {
            System.out.print("$ ");
            input = scanner.nextLine();
            instructions = input.split(" ");
            String cmd = instructions[0];

            if (!cmds.containsKey(cmd))
                cmd = "default";

            cmds.get(cmd).run();

        } while (true);
    }

    void exit() {
        scanner.close();
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

    void pwd() {
        System.out.println(currentDirectory);
    }

    void cd() {
        if (instructions.length < 2)
            return;

        String path = instructions[1];

        if (path.startsWith(".")) {
            // handle the relative paths
            path = String.format("%s\\%s", currentDirectory.toString(), path);
            Path resolvedPath = Paths.get(path).normalize();
            path = resolvedPath.toString();
        }

        if (!isDirectoryExists(path)) {
            System.out.printf("cd: %s: No such file or directory\n", path);
            return;
        }

        currentDirectory = Paths.get(path).toAbsolutePath();
    }

    void userPrograms() {
        String cmd = instructions[0];
        String path = getPath(cmd);
        if (path == null) {
            System.out.println(cmd + ": command not found");
            return;
        }
        runProgram();
    }

    void runProgram() {
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
        return cmds.containsKey(cmdName);
    }

    private String getPath(String cmdName) {
        String PATH = System.getenv("PATH");
        String pathSep = getPathSep();
        String[] paths = PATH.split(pathSep);

        for (String path : paths) {
            File folder = new File(path);
            if (!isDirectoryExists(path))
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

    private boolean isDirectoryExists(String path) {
        File folder = new File(path);
        return folder.exists() && folder.isDirectory();
    }
}
