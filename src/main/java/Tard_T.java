import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Provides the interactive command-line interface for the Tard_T task manager. */
public class Tard_T {
    public static final String LINE = "________________________________";
    /** The project-relative file that stores tasks between program runs. */
    private static final Path SAVE_FILE = Path.of("data", "tardt.txt");

    /** Represents the supported command keywords and an unrecognised input. */
    private enum Command {
        BYE("bye"),
        LIST("list"),
        MARK("mark"),
        UNMARK("unmark"),
        TODO("todo"),
        DEADLINE("deadline"),
        EVENT("event"),
        DELETE("delete"),
        INVALID(null);

        private final String keyword;

        Command(String keyword) {
            this.keyword = keyword;
        }

        /** Returns the command that matches the input keyword, if one exists. */
        static Command fromKeyword(String keyword) {
            for (Command command : values()) {
                if (keyword.equals(command.keyword)) {
                    return command;
                }
            }
            return INVALID;
        }
    }

    public static void main(String[] args) {

        String intro = "Hello! I'm Tard_T. \n" +
                "What can I do for you? \n" +
                LINE + "\n";
        System.out.println(intro);

        Scanner scanner = new Scanner(System.in);

        List<Task> taskList = loadTasks();

        // keep accepting inputs until user types "bye"
        while (true) {
            String userInput = scanner.nextLine();
            System.out.println(LINE);
            boolean toExit = false;


            String taskType = userInput.split(" ")[0];
            Command command = Command.fromKeyword(taskType);

            switch (command) {
                case BYE:
                    handleBye();
                    toExit = true;
                    break;
                case LIST:
                    handleList(taskList);
                    break;
                case MARK:
                    handleMark(taskList, userInput);
                    break;
                case UNMARK:
                    handleUnmark(taskList, userInput);
                    break;
                case TODO:
                    handleTodo(taskList, userInput);
                    break;
                case DEADLINE:
                    handleDeadline(taskList, userInput);
                    break;
                case EVENT:
                    handleEvent(taskList, userInput);
                    break;
                case DELETE:
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
            saveTasks(taskList);
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
            saveTasks(taskList);
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
            saveTasks(taskList);
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
            saveTasks(taskList);
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
            saveTasks(taskList);
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
            saveTasks(taskList);
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

    /**
     * Saves the current task list to the project's data file.
     *
     * <p>Each task is saved as a pipe-separated line containing its type,
     * completion status, description, and any type-specific details.</p>
     *
     * @param taskList the tasks to write after a successful list change
     */
    private static void saveTasks(List<Task> taskList) {
        List<String> savedTasks = new ArrayList<>();
        for (Task task : taskList) {
            savedTasks.add(formatTaskForSaving(task));
        }

        try {
            Files.createDirectories(SAVE_FILE.getParent());
            Files.writeString(SAVE_FILE, String.join(System.lineSeparator(), savedTasks));
        } catch (IOException exception) {
            System.out.println("Unable to save tasks: " + exception.getMessage());
        }
    }

    /**
     * Converts one task to its pipe-separated on-disk representation.
     *
     * @param task the task to save
     * @return a line containing the task's type, status, and details
     */
    private static String formatTaskForSaving(Task task) {
        String status = task.isDone ? "1" : "0";
        if (task instanceof Deadline deadline) {
            return "D | " + status + " | " + deadline.description + " | " + deadline.by;
        }
        if (task instanceof Event event) {
            return "E | " + status + " | " + event.description + " | "
                    + event.from + " | " + event.to;
        }
        return "T | " + status + " | " + task.description;
    }

    /**
     * Loads saved tasks from the project's data file when the application starts.
     *
     * @return the saved tasks, or an empty list when no save file exists
     */
    private static List<Task> loadTasks() {
        List<Task> taskList = new ArrayList<>();
        if (!Files.exists(SAVE_FILE)) {
            return taskList;
        }

        try {
            for (String savedTask : Files.readAllLines(SAVE_FILE)) {
                Task task = createTaskFromSavedLine(savedTask);
                if (task != null) {
                    taskList.add(task);
                }
            }
        } catch (IOException exception) {
            System.out.println("Unable to load tasks: " + exception.getMessage());
        }
        return taskList;
    }

    /**
     * Recreates one task from its pipe-separated on-disk representation.
     *
     * @param savedTask one line from the save file
     * @return the reconstructed task, or {@code null} if the line is not a supported task format
     */
    private static Task createTaskFromSavedLine(String savedTask) {
        String[] parts = savedTask.split("\\s*\\|\\s*", -1);
        if (parts.length < 3) {
            return null;
        }

        Task task;
        try {
            switch (parts[0]) {
                case "T":
                    if (parts.length != 3) {
                        return null;
                    }
                    task = new ToDo(parts[2]);
                    break;
                case "D":
                    if (parts.length != 4) {
                        return null;
                    }
                    task = new Deadline(parts[2], parts[3]);
                    break;
                case "E":
                    if (parts.length != 5) {
                        return null;
                    }
                    task = new Event(parts[2], parts[3], parts[4]);
                    break;
                default:
                    return null;
            }
        } catch (TardTException e) {
            System.out.println("Invalid date/time format in save file");
            return null;
        }

        if (parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }
}
