package Tard_T.parser;

import Tard_T.command.Command;
import Tard_T.exception.TardTException;
import Tard_T.storage.Storage;
import Tard_T.task.*;
import Tard_T.ui.Ui;

public class Parser {
    /** Parses and executes one line of input. Returns true if the app should exit. */
    public static boolean parse(String userInput, TaskList tasks, Ui ui, Storage storage) {
        boolean toExit = false;
        String taskType = userInput.split(" ")[0];
        Command command = Command.fromKeyword(taskType);

        switch (command) {
            case BYE:
                ui.showBye();
                toExit = true;
                break;
            case LIST:
                ui.showTaskList(tasks.getTasks());
                break;
            case MARK:
                handleMark(tasks, userInput, ui, storage);
                break;
            case UNMARK:
                handleUnmark(tasks, userInput, ui, storage);
                break;
            case TODO:
                handleTodo(tasks, userInput, ui, storage);
                break;
            case DEADLINE:
                handleDeadline(tasks, userInput, ui, storage);
                break;
            case EVENT:
                handleEvent(tasks, userInput, ui, storage);
                break;
            case DELETE:
                handleDelete(tasks, userInput, ui, storage);
                break;
            default:
                ui.showError("'" + taskType + "' is not a valid input.\n"
                        + "Valid input formats: \n"
                        + "bye -> exits the interface\n"
                        + "list -> lists all the tasks and their status\n"
                        + "mark [task number] -> marks the task and show their status\n"
                        + "unmark [task number] -> unmarks the task and show their status\n"
                        + "todo [task name] -> adds a todo task to taskList\n"
                        + "deadline [task name] /by [deadline] -> adds a deadline task to taskList\n"
                        + "event [task name] /from [start time] /to [end time] -> adds an event task to taskList\n"
                        + "delete [task number] -> deletes a task from taskList");
        }
        return toExit;
    }

    private static void handleMark(TaskList tasks, String userInput, Ui ui, Storage storage) {
        if (userInput.trim().equals("mark")) {
            ui.showError("    Missing task number after 'mark'. Use mark [task number].");
            return;
        }
        String rest = userInput.substring(5);
        try {
            int idx = Integer.parseInt(rest);
            if (idx > tasks.size()) {
                ui.showError("    Tasklist does not have that many tasks.");
                return;
            }
            Task task = tasks.get(idx - 1);
            task.markAsDone();
            storage.save(tasks.getTasks());
            ui.showMarked(task);
        } catch (NumberFormatException e) {
            ui.showError("    '" + rest + "' is not a valid integer.\n" + Ui.LINE);
        } catch (TardTException e) {
            ui.showError(e.getMessage());
        }
    }

    private static void handleUnmark(TaskList tasks, String userInput, Ui ui, Storage storage) {
        if (userInput.trim().equals("unmark")) {
            ui.showError("    Missing task number after 'unmark'. Use unmark [task number].");
            return;
        }
        String rest = userInput.substring(7);
        try {
            int idx = Integer.parseInt(rest);
            if (idx > tasks.size()) {
                ui.showError("    Tasklist does not have that many tasks.");
                return;
            }
            Task task = tasks.get(idx - 1);
            task.markAsNotDone();
            storage.save(tasks.getTasks());
            ui.showUnmarked(task);
        } catch (NumberFormatException e) {
            ui.showError("    '" + rest + "' is not a valid integer.");
        } catch (TardTException e) {
            ui.showError(e.getMessage());
        }
    }

    private static void handleTodo(TaskList tasks, String userInput, Ui ui, Storage storage) {
        String description = userInput.substring(5);
        try {
            if (description.isEmpty()) {
                throw new TardTException(
                        "    Invalid format: Description of todo cannot be empty. Use: todo [task name]");
            }
            Task newTask = new ToDo(description);
            tasks.add(newTask);
            storage.save(tasks.getTasks());
            ui.showAdded(newTask, tasks.size());
        } catch (TardTException e) {
            ui.showError(e.getMessage());
        }
    }

    private static void handleDeadline(TaskList tasks, String userInput, Ui ui, Storage storage) {
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
            tasks.add(newTask);
            storage.save(tasks.getTasks());
            ui.showAddedIndented(newTask, tasks.size());
        } catch (TardTException e) {
            ui.showError(e.getMessage());
        }
    }

    private static void handleEvent(TaskList tasks, String userInput, Ui ui, Storage storage) {
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
            tasks.add(newTask);
            storage.save(tasks.getTasks());
            ui.showAddedIndented(newTask, tasks.size());
        } catch (TardTException e) {
            ui.showError(e.getMessage());
        }
    }

    private static void handleDelete(TaskList tasks, String userInput, Ui ui, Storage storage) {
        if (userInput.trim().equals("delete")) {
            ui.showError("    Missing task number after 'delete'. Use delete [task number].");
            return;
        }
        String rest = userInput.substring(7);
        try {
            int idx = Integer.parseInt(rest);
            if (idx > tasks.size()) {
                ui.showError("    Tasklist does not have that many tasks.");
                return;
            }
            Task task = tasks.delete(idx - 1);
            storage.save(tasks.getTasks());
            ui.showDeleted(task, tasks.size());
        } catch (NumberFormatException e) {
            ui.showError("    '" + rest + "' is not a valid integer.");
        } catch (TardTException e) {
            ui.showError(e.getMessage());
        }
    }
}