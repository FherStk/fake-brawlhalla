package Fakehalla;
/* lets see if this works*/
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.stage.Stage;



public class Main extends Application {
    public static void main (String[] args)
    {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage)
    {
        Game game = new Game("Test",600,600,false);
        game.start();
    }
}
