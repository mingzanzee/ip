package tardt.task;

import static tardt.priority.Priority.LOW;

import tardt.priority.Priority;

/**
 * Encapsulates the description and status of a task.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected Priority priority = LOW;

    /**
     * Constructor for Task object
     * @param description Description of the task
     */
    public Task(String description) {
        assert description != null : "A task must have a description";
        this.description = description;
        this.isDone = false;
    }

    /**
     * Second constructor for Task Object including priority
     */
    public Task(String description, Priority priority) {
        assert description != null : "A task must have a description";
        this.description = description;
        this.isDone = false;
        this.priority = priority;
    }

    /**
     * Process the status icon of Task depending on whether it is done.
     * @return "X" if task is done, " " otherwise.
     */
    public String getStatusIcon() {
        // mark done task with X
        return (this.isDone ? "X" : " ");
    }

    public boolean isDone() {
        return this.isDone;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getDescription() {
        return this.description;
    }

    /**
     * Marks the Task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the Task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description + " | Priority: " + this.priority;
    }
}
