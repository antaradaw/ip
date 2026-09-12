package bambolino;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import bambolino.storage.Storage;
import bambolino.task.Deadline;
import bambolino.task.Event;
import bambolino.task.Task;
import bambolino.task.Todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Exercises real file persistence in temporary directories, never the user's data. */
class StorageTest {
    @TempDir
    Path directory;

    @Test
    void saveLoad_allTaskTypes_roundTripSpecialCharactersAndStatus() throws IOException {
        Storage storage = new Storage(directory.resolve("nested/tasks.txt"));
        List<Task> tasks = List.of(new Todo("中文 | café\nnext line"),
                new Deadline("return book", LocalDate.of(2024, 2, 29)),
                new Event("meeting", "Mon | 2pm", "4pm\nend"));
        tasks.forEach(Task::markAsDone);
        storage.save(tasks);
        List<Task> loaded = storage.load();
        assertEquals(tasks.stream().map(Task::toString).toList(), loaded.stream().map(Task::toString).toList());
        assertEquals(tasks.stream().map(Task::toStorageString).toList(),
                loaded.stream().map(Task::toStorageString).toList());
        storage.save(List.of());
        assertTrue(storage.load().isEmpty());
    }

    @Test
    void load_missingFile_returnsEmptyList() throws IOException {
        assertTrue(new Storage(directory.resolve("missing.txt")).load().isEmpty());
    }

    @Test
    void load_corruptRecords_keepsValidNeighbors() throws IOException {
        Path file = directory.resolve("tasks.txt");
        Files.write(file, List.of("T|0|Ym9vaw==", "bad", "T|2|Ym9vaw==", "T|0|%%%",
                "X|0|Ym9vaw==", "T|0|Ym9vaw==|extra", "D|0|Ym9vaw==|MjAyMy0wMi0yOQ==",
                "E|0|YQ==|Yg==|Yw==|extra", "E|0|YQ==|Yg==|Yw=="));
        assertEquals(List.of("[T][ ] book", "[E][ ] a (from: b to: c)"),
                new Storage(file).load().stream().map(Task::toString).toList());
    }

    @Test
    void storage_invalidFileLocations_reportIoErrors() throws IOException {
        assertThrows(IOException.class, () -> new Storage(directory).load());
        assertThrows(IOException.class, () -> new Storage(directory).save(List.of()));
        Path parentFile = directory.resolve("file");
        Files.writeString(parentFile, "occupied");
        assertThrows(IOException.class, () -> new Storage(parentFile.resolve("tasks.txt")).save(List.of()));
    }
}
