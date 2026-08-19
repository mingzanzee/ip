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
                case "delete":
                    handleDelete(taskList, userInput);
                    break;
                default:
                    printError(new TardTException("'" + taskType + "' is not a valid input.\n"
                    + "Valid input formats: \n"
                    + "bye -> exits the interface\n"
                    + "list -> lists all the tasks and their status\n"
                    + "mark [task number] -> marks the task and show their status\n"
                    + "unmark [task number] -> unmarks the task and show their status\n"
                    + "todo [task name] -> adds a todo task to taskList\n"
                    + "deadline [task name] /by [deadline] -> adds a deadline task to taskList\n"
                    + "event [task name] /from [start time] /to [end time] -> adds an event task to taskList\n"
                    + "delete [task number] -> deletes a task from taskList"));
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
        // check if its only "mark"
        if (userInput.trim().equals("mark")) {
            System.out.println("    Missing task number after 'mark'. Use mark [task number].");
            return;
        }
        String rest = userInput.substring(5);
        try {
            int idx = Integer.parseInt(rest);
            // should this be considered a TardTException?
            if (idx > taskList.size()) {
                System.out.println("    Tasklist does not have that many tasks.");
                return;
            }
            Task task = taskList.get(idx - 1);
            task.markAsDone();
            System.out.println("Nice! I've marked this task as done:\n" +
                    "  " + task.toString());

        } catch (NumberFormatException e) {
            System.out.println("    '" + rest + "' is not a valid integer.\n" + LINE);
        }
    }

    public static void handleUnmark(List<Task> taskList, String userInput) {
        if (userInput.trim().equals("unmark")) {
            System.out.println("    Missing task number after 'unmark'. Use unmark [task number].");
            return;
        }
        String rest = userInput.substring(7);
        try {
            int idx = Integer.parseInt(rest);
            if (idx > taskList.size()) {
                System.out.println("    Tasklist does not have that many tasks.");
                return;
            }
            Task task = taskList.get(idx - 1);
            task.markAsNotDone();
            System.out.println("OK, I've marked this task as not done yet: \n" +
                    "  " + task.toString());
        }  catch (NumberFormatException e) {
            System.out.println("    '" + rest + "' is not a valid integer.");
        }
    }

    public static void handleTodo(List<Task> taskList, String userInput) {
        String task = userInput.substring(5);

        try {
            if (task.isEmpty()) {
                throw new TardTException(
                        "    Invalid format: Description of todo cannot be empty. Use: todo [task name]");
            }
            Task newTask = new ToDo(task);
            taskList.add(newTask);
            System.out.println("Got it. I've added this task:\n  " + newTask.toString()
            + "\nNow you have " + taskList.size() + " tasks in the list.");
        } catch (TardTException e) {
            printError(e);
        }
    }

    // AI-generated to save time
    public static void handleDeadline(List<Task> taskList, String userInput) {
        try {
            String rest = userInput.substring(9).trim();
            int byIndex = rest.indexOf(" /by ");
            if (byIndex == -1) {
                throw new TardTException("    Invalid format. Use: deadline [task name] /by [deadline]");
            }

            String description = rest.substring(0, byIndex).trim();
            String by = rest.substring(byIndex + 5).trim();
            if (description.isEmpty()) {
                throw new TardTException("    Please provide a task description.");
            }
            if (by.isEmpty()) {
                throw new TardTException("    Please provide a deadline.");
            }

            Task newTask = new Deadline(description, by);
            taskList.add(newTask);
            System.out.println("    Got it. I've added this task:");
            System.out.println("      " + newTask.toString());
            System.out.println("    Now you have " + taskList.size() + " tasks in the list.");
        } catch (TardTException e) {
            printError(e);
        }
    }

    // AI-generated to save time
    public static void handleEvent(List<Task> taskList, String userInput) {
        try {
            String rest = userInput.substring(6).trim();
            int fromIndex = rest.indexOf(" /from ");
            if (fromIndex == -1) {
                throw new TardTException("    Invalid format. Use: event [task name] /from [start] /to [end]");
            }

            String description = rest.substring(0, fromIndex).trim();
            String afterDesc = rest.substring(fromIndex + 7).trim();
            int toIndex = afterDesc.indexOf(" /to ");
            if (toIndex == -1) {
                throw new TardTException("    Invalid format. Use: event [task name] /from [start] /to [end]");
            }

            String from = afterDesc.substring(0, toIndex).trim();
            String to = afterDesc.substring(toIndex + 5).trim();
            if (description.isEmpty()) {
                throw new TardTException("    Please provide a task description.");
            }
            if (from.isEmpty()) {
                throw new TardTException("    Please provide a start time.");
            }
            if (to.isEmpty()) {
                throw new TardTException("    Please provide an end time.");
            }

            Task newTask = new Event(description, from, to);
            taskList.add(newTask);
            System.out.println("    Got it. I've added this task:");
            System.out.println("      " + newTask.toString());
            System.out.println("    Now you have " + taskList.size() + " tasks in the list.");
        } catch (TardTException e) {
            printError(e);
        }
    }

    public static void handleDelete(List<Task> taskList, String userInput) {
        // handle lack of space after 'delete'
        if  (userInput.trim().equals("delete")) {
            System.out.println("    Missing task number after 'delete'. Use delete [task number].");
            return;
        }
        String rest = userInput.substring(7);
        try {
            int idx = Integer.parseInt(rest);
            if (idx > taskList.size()) {
                System.out.println("    Tasklist does not have that many tasks.");
                return;
            }
            Task task = taskList.get(idx - 1);
            taskList.remove(idx - 1);
            System.out.println("Noted, I've removed this task: \n" +
                    "  " + task.toString());
            System.out.println("    Now you have " + taskList.size() + " tasks in the list.");
        }  catch (NumberFormatException e) {
            System.out.println("    '" + rest + "' is not a valid integer.");
        }
    }

    /** Prints the message carried by a user-facing Tard_T exception. */
    private static void printError(TardTException exception) {
        System.out.println(exception.getMessage());
    }
}
