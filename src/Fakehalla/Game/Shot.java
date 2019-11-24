package Fakehalla.Game;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

public class Shot extends Entity implements Updatable{
    private Texture texture;
    private double shotSpeed;
    private boolean hit;
    private final double shotToPlayer = 0.25;
    private final String filename = "textures/donald.jpg";

    public Shot(Point2D startPosition,Face playerFace, double shotWidth,double shotHeight,double playerWidth, double playerHeight)
    {
        super(startPosition,playerFace,shotWidth,shotHeight);
        this.texture = new Texture(filename);
        this.shotSpeed = 10;
        this.hit = true;
        setVelocity(new Vector2D(new Point2D(shotSpeed,0)));
        chooseFace();
        chooseStartPosition(playerWidth,playerHeight);
        setDefaultTexture(this.texture);
    }

    @Override
    public void update(double dt, double gameWidth, double gameHeight, ArrayList<Updatable> objToInteract,ArrayList<Rectangle> gameObj)
    {
        this.getVelocity().multiply(dt);
        this.setPosition(this.getPosition().add(this.getVelocity().getDirection()));
    }

    @Override
    public boolean inBounds(double widthLimit,double heightLimit, double stepY)
    {
        return this.getBody().getX() <= widthLimit && this.getBody().getX() + this.getBody().getWidth() >= 0;
    }

    public boolean isHit() { return this.hit; }

    public void setHit(boolean hit) { this.hit = hit; }

    private void chooseFace()
    {
        if(this.getFace() == Face.LEFT)
        {
            this.shotSpeed *= -1;
            setVelocity(new Vector2D(new Point2D(this.getVelocity().getDirection().getX()*-1,this.getVelocity().getDirection().getY())));
        }
    }

    private void chooseStartPosition(double playerWidth, double playerHeight)
    {
        System.out.println(this.getFace());
        if(this.getFace() == Face.LEFT)
        {
            this.setPosition(new Point2D(this.getPosition().getX() - this.getBody().getWidth()/2 - 1, this.getPosition().getY() + playerHeight*shotToPlayer));
        }
        else
        {
            this.setPosition(new Point2D(this.getPosition().getX() + playerWidth + 1, this.getPosition().getY()+ playerHeight*shotToPlayer ));
        }

        System.out.println("shot position: " + this.getPosition());
    }
}
