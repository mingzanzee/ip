package tardt.parser;

import java.util.ArrayList;
import java.util.List;

import tardt.command.Command;
import tardt.exception.TardTException;
import tardt.storage.Storage;
import tardt.task.Deadline;
import tardt.task.Event;
import tardt.task.Task;
import tardt.task.TaskList;
import tardt.task.ToDo;
import tardt.ui.Ui;

/**
 * Class in charge of parsing user inputs.
 */
public class Parser {

    // ==================== CLI METHOD ====================

    /**
     * Parses and executes one line of input. Returns true if the app should exit.
     * This method is for the CLI version only.
     *
     * @param userInput The user input.
     * @param tasks The list of tasks as a TaskList object.
     * @param ui The Ui object.
     * @param storage The Storage Object.
     * @return True if the response is the goodbye message, False otherwise.
     */
    public static boolean parse(String userInput, TaskList tasks, Ui ui, Storage storage) {
        try {
            String response = parseForResponse(userInput, tasks, storage);
            if (response.equals("Bye. Hope to see you again soon!")) {
                ui.showBye();
                return true;
            }
            ui.showMessage(response);
            return false;
        } catch (TardTException e) {
            ui.showError(e.getMessage());
            return false;
        }
    }

    // ==================== GUI METHOD ====================

    /**
     * Parses user input and returns a response string.
     * This method does NOT use Ui for output — it returns the response instead.
     * Suitable for both GUI and CLI use.
     *
     * @param userInput User input command
     * @param tasks TaskList to operate on
     * @param storage Storage for saving/loading
     * @return Response message as a String
     * @throws TardTException If an error occurs during parsing
     */
    public static String parseForResponse(String userInput, TaskList tasks, Storage storage) throws TardTException {
        String[] parts = userInput.split(" ");
        String taskType = parts[0];
        Command command = Command.fromKeyword(taskType);

        switch (command) {
            case BYE:
                return "Bye. Hope to see you again soon!";
            case LIST:
                return getTaskListString(tasks);
            case MARK:
                return handleMarkForResponse(tasks, userInput, storage);
            case UNMARK:
                return handleUnmarkForResponse(tasks, userInput, storage);
            case TODO:
                return handleTodoForResponse(tasks, userInput, storage);
            case DEADLINE:
                return handleDeadlineForResponse(tasks, userInput, storage);
            case EVENT:
                return handleEventForResponse(tasks, userInput, storage);
            case DELETE:
                return handleDeleteForResponse(tasks, userInput, storage);
            case FIND:
                return handleFindForResponse(tasks, userInput, storage);
            default:
                throw new TardTException("'" + taskType + "' is not a valid input.\n"
                        + "Valid input formats: \n"
                        + "bye -> exits the interface\n"
                        + "list -> lists all the tasks and their status\n"
                        + "mark [task number] -> marks the task and show their status\n"
                        + "unmark [task number] -> unmarks the task and show their status\n"
                        + "todo [task name] -> adds a todo task to taskList\n"
                        + "deadline [task name] /by [deadline] -> adds a deadline task to taskList\n"
                        + "event [task name] /from [start time] /to [end time] -> adds an event task to taskList\n"
                        + "delete [task number] -> deletes a task from taskList\n"
                        + "find [search string] -> finds a task consisting of the search string");
        }
    }

