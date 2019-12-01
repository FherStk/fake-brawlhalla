package Fakehalla.Game.Entity;

import Fakehalla.Game.Utils.Vector2D;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import javax.swing.text.html.ImageView;
import java.util.ArrayList;
import java.util.Random;

public class Background implements Updatable {
    private Rectangle background;

    public Background(int width, int height) {
        background = new Rectangle();
        background.setWidth(width);
        background.setHeight(height);
    }

    public Rectangle getBackground() {
        return background;
    }

    @Override
    public void update(long currentTime, double dt, double gameWidth, double gameHeight, Vector2D gravity, ArrayList<Updatable> objToInteract, ArrayList<Block> gameObj) {
        //this.background = new image;
    }

    @Override
    public boolean inBounds(double widthLimit, double heightLimit, double stepY) {
        return true;
    }
}
