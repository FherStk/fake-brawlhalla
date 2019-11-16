package Fakehalla;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;

public class Game  {

    private Stage stage;
    private Group group;
    private Scene scene;
    private AnimationTimer loop;
    private final int numberOfPlayers = 1;

    private double width;
    private double height;
    private ArrayList<Updatable> objects;
    private ArrayList<Rectangle> blocks;

    private double prevTime;
    private double currentTime;



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
            public void handle(long l) { // i have no idea why there is an argument

                prevTime = currentTime;
                currentTime = System.currentTimeMillis();
                double dt = (currentTime - prevTime ) * 0.1;

                ArrayList<Updatable> objectsToRemove = new ArrayList<>();
                for (Updatable u : objects)
                {
                    u.update(dt,scene.getWidth(),scene.getHeight());
                    if(!u.inBounds(scene.getWidth(),scene.getHeight(),0,0))
                    {
                        objectsToRemove.add(u);
                    }
                }
                objects.removeAll(objectsToRemove); //removing all shots out of bounds
            }
        };
        startLoop(); // starting the loop
        stage.show();
    }

    public void startLoop() {loop.start();}
    public void stopLoop() { loop.stop();}

    private void createScene()
    {
        scene = new Scene(group);
        scene.setFill(Color.LIGHTGRAY);
        for(int i = 0; i < numberOfPlayers; i++) // adding players
        {
            Player newPlayer = new Player(Color.BLACK,this.width,this.height,this.width / 2 + 100*i, this.height / 2 -100,this.blocks,Face.LEFT);
            objects.add(newPlayer);

            scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) ->
            {
                if(key.getCode() == newPlayer.getMoveRightKey()) { newPlayer.setMoveR(true);}
                if(key.getCode() == newPlayer.getMoveLeftKey()) { newPlayer.setMoveL(true);}
                if(key.getCode() == newPlayer.getMoveShotKey())
                {
                    if(newPlayer.moveShot(this.width) instanceof Shot)
                    {
                        Shot temp = newPlayer.moveShot(this.width);
                        this.objects.add(temp);
                        group.getChildren().add(temp.getBody());
                        System.out.println(this.objects.size());
                    }
                }
                if(key.getCode() == newPlayer.getMoveJumpKey()) { newPlayer.moveJump(scene.getHeight()); }
            });
            scene.addEventHandler(KeyEvent.KEY_RELEASED, (key)->
            {
                if(key.getCode() == newPlayer.getMoveRightKey()) { newPlayer.setMoveR(false);}
                if(key.getCode() == newPlayer.getMoveLeftKey()) { newPlayer.setMoveL(false);}
            });

            group.getChildren().add(newPlayer.getBody());
        }
    }

    private void createMap()
    {
        MapGenerator mGen = new MapGenerator(this.width,this.height);
        blocks = mGen.generateBlocks(1);
        for(Rectangle r : blocks)
        {
            System.out.println("x: " + r.getX() + " y: " + r.getY());
            group.getChildren().add(r);
        }
    }
}
