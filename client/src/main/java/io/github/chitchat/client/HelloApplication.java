package io.github.chitchat.client;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloApplication extends Application {
    private static final Logger log = LogManager.getLogger(HelloApplication.class);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        log.info("Starting client...");

        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


    }
     @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Chatliste links
        ListView<String> chatList = new ListView<>();
        chatList.getItems().addAll("Chat 1", "Chat 2", "Chat 3");

        // Chat-Ansicht rechts
        VBox chatView = new VBox();
        chatView.setPadding(new Insets(10));
        chatView.setSpacing(10);

        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        TextArea inputArea = new TextArea();
        inputArea.setPromptText("Type a message...");
        inputArea.setWrapText(true);

        Button sendButton = new Button("Send");

        chatView.getChildren().addAll(chatArea, inputArea, sendButton);

        root.setLeft(chatList);
        root.setRight(chatView);

        BorderPane.setMargin(chatList, new Insets(10));
        BorderPane.setMargin(chatView, new Insets(10));

        chatList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Hier könntest du Logik implementieren, um die Nachrichten für den ausgewählten Chat zu laden und anzuzeigen
            chatArea.setText("Loading messages for chat: " + newValue);
        });

        sendButton.setOnAction(event -> {
            String message = inputArea.getText().trim();
            if (!message.isEmpty()) {
                chatArea.appendText("You: " + message + "\n");
                inputArea.clear();
            }
        });
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat App");
        primaryStage.show();
    }


}
