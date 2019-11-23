package Fakehalla;
/* lets see if this works*/
import Fakehalla.Menu.Launcher;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
    public static void main (String[] args)
    {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        Launcher launcher = new Launcher(primaryStage);
        launcher.run();

    }
}
