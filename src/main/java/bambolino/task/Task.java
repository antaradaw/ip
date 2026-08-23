package bambolino.task;

import java.util.Locale;

/**
 * Represents one task in the task list.
 * A task has a description and can be marked as completed or incomplete.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public void markAsDone() {
        isDone = true;
    }

    public void unmarkAsDone() {
        isDone = false;
    }

    /**
     * Returns whether this task description contains a keyword, ignoring case.
     *
     * @param keyword The text to search for.
     * @return True if the description contains the keyword.
     */
    public boolean containsKeyword(String keyword) {
        return description.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
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

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
