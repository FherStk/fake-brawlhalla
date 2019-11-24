package Fakehalla.Game.Entity;

import Fakehalla.Game.Vector2D;
import  javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Player extends Entity implements Updatable {
    private Point2D spawnPosition;
    private Vector2D maxVelocity;
    private Direction shotDirection; // passing this to shot constructor

    private boolean moveR;
    private boolean moveL;
    private boolean moveS;
    private boolean justFell;
    private boolean shotsFired;
    private int numberOfJumps;
    private int currentJump;
    private int numberOfFells = 0;
    private String playerName;
    private String animationResources;
    private PlayerAnimation playerAnimation;

    private KeyCode moveRightKey;
    private KeyCode moveLeftKey;
    private KeyCode moveJumpKey;
    private KeyCode moveShotKey;

    private double yCorOffset;
    private  final double jumpStrength;

    //todo moving animations
    private ArrayList<Texture> moveRightAnimation;
    private ArrayList<Texture> moveLeftAnimation;
    private ArrayList<Texture> moveJumpAnimation;
    private ArrayList<Texture> moveShootAnimation;


    public Player(Texture texture, double gameWidth, double gameHeight, double defaultPosX, double defaultPosY, Direction direction,String playerName, KeyCode jump, KeyCode shoot, KeyCode left, KeyCode right, String animationResources)
    {
        super(texture,new Point2D(defaultPosX,defaultPosY),direction,gameWidth/30,(gameHeight/15));
        this.shotDirection = direction;

        if(direction == Direction.DOWN || direction == Direction.UP || direction == Direction.NONE)
        {
            this.shotDirection = Direction.RIGHT;
        }
        maxVelocity = new Vector2D(new Point2D(gameWidth / 150,gameHeight / 40));
        jumpStrength = gameHeight/ 60;
        moveRightKey = right;
        moveLeftKey = left;
        moveJumpKey = jump;
        moveShotKey = shoot;
        numberOfJumps = 2;
        currentJump = numberOfJumps;
        moveL = moveR = moveS = shotsFired = justFell = false;
        spawnPosition = this.getPosition();
        this.playerName = playerName;
        this.animationResources = "src/resources/PlayerAnimation/"+animationResources+"/";

        this.setVelocity(new Vector2D(new Point2D(0,1))); // direction of the gravity.. straight down (0,1) vector

        this.playerAnimation = new PlayerAnimation(this.animationResources);
    }

    @Override
    public void update(double dt, double gameWidth, double gameHeight, ArrayList<Updatable> objToInteract, ArrayList<Block> gameObj)
    {
        for(Updatable u : objToInteract) //interacting with shots
        {
            if(u instanceof Shot)
            {
                if(isHit((Shot)u) && ((Shot) u).isHit())
                {
                    this.getVelocity().add(((Shot) u).getVelocity());
                    ((Shot) u).setHit(false);
                    this.shotsFired = true;
                }
            }
        }


        //dumping on the X axis

        double dumping = dt /2;
        if(dumping >= 1) {dumping = 0.9;}
        this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX() * dumping,this.getVelocity().getDirection().getY()));

        if(!isOnBlock(this.getVelocity().getDirection().getX(),this.getVelocity().getDirection().getY(),gameObj)) //falling
        {
            justFell = true;

            this.getVelocity().add(gravity); // adding gravity vector to player's velocity vector
            this.getVelocity().multiply(dt); // adjusting gravity in respect to dt
        }
        else //not falling
        {
            if(justFell) // once player hits the block for the first time
            {
                this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX(),0));
            }
            if(shotsFired) // is player is hit
            {
                setPosition(this.getPosition().add(this.getVelocity().getDirection().getX(),0));
                shotsFired = false;
            }
            justFell = false;
            currentJump = 0;
        }
        setPosition(this.getPosition().add(this.getVelocity().getDirection())); //setting player to his new location

        if(!inBounds(gameWidth,gameHeight,0)) // if player falls off
        {
            numberOfFells++;
            setPosition(new Point2D(spawnPosition.getX(),0));
        }

        if(moveL) { moveLeft(dt,gameWidth,gameObj);}
        else if(moveR) {moveRight(dt,gameWidth,gameObj);}
        if(moveS) {moveShot(gameWidth);}

        Texture oldDirection = this.getDefaultTexture();
        checkVelocity(this.maxVelocity);
        Texture newTexture = playerAnimation.getTexture(this.getDirection(), (int) this.getPosition().getX());
        if(oldDirection != newTexture)
            this.setDefaultTexture(newTexture); //setting texture according to direcion and position
    }

    @Override
    public boolean inBounds(double widthLimit,double heightLimit, double stepY) // checking if the player is off the screen
    {
        return ((this.getPosition().getY() + this.getBody().getHeight() + stepY) <= heightLimit); // checking one step ahead (stepx and stepy)
    }


    public KeyCode getMoveRightKey() { return this.moveRightKey; }
    public KeyCode getMoveJumpKey() { return this.moveJumpKey; }
    public KeyCode getMoveLeftKey() { return this.moveLeftKey; }
    public KeyCode getMoveShotKey() { return moveShotKey; }


    public int getScore() { return this.numberOfFells;}

    public String getPlayerName() {
        return playerName;
    }

    public void setMoveRightKey(KeyCode key) { this.moveRightKey = key; }
    public void setMoveLeftKey(KeyCode key) { this.moveLeftKey = key; }
    public void setMoveJumpKey(KeyCode key) { this.moveJumpKey = key; }
    public void setMoveShotKey(KeyCode moveShotKey) { this.moveShotKey = moveShotKey; }

    public void setMoveR(boolean b) { this.moveR = b; }
    public void setMoveL(boolean b) { this.moveL = b; }
    public void setMoveS(boolean b) { this.moveS = b; }


    private boolean isOnBlock(double stepX,double stepY,ArrayList<Block> gameObj)
    {
        for(Block e : gameObj)
        {
            if(this.getPosition().getY() + this.getBody().getHeight() + stepY >= e.getBody().getY() && this.getPosition().getX() + this.getBody().getWidth()  > e.getBody().getX() && this.getPosition().getX()  < e.getBody().getX() + e.getWidth()
                && this.getPosition().getY() + this.getBody().getHeight() <= e.getBody().getY() + 1 && this.getVelocity().getEnd().getY() >= 0)
            {
                yCorOffset = e.getBody().getY() - (this.getPosition().getY() + this.getBody().getHeight());
                return true;
            }
        }
        return false;
    }

    private void moveRight(double speedX,double gameWidth,ArrayList<Block> gameObj) {
        if(this.getPosition().getX() + this.getBody().getWidth() + this.getVelocity().getDirection().getX() <= gameWidth )
        {
            this.getVelocity().add(new Vector2D(new Point2D(speedX,0)));
            this.shotDirection = Direction.RIGHT;
        }
    }

    private void moveLeft(double speedX, double gameWidth,ArrayList<Block> gameObj) {
        if(this.getPosition().getX() - this.getVelocity().getDirection().getX() >= 0 )
        {
            this.getVelocity().add(new Vector2D(new Point2D(-1*speedX,0)));
            this.shotDirection = Direction.LEFT;
        }
    }

    public Shot moveShot(double gameWidth)
    {
        return new Shot(this.getPosition(),this.shotDirection,this.getWidth(),this.getHeight() / 2,this.getWidth(),this.getHeight());
    }

    public void moveJump(double gameHeight)
    {
        if(this.getPosition().getY() >= 0 && currentJump < numberOfJumps)
        {
            this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX(),-1*jumpStrength));
            currentJump ++;
            if(currentJump == numberOfJumps)
            {
                currentJump = numberOfJumps;
            }
        }
    }

    private boolean allowedMoveLeft(ArrayList<Rectangle> gameObj) // prevents player from getting stuck inside the block ;left side
    {
        for(Rectangle r : gameObj)
        {
            if(!(this.getPosition().getY() + this.getBody().getHeight() <= r.getY() || this.getPosition().getY() >= r.getY() + r.getHeight())
                    && this.getPosition().getX()   <= r.getX() + r.getWidth() && this.getPosition().getX() >= r.getX() && this.getVelocity().getEnd().getY() >= 0 )
            {
                return false;
            }
        }
        return true;
    }

    private boolean allowedMoveRight(ArrayList<Rectangle> gameObj) // prevents player from getting stuck inside the block ;right side
    {
        for(Rectangle r : gameObj)
        {
            if(! (this.getPosition().getY() + this.getBody().getHeight() <= r.getY() || this.getPosition().getY() >= r.getY() + r.getHeight())
                    && this.getPosition().getX() + this.getBody().getWidth() >= r.getX() && this.getPosition().getX() <= r.getX() + r.getWidth() && this.getVelocity().getEnd().getY() >= 0)
            {
                return false;
            }
        }
        return true;
    }

    private boolean isHit(Shot shot)
    {
        if(this.getBody().contains(new Point2D(shot.getPosition().getX() + shot.getBody().getWidth() / 2, shot.getPosition().getY())))
        {
            return true;
        }
        return false;
    }

    private void checkVelocity(Vector2D maxVelocity)
    {
        if(this.getVelocity().getDirection().getX() < 0.01 && this.getVelocity().getDirection().getX() > -0.01 ) { this.getVelocity().setEnd(new Point2D(0,this.getVelocity().getEnd().getY()));}

        double maxSpeedX = maxVelocity.getDirection().getX();
        double maxSpeedY = maxVelocity.getDirection().getY();
        if(this.getVelocity().getDirection().getX() > maxSpeedX)
        {
            this.getVelocity().setEnd(new Point2D(maxSpeedX,this.getVelocity().getDirection().getY()));
        }
        if(this.getVelocity().getDirection().getY() > maxSpeedY)
        {
            this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX(),maxSpeedY));
        }
        if(this.getVelocity().getDirection().getX() < -1*maxSpeedX)
        {
            this.getVelocity().setEnd(new Point2D(-1*maxSpeedX,this.getVelocity().getDirection().getY()));
        }
        if(this.getVelocity().getDirection().getY() < -1*maxSpeedY)
        {
            this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX(),-1*maxSpeedY));
        }


        if (this.getVelocity().getDirection().getY() == 0){
            if(this.getVelocity().getDirection().getX() > 0)
                setDirection(Direction.RIGHT);
            else if(this.getVelocity().getDirection().getX() < 0)
                setDirection(Direction.LEFT);
            else
                setDirection(Direction.NONE);
        }
        else if (this.getVelocity().getDirection().getY() > 0){
            if(this.getVelocity().getDirection().getX() > 0)
                setDirection(Direction.DOWNRIGHT);
            else if(this.getVelocity().getDirection().getX() < 0)
                setDirection(Direction.DOWNLEFT);
            else
                setDirection(Direction.DOWN);
        }
        else {
            if (this.getVelocity().getDirection().getX() > 0)
                setDirection(Direction.UPRIGHT);
            else if (this.getVelocity().getDirection().getX() < 0)
                setDirection(Direction.UPLEFT);
            else
                setDirection(Direction.UP);
        }
        //System.out.println(this.getVelocity().getDirection().getX());
    }
    //
}
