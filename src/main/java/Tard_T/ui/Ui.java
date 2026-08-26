package Tard_T.ui;

import Tard_T.task.Task;

import java.util.List;
import java.util.Scanner;

public class Ui {
    public static final String LINE = "____________________________________________________________"; // match your existing value

    private final Scanner scanner = new Scanner(System.in);

    public void showWelcome() {
        String intro = "Hello! I'm Tard_T.Tard_T. \n"
                + "What can I do for you? \n"
                + LINE + "\n";
        System.out.println(intro);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public void showTaskList(List<Task> taskList) {
        int num = 1;
        for (Task task : taskList) {
            System.out.println(num + ". " + task.toString());
            num += 1;
        }
    }

    public void showMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:\n  " + task.toString());
    }

    public void showUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet: \n  " + task.toString());
    }

    /** "Added" format used by todo.
     *
     * @param task A Task object.
     * @param size An integer representing the number of Tasks in the taskList
     * */
    public void showAdded(Task task, int size) {
        System.out.println("Got it. I've added this task:\n  " + task.toString()
                + "\nNow you have " + size + " tasks in the list.");
    }

    /** "Added" format used by deadline/event (indented, multi-line).
     * @param task A Task object.
     * @param size An integer representing the number of Tasks in the taskList
     */
    public void showAddedIndented(Task task, int size) {
        System.out.println("    Got it. I've added this task:");
        System.out.println("      " + task.toString());
        System.out.println("    Now you have " + size + " tasks in the list.");
    }

    /** "Deleted" format used by deadline/event (indented, multi-line).
     * @param task A Task object.
     * @param size An integer representing the number of Tasks in the taskList
     */
    public void showDeleted(Task task, int size) {
        System.out.println("Noted, I've removed this task: \n  " + task.toString());
        System.out.println("    Now you have " + size + " tasks in the list.");
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void close() {
        scanner.close();
    }
}