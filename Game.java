package Fakehalla;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Game  {

    private Stage stage;
    private Group group;
    private Scene scene;

    private int width;
    private int heigth;
    private ArrayList<Entity> objects;


    public Game(String title,int w, int h)
    {
        width = w;
        heigth = h;
        stage = new Stage();
        group = new Group();


        objects = new ArrayList<Entity>();
        scene = new Scene(group);
        setScene();
        stage.setScene(scene);
        stage.setTitle(title);

        stage.setWidth(600);
        stage.setHeight(800);
    }

    public Stage getStage() { return stage; }
    public Scene getScene() {return scene;}
    public ArrayList<Entity> getAllObjects() { return objects;}

    private void setScene()
    {
        Player ball = new Player(100,100,20);
        objects.add(ball);

        for (Entity e : objects)
        {
            if(e instanceof Player)
            {
                scene.addEventHandler(KeyEvent.KEY_PRESSED, (key)->
                {
                    if (key.getCode() == KeyCode.A ) {
                        ((Player)e).setPressedLeft(true);
                    }
                    if (key.getCode() == KeyCode.D ) {
                        ((Player)e).setPressedRight(true);
                    }
                    if(key.getCode() == KeyCode.SPACE)
                    {
                        ((Player)e).setPressedJump(true);
                        ((Player) e).incrementIndex();
                    }
                });
                scene.addEventHandler(KeyEvent.KEY_RELEASED, (key)->
                {
                    if (key.getCode() == KeyCode.A ) {
                        ((Player)e).setPressedLeft(false);
                    }
                    if (key.getCode() == KeyCode.D ) {
                        ((Player)e).setPressedRight(false);
                    }
                    if(key.getCode() == KeyCode.SPACE )
                    {
                        ((Player)e).setPressedJump(false);
                    }
                });

                group.getChildren().add(((Player)e).getBody());
            }
        }
    }
}
