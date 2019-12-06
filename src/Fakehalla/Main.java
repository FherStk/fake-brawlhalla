package Fakehalla;
/* lets see if this works*/
import Fakehalla.Menu.Launcher;
import Fakehalla.Settings.Settings;
import Fakehalla.Settings.SettingsSaver;
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

/*Settings settings = new Settings();
        SettingsSaver settingsSaver = new SettingsSaver();
        settings.setPlayer1Jump(KeyCode.K);
        settings.setPlayer2Controls(KeyCode.K, KeyCode.K, KeyCode.K, KeyCode.K);
        settings.setSound(false);
        settings.getPlayer1().setName("s");
        settings.getPlayer2().setName("a");
        settings.setResolution(1920,1080,false);
        settingsSaver.saveSettings("settings.txt", settings);*/