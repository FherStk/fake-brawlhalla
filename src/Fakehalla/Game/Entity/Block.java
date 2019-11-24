package Fakehalla.Game.Entity;

import javafx.geometry.Point2D;

public class Block extends Entity {
    public Block(Texture texture, Point2D position,double width, double height)
    {
        super(texture,position,null,width,height);
        setVelocity(null);
    }
}
