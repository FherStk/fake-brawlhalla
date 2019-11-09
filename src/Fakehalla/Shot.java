package Fakehalla;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Shot implements Updatable{
    private Rectangle body;
    private Vector2D velocity;
    private final double shotSpeed = 10;
    private final double shotWidth = 20;
    private final double shotHeight = 5;

    public Shot(double xCor, double yCor)
    {
        body.setWidth(shotWidth);
        body.setHeight(shotHeight);
        body.setX(xCor);
        body.setY(yCor);
        velocity = new Vector2D(new Point2D(shotSpeed,0));
    }

    @Override
    public void update(double dt, double gameWidth, double gameHeight) {
        //todo
    }

    @Override
    public boolean inBounds(double widthLimit, double heightLimit, double stepX, double stepY) {
        //todo
        return false;
    }

    private Color getRandomColor()
    {
        //todo: return random color
        return null;
    }

}
