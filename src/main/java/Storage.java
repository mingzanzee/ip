import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path saveFile;

    public Storage(Path saveFile) {
        this.saveFile = saveFile;
    }

    /** Saves the current task list to the data file. */
    public void save(List<Task> taskList) throws TardTException {
        List<String> savedTasks = new ArrayList<>();
        for (Task task : taskList) {
            savedTasks.add(formatTaskForSaving(task));
        }
        try {
            Files.createDirectories(saveFile.getParent());
            Files.writeString(saveFile, String.join(System.lineSeparator(), savedTasks));
        } catch (IOException exception) {
            throw new TardTException("Unable to save tasks: " + exception.getMessage());
        }
    }

    private String formatTaskForSaving(Task task) {
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

    /** Loads saved tasks from the data file, or an empty list if none exists. */
    public List<Task> load() throws TardTException {
        List<Task> taskList = new ArrayList<>();
        if (!Files.exists(saveFile)) {
            return taskList;
        }
        try {
            for (String savedTask : Files.readAllLines(saveFile)) {
                Task task = createTaskFromSavedLine(savedTask);
                if (task != null) {
                    taskList.add(task);
                }
            }
        } catch (IOException exception) {
            throw new TardTException("Unable to load tasks: " + exception.getMessage());
        }
        return taskList;
    }

    private Task createTaskFromSavedLine(String savedTask) {
        String[] parts = savedTask.split("\\s*\\|\\s*", -1);
        if (parts.length < 3) {
            return null;
        }
        Task task;
        try {
            switch (parts[0]) {
                case "T":
                    if (parts.length != 3) return null;
                    task = new ToDo(parts[2]);
                    break;
                case "D":
                    if (parts.length != 4) return null;
                    task = new Deadline(parts[2], parts[3]);
                    break;
                case "E":
                    if (parts.length != 5) return null;
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