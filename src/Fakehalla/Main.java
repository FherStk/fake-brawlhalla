package Fakehalla;
/* lets see if this works*/
import Fakehalla.Launcher.Launcher;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
    public static void main (String[] args)
    {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        Launcher launcher = new Launcher(primaryStage);
        launcher.run();
    }
}
