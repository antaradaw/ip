package bambolino.task;

/**
 * Represents one task in the task list.
 * A task has a description and can be marked as completed or incomplete.
 */
public class Task {
    /** The user-provided text describing this task. */
    protected String description;

    /** Whether this task has been completed. */
    protected boolean isDone;

    /**
     * Creates an incomplete task with a description.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the symbol representing this task's completion status.
     *
     * @return {@code X} when complete, or a space when incomplete
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void unmarkAsDone() {
        isDone = false;
    }

    /**
     * Converts this task into one line of storage data.
     *
     * @return a storage line containing the task type, status, and fields
     */
    public String toStorageString() {
        return "T|" + (isDone ? "1" : "0") + "|" + encode(description);
    }

    /** Encodes text safely for use as a pipe-separated storage field. */
    protected String encode(String text) {
        return java.util.Base64.getEncoder().encodeToString(
                text.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    /**
     * Returns this task in the list-display format.
     *
     * @return the completion status icon and task description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
