package io.github.chitchat.client.view.pages.main;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

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


    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        chatList.getItems().addAll("Chat 1", "Chat 2", "Chat 3");



        sendButton.setOnAction(event -> sendMessage());
    }

    @FXML
    private void sendMessage()
    {



    }

    @FXML
    private void loadChatMessages()
    {

    }

}
