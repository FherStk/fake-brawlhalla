package Fakehalla.Menu;

import Fakehalla.Game.Game;
import Fakehalla.Settings.Settings;
import Fakehalla.Settings.SettingsLoader;
import Fakehalla.Settings.SettingsSaver;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;

public class Launcher { //TODO Change launcher tu menu, use only one stage
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


    private void runGame() throws IOException, ClassNotFoundException {
        SettingsLoader settingsLoader = new SettingsLoader();
        Settings settings = settingsLoader.loadSettings("settings.txt");
        Game game = new Game("Fakehalla", settings.getWidth(), settings.getHeight(), settings.isFullscreen());
        game.start();
        stage.close();
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
            try {
                runGame();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
        player1Texts[0] = new Text("Player 1 JUMP:");
        player1Texts[1] = new Text("Player 1 LEFT:");
        player1Texts[3] = new Text("Player 1 SHOOT:");
        player1Texts[2] = new Text("Player 1 RIGHT:");

        Button[] player1Buttons = new Button[4];
        player1Buttons[0] = new Button(settings.getPlayer1Jump().toString());
        player1Buttons[1] = new Button(settings.getPlayer1Left().toString());
        player1Buttons[3] = new Button(settings.getPlayer1Shoot().toString());
        player1Buttons[2] = new Button(settings.getPlayer1Right().toString());

        player1Buttons[0].setOnAction(e-> settingsScene.setOnKeyPressed(ek -> {settings.setPlayer1Jump(ek.getCode()); player1Buttons[0].setText(settings.getPlayer1Jump().toString());}));
        player1Buttons[1].setOnAction(e-> settingsScene.setOnKeyPressed(ek -> {settings.setPlayer1Left(ek.getCode()); player1Buttons[1].setText(settings.getPlayer1Left().toString());}));
        player1Buttons[3].setOnAction(e-> settingsScene.setOnKeyPressed(ek -> {settings.setPlayer1Shoot(ek.getCode()); player1Buttons[3].setText(settings.getPlayer1Shoot().toString());}));
        player1Buttons[2].setOnAction(e-> settingsScene.setOnKeyPressed(ek -> {settings.setPlayer1Right(ek.getCode()); player1Buttons[2].setText(settings.getPlayer1Right().toString());}));

        Text[] player2Texts = new Text[4];
        player2Texts[0] = new Text("Player 2 JUMP:");
        player2Texts[1] = new Text("Player 2 LEFT:");
        player2Texts[3] = new Text("Player 2 SHOOT:");
        player2Texts[2] = new Text("Player 2 RIGHT:");

        Button[] player2Buttons = new Button[4];
        player2Buttons[0] = new Button(settings.getPlayer2Jump().toString());
        player2Buttons[1] = new Button(settings.getPlayer2Left().toString());
        player2Buttons[3] = new Button(settings.getPlayer2Shoot().toString());
        player2Buttons[2] = new Button(settings.getPlayer2Right().toString());

        player2Buttons[0].setOnAction(e-> settingsScene.setOnKeyPressed(ek -> {settings.setPlayer2Jump(ek.getCode()); player2Buttons[0].setText(settings.getPlayer2Jump().toString());}));
        player2Buttons[1].setOnAction(e-> settingsScene.setOnKeyPressed(ek -> {settings.setPlayer2Left(ek.getCode()); player2Buttons[1].setText(settings.getPlayer2Left().toString());}));
        player2Buttons[3].setOnAction(e-> settingsScene.setOnKeyPressed(ek -> {settings.setPlayer2Shoot(ek.getCode()); player2Buttons[3].setText(settings.getPlayer2Shoot().toString());}));
        player2Buttons[2].setOnAction(e-> settingsScene.setOnKeyPressed(ek -> {settings.setPlayer2Right(ek.getCode()); player2Buttons[2].setText(settings.getPlayer2Right().toString());}));

        Text[] texts = new Text[3];
        texts[0] = new Text("Resolution: ");
        texts[1] = new Text("Fullscreen: ");
        texts[2] = new Text("Sound: ");
        //Text textSound = new Text("Sound:");

        /*TextField[] textFields = new TextField[2];
        textFields[0] = new TextField(Integer.toString(settings.getWidth()));
        textFields[1] = new TextField(Integer.toString(settings.getHeight()));*/

        final ComboBox resolutionsComboBox = new ComboBox();
        resolutionsComboBox.getItems().addAll(
                "854x480",
                "1024x576",
                "1280x720",
                "1366x768",
                "1600x900",
                "1920x1080",
                "2560x1440",
                "3840x2160"
        );
        resolutionsComboBox.setValue(settings.getWidth()+"x"+settings.getHeight());

        CheckBox checkboxFullscreen = new CheckBox();
        checkboxFullscreen.setSelected(settings.isFullscreen());
        CheckBox checkboxsound = new CheckBox();
        checkboxsound.setSelected(settings.isSound());

        Button[] buttons = new Button[2];
        buttons[0] = new Button("Save");
        buttons[0].setOnAction(e -> {

            String resolution = resolutionsComboBox.getValue().toString();
            int posX = resolution.indexOf("x");
            int width = Integer.parseInt(resolution.substring(0, posX));
            int height = Integer.parseInt(resolution.substring(posX+1));

            settings.setResolution(width, height ,checkboxFullscreen.isSelected());
            settings.setSound(checkboxsound.isSelected());
            SettingsSaver settingsSaver = new SettingsSaver();
            try {
                settingsSaver.saveSettings("settings.txt", settings);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            stage.setScene(defaultScene);
        });

        buttons[0].setStyle("-fx-font-size: 1.5em;");
        resolutionsComboBox.setStyle("-fx-font-size: 1.5em;");

        buttons[1] = new Button("Close");
        buttons[1].setStyle("-fx-font-size: 1.5em;");
        buttons[1].setOnAction(e-> stage.setScene(defaultScene));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        for(int i = 0;i<4;i++){
            player1Texts[i].setStyle("-fx-font-size: 2em;");
            player1Buttons[i].setStyle("-fx-font-size: 1.5em;");
            player1Buttons[i].setMaxWidth(140);
            player1Buttons[i].setMinWidth(140);

            player2Texts[i].setStyle("-fx-font-size: 2em;");
            player2Buttons[i].setStyle("-fx-font-size: 1.5em;");
            player2Buttons[i].setMaxWidth(140);
            player2Buttons[i].setMinWidth(140);

            gridPane.add(player1Texts[i], 2, i);
            gridPane.add(player1Buttons[i], 3, i);

            gridPane.add(player2Texts[i], 2, i+4);
            gridPane.add(player2Buttons[i], 3, i+4);
        }
        Label label = new Label("Press the button and press key");
        label.setStyle("-fx-font-size: 0.8em;");
        gridPane.add(label, 3,8);
        for (int i = 0; i < 2; i++) {
            texts[i].setStyle("-fx-font-size: 2em;");
            gridPane.add(texts[i],0,i);
            gridPane.add(buttons[i], i,3);
        }
        gridPane.add(resolutionsComboBox, 1,0);
        //textSound.setStyle("-fx-font-size: 2em;");
        //gridPane.add(textSound,0,3);
        texts[2].setStyle("-fx-font-size: 2em;");
        checkboxFullscreen.setStyle("-fx-font-size: 2em;");
        gridPane.add(texts[2], 0, 2);
        gridPane.add(checkboxFullscreen, 1, 1);
        checkboxsound.setStyle("-fx-font-size: 2em;");
        gridPane.add(checkboxsound, 1, 2);
        return(new Scene(gridPane, 800,600));

    }

    //private Scene generateCreditsScene(){} TODO

    //private Scene generateScoreboardScene(){} TODO
}
