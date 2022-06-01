package Fakehalla.Game.Entity;

import Fakehalla.Game.Entity.Animations.BackgroundImageView;
import Fakehalla.Game.Utils.Vector2D;
import Fakehalla.Settings.Settings;
import Fakehalla.Settings.SettingsLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Background implements Updatable { //setting bacground class
    private BackgroundImageView background;
    private BackgroundImageView foreground;
    private BackgroundImageView mountain_far;
    private BackgroundImageView mountains;
    private BackgroundImageView trees;
    private BackgroundImageView eventImageView;
    private StackPane out;
    private Settings settings;
    private int width, height, eventMultiplier = 1;


    public Background() throws IOException, ClassNotFoundException {
        settings = new SettingsLoader().loadSettings("settings.txt");
        width = settings.getWidth();
        height = settings.getHeight();
        background = new BackgroundImageView(new Image(new FileInputStream("src/resources/layers/parallax-mountain-bg.png"),width,height,false,false));
        foreground = new BackgroundImageView(new Image(new FileInputStream("src/resources/layers/parallax-mountain-foreground-trees.png"),width*2,height,false,false));
        mountain_far = new BackgroundImageView(new Image(new FileInputStream("src/resources/layers/parallax-mountain-montain-far.png"),width,height,false,false));
        mountains = new BackgroundImageView(new Image(new FileInputStream("src/resources/layers/parallax-mountain-mountains.png"),width*2,height,false,false));
        trees = new BackgroundImageView(new Image(new FileInputStream("src/resources/layers/parallax-mountain-trees.png"),width*2,height,false,false));
        eventImageView = new BackgroundImageView(new Image(new FileInputStream("src/resources/layers/eventBackground.png"),width ,height,false,false));
        eventImageView.setVisible(false);
        out = new StackPane(background, mountain_far, mountains, trees, foreground,eventImageView);
    }

    public StackPane getBackground() {
        return out;
    }

    public void activatedEvent(boolean activation){
        if(activation && !eventImageView.isVisible()) {
            eventMultiplier = 4;
            eventImageView.setVisible(true);
        }
        if(!activation && eventImageView.isVisible()){
            eventMultiplier = 1;
            eventImageView.setVisible(false);
        }
    }

    @Override
    public void update(long currentTime, double dt, double gameWidth, double gameHeight, Vector2D gravity, ArrayList<Updatable> objToInteract, LinkedList<Block> gameObj) { //updating background according to game time etc
        mountains.update(currentTime*dt*0.0625*eventMultiplier,0);
        trees.update(currentTime*dt*0.08*eventMultiplier,0);
        foreground.update(currentTime*dt*0.125*eventMultiplier,(int) gameWidth/32); //bulgarian constant, because of bugged model
    }

    @Override
    public boolean inBounds(double widthLimit, double heightLimit, double stepY) {
        return true;
    }
}
