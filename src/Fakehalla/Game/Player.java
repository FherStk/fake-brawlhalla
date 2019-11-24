package Fakehalla.Game;

import  javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Player extends Entity implements Updatable {
    private Point2D spawnPosition;
    private Vector2D maxVelocity;
    private Direction direction;

    private boolean moveR;
    private boolean moveL;
    private boolean moveS;
    private boolean justFell;
    private boolean shotsFired;
    private int numberOfJumps;
    private int currentJump;
    private int numberOfFells = 0;
    private String playerName;

    private KeyCode moveRightKey;
    private KeyCode moveLeftKey;
    private KeyCode moveJumpKey;
    private KeyCode moveShotKey;

    private double yCorOffset;
    private  final double jumpStrength;


    public Player(Texture texture, double gameWidth, double gameHeight, double defaultPosX, double defaultPosY, Face face,String playerName, KeyCode jump, KeyCode shoot, KeyCode left, KeyCode right)
    {
        super(texture,new Point2D(defaultPosX,defaultPosY),face,gameWidth/40,(gameHeight/20));
        maxVelocity = new Vector2D(new Point2D(gameWidth / 150,gameHeight / 40));
        jumpStrength = gameHeight/ 60;
        moveRightKey = right;
        moveLeftKey = left;
        moveJumpKey = jump;
        moveShotKey = shoot;
        numberOfJumps = 2;
        currentJump = 0;
        moveL = moveR = moveS = shotsFired = justFell = false;
        spawnPosition = this.getPosition();
        this.playerName = playerName;

        this.setVelocity(new Vector2D(new Point2D(0,1))); // direction of the gravity.. straight down (0,1) vector
    }

    /*
    public Player(Paint p,double gameWidth, double gameHeight, double defaultPosX, double defaultPosY, KeyCode r, KeyCode l, KeyCode j,KeyCode s,Face face,String playerName)
    {
        maxVelocity = new Vector2D(new Point2D(gameWidth / 150,gameHeight / 40));
        jumpStrength = gameHeight/ 60;
        playerWidth = gameWidth / 20;
        playerHeight = playerWidth*1.6;
        bodyPosition = new Point2D(defaultPosX, defaultPosY);
        body = new Rectangle();
        body.setWidth(playerWidth);
        body.setHeight(playerHeight);
        body.setFill(p);
        body.setStroke(Color.GREEN);
        body.setX(bodyPosition.getX());
        body.setY(bodyPosition.getX());
        this.playerName = playerName;

        moveRightKey = r;
        moveLeftKey = l;
        moveJumpKey = j;
        moveShotKey = s;
        numberOfJumps = 2;
        currentJump = 0;
        this.face = face;
        spawnPosition = bodyPosition;
        moveL = moveR = moveS = shotsFired = justFell = false;

        velocity = new Vector2D(new Point2D(0,1)); // direction of the gravity.. straight down (0,1) vector
    }*/

    @Override
    public void update(double dt, double gameWidth, double gameHeight,ArrayList<Updatable> objToInteract,ArrayList<Rectangle> gameObj)
    {
        for(Updatable u : objToInteract)
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
        this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX() * dt / 2,this.getVelocity().getDirection().getY()));
        if(!isOnBlock(this.getVelocity().getDirection().getX(),this.getVelocity().getDirection().getY(),gameObj))
        {
            justFell = true;
            setPosition(this.getPosition().add(this.getVelocity().getDirection()));
            this.getVelocity().add(gravity);
            this.getVelocity().multiply(dt);
        }
        else //nepada
        {
            if(justFell) // once player hits the block for the first time
            {
                setPosition(this.getPosition().add(new Point2D(0,yCorOffset - 1)));
                this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX(),0));
            }
            if(shotsFired)
            {
                setPosition(this.getPosition().add(this.getVelocity().getDirection().getX(),0));
                shotsFired = false;
            }
            justFell = false;
            currentJump = 0;
        }


        if(!inBounds(gameWidth,gameHeight,0))
        {
            numberOfFells++;
            setPosition(new Point2D(spawnPosition.getX(),0));
        }

        if(moveL) { moveLeft(dt,gameWidth,gameObj);}
        else if(moveR) {moveRight(dt,gameWidth,gameObj);}
        if(moveS) {moveShot(gameWidth);}

        checkVelocity(this.maxVelocity);
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


    private boolean isOnBlock(double stepX,double stepY,ArrayList<Rectangle> gameObj)
    {
        for(Rectangle e : gameObj)
        {
            if(this.getPosition().getY() + this.getBody().getHeight() + stepY >= e.getY() && this.getPosition().getX() + this.getBody().getWidth()  > e.getX() && this.getPosition().getX()  < e.getX() + e.getWidth()
                && this.getPosition().getY() + this.getBody().getHeight() <= e.getY()  && this.getVelocity().getEnd().getY() >= 0)
            {
                yCorOffset = e.getY() - (this.getPosition().getY() + this.getBody().getHeight());
                return true;
            }
        }
        return false;
    }

    private void moveRight(double speedX,double gameWidth,ArrayList<Rectangle> gameObj) {
        if(this.getPosition().getX() + this.getBody().getWidth() + this.getVelocity().getDirection().getX() <= gameWidth )
        {
            this.getVelocity().add(new Vector2D(new Point2D(speedX,0)));
            this.setFace(Face.RIGHT);
        }
    }

    private void moveLeft(double speedX, double gameWidth,ArrayList<Rectangle> gameObj) {
        if(this.getPosition().getX() - this.getVelocity().getDirection().getX() >= 0 )
        {
            this.getVelocity().add(new Vector2D(new Point2D(-1*speedX,0)));
            this.setFace(Face.LEFT);
        }
    }

    public Shot moveShot(double gameWidth)
    {
        System.out.println("player position: " + this.getPosition());
        return new Shot(this.getPosition(),this.getFace(),this.getWidth() / 2,this.getHeight() / 6,this.getWidth(),this.getHeight());
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


        if(this.getVelocity().getDirection().getX() > 0) {
            direction = Direction.RIGHT;
        }
        else if(this.getVelocity().getDirection().getX() < 0) {
            direction = Direction.LEFT;
        }
        else if(this.getVelocity().getDirection().getY() > 0) {
            direction = Direction.DOWN;
        }
        else if(this.getVelocity().getDirection().getY() < 0) {
            direction = Direction.UP;;
        }
        else {
            direction = Direction.NONE;
        }
    }

}
