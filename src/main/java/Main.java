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

                default:
                    System.out.println(input + ": command not found");
                    break;
            }

        } while (true);

    }
}
