package Fakehalla.Menu;

import Fakehalla.Game.Game;
import Fakehalla.Game.HashElement;
import Fakehalla.Game.PlayerScore;
import Fakehalla.Settings.Settings;
import Fakehalla.Settings.SettingsLoader;
import Fakehalla.Settings.SettingsSaver;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

public class Launcher { //TODO Change launcher tu menu, use only one stage
    private Stage stage;
    private Scene defaultScene;
    private Scene settingsScene;
    private Scene playerSelect;
    private Settings settings;

    public Launcher(Stage primaryStage) throws IOException, ClassNotFoundException {
        settings = new SettingsLoader().loadSettings("settings.txt");
        stage = primaryStage;
        stage.setTitle("Fakehalla Launcher");
        defaultScene = generateLauncherScene();
        settingsScene = generateSettingsScene();
        playerSelect = generatePlayerSelectionScene();
        //defaultScene.setOnMouseExited(event -> System.out.println("pa"));
        stage.setScene(defaultScene);
    }


    public void run() {
        stage.show();
    }


    private void runGame() throws IOException, ClassNotFoundException {
        Game game = new Game("Fakehalla", settings.isFullscreen());
        game.start();
        stage.close();
    }
    private void runSettings() {
        stage.setScene(settingsScene);
    }

    private void runScoreboard() throws FileNotFoundException {
        stage.setScene(generateScoreboardScene());
    }

    private void runPlayerSelectionScene(){
        stage.setScene(playerSelect);
    }

    private Scene generateScoreboardScene() throws FileNotFoundException {
        PlayerScore ps = new PlayerScore("src\\resources\\score.txt");
        LinkedList<HashElement> scoreMap = ps.getScoreMap();
        GridPane gridPane = new GridPane();
        int count = 1;
        Label first = new Label("Top scores");
        first.setStyle("-fx-font-size: 1.5em;");
        gridPane.add(first, 0, 0);
        for(HashElement hashElement : scoreMap) {
            Label label = new Label(hashElement.toString());
            label.setStyle("-fx-font-size: 3em; -fx-border-width: 2;");
            label.setMinWidth(400);
            gridPane.add(label, 0, count);
            if(++count==6)
                break;
        }
        Button button = new Button("Close");
        button.setStyle("-fx-font-size: 1.5em;");
        button.setOnAction(e-> stage.setScene(defaultScene));
        gridPane.add(button, 0, count);
        gridPane.setAlignment(Pos.CENTER);
        return new Scene(gridPane,800,600);
    }

