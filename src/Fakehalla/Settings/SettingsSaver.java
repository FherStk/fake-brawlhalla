package Fakehalla.Settings;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SettingsSaver {
    public void saveSettings(String filename, Settings settings) throws IOException {
        try(ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(filename))) {
            writer.writeObject(settings);
        }
    }
}
