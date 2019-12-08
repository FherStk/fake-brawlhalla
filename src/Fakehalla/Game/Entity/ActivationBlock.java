package Fakehalla.Game.Entity;

import Fakehalla.Game.Utils.Vector2D;
import javafx.geometry.Point2D;

import java.util.ArrayList;

public class ActivationBlock extends Block implements Updatable {
    private boolean isActivated = false;
    public ActivationBlock(Texture texture, Point2D position, double width, double height) {
        super(texture, position, width, height);
    }

    public boolean collisionPlayer(Player player)
    {
        /*if(player.getPosition().getX() >= this.getPosition().getX() && player.getPosition().getX() + player.getWidth() <= this.getPosition().getX() + this.getWidth()
        && player.getPosition().getY() >= this.getBody().getY() && player.getPosition().getY() + player.getHeight() <= this.getPosition().getY() + this.getHeight()) */
        if(this.getBody().contains(player.getPosition()))
        {
            return true;
        }
        return false;
    }

    public void update(long currentTime, double dt, double gameWidth, double gameHeight, Vector2D gravity, ArrayList<Updatable> objToInteract, ArrayList<Block> gameObj)
    {
        for(Updatable u : objToInteract)
        {
            if(u instanceof Player)
            {
                if(collisionPlayer((Player) u) && !isActivated)
                {
                    this.isActivated = true;
                }
            }
        }
    }

    public boolean isActivated() { return this.isActivated; }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }
}