    private Scene generateLauncherScene() throws FileNotFoundException {
        Button[] buttons = new Button[4];


        buttons[0] = new Button("Play");
        buttons[0].setOnAction(event -> {
            runPlayerSelectionScene();
        });

        buttons[1] = new Button("Options");
        buttons[1].setOnAction(event -> {
            runSettings();
        });

        buttons[2] = new Button("Scoreboard");
        buttons[2].setOnAction(event -> {
            try {
                runScoreboard();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        buttons[3] = new Button("Exit");
        buttons[3].setOnAction(event -> {
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
            i.onMouseEnteredProperty().set(event -> i.setBackground(new Background(new BackgroundFill(Color.MEDIUMPURPLE, CornerRadii.EMPTY, Insets.EMPTY))));
            i.onMouseExitedProperty().set(event -> i.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));
        }

        Label authors = new Label("© Danko a Jozko 2019");
        authors.setTextFill(Color.WHITE);

        Image image = new Image(new FileInputStream("src/resources/Logo.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(300);
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        VBox vbox = new VBox(buttons);
        vbox.setSpacing(40);
        vbox.setAlignment(Pos.CENTER);

        BorderPane borderpane = new BorderPane();
        borderpane.setCenter(vbox);
        borderpane.setTop(imageView);
        borderpane.setBottom(authors);
        borderpane.setAlignment(imageView, Pos.CENTER);
        borderpane.setAlignment(authors, Pos.BOTTOM_RIGHT);

        borderpane.setStyle("-fx-background-color: #cacaca;");
        return (new Scene(borderpane, 800, 600));
    }

    private Scene generateSettingsScene() {

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

    private Scene generatePlayerSelectionScene(){ //TODO name and player select
        Text[] texts = new Text[4];

        texts[0] = new Text("Player 1 name: ");
        texts[1] = new Text("Player 2 name: ");
        texts[2] = new Text("Player 1 skin: ");
        texts[3] = new Text("Player 2 skin: ");

        TextField[] textFields = new TextField[2];
        textFields[0] = new TextField(settings.getPlayer1().getName());
        textFields[1] = new TextField(settings.getPlayer2().getName());

        ComboBox[] comboBoxes = new ComboBox[2];
        comboBoxes[0] = new ComboBox();
        comboBoxes[1] = new ComboBox();

        ImageView img1 = new ImageView(new Image("resources/PlayerAnimation/Player1/front.png"));
        ImageView img2 = new ImageView(new Image("resources/PlayerAnimation/Player2/front.png"));
        img1.setId(String.valueOf(1));
        img2.setId(String.valueOf(2));
        comboBoxes[0].setValue(img1);
        comboBoxes[1].setValue(img2);

        comboBoxes[0].setOnMouseClicked(event -> {
            ImageView temp = (ImageView) comboBoxes[0].getValue();
            comboBoxes[0].getItems().clear();
            for (int i = 0; i < 3; i++) {
                ImageView img = new ImageView(new Image("resources/PlayerAnimation/Player"+ (i + 1) +"/front.png"));
                img.setId(String.valueOf(i+1));
                comboBoxes[0].getItems().add(img);
            }
            comboBoxes[0].setValue(temp);
        });

        comboBoxes[1].setOnMouseClicked(event -> {
            ImageView temp = (ImageView) comboBoxes[1].getValue();
            comboBoxes[1].getItems().clear();
            for (int i = 0; i < 3; i++) {
                ImageView img = new ImageView(new Image("resources/PlayerAnimation/Player"+ (i + 1) +"/front.png"));
                img.setId(String.valueOf(i+1));
                comboBoxes[1].getItems().add(img);
            }
            comboBoxes[1].setValue(temp);
        });

        Button play = new Button("Play");
        play.setOnAction(e-> {
            settings.getPlayer1().setSkin("Player"+((ImageView) comboBoxes[0].getValue()).getId());
            settings.getPlayer2().setSkin("Player"+((ImageView) comboBoxes[1].getValue()).getId());
            settings.getPlayer1().setName(textFields[0].getText());
            settings.getPlayer2().setName(textFields[1].getText());
            try {
                new SettingsSaver().saveSettings("settings.txt", settings);
                runGame();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }

        });

        Button back = new Button("Back");
        back.setOnAction(e-> {
            this.playerSelect = generatePlayerSelectionScene();
            stage.setScene(defaultScene);});

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        for (int i = 0; i < 2; i++) {
            texts[i].setStyle("-fx-font-size: 1.5em;");
            texts[i+2].setStyle("-fx-font-size: 1.5em;");
            textFields[i].setStyle("-fx-font-size: 1.5em;");
            textFields[i].setMaxWidth(120);
            comboBoxes[i].setStyle("-fx-font-size: 1.5em;");
            comboBoxes[i].setMinWidth(120);
            gridPane.add(texts[i], 0,i);
            gridPane.add(textFields[i],1,i);
            gridPane.add(texts[i+2], i,2);
            gridPane.add(comboBoxes[i], i, 3);
        }

        play.setStyle("-fx-font-size: 1.5em;");
        play.setMinWidth(120);
        back.setStyle("-fx-font-size: 1.5em;");
        back.setMinWidth(120);
        gridPane.add(play, 0,4);
        gridPane.add(back, 1,4);

        return new Scene(gridPane, 800, 600);
    }
    //private Scene generateCreditsScene(){} TODO

}
