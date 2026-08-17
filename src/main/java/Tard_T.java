import java.util.Scanner;

public class Tard_T {
    public static void main(String[] args) {
        String intro = "Hello! I'm Tard_T. \n" +
                "What can I do for you? \n" +
                "________________________________\n";
        System.out.println(intro);

        Scanner scanner = new Scanner(System.in);

        // keep accepting inputs until user types "bye"
        // some customization
        int command_count = 0;
        while (true) {
            String userInput = scanner.nextLine();
            command_count++;

            if (userInput.equals("bye")) {
                String exit = "Bye. Hope to see you again soon! \n" +
                        "________________________________";
                System.out.println(exit);
                break;
            } else if (command_count > 5) {
                String lazy_echo = "___________________________________\n" + "    " + userInput + "\n        Tired...\n" + "___________________________________";
                System.out.println(lazy_echo);
            } else {
                String echo = "___________________________________\n" + "    " + userInput + "\n___________________________________";
                System.out.println(echo);
            }
        }



    }
}
