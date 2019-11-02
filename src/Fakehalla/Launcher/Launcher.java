package Fakehalla.Launcher;

import Fakehalla.Game;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class Launcher {
    private Stage stage;
    private Scene defaultScene;
    private int gameHeight = 1080;
    private int gameWidth = 1920;
    private boolean fullscreen = true;

    public Launcher(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Fakehalla Launcher");

        Button[] buttons = new Button[5];


        buttons[0] = new Button("Multiplayer");
        buttons[0].setOnAction(event -> {
            runGame();
        });

        buttons[1] = new Button("Options");
        buttons[1].setOnAction(event -> {
            runSettings();
        });

        buttons[2] = new Button("Scoreboard");
        buttons[2].setOnAction(event -> {
            runScoreboard();
        });

        buttons[3] = new Button("Credits");
        buttons[3].setOnAction(event -> {
            runCredits();
        });

        buttons[4] = new Button("Exit");
        buttons[4].setOnAction(event -> {
            Platform.exit();
        });

        for (Button i : buttons) {
            i.setStyle("-fx-font-size: 3em; ");
            i.setMinWidth(400);

        }

        Label authors = new Label("© Danko a Jozko 2019");

        VBox vbox = new VBox(buttons);
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);

        BorderPane borderpane = new BorderPane();
        borderpane.setCenter(vbox);

        borderpane.setStyle("-fx-background-color: #333333;");

        borderpane.setBottom(authors);

        defaultScene = new Scene(borderpane, 800, 600);
        defaultScene.setOnMouseExited(event -> System.out.println("pa"));
        stage.setScene(defaultScene);
    }


    public void run() {
        stage.show();
    }

    private void runGame() {
        Game game = new Game("Fakehalla", gameWidth, gameHeight, fullscreen);
        game.start();
    }

    private void runSettings() {
        //TODO
    }

    private void runCredits() {
        //TODO
    }

    private void runScoreboard() {
        //TODO
    }
}