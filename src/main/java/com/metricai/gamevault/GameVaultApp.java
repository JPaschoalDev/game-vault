package com.metricai.gamevault;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameVaultApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameVaultApp.class.getResource("jogo-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1210,900);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("GAME VAULT");
        stage.setScene(scene);
        stage.show();
    }
}
