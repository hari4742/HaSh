import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            System.out.print("$ ");
            input = scanner.nextLine();
            String[] instructions = input.split(" ");
            String cmd = instructions[0];
            String[] builtins = { "exit", "echo", "type" };

            switch (cmd) {
                case "exit":
                    System.exit(0);
                    scanner.close();
                    break;

                case "echo":
                    for (int i = 1; i < instructions.length; i++) {
                        System.out.printf("%s ", instructions[i]);
                    }
                    System.out.println();
                    break;

                case "type":
                    if (instructions.length < 2) {
                        System.out.println("usage type <command>");
                        break;
                    }
                    String cmdName = instructions[1];
                    if (Arrays.asList(builtins).contains(cmdName)) {
                        System.out.printf("%s is a shell builtin\n", cmdName);
                    } else {
                        System.out.printf("%s: not found\n", cmdName);
                    }
                    break;

                default:
                    System.out.println(input + ": command not found");
                    break;
            }

        } while (true);

    }
}
