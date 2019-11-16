package Fakehalla;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Shot implements Updatable{
    private Rectangle body;
    private Point2D pos;
    private Vector2D velocity;
    private double shotSpeed;
    private final double shotWidth = 20;
    private final double shotHeight = 5;

    public Shot(double xCor, double yCor,Face playerFace)
    {
        body = new Rectangle();
        body.setWidth(shotWidth);
        body.setHeight(shotHeight);
        body.setX(xCor);
        body.setY(yCor);
        pos = new Point2D(xCor,yCor);
        body.setFill(getRandomColor());
        if(playerFace == Face.LEFT) {shotSpeed = -10;}
        else{ shotSpeed = 10 ;}
        velocity = new Vector2D(new Point2D(shotSpeed,0));
    }

    @Override
    public void update(double dt,double gameWidth, double gameHeight) {
        setPos(pos.add(new Point2D(velocity.getDirection().getX()*dt,0)));
    }

    @Override
    public boolean inBounds(double widthLimit, double heightLimit, double stepX, double stepY) {
        return body.getX() <= widthLimit && body.getX() + body.getWidth() >= 0;
    }

    public Rectangle getBody() { return this.body; }

    private Color getRandomColor()
    {
        Random r = new Random();
        return new Color(r.nextFloat(),r.nextFloat(),r.nextFloat(),1);
    }

    private void setPos(Point2D pos)
    {
        this.pos = pos;
        this.body.setX(pos.getX());
        this.body.setY(pos.getY());
    }


}
