package tardt.task;

/**
 * Encapsulates the description and status of a task.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructor for Task object
     * @param description Description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
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
        return "[" + getStatusIcon() + "] " + this.description;
    }
}

