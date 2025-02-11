import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            System.out.print("$ ");
            input = scanner.nextLine();
            if (input.startsWith("exit")) {
                System.exit(0);
                scanner.close();
            }
            System.out.println(input + ": command not found");

        } while (true);

    }
}
