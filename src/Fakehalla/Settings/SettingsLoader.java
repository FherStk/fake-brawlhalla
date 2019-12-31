package Fakehalla.Settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SettingsLoader {
    public Settings loadSettings(String filename) throws IOException, ClassNotFoundException //settings loader
    {
        Settings settings;
        try(ObjectInputStream reader = new ObjectInputStream(new FileInputStream(filename))){
            settings = (Settings) reader.readObject();
        }
        return settings;
    }
}
