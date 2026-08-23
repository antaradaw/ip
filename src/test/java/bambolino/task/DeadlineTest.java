package bambolino.task;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Tests deadline task display and storage representations. */
class DeadlineTest {
    @Test
    void toString_incompleteDeadline_displaysFormattedDate() {
        Deadline deadline = new Deadline("return book", LocalDate.of(2019, 10, 15));

        assertEquals("[D][ ] return book (by: Oct 15 2019)", deadline.toString());
    }

    @Test
    void toStorageString_completedDeadline_storesCompletionStatusAndIsoDate() {
        Deadline deadline = new Deadline("return book", LocalDate.of(2019, 10, 15));
        deadline.markAsDone();

        assertEquals("D|1|cmV0dXJuIGJvb2s=|MjAxOS0xMC0xNQ==", deadline.toStorageString());
    }
}
