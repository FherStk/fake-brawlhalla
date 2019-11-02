package Fakehalla.Launcher;

import Fakehalla.Game;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Stack;

public class Launcher {
    private Stage stage;
    private Scene defaultScene;
    private int gameHeight = 1080;
    private int gameWidth = 1920;
    private boolean fullscreen = true;

    public Launcher(Stage primaryStage) throws FileNotFoundException {
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
            i.setStyle("-fx-font-size: 3em; -fx-border-color: black; -fx-border-width: 2;");
            i.setMinWidth(400);
            i.onMouseEnteredProperty().set(event -> i.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY))));
            i.onMouseExitedProperty().set(event -> i.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));
        }

        Label authors = new Label("© Danko a Jozko 2019");
        authors.setTextFill(Color.WHITE);

        Image image = new Image(new FileInputStream("src/resources/Logo.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        VBox vbox = new VBox(buttons);
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);

        BorderPane borderpane = new BorderPane();
        borderpane.setCenter(vbox);
        borderpane.setTop(imageView);
        borderpane.setBottom(authors);
        borderpane.setAlignment(imageView, Pos.CENTER);
        borderpane.setAlignment(authors, Pos.BOTTOM_RIGHT);

        borderpane.setStyle("-fx-background-color: #444444;");
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
        //TODO - make score
    }
}