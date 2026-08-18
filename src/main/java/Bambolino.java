import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A command-line task list application.
 */
public class Bambolino {
    /** The line used to separate Bambolino's responses. */
    private static final String DIVIDER = "____________________________________________________________";

    /**
     * Starts Bambolino and processes commands until the user says goodbye.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String banner = " ____                  _           _ _             \n"
                + "| __ )  __ _ _ __ ___ | |__   ___ | (_)_ __   ___  \n"
                + "|  _ \\ / _` | '_ ` _ \\| '_ \\ / _ \\| | | '_ \\ / _ \\ \n"
                + "| |_) | (_| | | | | | | |_) | (_) | | | | | | (_) |\n"
                + "|____/ \\__,_|_| |_| |_|_.__/ \\___/|_|_|_| |_|\\___/ \n";
        System.out.println(banner);
        System.out.println("Hello! I'm Bambolino.\n" +
                "What can I do for you?");
        List<Task> tasks = new ArrayList<>();
        while (true) {
            String userInput = sc.nextLine().trim();

            if (userInput.equalsIgnoreCase("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }

            try {
                processCommand(userInput, tasks);
            } catch (BambolinoException error) {
                printError(error);
            }
        }
    }

    /**
     * Processes one command.
     *
     * @param userInput the command entered by the user
     * @param tasks the task list
     * @throws BambolinoException if the command or its arguments are invalid
     */
    private static void processCommand(String userInput, List<Task> tasks)
            throws BambolinoException {
        if (userInput.isEmpty()) {
            throw new BambolinoException("please enter a command.");
        }

        String[] commandParts = userInput.split("\\s+", 2);
        String command = commandParts[0].toLowerCase();
        String arguments = commandParts.length == 2 ? commandParts[1].trim() : "";

        if (command.equals("list")) {
            if (!arguments.isEmpty()) {
                throw new BambolinoException("the list command does not take any extra words.");
            }
            printTaskList(tasks);
        } else if (command.equals("todo")) {
            if (arguments.isEmpty()) {
                throw new BambolinoException("a todo needs a description. Try: todo borrow book");
            }
            tasks.add(new Todo(arguments));
            printTaskAdded(tasks.getLast(), tasks.size());
        } else if (command.equals("deadline")) {
            addDeadline(arguments, tasks);
        } else if (command.equals("event")) {
            addEvent(arguments, tasks);
        } else if (command.equals("mark")) {
            Task taskToMark = getTask(arguments, tasks, "mark");
            taskToMark.markAsDone();
            printMarkedTask(taskToMark, true);
        } else if (command.equals("unmark")) {
            Task taskToUnmark = getTask(arguments, tasks, "unmark");
            taskToUnmark.unmarkAsDone();
            printMarkedTask(taskToUnmark, false);
        } else if (command.equals("delete")) {
            int taskIndex = getTaskIndex(arguments, tasks, "delete");
            Task deletedTask = tasks.remove(taskIndex);
            printDeletedTask(deletedTask, tasks.size());
        } else {
            throw new BambolinoException("I don't recognise that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
        }
    }

    /** Adds a deadline task after validating its description and deadline text. */
    private static void addDeadline(String arguments, List<Task> tasks)
            throws BambolinoException {
        int byIndex = arguments.startsWith("/by ") ? 0 : arguments.indexOf(" /by ");
        if (byIndex < 0) {
            throw new BambolinoException("a deadline needs /by followed by its deadline. Try: deadline return book /by Sunday");
        }
        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex == 0 ? 4 : byIndex + 5).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new BambolinoException("a deadline needs both a description and text after /by.");
        }
        tasks.add(new Deadline(description, by));
        printTaskAdded(tasks.getLast(), tasks.size());
    }

    /** Adds an event task after validating its description, start, and end text. */
    private static void addEvent(String arguments, List<Task> tasks)
            throws BambolinoException {
        int fromIndex = arguments.indexOf(" /from ");
        int toIndex = arguments.indexOf(" /to ");
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= toIndex) {
            throw new BambolinoException("an event needs /from and /to. Try: event meeting /from Mon 2pm /to 4pm");
        }
        String description = arguments.substring(0, fromIndex).trim();
        String from = arguments.substring(fromIndex + 7, toIndex).trim();
        String to = arguments.substring(toIndex + 5).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new BambolinoException("an event needs a description, a start after /from, and an end after /to.");
        }
        tasks.add(new Event(description, from, to));
        printTaskAdded(tasks.getLast(), tasks.size());
    }

    /** Finds a valid task selected by a command. */
    private static Task getTask(String arguments, List<Task> tasks, String command)
            throws BambolinoException {
        return tasks.get(getTaskIndex(arguments, tasks, command));
    }

    /** Validates and converts a command's one-based task number to a list index. */
    private static int getTaskIndex(String arguments, List<Task> tasks, String command)
            throws BambolinoException {
        if (arguments.isEmpty()) {
            throw new BambolinoException("the " + command + " command needs a task number. Try: " + command + " 1");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(arguments);
        } catch (NumberFormatException error) {
            throw new BambolinoException("the task number for " + command + " must be a whole number.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new BambolinoException("task " + taskNumber + " is not in your list yet.");
        }
        return taskNumber - 1;
    }

    /** Prints the current task list. */
    private static void printTaskList(List<Task> tasks) {
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

    /** Prints the confirmation shown after marking or unmarking a task. */
    private static void printMarkedTask(Task task, boolean isDone) {
        System.out.println(DIVIDER);
        System.out.println(isDone ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        System.out.println(DIVIDER);
    }

    /** Prints the confirmation shown after deleting a task. */
    private static void printDeletedTask(Task task, int taskCount) {
        System.out.println(DIVIDER);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /** Prints a user-friendly command error. */
    private static void printError(BambolinoException error) {
        System.out.println(DIVIDER);
        System.out.println("Sorry, " + error.getMessage());
        System.out.println(DIVIDER);
    }

    /**
     * Prints the confirmation shown after adding a task.
     *
     * @param task the task that was added
     * @param taskCount the number of tasks now in the list
     */
    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println(DIVIDER);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }
}
