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

        List<Task> taskList = new ArrayList<>();

        while (true) {
            String userInput = scanner.nextLine();
            commandCount++;
            System.out.println(LINE);

            if (userInput.equals("bye")) {
                String exit = "Bye. Hope to see you again soon! \n" +
                        LINE;
                System.out.println(exit);
                break;
            } else if (userInput.equals("list")) {
                int num = 1;

                for (Task task : taskList) {
                    System.out.println(num + ". " + task.toString());
                    num += 1;
                }
                System.out.println(LINE + "\n");
            } else if (userInput.startsWith("mark ")) {
                try {
                    int idx = Integer.parseInt(userInput.substring(5));
                    if (idx > taskList.size()) {
                        System.out.println("Tasklist does not have that many tasks.\n" + LINE);
                        continue;
                    }
                    Task task = taskList.get(idx - 1);
                    task.markAsDone();
                    System.out.println("Nice! I've marked this task as done:\n" +
                            "  " + task.toString());
                    System.out.println(LINE);

                } catch (NumberFormatException e) {
                    System.out.println("'" + userInput + "' is not a valid integer.\n" + LINE);
                }
            } else if (userInput.startsWith("unmark ")) {
               try {
                    int idx = Integer.parseInt(userInput.substring(7));
                    if (idx > taskList.size()) {
                        System.out.println("Tasklist does not have that many tasks.\n" + LINE);
                        continue;
                    }
                    Task task = taskList.get(idx - 1);
                    task.markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet: \n" +
                            "  " + task.toString());
                    System.out.println(LINE);
               }  catch (NumberFormatException e) {
                   System.out.println("'" + userInput + "' is not a valid integer.");
               }
            } else {
                taskList.add(new Task(userInput));
                String echo = "    added: " + userInput + "\n" + LINE + "\n";

                // Customization: tired bot
                if (commandCount > 5) {
                    echo += "        Tard_T is tired...\n" + LINE + "\n";
                }
                System.out.println(echo);
            }
        }
    }
}
