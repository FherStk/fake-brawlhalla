package Fakehalla.Game.Entity;

import Fakehalla.Game.Utils.Vector2D;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ActivationBlock extends Block implements Updatable {
    private boolean isActivated = false;
    private long currentTimeFlag;
    private int duration;

    public ActivationBlock(Texture texture, Point2D position, double width, double height) {
        super(texture, position, width, height);
    }

    public boolean collisionPlayer(Player player)
    {
        return player.getPosition().getX() < this.getPosition().getX() + this.getWidth()
            && player.getPosition().getX() + player.getWidth() > this.getPosition().getX()
            && player.getPosition().getY() < this.getBody().getY() + this.getHeight()
            && player.getPosition().getY() + player.getHeight() > this.getPosition().getY();
    }

    public void update(long currentTime, double dt, double gameWidth, double gameHeight, Vector2D gravity, ArrayList<Updatable> objToInteract, ArrayList<Block> gameObj)
    {
        for(Updatable u : objToInteract)
        {
            if(u instanceof Player)
            {
                if(collisionPlayer((Player) u) && !this.isActivated && this.getBody().isVisible())
                {
                    this.isActivated = true;
                }
            }
        }
        int spawnDelay = 10000;
        if(currentTime <= this.currentTimeFlag + this.duration + spawnDelay)
        {
            this.getBody().setVisible(false);
        }
        else
        {
            this.getBody().setVisible(true);
        }
    }

    public boolean isActivated() { return this.isActivated; }

    public void reset(int duration,long currentTime,double gameWidth,double gameHeight)
    {
        this.isActivated = false;
        this.duration = duration;
        this.currentTimeFlag = currentTime;
        this.setPosition(newPosition(gameWidth,gameHeight));
    }

    private Point2D newPosition(double gameWidth,double gameHeight)
    {
        int tempX = ThreadLocalRandom.current().nextInt((int)(gameWidth/10),(int)(gameWidth-gameWidth/10));
        int tempY = ThreadLocalRandom.current().nextInt((int)(gameHeight/3),(int)(gameHeight/2));
        return new Point2D(tempX,tempY);
    }
}
