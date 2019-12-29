package Fakehalla.Game;

import Fakehalla.Game.Entity.*;
import Fakehalla.Game.Entity.Animations.Sound;
import Fakehalla.Game.Entity.Event;
import Fakehalla.Game.Utils.Vector2D;
import Fakehalla.Menu.Launcher;
import Fakehalla.Settings.Settings;
import Fakehalla.Settings.SettingsLoader;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Game  {

    private Stage stage;
    private Group group;
    private Scene scene;
    private AnimationTimer loop;
    private Label scoreBoard;
    private int numberToWin = 10;
    private String fontName = "Verdana";
    private Settings settings;
    private Background background;
    private Sound sound;

    private double width;
    private double height;
    private ArrayList<Updatable> objects;
    private LinkedList<Block> blocks;
    private Player player1;
    private Player player2;
    private Vector2D gravity;
    private ActivationBlock activationBlock;
    private Event event;
    private PlayerScore ps;

    private final String scoreFile = "src\\resources\\score.txt";

    private long prevTime;
    private long currentTime;

    private boolean gameOver = false;

    public Game(String title,boolean fullscreen) throws IOException, ClassNotFoundException // dev constructor
    {
        settings = new SettingsLoader().loadSettings("settings.txt");
        ps = new PlayerScore(scoreFile);
        width = settings.getWidth();
        height = settings.getHeight();
        stage = new Stage();
        group = new Group();
        currentTime = 0;

        objects = new ArrayList<>();
        createMap();
        createScene();
        stage.setScene(scene);
        stage.setTitle(title);
        currentTime = System.currentTimeMillis();

        stage.setResizable(false);

        sound = new Sound(settings.isSound());
         //Playing music accordint to settings

        if(!fullscreen)
        {
            stage.setWidth(width);
            stage.setHeight(height);
        }
        else
            {
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("Press ESC to exit game");
        }

        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode()== KeyCode.ESCAPE)
            {
                System.out.println("Stop");
                this.stopLoop();
                stage.close();
            }
        });

        stage.setOnHiding( event -> {
            sound.stop();
            Launcher launcher = null;
            try {
                launcher = new Launcher(new Stage());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            assert launcher != null;
            launcher.run();} );

    }

    public Game(Stage stage) throws IOException, ClassNotFoundException // ready constructor (i hope)
    {
        ps = new PlayerScore(scoreFile);
        this.stage = stage;
        group = new Group();
        currentTime = 0;
        objects = new ArrayList<>();
        createMap();
        createScene();
        stage.setScene(scene);
        scene.setFill(Color.LIGHTGRAY);
        currentTime = System.currentTimeMillis();

    }

    public Scene getScene() {return scene;}
    public PlayerScore getPlayerScore() { return this.ps; }

    public void start() // starting the game loop
    {
        this.event = new Event(player1.getWidth());
        this.objects.add(event);
        loop = new AnimationTimer() { // the game loop is implemented by AnimationTimer (built in javafx)
            @Override
            public void handle(long l) {

                gravity = new Vector2D(new Point2D(0,scene.getHeight()*0.0008));
                prevTime = currentTime;
                currentTime = System.currentTimeMillis();
                double dt = (currentTime - prevTime ) * 0.1;

                if(activationBlock.isActivated() && !event.isOn())
                {
                    event.start();
                    activationBlock.reset(event.getDuration(),currentTime,scene.getWidth(),scene.getHeight());
                }

                background.activatedEvent(event.isOn());
                sound.activatedEvent(event.isOn());

                ArrayList<Entity> objectsToRemove = new ArrayList<>();
                for (Updatable u : objects)
                {
                    u.update(currentTime,dt,scene.getWidth(),scene.getHeight(),gravity,objects,blocks);
                    if(u instanceof Shot)
                    {
                        if(!u.inBounds(scene.getWidth(),scene.getHeight(),0))
                        {
                            objectsToRemove.add(((Shot) u));
                        }
                    }
                }
                objects.removeAll(objectsToRemove); //removing all shots out of bounds

                if(event.getObjToAdd() != null)
                {
                    objects.add(event.getObjToAdd());
                    group.getChildren().add(((Shot) event.getObjToAdd()).getBody());
                }
                
                group.getChildren().removeAll(objectsToRemove);
                updateScoreBoard(player2.getScore(),player1.getScore());


                if(player1.getScore() >= numberToWin)
                {
                    group.getChildren().add(winLabel(player2.getPlayerName()));
                    try {
                        ps.refreshScore(player2.getPlayerName().replaceAll(" ",""));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stopLoop();
                }
                if(player2.getScore() >= numberToWin)
                {
                    group.getChildren().add(winLabel(player1.getPlayerName()));
                    try {
                        ps.refreshScore(player1.getPlayerName().replaceAll(" ",""));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stopLoop();
                }

            }
        };
        startLoop(); // starting the loop
        stage.show();
    }

    private void startLoop() {loop.start();}

    private void stopLoop()
    {
        gameOver = true;
        loop.stop();
    }

    private void createScene() throws IOException, ClassNotFoundException {

        scene = new Scene(group);
        scene.setFill(Color.LIGHTGRAY);
        scoreBoard = new Label("0 : 0");
        scoreBoard.setFont(new Font(fontName,width / 20));
        scoreBoard.setMinSize(width / 10,height/20);
        scoreBoard.setTranslateX(width/2 - scoreBoard.getMinWidth()/2);

        player1 = new Player(this.width,this.height,this.width / 2 - this.width/8,this.height / 4, settings.getPlayer1(),this.group);
        player2 = new Player(this.width,this.height,this.width / 2 + this.width/8,this.height / 4, settings.getPlayer2(),this.group);

        this.objects.add(background);
        this.objects.add(player1);
        this.objects.add(player2);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) ->
        {
            if(key.getCode() == player1.getMoveRightKey()) { player1.setMoveR(true);}
            if(key.getCode() == player1.getMoveLeftKey()) { player1.setMoveL(true);}
            if(key.getCode() == player1.getMoveShotKey())
            {
                if(player1.getCanShoot())
                {
                    Shot temp = player1.moveShot();
                    this.objects.add(temp);
                    group.getChildren().add(temp.getBody());
                }
            }
            if(key.getCode() == player1.getMoveJumpKey()) { player1.moveJump(); }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, (key)->
        {
            if(key.getCode() == player1.getMoveRightKey()) { player1.setMoveR(false);}
            if(key.getCode() == player1.getMoveLeftKey()) { player1.setMoveL(false);}
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) ->
        {
            if(key.getCode() == player2.getMoveRightKey()) { player2.setMoveR(true);}
            if(key.getCode() == player2.getMoveLeftKey()) { player2.setMoveL(true);}
            if(key.getCode() == player2.getMoveShotKey())
            {
                if(player2.getCanShoot())
                {
                    Shot temp = player2.moveShot();
                    this.objects.add(temp);
                    group.getChildren().add(temp.getBody());
                }

            }
            if(key.getCode() == player2.getMoveJumpKey()) {  player2.moveJump(); }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, (key)->
        {
            if(key.getCode() == player2.getMoveRightKey()) { player2.setMoveR(false);}
            if(key.getCode() == player2.getMoveLeftKey()) { player2.setMoveL(false);}
        });


        group.getChildren().add(player2.getBody());
        group.getChildren().add(player1.getBody());
        group.getChildren().add(scoreBoard);

    }

    private void createMap() throws IOException, ClassNotFoundException {
        background = new Background();
        group.getChildren().add(background.getBackground());
        group.getChildren().get(0).toBack();

        MapGenerator mGen = new MapGenerator();
        blocks = mGen.generateBlocks(this.width,this.height);
        for(Block r : blocks)
        {
            group.getChildren().add(r.getBody());
        }
        this.activationBlock = mGen.generateActivationBlock(this.width,new Point2D(this.width/2, this.height/3));
        this.group.getChildren().add(this.activationBlock.getBody());
        this.objects.add(this.activationBlock);
    }

    private void updateScoreBoard(int score1, int score2)
    {
        scoreBoard.setText(score1 + " : " + score2);
    }

    private Label winLabel(String playerName)
    {
        Label win = new Label(playerName + " won !\n press ESC");
        win.setMinSize(width/4,height/4);
        win.setTranslateX(width/2 - win.getMinWidth()*1.5);
        win.setTranslateY(height/2 - win.getMinHeight());
        win.setFont(new Font(fontName,win.getMinWidth()/2));
        return win;
    }
}
