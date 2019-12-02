package Fakehalla.Game.Entity;

import Fakehalla.Game.Entity.Animations.BackgroundImageView;
import Fakehalla.Game.Utils.Vector2D;
import Fakehalla.Settings.Settings;
import Fakehalla.Settings.SettingsLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;


import java.io.IOException;
import java.util.ArrayList;

public class Background implements Updatable {
    private BackgroundImageView background;
    private BackgroundImageView foreground;
    private BackgroundImageView mountain_far;
    private BackgroundImageView mountains;
    private BackgroundImageView trees;
    private StackPane out;
    private Settings settings;
    private int width, height;


    public Background() throws IOException, ClassNotFoundException {
        settings = new SettingsLoader().loadSettings("settings.txt");
        width = settings.getWidth();
        height = settings.getHeight();
        background = new BackgroundImageView(new Image("resources/layers/parallax-mountain-bg.png",width,height,false,false));
        foreground = new BackgroundImageView(new Image("resources/layers/parallax-mountain-foreground-trees.png",width*2,height,false,false));
        mountain_far = new BackgroundImageView(new Image("resources/layers/parallax-mountain-montain-far.png",width,height,false,false));
        mountains = new BackgroundImageView(new Image("resources/layers/parallax-mountain-mountains.png",width*2,height,false,false));
        trees = new BackgroundImageView(new Image("resources/layers/parallax-mountain-trees.png",width*2,height,false,false));

        out = new StackPane(background, mountain_far, mountains, trees, foreground);
    }

    public StackPane getBackground() {
        return out;
    }

    @Override
    public void update(long currentTime, double dt, double gameWidth, double gameHeight, Vector2D gravity, ArrayList<Updatable> objToInteract, ArrayList<Block> gameObj) {
        mountains.update(gameWidth/2000*(currentTime/16),0);
        trees.update(gameWidth/2000*(currentTime/12),0);
        foreground.update(gameWidth/2000*(currentTime/8),(int) gameWidth/32); //bulgarian constant, because of bugged model
    }

    @Override
    public boolean inBounds(double widthLimit, double heightLimit, double stepY) {
        return true;
    }
}
