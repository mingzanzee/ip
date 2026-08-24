package Tard_T;

import Tard_T.exception.TardTException;
import Tard_T.parser.Parser;
import Tard_T.storage.Storage;
import Tard_T.task.TaskList;
import Tard_T.ui.Ui;

import java.nio.file.Path;
import java.nio.file.Paths;


/** Provides the interactive command-line interface for the Tard_T.Tard_T task manager. */
public class Tard_T {
    private static final Path SAVE_FILE = Paths.get("data", "tasks.txt");

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    public Tard_T() {
        ui = new Ui();
        storage = new Storage(SAVE_FILE);
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (TardTException e) {
            ui.showError(e.getMessage());
            loadedTasks = new TaskList();
        }

        tasks = loadedTasks;
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            String userInput = ui.readCommand();
            ui.showLine();
            isExit = Parser.parse(userInput, tasks, ui, storage);
            ui.showLine();
        }
        ui.close();
    }

    public static void main(String[] args) {
        new Tard_T().run();
    }
}