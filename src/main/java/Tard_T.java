import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Tard_T {
    public static final String LINE = "________________________________";

    public static void main(String[] args) {

        String intro = "Hello! I'm Tard_T. \n" +
                "What can I do for you? \n" +
                LINE + "\n";
        System.out.println(intro);

        Scanner scanner = new Scanner(System.in);

        List<Task> taskList = new ArrayList<>();

        // keep accepting inputs until user types "bye"
        while (true) {
            String userInput = scanner.nextLine();
            System.out.println(LINE);
            boolean toExit = false;


            String taskType = userInput.split(" ")[0];

            switch (taskType) {
                case "bye":
                    handleBye();
                    toExit = true;
                    break;
                case "list":
                    handleList(taskList);
                    break;
                case "mark":
                    handleMark(taskList, userInput);
                    break;
                case "unmark":
                    handleUnmark(taskList, userInput);
                    break;
                case "todo":
                    handleTodo(taskList, userInput);
                    break;
                case "deadline":
                    handleDeadline(taskList, userInput);
                    break;
                case "event":
                    handleEvent(taskList, userInput);
                    break;
                default:
                    System.out.println("'" + taskType + "' is not a valid input.\n"
                    + "Valid input formats: \n"
                    + "bye -> exits the interface\n"
                    + "list -> lists all the tasks and their status\n"
                    + "mark [task number] -> marks the task and show their status\n"
                    + "unmark [task number] -> unmarks the task and show their status\n"
                    + "todo [task name] -> adds a todo task to taskList\n"
                    + "deadline [task name] /by [deadline] -> adds a deadline task to taskList\n"
                    + "event [task name] /from [start time] /to [end time] -> adds an event task to taskList");
            }

            System.out.println(LINE);

            if (toExit) {
                break;
            }
        }
    }


    public static void handleBye() {
        String exit = "Bye. Hope to see you again soon!";
        System.out.println(exit);
    }

    public static void handleList(List<Task> taskList) {
        int num = 1;

        for (Task task : taskList) {
            System.out.println(num + ". " + task.toString());
            num += 1;
        }
    }

    public static void handleMark(List<Task> taskList, String userInput) {
        try {
            int idx = Integer.parseInt(userInput.substring(5));
            if (idx > taskList.size()) {
                System.out.println("Tasklist does not have that many tasks.\n" + LINE);
            }
            Task task = taskList.get(idx - 1);
            task.markAsDone();
            System.out.println("Nice! I've marked this task as done:\n" +
                    "  " + task.toString());

        } catch (NumberFormatException e) {
            System.out.println("'" + userInput + "' is not a valid integer.\n" + LINE);
        }
    }

    public static void handleUnmark(List<Task> taskList, String userInput) {
        try {
            int idx = Integer.parseInt(userInput.substring(7));
            if (idx > taskList.size()) {
                System.out.println("Tasklist does not have that many tasks.\n" + LINE);
            }
            Task task = taskList.get(idx - 1);
            task.markAsNotDone();
            System.out.println("OK, I've marked this task as not done yet: \n" +
                    "  " + task.toString());
        }  catch (NumberFormatException e) {
            System.out.println("'" + userInput + "' is not a valid integer.");
        }
    }

    public static void handleTodo(List<Task> taskList, String userInput) {
        String task = userInput.substring(5);

        if (task.isEmpty()) {
            System.out.println("    Invalid format: Description of todo cannot be empty. Use: todo [task name]");
            return;
        }
        Task newTask = new ToDo(task);
        taskList.add(newTask);
        System.out.println("Got it. I've added this task:\n  " + newTask.toString()
        + "\nNow you have " + taskList.size() + " tasks in the list.");
    }

    // AI-generated to save time
    public static void handleDeadline(List<Task> taskList, String userInput) {
        // Remove "deadline " prefix
        String rest = userInput.substring(9).trim();

        // Check if it contains " /by "
        int byIndex = rest.indexOf(" /by ");

        if (byIndex == -1) {
            System.out.println("    Invalid format. Use: deadline [task name] /by [deadline]");
            return;
        }

        // Extract description and deadline
        String description = rest.substring(0, byIndex).trim();
        String by = rest.substring(byIndex + 5).trim(); // Remove " /by "

        // Check if description is empty
        if (description.isEmpty()) {
            System.out.println("    Please provide a task description.");
            return;
        }

        // Check if deadline is empty
        if (by.isEmpty()) {
            System.out.println("    Please provide a deadline.");
            return;
        }

        // Create and add the task
        Task newTask = new Deadline(description, by);
        taskList.add(newTask);

        System.out.println("    Got it. I've added this task:");
        System.out.println("      " + newTask.toString());
        System.out.println("    Now you have " + taskList.size() + " tasks in the list.");
    }

    // AI-generated to save time
    public static void handleEvent(List<Task> taskList, String userInput) {
        // Remove "event " prefix
        String rest = userInput.substring(6).trim();

        // Check if it contains " /from "
        int fromIndex = rest.indexOf(" /from ");

        if (fromIndex == -1) {
            System.out.println("    Invalid format. Use: event [task name] /from [start] /to [end]");
            return;
        }

        // Extract description
        String description = rest.substring(0, fromIndex).trim();
        String afterDesc = rest.substring(fromIndex + 7).trim(); // Remove " /from "

        // Check if it contains " /to "
        int toIndex = afterDesc.indexOf(" /to ");

        if (toIndex == -1) {
            System.out.println("    Invalid format. Use: event [task name] /from [start] /to [end]");
            return;
        }

        // Extract start and end times
        String from = afterDesc.substring(0, toIndex).trim();
        String to = afterDesc.substring(toIndex + 5).trim(); // Remove " /to "

        // Validate
        if (description.isEmpty()) {
            System.out.println("    Please provide a task description.");
            return;
        }

        if (from.isEmpty()) {
            System.out.println("    Please provide a start time.");
            return;
        }

        if (to.isEmpty()) {
            System.out.println("    Please provide an end time.");
            return;
        }

        // Create and add the task
        Task newTask = new Event(description, from, to);
        taskList.add(newTask);

        System.out.println("    Got it. I've added this task:");
        System.out.println("      " + newTask.toString());
        System.out.println("    Now you have " + taskList.size() + " tasks in the list.");
    }
}