    /**
     * Gets the task list as a formatted string.
     *
     * @param tasks The TaskList object storing a list of tasks.
     * @return A list of tasks as a String.
     */
    private static String getTaskListString(TaskList tasks) {
        List<Task> taskList = tasks.getTasks();
        if (taskList.isEmpty()) {
            return "Your task list is empty!";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < taskList.size(); i++) {
            sb.append(i + 1).append(". ").append(taskList.get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Parses and validates a task index from user input.
     *
     * @param userInput The full user input string
     * @param tasks The task list
     * @param prefix The command prefix (e.g., "mark", "unmark")
     * @return The validated task index (0-based)
     * @throws TardTException If the index is invalid
     */
    private static int parseTaskIndex(String userInput, TaskList tasks, String prefix) throws TardTException {
        String trimmed = userInput.trim();
        if (trimmed.equals(prefix)) {
            throw new TardTException("Missing task number after '" + prefix + "'. Use " + prefix + " [task number].");
        }

        String rest = userInput.substring(prefix.length()).trim();
        try {
            int idx = Integer.parseInt(rest) - 1; // Convert to 0-based
            if (idx < 0 || idx >= tasks.size()) {
                throw new TardTException("Task number out of range. There are " + tasks.size() + " tasks.");
            }
            return idx;
        } catch (NumberFormatException e) {
            throw new TardTException("'" + rest + "' is not a valid integer.");
        }
    }

    /**
     * Handles the MARK command by marking a task as done.
     *
     * @param tasks A list of tasks as a TaskList object.
     * @param userInput The user input.
     * @param storage The Storage object.
     * @return The chatbot's string response.
     * @throws TardTException A unique exception class for TardT.
     */
    private static String handleMarkForResponse(TaskList tasks, String userInput, Storage storage)
            throws TardTException {
        int idx = parseTaskIndex(userInput, tasks, "mark");
        Task task = tasks.get(idx);
        task.markAsDone();
        storage.save(tasks.getTasks());
        return "Nice! I've marked this task as done:\n  " + task;
    }

    /**
     * Handles the UNMARK command by marking a task as undone.
     *
     * @param tasks A list of tasks as a TaskList object.
     * @param userInput The user input.
     * @param storage The Storage object.
     * @return The chatbot's string response.
     * @throws TardTException A unique exception class for TardT.
     */
    private static String handleUnmarkForResponse(TaskList tasks, String userInput, Storage storage)
            throws TardTException {
        int idx = parseTaskIndex(userInput, tasks, "unmark");
        Task task = tasks.get(idx);
        task.markAsNotDone();
        storage.save(tasks.getTasks());
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    /**
     * Handles the TODO command by adding a ToDo Task to taskList.
     *
     * @param tasks A list of tasks as a TaskList object.
     * @param userInput The user input.
     * @param storage The Storage object.
     * @return The chatbot's string response.
     * @throws TardTException A unique exception class for TardT.
     */
    private static String handleTodoForResponse(TaskList tasks, String userInput, Storage storage)
            throws TardTException {
        String description = userInput.substring(5).trim();
        if (description.isEmpty()) {
            throw new TardTException("Invalid format: Description of todo cannot be empty. Use: todo [task name]");
        }
        Task newTask = new ToDo(description);
        tasks.add(newTask);
        storage.save(tasks.getTasks());
        return "Got it. I've added this task:\n  " + newTask + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Handles the DEADLINE command by adding a Deadline Task to taskList.
     *
     * @param tasks A list of tasks as a TaskList object.
     * @param userInput The user input.
     * @param storage The Storage object.
     * @return The chatbot's string response.
     * @throws TardTException A unique exception class for TardT.
     */
    private static String handleDeadlineForResponse(TaskList tasks, String userInput, Storage storage)
            throws TardTException {
        String rest = userInput.substring(9).trim();
        int byIndex = rest.indexOf(" /by ");
        if (byIndex == -1) {
            throw new TardTException("Invalid format. Use: deadline [task name] /by [deadline]");
        }
        String description = rest.substring(0, byIndex).trim();
        String by = rest.substring(byIndex + 5).trim();
        if (description.isEmpty()) {
            throw new TardTException("Please provide a task description.");
        }
        if (by.isEmpty()) {
            throw new TardTException("Please provide a deadline.");
        }
        Task newTask = new Deadline(description, by);
        tasks.add(newTask);
        storage.save(tasks.getTasks());
        return "Got it. I've added this task:\n  " + newTask + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Handles the EVENT command by adding an Event Task to taskList.
     *
     * @param tasks A list of tasks as a TaskList object.
     * @param userInput The user input.
     * @param storage The Storage object.
     * @return The chatbot's string response.
     * @throws TardTException A unique exception class for TardT.
     */
    private static String handleEventForResponse(TaskList tasks, String userInput, Storage storage)
            throws TardTException {
        String rest = userInput.substring(6).trim();
        int fromIndex = rest.indexOf(" /from ");
        if (fromIndex == -1) {
            throw new TardTException("Invalid format. Use: event [task name] /from [start] /to [end]");
        }
        String description = rest.substring(0, fromIndex).trim();
        String afterDesc = rest.substring(fromIndex + 7).trim();
        int toIndex = afterDesc.indexOf(" /to ");
        if (toIndex == -1) {
            throw new TardTException("Invalid format. Use: event [task name] /from [start] /to [end]");
        }
        String from = afterDesc.substring(0, toIndex).trim();
        String to = afterDesc.substring(toIndex + 5).trim();
        if (description.isEmpty()) {
            throw new TardTException("Please provide a task description.");
        }
        if (from.isEmpty()) {
            throw new TardTException("Please provide a start time.");
        }
        if (to.isEmpty()) {
            throw new TardTException("Please provide an end time.");
        }
        Task newTask = new Event(description, from, to);
        tasks.add(newTask);
        storage.save(tasks.getTasks());
        return "Got it. I've added this task:\n  " + newTask + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Handles the DELETE command by removing a Task from taskList.
     *
     * @param tasks A list of tasks as a TaskList object.
     * @param userInput The user input.
     * @param storage The Storage object.
     * @return The chatbot's string response.
     * @throws TardTException A unique exception class for TardT.
     */
    private static String handleDeleteForResponse(TaskList tasks, String userInput, Storage storage)
            throws TardTException {
        int idx = parseTaskIndex(userInput, tasks, "delete");
        Task task = tasks.delete(idx);
        storage.save(tasks.getTasks());
        return "Noted. I've removed this task:\n  " + task + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Handles the FIND command by filtering the Tasks matching the input.
     *
     * @param tasks A list of tasks as a TaskList object.
     * @param userInput The user input.
     * @param storage The Storage object.
     * @return The chatbot's string response.
     * @throws TardTException A unique exception class for TardT.
     */
    private static String handleFindForResponse(TaskList tasks, String userInput, Storage storage)
            throws TardTException {
        String trimmed = userInput.trim();
        if (trimmed.equals("find")) {
            throw new TardTException("Missing search string after 'find'.");
        }

        String keyword = userInput.substring(5).trim();
        if (keyword.isEmpty()) {
            throw new TardTException("Missing search string after 'find'.");
        }

        List<Task> matches = new ArrayList<>();
        for (Task task : tasks.getTasks()) {
            if (task.getDescription().contains(keyword)) {
                matches.add(task);
            }
        }

        if (matches.isEmpty()) {
            return "No tasks found matching: " + keyword;
        }

        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matches.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(matches.get(i)).append("\n");
        }
        return sb.toString();
    }
}
