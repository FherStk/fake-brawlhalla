package Fakehalla.Game.Entity;

import Fakehalla.Game.Entity.Animations.BackgroundAnimation;
import Fakehalla.Game.Utils.Vector2D;
import Fakehalla.Settings.Settings;
import Fakehalla.Settings.SettingsLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;


import java.io.IOException;
import java.util.ArrayList;

public class Background implements Updatable {
    private BackgroundAnimation background;
    private BackgroundAnimation foreground;
    private BackgroundAnimation mountain_far;
    private BackgroundAnimation mountains;
    private BackgroundAnimation trees;
    private StackPane out;
    private Settings settings;
    private int width, height;


    public Background() throws IOException, ClassNotFoundException {
        settings = new SettingsLoader().loadSettings("settings.txt");
        width = settings.getWidth();
        height = settings.getHeight();
        background = new BackgroundAnimation(new Image("resources/layers/parallax-mountain-bg.png",width,height,false,false));
        foreground = new BackgroundAnimation(new Image("resources/layers/parallax-mountain-foreground-trees.png",width*2,height,false,false));
        mountain_far = new BackgroundAnimation(new Image("resources/layers/parallax-mountain-montain-far.png",width,height,false,false));
        mountains = new BackgroundAnimation(new Image("resources/layers/parallax-mountain-mountains.png",width*2,height,false,false));
        trees = new BackgroundAnimation(new Image("resources/layers/parallax-mountain-trees.png",width*2,height,false,false));

        out = new StackPane(background, mountain_far, mountains, trees, foreground);
    }

    public StackPane getBackground() {
        return out;
    }

    @Override
    public void update(long currentTime, double dt, double gameWidth, double gameHeight, Vector2D gravity, ArrayList<Updatable> objToInteract, ArrayList<Block> gameObj) {
        mountains.update(currentTime/16,0);
        trees.update(currentTime/12,0);
        foreground.update(currentTime/8,(int) gameWidth/32); //bulgarian constant, because of bugged model
    }

    @Override
    public boolean inBounds(double widthLimit, double heightLimit, double stepY) {
        return true;
    }
}
