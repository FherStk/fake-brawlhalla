package Fakehalla;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application {
    public static void main (String[] args)
    {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage)
    {
        Game game = new Game("AAA",800,600  );
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                for(Entity e : game.getAllObjects())
                {
                    e.fall(game.getScene().getWidth(),game.getScene().getHeight());
                    if(e instanceof Player)
                    {
                        if(((Player) e).getPressedJump()) {((Player) e).Jump();}
                        if(((Player) e).getPressedLeft()) {((Player) e).moveLeft();}
                        if(((Player) e).getPressedRight()) {((Player) e).moveRight();}
                    }
                }
            }
        }.start();
        game.getStage().show();
    }
}
