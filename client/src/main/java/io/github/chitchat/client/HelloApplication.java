package io.github.chitchat.client;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloApplication extends Application {
    private static final Logger log = LogManager.getLogger(HelloApplication.class);

    public static void main(String[] args) {
        launch();
    }


     @Override
    public void start(Stage primaryStage) throws IOException {
         log.info("Starting client...");

         FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pages/main/main.fxml"));
         Scene scene = new Scene(fxmlLoader.load(), 800, 800);
         primaryStage.setTitle("Chat");
         primaryStage.setScene(scene);
         primaryStage.show();
    }


}
