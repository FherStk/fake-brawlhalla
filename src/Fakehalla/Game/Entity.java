package Fakehalla.Game;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Entity implements Updatable{
    private Texture defaultTexture;
    private Point2D position;
    private Vector2D velocity;
    private Rectangle body;
    private Face face;
    private final double width;
    private final double height;

    public Entity(Texture defaultTexture,Point2D position, Face face,double width, double height)
    {
        this.position = position;
        this.face = face;
        this.defaultTexture = defaultTexture;
        this.width = width;
        this.height = height;
        this.body = new Rectangle();
        this.body.setWidth(this.width); this.body.setHeight(this.height);
        this.body.setFill(defaultTexture.getTexture());
    }

    public Entity(Point2D position, Face face,double width, double height)
    {
        this.position = position;
        this.face = face;
        this.width = width;
        this.height = height;
        this.body = new Rectangle();
        this.body.setWidth(this.width); this.body.setHeight(this.height);
    }

    public void update(double dt, double gameWidth, double gameHeight, ArrayList<Updatable> objToInteract, ArrayList<Rectangle> gameObj)
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

    public Face getFace() {
        return face;
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

    public void setDefaultTexture(Texture t) { this.defaultTexture = t; }

    public void setVelocity(Vector2D velocity) { this.velocity = velocity; }

    public void setFace(Face face) { this.face = face; }
}
