package bambolino;

import java.util.ArrayList;
import java.util.List;

import bambolino.exception.BambolinoException;
import bambolino.storage.Storage;
import bambolino.task.TaskList;
import bambolino.ui.Ui;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Provides a chat-style JavaFX interface for Bambolino. */
public class BambolinoGui extends Application {
    private final Storage storage = new Storage();
    private TaskList tasks;
    private VBox conversation;
    private TextField input;

    /** Starts the JavaFX application. */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        conversation = new VBox(12);
        conversation.getStyleClass().add("conversation");
        ScrollPane scrollPane = new ScrollPane(conversation);
        scrollPane.setFitToWidth(true);
        scrollPane.vvalueProperty().bind(conversation.heightProperty());
        scrollPane.getStyleClass().add("conversation-scroll");

        input = new TextField();
        input.setPromptText("Enter a command, e.g. todo read book");
        input.getStyleClass().add("command-input");
        Button send = new Button("Send");
        send.getStyleClass().add("send-button");
        send.setOnAction(event -> submit());
        input.setOnAction(event -> submit());

        HBox composer = new HBox(8, input, send);
        composer.setAlignment(Pos.CENTER);
        composer.getStyleClass().add("composer");
        HBox.setHgrow(input, Priority.ALWAYS);

        ImageView headerAvatar = createAvatar("/bambolino.jpeg", 52);
        Label title = new Label("Bambolino");
        title.getStyleClass().add("title");
        Label subtitle = new Label("Your friendly task companion  •  Online");
        subtitle.getStyleClass().add("subtitle");
        VBox header = new VBox(2, title, subtitle);
        header.getStyleClass().add("header");
        HBox headerContent = new HBox(12, headerAvatar, header);
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.getStyleClass().add("header-content");

        BorderPane root = new BorderPane(scrollPane, headerContent, null, composer, null);
        BorderPane.setMargin(scrollPane, new Insets(0, 16, 8, 16));
        BorderPane.setMargin(composer, new Insets(8, 16, 16, 16));
        tasks = Bambolino.loadTasks(storage, new Ui(message -> { }));
        addMessage("Hello! I'm Bambolino.\nWhat can I do for you?", false);
        stage.setTitle("Bambolino");
        Scene scene = new Scene(root, 680, 520);
        scene.getStylesheets().add(getClass().getResource("/bambolino.css").toExternalForm());
        stage.setScene(scene);
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
                addMessage(command, true);
                addMessage(String.join("\n", lines), false);
                input.clear();
                return;
            }
            Bambolino.processCommand(command, tasks, storage, ui);
        } catch (BambolinoException error) {
            ui.showError(error.getMessage());
        }
        addMessage(command, true);
        addMessage(String.join("\n", lines), false);
        input.clear();
    }

    /** Adds one user or Bambolino message to the conversation. */
    private void addMessage(String text, boolean isUser) {
        javafx.scene.Node avatar;
        if (isUser) {
            avatar = createAvatar("/user.jpeg", 60);
        } else {
            avatar = createAvatar("/bambolino.jpeg", 60);
        }
        Label message = new Label(text);
        message.setWrapText(true);
        message.setMaxWidth(500);
        message.getStyleClass().add(isUser ? "user-message" : "bambolino-message");
        HBox row = new HBox(10, avatar, message);
        row.setAlignment(Pos.TOP_LEFT);
        if (isUser) {
            row.setAlignment(Pos.TOP_RIGHT);
            row.getChildren().setAll(message, avatar);
        }
        conversation.getChildren().add(row);
    }

    /** Returns a consistently sized avatar loaded from the application's resources. */
    private ImageView createAvatar(String resourcePath, double size) {
        ImageView avatar = new ImageView(new Image(getClass().getResourceAsStream(resourcePath)));
        avatar.setFitWidth(size);
        avatar.setFitHeight(size);
        avatar.setPreserveRatio(false);
        avatar.setClip(new Circle(size / 2, size / 2, size / 2));
        avatar.getStyleClass().add(resourcePath.contains("user") ? "user-avatar" : "bambolino-avatar");
        return avatar;
    }
}
