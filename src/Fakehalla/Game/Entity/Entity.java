package Fakehalla.Game.Entity;

import Fakehalla.Game.Utils.Vector2D;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Entity implements Updatable{ //abstract class
    public Texture getDefaultTexture() {
        return defaultTexture;
    }

    private Texture defaultTexture;
    private Point2D position;
    private Vector2D velocity;
    private Rectangle body;
    private Direction direction;
    private final double width;
    private final double height;

    public Entity(Texture defaultTexture,Point2D position, Direction direction,double width, double height)
    {
        this.position = position;
        this.direction = direction;
        this.defaultTexture = defaultTexture;
        this.width = width;
        this.height = height;
        this.body = new Rectangle();
        this.body.setWidth(this.width); this.body.setHeight(this.height);
        this.body.setFill(defaultTexture.getTexture());
        setPosition(this.position);
    }

    public Entity(Point2D position, Direction direction,double width, double height)
    {
        this.position = position;
        this.direction = direction;
        this.width = width;
        this.height = height;
        this.body = new Rectangle();
        this.body.setWidth(this.width); this.body.setHeight(this.height);
        setPosition(this.position);
    }

    public void update(long currentTime,double dt, double gameWidth, double gameHeight,Vector2D gravity, ArrayList<Updatable> objToInteract, ArrayList<Block> gameObj)
    {
        setPosition(position.add(velocity.getDirection()));
    }

    public boolean inBounds(double widthLimit,double heightLimit, double stepY)
    {
        return position.getY() + body.getWidth() + stepY > heightLimit;
    }

    public void setPosition(Point2D newPos)
    {
        this.position = newPos;
        this.body.setX(newPos.getX());
        this.body.setY(newPos.getY());
    }

    public Point2D getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public Rectangle getBody() {
        return body;
    }

    public Vector2D getVelocity() { return velocity; }

    public void setDefaultTexture(Texture t) {
        this.defaultTexture = t;
        this.getBody().setFill(this.defaultTexture.getTexture());
        if(t.isMirrored())
            this.getBody().setScaleX(-1);
        else
            this.getBody().setScaleX(1);
    }

    public void setVelocity(Vector2D velocity) { this.velocity = velocity; }

    public void setDirection(Direction direction) {
        if(direction != this.direction) {
            this.direction = direction;

        }
    }
}
