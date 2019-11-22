package Fakehalla;

import  javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Player implements Updatable {
    private Rectangle body;
    private Point2D bodyPosition;
    private Point2D spawnPosition;
    private Vector2D velocity;
    private Vector2D maxVelocity;
    private Face face;

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

    private  final double playerWidth;
    private  final double playerHeight;
    private  final double jumpStrength;


    public Player(Paint p, double gameWidth, double gameHeight, double defaultPosX, double defaultPosY, Face face,String playerName)
    {
        maxVelocity = new Vector2D(new Point2D(gameWidth / 150,gameHeight / 80));
        jumpStrength = gameHeight/ 60;
        playerWidth = gameWidth / 40;
        playerHeight = playerWidth*1.6;
        bodyPosition = new Point2D(defaultPosX, defaultPosY);
        body = new Rectangle();
        body.setWidth(playerWidth);
        body.setHeight(playerHeight);
        body.setFill(p);
        body.setStroke(Color.GREEN);
        body.setX(bodyPosition.getX());
        body.setY(bodyPosition.getX());
        moveRightKey = KeyCode.D;
        moveLeftKey = KeyCode.A;
        moveJumpKey = KeyCode.SPACE;
        moveShotKey = KeyCode.S;
        numberOfJumps = 2;
        currentJump = 0;
        this.face = face;
        moveL = moveR = moveS = shotsFired = justFell = false;
        spawnPosition = bodyPosition;
        this.playerName = playerName;

        velocity = new Vector2D(new Point2D(0,1)); // direction of the gravity.. straight down (0,1) vector
    }

    public Player(Paint p,double gameWidth, double gameHeight, double defaultPosX, double defaultPosY, KeyCode r, KeyCode l, KeyCode j,KeyCode s,Face face,String playerName)
    {
        maxVelocity = new Vector2D(new Point2D(gameWidth / 150,gameHeight / 80));
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
    }

    @Override
    public void update(double dt, double gameWidth, double gameHeight,ArrayList<Updatable> objToInteract,ArrayList<Rectangle> gameObj)
    {

        for(Updatable u : objToInteract)
        {
            if(u instanceof Shot)
            {
                if(isHit((Shot)u) && ((Shot) u).isHit())
                {
                    this.velocity.add(((Shot) u).getVelocity());
                    ((Shot) u).setHit(false);
                    this.shotsFired = true;
                }
            }
        }
        velocity.setEnd(new Point2D(velocity.getDirection().getX() * dt / 2,velocity.getDirection().getY()));
        if(!isOnBlock(velocity.getDirection().getX(),velocity.getDirection().getY(),gameObj))
        {
            setPos(bodyPosition.add(velocity.getDirection())); // moving the player in the direction of vector velocity
            velocity.add(gravity); // adding gravity to velocity
            velocity.multiply(dt);
            justFell = true;
        }
        else //nepada
        {
            if(justFell) // once player hits the block for the first time
            {
                setPos(bodyPosition.add(new Point2D(0,yCorOffset - 1))); // adjusting player's position to be aligned with the block
                velocity.setEnd(new Point2D(velocity.getDirection().getX(),0));// resetting Yvelocity
            }
            if(shotsFired)
            {
                setPos(bodyPosition.add(velocity.getDirection().getX(),0));
                shotsFired = false;
            }
            justFell = false;
            currentJump = 0;
        }


        if(!inBounds(gameWidth,gameHeight,0,0))
        {
            numberOfFells++;
            setPos(new Point2D(spawnPosition.getX(),0));
        }

        if(moveL) { moveLeft(dt,gameWidth,gameObj);}
        else if(moveR) {moveRight(dt,gameWidth,gameObj);}
        if(moveS) {moveShot(gameWidth);}

        checkVelocity(this.maxVelocity);
    }

    @Override
    public boolean inBounds(double widthLimit, double heightLimit,double stepX, double stepY) // checking if the player is off the screen
    {
        return ((bodyPosition.getY() + body.getHeight() + stepY) <= heightLimit); // checking one step ahead (stepx and stepy)
    }

    public Rectangle getBody() { return this.body;}

    public KeyCode getMoveRightKey() { return this.moveRightKey; }
    public KeyCode getMoveJumpKey() { return this.moveJumpKey; }
    public KeyCode getMoveLeftKey() { return this.moveLeftKey; }
    public KeyCode getMoveShotKey() { return moveShotKey; }

    public Face getFace() { return this.face; }

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

    private void setPos(Point2D point)
    {
        this.bodyPosition = point;
        this.body.setX(bodyPosition.getX());
        this.body.setY(bodyPosition.getY());
    }

    private boolean isOnBlock(double stepX,double stepY,ArrayList<Rectangle> gameObj)
    {
        for(Rectangle e : gameObj)
        {
            if(bodyPosition.getY() + body.getHeight() + stepY >= e.getY() && bodyPosition.getX() + body.getWidth()  > e.getX() && bodyPosition.getX()  < e.getX() + e.getWidth()
                && bodyPosition.getY() + body.getHeight() <= e.getY()  && velocity.getEnd().getY() >= 0)
            {
                yCorOffset = e.getY() - (bodyPosition.getY() + body.getHeight());
                return true;
            }
        }
        return false;
    }

    private void moveRight(double speedX,double gameWidth,ArrayList<Rectangle> gameObj) {
        if(bodyPosition.getX() + body.getWidth() + velocity.getDirection().getX() <= gameWidth )
        {
            velocity.add(new Vector2D(new Point2D(speedX,0)));
            face = Face.RIGHT;
        }
    }

    private void moveLeft(double speedX, double gameWidth,ArrayList<Rectangle> gameObj) {
        if(bodyPosition.getX() - velocity.getDirection().getX() >= 0 )
        {
            velocity.add(new Vector2D(new Point2D(-1*speedX,0)));
            face = Face.LEFT;
        }
    }

    public Shot moveShot(double gameWidth)
    {
        return new Shot(this.body.getX(), this.body.getY() + body.getHeight() / 2,body.getWidth(), this.getFace());
    }

    public void moveJump(double gameHeight)
    {
        if(bodyPosition.getY() >= 0 && currentJump < numberOfJumps)
        {
            velocity.setEnd(new Point2D(velocity.getDirection().getX(),-1*jumpStrength));
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
            if(!(bodyPosition.getY() + body.getHeight() <= r.getY() || bodyPosition.getY() >= r.getY() + r.getHeight())
                    && bodyPosition.getX()   <= r.getX() + r.getWidth() && bodyPosition.getX() >= r.getX() && velocity.getEnd().getY() >= 0 )
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
            if(! (bodyPosition.getY() + body.getHeight() <= r.getY() || bodyPosition.getY() >= r.getY() + r.getHeight())
                    && bodyPosition.getX() + body.getWidth() >= r.getX() && bodyPosition.getX() <= r.getX() + r.getWidth() && velocity.getEnd().getY() >= 0)
            {
                return false;
            }
        }
        return true;
    }

    private boolean isHit(Shot shot)
    {
        if(body.contains(new Point2D(shot.getPos().getX() + shot.getBody().getWidth() / 2, shot.getPos().getY())))
        {
            return true;
        }
        return false;
    }

    private void checkVelocity(Vector2D maxVelocity)
    {
        double maxSpeedX = maxVelocity.getDirection().getX();
        double maxSpeedY = maxVelocity.getDirection().getY();
        if(velocity.getDirection().getX() > maxSpeedX)
        {
            velocity.setEnd(new Point2D(maxSpeedX,velocity.getDirection().getY()));
        }
        if(velocity.getDirection().getY() > maxSpeedY)
        {
            velocity.setEnd(new Point2D(velocity.getDirection().getX(),maxSpeedY));
        }
        if(velocity.getDirection().getX() < -1*maxSpeedX)
        {
            velocity.setEnd(new Point2D(-1*maxSpeedX,velocity.getDirection().getY()));
        }
        if(velocity.getDirection().getY() < -1*maxSpeedY)
        {
            velocity.setEnd(new Point2D(velocity.getDirection().getX(),-1*maxSpeedY));
        }
    }

}
