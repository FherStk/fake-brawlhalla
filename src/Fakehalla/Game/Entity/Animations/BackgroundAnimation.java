package Fakehalla.Game.Entity.Animations;

import Fakehalla.Settings.Settings;
import Fakehalla.Settings.SettingsLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class BackgroundAnimation extends ImageView {
    private Rectangle2D croppedPortion;
    private Settings settings;
    int width, height;
    public BackgroundAnimation(Image image) throws IOException, ClassNotFoundException {
        super(image);
        this.settings = new SettingsLoader().loadSettings("settings.txt");
        width = settings.getWidth();
        height = settings.getHeight();
        this.setFitHeight(height);
        this.setFitWidth(width);
        this.setPreserveRatio(false);
        this.setViewport(new Rectangle2D(0, 0, width, height));
    }

    public void update(long currentTime){
        croppedPortion = new Rectangle2D(currentTime%width, 0, width, height);
        this.setViewport(croppedPortion);
    }
}
