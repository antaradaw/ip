package bambolino;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import bambolino.exception.BambolinoException;
import bambolino.parser.Parser;
import bambolino.storage.Storage;
import bambolino.task.Deadline;
import bambolino.task.Event;
import bambolino.task.Task;
import bambolino.task.TaskList;
import bambolino.task.Todo;
import bambolino.ui.Ui;

/**
 * A command-line task list application.
 */
public class Bambolino {
    /**
     * Starts Bambolino and processes commands until the user says goodbye.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();
        Storage storage = new Storage();
        TaskList tasks = loadTasks(storage, ui);
        while (true) {
            String userInput = ui.readCommand();

            if (userInput.equalsIgnoreCase("bye")) {
                ui.showGoodbye();
                break;
            }

            try {
                processCommand(userInput, tasks, storage, ui);
            } catch (BambolinoException error) {
                ui.showError(error.getMessage());
            }
        }
    }

    /**
     * Processes one command.
     *
     * @param userInput The command entered by the user.
     * @param tasks The task list.
     * @throws BambolinoException If the command or its arguments are invalid.
     */
    private static void processCommand(String userInput, TaskList tasks, Storage storage, Ui ui)
            throws BambolinoException {
        if (userInput.isEmpty()) {
            throw new BambolinoException("please enter a command.");
        }

        Parser.Command parsedCommand = new Parser().parse(userInput);
        String command = parsedCommand.name();
        String arguments = parsedCommand.arguments();

        if (command.equals("list")) {
            if (!arguments.isEmpty()) {
                throw new BambolinoException("the list command does not take any extra words.");
            }
            ui.showTaskList(tasks);
        } else if (command.equals("find")) {
            if (arguments.isEmpty()) {
                throw new BambolinoException("the find command needs a keyword. Try: find book");
            }
            ui.showMatchingTasks(tasks, arguments);
        } else if (command.equals("todo")) {
            if (arguments.isEmpty()) {
                throw new BambolinoException("a todo needs a description. Try: todo borrow book");
            }
            tasks.add(new Todo(arguments));
            saveTasks(storage, tasks);
            ui.showTaskAdded(tasks.getLast(), tasks.size());
        } else if (command.equals("deadline")) {
            addDeadline(arguments, tasks, storage, ui);
        } else if (command.equals("event")) {
            addEvent(arguments, tasks, storage, ui);
        } else if (command.equals("mark")) {
            Task taskToMark = getTask(arguments, tasks, "mark");
            taskToMark.markAsDone();
            saveTasks(storage, tasks);
            ui.showMarkedTask(taskToMark, true);
        } else if (command.equals("unmark")) {
            Task taskToUnmark = getTask(arguments, tasks, "unmark");
            taskToUnmark.unmarkAsDone();
            saveTasks(storage, tasks);
            ui.showMarkedTask(taskToUnmark, false);
        } else if (command.equals("delete")) {
            int taskIndex = getTaskIndex(arguments, tasks, "delete");
            Task deletedTask = tasks.remove(taskIndex);
            saveTasks(storage, tasks);
            ui.showDeletedTask(deletedTask, tasks.size());
        } else {
            throw new BambolinoException("I don't recognise that command. Try todo, deadline, event, list, "
                    + "find, mark, unmark, delete, or bye.");
        }
    }

    /** Adds a deadline task after validating its description and date. */
    private static void addDeadline(String arguments, TaskList tasks, Storage storage, Ui ui)
            throws BambolinoException {
        int byIndex = arguments.startsWith("/by ") ? 0 : arguments.indexOf(" /by ");
        if (byIndex < 0) {
            throw new BambolinoException("a deadline needs /by followed by a date. Try: deadline return book "
                    + "/by 2019-10-15");
        }
        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex == 0 ? 4 : byIndex + 5).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new BambolinoException("a deadline needs both a description and a date after /by.");
        }
        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(by);
        } catch (DateTimeParseException error) {
            throw new BambolinoException("deadline dates must use yyyy-mm-dd. Try: deadline return book "
                    + "/by 2019-10-15");
        }
        tasks.add(new Deadline(description, dueDate));
        saveTasks(storage, tasks);
        ui.showTaskAdded(tasks.getLast(), tasks.size());
    }

    /** Adds an event task after validating its description, start, and end text. */
    private static void addEvent(String arguments, TaskList tasks, Storage storage, Ui ui)
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
        saveTasks(storage, tasks);
        ui.showTaskAdded(tasks.getLast(), tasks.size());
    }

    /** Finds a valid task selected by a command. */
    private static Task getTask(String arguments, TaskList tasks, String command)
            throws BambolinoException {
        return tasks.get(getTaskIndex(arguments, tasks, command));
    }

    /** Validates and converts a command's one-based task number to a list index. */
    private static int getTaskIndex(String arguments, TaskList tasks, String command)
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

    /** Loads saved tasks while allowing the chatbot to start if storage fails. */
    private static TaskList loadTasks(Storage storage, Ui ui) {
        try {
            return new TaskList(storage.load());
        } catch (IOException error) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

    /** Saves task changes and reports a storage problem without ending the chatbot. */
    private static void saveTasks(Storage storage, TaskList tasks) throws BambolinoException {
        try {
            storage.save(tasks.asList());
        } catch (IOException error) {
            throw new BambolinoException("I couldn't save your tasks. Please check the data folder.");
        }
    }

}
