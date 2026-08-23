package bambolino.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import bambolino.task.Deadline;
import bambolino.task.Event;
import bambolino.task.Task;
import bambolino.task.Todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests saving and loading task lists without affecting the application's data file. */
class StorageTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void load_missingFile_returnsEmptyList() throws IOException {
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt"));

        assertTrue(storage.load().isEmpty());
    }

    @Test
    void saveAndLoad_mixedTasks_preservesTypesDetailsAndCompletionStatus() throws IOException {
        Storage storage = new Storage(temporaryDirectory.resolve("nested/tasks.txt"));
        Todo todo = new Todo("borrow book");
        Deadline deadline = new Deadline("return book", LocalDate.of(2019, 10, 15));
        Event event = new Event("project meeting", "Mon 2pm", "4pm");
        deadline.markAsDone();

        storage.save(List.of(todo, deadline, event));
        List<Task> loadedTasks = storage.load();

        assertEquals(List.of(
                "[T][ ] borrow book",
                "[D][X] return book (by: Oct 15 2019)",
                "[E][ ] project meeting (from: Mon 2pm to: 4pm)"),
                loadedTasks.stream().map(Task::toString).toList());
    }
}
