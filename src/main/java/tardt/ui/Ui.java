package tardt.ui;

import java.util.List;
import java.util.Scanner;

import tardt.task.Task;

/**
 * Class that handles interaction with the user.
 */
public class Ui {
    public static final String LINE = "____________________________________________________________";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Shows the welcome message when the application starts.
     */
    public void showWelcome() {
        String intro = "Hello! I'm Tard_T. \n"
                + "What can I do for you? \n"
                + LINE;
        System.out.println(intro);
    }

    /**
     * Reads the user input.
     *
     * @return The latest user input as a String.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints a line in standard format in the terminal.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Shows a 'bye' message.
     */
    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Shows all Tasks, numbered, by printing it in the terminal.
     *
     * @param taskList The List of Tasks.
     */
    public void showTaskList(List<Task> taskList) {
        int num = 1;
        for (Task task : taskList) {
            System.out.println(num + ". " + task.toString());
            num += 1;
        }
    }

    /**
     * Shows message that acknowledges a Task as marked in the terminal.
     *
     * @param task A Task object.
     */
    public void showMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:\n  " + task.toString());
    }

    /**
     * Shows message that acknowledges a Task as unmarked in the terminal.
     *
     * @param task
     */
    public void showUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet: \n  " + task.toString());
    }

    /**
     * "Added" format used by todo.
     *
     * @param task A Task object.
     * @param size An integer representing the number of Tasks in the taskList
     * */
    public void showAdded(Task task, int size) {
        System.out.println("Got it. I've added this task:\n  " + task.toString()
                + "\nNow you have " + size + " tasks in the list.");
    }

    /**
     * "Added" format used by deadline/event (indented, multi-line).
     *
     * @param task A Task object.
     * @param size An integer representing the number of Tasks in the taskList
     */
    public void showAddedIndented(Task task, int size) {
        System.out.println("    Got it. I've added this task:");
        System.out.println("      " + task.toString());
        System.out.println("    Now you have " + size + " tasks in the list.");
    }

    /**
     * "Deleted" format used by deadline/event (indented, multi-line).
     *
     * @param task A Task object.
     * @param size An integer representing the number of Tasks in the taskList
     */
    public void showDeleted(Task task, int size) {
        System.out.println("Noted, I've removed this task: \n  " + task.toString());
        System.out.println("    Now you have " + size + " tasks in the list.");
    }

    /**
     * Prints the error message in the terminal.
     *
     * @param message A String containing the error message.
     */
    public void showError(String message) {
        System.out.println(message.stripTrailing());
    }

    /**
     * Closes the Scanner object.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Displays the tasks that matched a find command's search string.
     *
     * @param matches the tasks whose description contained the search string
     */
    public void showFoundTasks(List<Task> matches) {
        if (matches.isEmpty()) {
            System.out.println("No matching tasks found.");
            return;
        }
        System.out.println("Here are the matching tasks in your list:");
        int num = 1;
        for (Task task : matches) {
            System.out.println(num + ". " + task.toString());
            num += 1;
        }
    }

    /**
     * Prints the response message to the terminal. For CLI use.
     * @param message The response message upon user input.
     */
    public void showMessage(String message) {
        System.out.println(message.stripTrailing());
    }
}
