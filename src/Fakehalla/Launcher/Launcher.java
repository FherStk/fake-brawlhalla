package Fakehalla.Launcher;

import Fakehalla.Game;
import Fakehalla.Settings.Settings;
import Fakehalla.Settings.SettingsLoader;
import Fakehalla.Settings.SettingsSaver;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class Launcher {
    private Stage stage;
    private Scene defaultScene;
    private Scene settingsScene;
    private Scene scoreboardScene;
    private Scene creditsScene;
    //private Settings set;
    private int gameHeight = 1080;
    private int gameWidth = 1920;
    private boolean gameFullscreen = true;

    public Launcher(Stage primaryStage) throws IOException, ClassNotFoundException {
        stage = primaryStage;
        stage.setTitle("Fakehalla Launcher");
        defaultScene = generateLauncherScene();
        settingsScene = generateSettingsScene();
        //defaultScene.setOnMouseExited(event -> System.out.println("pa"));
        stage.setScene(defaultScene);
    }


    public void run() {
        stage.show();
    }


    private void runGame() {
        Game game = new Game("Fakehalla", gameWidth, gameHeight, gameFullscreen);
        game.start();
    }

    private void runSettings() {
        stage.setScene(settingsScene);
    }

    private void runCredits() {
        //stage.setScene(credits);
    }

    private void runScoreboard() {
        //stage.setScene(scoreboard);
    }

    private Scene generateLauncherScene() throws FileNotFoundException {
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
            i.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            Rectangle but = new Rectangle();
            but.setWidth(500);
            but.setHeight(80);
            but.setArcHeight(30);
            but.setArcWidth(30);
            i.setShape(but);
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
        return (new Scene(borderpane, 800, 600));
    }

    private Scene generateSettingsScene() throws IOException, ClassNotFoundException {
        SettingsLoader settingsLoader = new SettingsLoader();

        Settings settings = settingsLoader.loadSettings("settings.txt");


        Text[] player1Texts = new Text[4];
        player1Texts[0] = new Text("Player 1 UP:");
        player1Texts[1] = new Text("Player 1 LEFT:");
        player1Texts[2] = new Text("Player 1 DOWN:");
        player1Texts[3] = new Text("Player 1 RIGHT:");

        TextField[] player1TextFields = new TextField[4];
        player1TextFields[0] = new TextField(String.valueOf(settings.getPlayer1Up()));
        player1TextFields[1] = new TextField(String.valueOf(settings.getPlayer1Left()));
        player1TextFields[2] = new TextField(String.valueOf(settings.getPlayer1Down()));
        player1TextFields[3] = new TextField(String.valueOf(settings.getPlayer1Right()));

        Text[] player2Texts = new Text[4];
        player2Texts[0] = new Text("Player 2 UP:");
        player2Texts[1] = new Text("Player 2 LEFT:");
        player2Texts[2] = new Text("Player 2 DOWN:");
        player2Texts[3] = new Text("Player 2 RIGHT:");

        TextField[] player2TextFields = new TextField[4];
        player2TextFields[0] = new TextField(String.valueOf(settings.getPlayer2Up()));
        player2TextFields[1] = new TextField(String.valueOf(settings.getPlayer2Left()));
        player2TextFields[2] = new TextField(String.valueOf(settings.getPlayer2Down()));
        player2TextFields[3] = new TextField(String.valueOf(settings.getPlayer2Right()));

        Text[] texts = new Text[3];
        texts[0] = new Text("Width: ");
        texts[1] = new Text("Height: ");
        texts[2] = new Text("Fullscreen: ");

        TextField[] textFields = new TextField[2];
        textFields[0] = new TextField(Integer.toString(settings.getWidth()));
        textFields[1] = new TextField(Integer.toString(settings.getHeight()));

        CheckBox checkboxFullscreen = new CheckBox();
        checkboxFullscreen.setSelected(settings.isFullscreen());

        Button[] buttons = new Button[2];
        buttons[0] = new Button("Save");
        buttons[0].setOnAction(e -> {
            if (textFields[0].getText().matches("[0-9]*") && textFields[1].getText().matches("[0-9]*") &&
                    validateControlSettings(player1TextFields) && validateControlSettings(player2TextFields)) {
                settings.setResolution(Integer.parseInt(textFields[0].getText()),Integer.parseInt(textFields[1].getText()),checkboxFullscreen.isSelected());
                settings.setPlayer1Controls(player1TextFields[0].getText().charAt(0),player1TextFields[1].getText().charAt(0),player1TextFields[2].getText().charAt(0),player1TextFields[3].getText().charAt(0));
                settings.setPlayer2Controls(player2TextFields[0].getText().charAt(0),player2TextFields[1].getText().charAt(0),player2TextFields[2].getText().charAt(0),player2TextFields[3].getText().charAt(0));
                SettingsSaver settingsSaver = new SettingsSaver();
                try {
                    settingsSaver.saveSettings("settings.txt", settings);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else{
                textFields[0].setText(Integer.toString(settings.getWidth()));
                textFields[1].setText(Integer.toString(settings.getHeight()));
                checkboxFullscreen.setSelected(settings.isFullscreen());
                player1TextFields[0].setText(String.valueOf(settings.getPlayer1Up()));
                player1TextFields[1].setText(String.valueOf(settings.getPlayer1Left()));
                player1TextFields[2].setText(String.valueOf(settings.getPlayer1Down()));
                player1TextFields[3].setText(String.valueOf(settings.getPlayer1Right()));
                player2TextFields[0].setText(String.valueOf(settings.getPlayer2Up()));
                player2TextFields[1].setText(String.valueOf(settings.getPlayer2Left()));
                player2TextFields[2].setText(String.valueOf(settings.getPlayer2Down()));
                player2TextFields[3].setText(String.valueOf(settings.getPlayer2Right()));
            }
            stage.setScene(defaultScene);
        });

        buttons[0].setStyle("-fx-font-size: 2em;");

        buttons[1] = new Button("Close");
        buttons[1].setStyle("-fx-font-size: 2em;");
        buttons[1].setOnAction(e-> stage.setScene(defaultScene));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        for(int i = 0;i<4;i++){
            player1Texts[i].setStyle("-fx-font-size: 2em;");
            player1TextFields[i].setStyle("-fx-font-size: 2em;");
            player1TextFields[i].setMaxWidth(50);

            player2Texts[i].setStyle("-fx-font-size: 2em;");
            player2TextFields[i].setStyle("-fx-font-size: 2em;");
            player2TextFields[i].setMaxWidth(50);

            gridPane.add(player1Texts[i], 2, i);
            gridPane.add(player1TextFields[i], 3, i);

            gridPane.add(player2Texts[i], 4, i);
            gridPane.add(player2TextFields[i], 5, i);
        }

        for (int i = 0; i < 2; i++) {
            texts[i].setStyle("-fx-font-size: 2em;");
            textFields[i].setStyle("-fx-font-size: 2em;");
            textFields[i].setMaxWidth(100);
            gridPane.add(texts[i],0,i);
            gridPane.add(textFields[i], 1, i);
            gridPane.add(buttons[i], i,3);
        }

        texts[2].setStyle("-fx-font-size: 2em;");
        checkboxFullscreen.setStyle("-fx-font-size: 2em;");
        gridPane.add(texts[2], 0, 2);
        gridPane.add(checkboxFullscreen, 1, 2);

        return(new Scene(gridPane, 800,600));

    }

    private boolean validateControlSettings(TextField[] Controls)
    {
        for (TextField i: Controls) {
            if (i.getLength()!=1)
                return false;
        }
        return true;
    }

    //private Scene generateCreditsScene(){} TODO

    //private Scene generateScoreboardScene(){} TODO
}
