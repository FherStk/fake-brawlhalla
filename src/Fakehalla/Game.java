package Fakehalla;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;

public class Game  {

    private Stage stage;
    private Group group;
    private Scene scene;
    private AnimationTimer loop;
    private Label scoreBoard;
    private int numberToWin = 10;
    private String fontName = "Verdana";

    private double width;
    private double height;
    private ArrayList<Updatable> objects;
    private ArrayList<Rectangle> blocks;
    private Player player1;
    private Player player2;

    private double prevTime;
    private double currentTime;

    private boolean gameOver = false;

    public Game(String title,double w, double h,boolean fullscreen) // dev constructor
    {
        width = w;
        height = h;
        stage = new Stage();
        group = new Group();
        currentTime = 0;

        objects = new ArrayList<Updatable>();
        createMap();
        createScene();
        stage.setScene(scene);
        stage.setTitle(title);
        currentTime = System.currentTimeMillis();

        if(!fullscreen)
        {
            stage.setWidth(w);
            stage.setHeight(h);
        }
        else{
            stage.setFullScreen(true);
        }


    }

    public Game(Stage stage) // ready constructor (i hope)
    {
        this.stage = stage;
        group = new Group();
        currentTime = 0;
        objects = new ArrayList<Updatable>();
        createMap();
        createScene();
        stage.setScene(scene);
        scene.setFill(Color.LIGHTGRAY);
        currentTime = System.currentTimeMillis();

    }

    public Stage getStage() { return stage; }
    public Scene getScene() {return scene;}
    public ArrayList<Updatable> getAllObjects() { return objects;}

    public void start() // starting the game loop
    {
        loop = new AnimationTimer() { // the game loop is implemented by AnimationTimer (built in javafx)
            @Override
            public void handle(long l) {
                prevTime = currentTime;
                currentTime = System.currentTimeMillis();
                double dt = (currentTime - prevTime ) * 0.1;

                ArrayList<Updatable> objectsToRemove = new ArrayList<>();
                for (Updatable u : objects)
                {
                    u.update(dt,scene.getWidth(),scene.getHeight(),objects,blocks);
                    if(!u.inBounds(scene.getWidth(),scene.getHeight(),0,0) && u instanceof Shot)
                    {
                        objectsToRemove.add(u);
                    }

                }
                objects.removeAll(objectsToRemove); //removing all shots out of bounds
                updateScoreBoard(player2.getScore(),player1.getScore());

                if(player1.getScore() >= numberToWin)
                {
                    group.getChildren().add(winLabel(player2.getPlayerName()));
                    stopLoop();
                }
                if(player2.getScore() >= numberToWin)
                {
                    group.getChildren().add(winLabel(player1.getPlayerName()));
                    stopLoop();
                }
            }
        };
        startLoop(); // starting the loop
        stage.show();
    }

    public void startLoop() {loop.start();}
    public void stopLoop() {
        System.out.println("game over");
        gameOver = true;
        loop.stop();
    }

    private void createScene()
    {
        scene = new Scene(group);
        scene.setFill(Color.LIGHTGRAY);

        scoreBoard = new Label("0 : 0");
        scoreBoard.setFont(new Font(fontName,width / 20));
        scoreBoard.setMinSize(width / 10,height/20);
        scoreBoard.setTranslateX(width/2 - scoreBoard.getMinWidth()/2);


        player1 = new Player(Color.BLACK,this.width,this.height,this.width / 2 - this.width/8,this.height / 4,Face.RIGHT,"Adolf");
        player2 = new Player(Color.BLUE,this.width,this.height,this.width / 2 + this.width/8,this.height / 4,Face.LEFT,"Hitler");

        this.objects.add(player1);
        this.objects.add(player2);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) ->
        {
            if(key.getCode() == player1.getMoveRightKey()) { player1.setMoveR(true);}
            if(key.getCode() == player1.getMoveLeftKey()) { player1.setMoveL(true);}
            if(key.getCode() == player1.getMoveShotKey())
            {
                if(player1.moveShot(this.width) instanceof Shot)
                {
                    Shot temp = player1.moveShot(this.width);
                    this.objects.add(temp);
                    group.getChildren().add(temp.getBody());
                }
            }
            if(key.getCode() == player1.getMoveJumpKey()) { player1.moveJump(scene.getHeight()); }
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
                if(player2.moveShot(this.width) instanceof Shot)
                {
                    Shot temp = player2.moveShot(this.width);
                    this.objects.add(temp);
                    group.getChildren().add(temp.getBody());
                }
            }
            if(key.getCode() == player2.getMoveJumpKey()) { player2.moveJump(scene.getHeight()); }
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

    private void createMap()
    {
        MapGenerator mGen = new MapGenerator(this.width,this.height);
        blocks = mGen.generateBlocks(1);
        System.out.println(this.width + " " + this.height);
        for(Rectangle r : blocks)
        {
            group.getChildren().add(r);
        }
    }

    private void updateScoreBoard(int score1, int score2)
    {
        scoreBoard.setText(score1 + " : " + score2);
    }

    private Label winLabel(String playerName)
    {
        Label win = new Label(playerName + " won !");
        win.setMinSize(width/4,height/4);
        win.setTranslateX(width/2 - win.getMinWidth()*1.5);
        win.setTranslateY(height/2);
        win.setFont(new Font(fontName,win.getMinWidth()/2));
        return win;
    }
}
