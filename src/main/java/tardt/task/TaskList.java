package tardt.task;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that manages a collection of Task objects.
 */
public class TaskList {
    private final List<Task> tasks;

    public TaskList(List<Task> tasks) {
        assert tasks != null : "A TaskList must wrap a non-null task collection";
        this.tasks = tasks;
    }

    public TaskList() {
        this(new ArrayList<>());
    }

    /**
     * Adds a Task to the taskLst.
     *
     * @param task
     */
    public void add(Task task) {
        assert task != null : "A TaskList must not contain null tasks";
        int oldSize = tasks.size();
        tasks.add(task);
        assert tasks.size() == oldSize + 1 : "Adding a task must increase the list size by one";
    }

    /**
     * Deletes a Task from the taskList.
     *
     * @param index 0-based index in specifying which Task should be deleted
     * @return The removed Task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Gets a Task at a given index.
     *
     * @param index 0-based index specifying position in taskList to retrieve from.
     * @return A Task at the position of the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Gets the number of Tasks in the taskList.
     *
     * @return An integer representing number of Tasks in the taskList.
     */
    public int size() {
        return tasks.size();
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
