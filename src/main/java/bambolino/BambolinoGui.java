package bambolino;

import java.util.ArrayList;
import java.util.List;

import bambolino.exception.BambolinoException;
import bambolino.storage.Storage;
import bambolino.task.TaskList;
import bambolino.ui.Ui;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/** A fit-for-purpose JavaFX interface for Bambolino. */
public class BambolinoGui extends Application {
    private final Storage storage = new Storage();
    private TaskList tasks;
    private TextArea conversation;
    private TextField input;

    /** Starts the JavaFX application. */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        conversation = new TextArea();
        conversation.setEditable(false);
        conversation.setWrapText(true);
        input = new TextField();
        input.setPromptText("Enter a command, e.g. todo read book");
        Button send = new Button("Send");
        send.setOnAction(event -> submit());
        input.setOnAction(event -> submit());
        BorderPane root = new BorderPane(conversation, null, null, new HBox(8, input, send), null);
        BorderPane.setMargin(conversation, new Insets(10));
        BorderPane.setMargin(root.getBottom(), new Insets(0, 10, 10, 10));
        HBox.setHgrow(input, javafx.scene.layout.Priority.ALWAYS);
        tasks = Bambolino.loadTasks(storage, new Ui(message -> { }));
        conversation.appendText("Hello! I'm Bambolino.\nWhat can I do for you?\n\n");
        stage.setTitle("Bambolino");
        stage.setScene(new Scene(root, 620, 420));
        stage.show();
    }

    private void submit() {
        String command = input.getText().trim();
        if (command.isEmpty()) {
            return;
        }
        List<String> lines = new ArrayList<>();
        Ui ui = new Ui(lines::add);
        try {
            if (command.equalsIgnoreCase("bye")) {
                ui.showGoodbye();
                conversation.appendText("You: " + command + "\n" + String.join("\n", lines) + "\n");
                input.clear();
                return;
            }
            Bambolino.processCommand(command, tasks, storage, ui);
        } catch (BambolinoException error) {
            ui.showError(error.getMessage());
        }
        conversation.appendText("You: " + command + "\n" + String.join("\n", lines) + "\n");
        input.clear();
    }
}
