package Fakehalla;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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

    private double startTime;
    private double currentTime;



    public Game(String title,double w, double h,boolean fullscreen) // dev constructor
    {
        width = w;
        height = h;
        stage = new Stage();
        group = new Group();
        startTime = System.nanoTime();
        currentTime = 0;

        objects = new ArrayList<Updatable>();
        setScene();
        stage.setScene(scene);
        stage.setTitle(title);

        if(!fullscreen)
        {
            stage.setWidth(w);
            stage.setHeight(h);
        }
        else{
            stage.setFullScreen(true);
        }


        scene.setFill(Color.LIGHTGRAY);
    }

    public Game(Stage stage) // ready constructor (i hope)
    {
        this.stage = stage;
        group = new Group();
        startTime = System.nanoTime();
        currentTime = 0;
        objects = new ArrayList<Updatable>();
        setScene();
        stage.setScene(scene);
        scene.setFill(Color.LIGHTGRAY);
    }

    public Stage getStage() { return stage; }
    public Scene getScene() {return scene;}
    public ArrayList<Updatable> getAllObjects() { return objects;}

    public void start() // starting the game loop
    {
        loop = new AnimationTimer() { // the game loop is implemented by AnimationTimer (built in javafx)
            @Override
            public void handle(long l) { // i have no idea why there is an argument
                currentTime = startTime;
                startTime = System.nanoTime();
                double dt = currentTime - startTime; // calculating dt (time that passed since the last loop)
                if(dt > 0.15){ // checking if the time isnt too big
                    dt = 0.15;
                }
                for (Updatable u : objects)
                {
                    u.update(scene.getWidth(),scene.getHeight());
                }

            }
        };
        startLoop(); // starting the loop
        stage.show();
        //System.out.println(stage.getHeight());
    }

    public void startLoop() {loop.start();}
    public void stopLoop() { loop.stop();}

    private void setScene()
    {
        scene = new Scene(group);
        for(int i = 0; i < numberOfPlayers; i++) // adding players
        {
            Player newPlayer = new Player(Color.BLACK,this.width,this.height,this.width / 2 + 100*i, this.height / 2);
            objects.add(newPlayer);

            scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) ->
            {
                if(key.getCode() == newPlayer.getMoveRightKey()) { newPlayer.setMoveR(true);}
                if(key.getCode() == newPlayer.getMoveLeftKey()) { newPlayer.setMoveL(true);}
            });
            scene.addEventHandler(KeyEvent.KEY_RELEASED, (key)->
            {
                if(key.getCode() == newPlayer.getMoveRightKey()) { newPlayer.setMoveR(false);}
                if(key.getCode() == newPlayer.getMoveLeftKey()) { newPlayer.setMoveL(false);}
            });

            group.getChildren().add(newPlayer.getBody());
        }
        scene.setFill(Color.LIGHTGRAY);
    }
}
