package Fakehalla.Game;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Shot implements Updatable{
    private Rectangle body;
    private Point2D pos;
    private Vector2D velocity;
    private Face playerFace;
    private double shotSpeed;
    private final double shotWidth;
    private final double shotHeight;
    private boolean hit;

    public Shot(double xCor, double yCor,double playerWidth,Face playerFace)
    {
        shotWidth = playerWidth/2;
        shotHeight = playerWidth / 4;
        this.playerFace = playerFace;
        body = new Rectangle();
        body.setWidth(shotWidth);
        body.setHeight(shotHeight);
        body.setFill(getRandomColor());

        if(playerFace == Face.LEFT)
        {
            body.setX(xCor - body.getWidth());
            shotSpeed = -10;
        }
        else
        {
            body.setX(xCor + playerWidth + 1);
            shotSpeed = 10 ;
        }
        body.setY(yCor);
        pos = new Point2D(body.getX(),body.getY());
        velocity = new Vector2D(new Point2D(shotSpeed,0));
        hit = true;
    }

    @Override
    public void update(double dt, double gameWidth, double gameHeight, ArrayList<Updatable> objToInteract,ArrayList<Rectangle> gameObj) {
        if(inBounds(gameWidth,gameHeight,velocity.getDirection().getX()*dt,0))
        {
            setPos(pos.add(new Point2D(velocity.getDirection().getX()*dt,0)));
        }
    }

    @Override
    public boolean inBounds(double widthLimit, double heightLimit, double stepX, double stepY) {
        return body.getX() <= widthLimit && body.getX() + body.getWidth() >= 0;
    }

    public Rectangle getBody() { return this.body; }
    public boolean isHit() { return this.hit; }
    public Face getPlayerFace() { return this.playerFace; }
    public Vector2D getVelocity() { return this.velocity; }

    public Point2D getPos() { return this.pos; }

    public void setHit(boolean hit) { this.hit = hit; }

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
