package bambolino.ui;

import bambolino.task.Task;
import java.util.List;
import java.util.Scanner;

/**
 * Handles console input and output for Bambolino.
 */
public class Ui {
    /** The line used to separate Bambolino's responses. */
    private static final String DIVIDER = "____________________________________________________________";

    /** The banner displayed when Bambolino starts. */
    private static final String BANNER = " ____                  _           _ _             \n"
            + "| __ )  __ _ _ __ ___ | |__   ___ | (_)_ __   ___  \n"
            + "|  _ \\ / _` | '_ ` _ \\| '_ \\ / _ \\| | | '_ \\ / _ \\ \n"
            + "| |_) | (_| | | | | | | |_) | (_) | | | | | | (_) |\n"
            + "|____/ \\__,_|_| |_| |_|_.__/ \\___/|_|_|_| |_|\\___/ \n";

    /** The source of user commands. */
    private final Scanner scanner;

    /** Creates a user interface that reads commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays Bambolino's welcome message. */
    public void showWelcome() {
        System.out.println(BANNER);
        System.out.println("Hello! I'm Bambolino.\n"
                + "What can I do for you?");
    }

    /** Reads and trims the next command entered by the user. */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Displays Bambolino's goodbye message. */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /** Displays the task list or an empty-list message. */
    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks added yet.");
            return;
        }
        System.out.println(DIVIDER);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(DIVIDER);
    }

    /**
     * Displays the tasks whose descriptions match a search keyword.
     *
     * @param tasks The matching tasks to display.
     */
    public void showMatchingTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No matching tasks found.");
            return;
        }
        System.out.println(DIVIDER);
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(DIVIDER);
    }

    /** Displays the confirmation for an added task. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(DIVIDER);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /** Displays the confirmation for a marked or unmarked task. */
    public void showMarkedTask(Task task, boolean isDone) {
        System.out.println(DIVIDER);
        System.out.println(isDone ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        System.out.println(DIVIDER);
    }

    /** Displays the confirmation for a deleted task. */
    public void showDeletedTask(Task task, int taskCount) {
        System.out.println(DIVIDER);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /** Displays a user-friendly command error. */
    public void showError(String message) {
        System.out.println(DIVIDER);
        System.out.println("Sorry, " + message);
        System.out.println(DIVIDER);
    }

    /** Displays the warning shown when saved tasks cannot be loaded. */
    public void showLoadingError() {
        System.out.println("Warning: I couldn't load your saved tasks. Starting with an empty list.");
    }
}
