package io.github.chitchat.client.view.pages.main;

public class MainController 
{

    @FXML
    private ListView<String> chatList;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextArea inputArea;

    @FXML
    private Button sendButton;

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Füge die Chatlisteinträge hinzu
        chatList.getItems().addAll("Chat 1", "Chat 2", "Chat 3");


        // Eventhandler für das Senden einer Nachricht
        sendButton.setOnAction(event -> sendMessage());
    }

    @FXML
    private void sendMessage() {
        // Sende die eingegebene Nachricht
        String message = inputArea.getText().trim();
        if (!message.isEmpty()) {
            chatArea.appendText("You: " + message + "\n");
            inputArea.clear();
        }
    }

    @FXML
    private void loadChatMessages() {
        // Lade Nachrichten für den ausgewählten Chat
        String selectedChat = chatList.getSelectionModel().getSelectedItem();
        if (selectedChat != null) {
            chatArea.setText("Loading messages for chat: " + selectedChat);
        }
    }

}
