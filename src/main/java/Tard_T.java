import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tard_T {
    public static void main(String[] args) {
        final String LINE = "________________________________";
        String intro = "Hello! I'm Tard_T. \n" +
                "What can I do for you? \n" +
                LINE + "\n";
        System.out.println(intro);

        Scanner scanner = new Scanner(System.in);

        // keep accepting inputs until user types "bye"
        // some customization
        int commandCount = 0;

        List<String> taskList = new ArrayList<>();

        while (true) {
            String userInput = scanner.nextLine();
            commandCount++;

            if (userInput.equals("bye")) {
                String exit = "Bye. Hope to see you again soon! \n" +
                        LINE;
                System.out.println(exit);
                break;
            } else if (userInput.equals("list")) {
                int num = 1;
                System.out.println(LINE + "\n");
                for (String task : taskList) {
                    System.out.println(num + ". " + task);
                    num += 1;
                }
                System.out.println(LINE + "\n");
            } else {
                taskList.add(userInput);
                String echo = LINE + "\n" + "    added: " + userInput + "\n" + LINE + "\n";

                if (commandCount > 5) {
                    echo += "        Tard_T is tired...\n" + LINE + "\n";
                }
                System.out.println(echo);
            }
        }
    }
}
