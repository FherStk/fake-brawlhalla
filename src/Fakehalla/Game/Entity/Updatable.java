package Fakehalla.Game.Entity;

import Fakehalla.Game.Utils.Vector2D;
import javafx.geometry.Point2D;

import java.util.ArrayList;

public interface Updatable { // interface of every object that is going to be moving (move = update + draw)
    public void update(long currentTime,double dt, double gameWidth, double gameHeight,Vector2D gravity, ArrayList<Updatable> objToInteract, ArrayList<Block> gameObj);
    public boolean inBounds(double widthLimit,double heightLimit, double stepY);

}
