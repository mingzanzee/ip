package tardt;

import java.nio.file.Path;
import java.nio.file.Paths;

import tardt.exception.TardTException;
import tardt.parser.Parser;
import tardt.storage.Storage;
import tardt.task.TaskList;
import tardt.ui.Ui;


/** Provides the interactive command-line interface for the Tard_T.Tard_T task manager. */
public class TardT {
    private static final Path SAVE_FILE = Paths.get("data", "tasks.txt");

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Constructor for TardT object
     */
    public TardT() {
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

    /**
     * Runs the interactive application
     */
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
        new TardT().run();
    }
}
