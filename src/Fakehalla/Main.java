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
        Stage s = new Stage();
        s.setWidth(600);
        s.setHeight(800);

        Game game = new Game(s);
        game.start();
    }
}
