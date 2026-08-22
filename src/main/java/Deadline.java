/**
 * A task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description the task description
     * @param by the deadline, stored as text
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline task in the list-display format.
     *
     * @return the task type, status icon, description, and deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    /** Returns this deadline in a format used by persistent storage. */
    @Override
    public String toStorageString() {
        return "D|" + (isDone ? "1" : "0") + "|" + encode(description) + "|" + encode(by);
    }
}
