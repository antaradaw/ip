package bambolino.storage;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import bambolino.task.Deadline;
import bambolino.task.Event;
import bambolino.task.Task;
import bambolino.task.Todo;

/**
 * Saves and restores Bambolino tasks using a file relative to the project directory.
 */
public class Storage {
    /** The location used for task data. */
    private static final Path FILE_PATH = Path.of("data", "bambolino.txt");

    /**
     * Loads every valid task in the data file.
     *
     * @return The loaded tasks, or an empty list when no data file exists.
     * @throws IOException If the data file cannot be read.
     */
    public List<Task> load() throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(FILE_PATH)) {
            return tasks;
        }

        for (String line : Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8)) {
            try {
                tasks.add(parseTask(line));
            } catch (IllegalArgumentException | DateTimeException error) {
                System.out.println("Warning: Ignored a corrupted task in " + FILE_PATH + ".");
            }
        }
        return tasks;
    }

    /**
     * Writes the current task list, creating its data directory if needed.
     *
     * @param tasks The tasks to save.
     * @throws IOException If the task data cannot be written.
     */
    public void save(List<Task> tasks) throws IOException {
        Files.createDirectories(FILE_PATH.getParent());
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toStorageString());
        }
        Files.write(FILE_PATH, lines, StandardCharsets.UTF_8);
    }

    /** Converts one stored line into a task. */
    private Task parseTask(String line) {
        String[] fields = line.split("\\|", -1);
        if (fields.length < 3 || fields.length > 5) {
            throw new IllegalArgumentException("Incorrect number of fields");
        }
        boolean isDone = switch (fields[1]) {
        case "0" -> false;
        case "1" -> true;
        default -> throw new IllegalArgumentException("Invalid completion status");
        };
        Task task;
        if (fields[0].equals("T") && fields.length == 3) {
            task = new Todo(decode(fields[2]));
        } else if (fields[0].equals("D") && fields.length == 4) {
            task = new Deadline(decode(fields[2]), LocalDate.parse(decode(fields[3])));
        } else if (fields[0].equals("E") && fields.length == 5) {
            task = new Event(decode(fields[2]), decode(fields[3]), decode(fields[4]));
        } else {
            throw new IllegalArgumentException("Invalid task type");
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /** Decodes one Base64-encoded text field. */
    private String decode(String text) {
        return new String(Base64.getDecoder().decode(text), StandardCharsets.UTF_8);
    }
}
