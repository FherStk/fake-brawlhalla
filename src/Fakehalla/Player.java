package Fakehalla;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Player implements Updatable {
    private Rectangle body;
    private Point2D bodyPosition;
    private Vector2D velocity;
    private ArrayList<Rectangle> gameObj;
    private Face face;

    private boolean moveR;
    private boolean moveL;
    private boolean moveS;
    private boolean shotsFired;
    private int numberOfJumps;
    private int currentJump;

    private KeyCode moveRightKey;
    private KeyCode moveLeftKey;
    private KeyCode moveJumpKey;
    private KeyCode moveShotKey;

    private  final double playerWidth;
    private  final double playerHeight;
    private  final double speedX = 5;
    private  final double jumpStrength = 12;


    public Player(Color p, double gameWidth, double gameHeight, double defaultPosX, double defaultPosY, ArrayList<Rectangle> gameObj,Face face)
    {
        this.gameObj = gameObj;
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
        moveRightKey = KeyCode.D;
        moveLeftKey = KeyCode.A;
        moveJumpKey = KeyCode.SPACE;
        moveShotKey = KeyCode.S;
        numberOfJumps = 2;
        currentJump = 0;
        this.face = face;
        moveL = moveR = moveS = shotsFired =  false;

        velocity = new Vector2D(new Point2D(0,1)); // direction of the gravity.. straight down (0,1) vector
    }

    public Player(Color p,double gameWidth, double gameHeight, double defaultPosX, double defaultPosY, KeyCode r, KeyCode l, KeyCode j,KeyCode s, ArrayList<Rectangle> gameObj,Face face)
    {
        this.gameObj = gameObj;
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

        moveRightKey = r;
        moveLeftKey = l;
        moveJumpKey = j;
        moveShotKey = s;
        numberOfJumps = 2;
        currentJump = 0;
        this.face = face;

        moveL = moveR = moveS = shotsFired = false;

        velocity = new Vector2D(new Point2D(0,1)); // direction of the gravity.. straight down (0,1) vector
    }

    @Override
    public void update(double dt, double gameWidth, double gameHeight,ArrayList<Updatable> objToInteract)
    {

        for(Updatable u : objToInteract)
        {
            if(u instanceof Shot)
            {
                if(isHit((Shot)u) && ((Shot) u).isHit())
                {
                    velocity.add(((Shot) u).getVelocity());
                    ((Shot) u).setHit(false);
                    shotsFired = true;
                }
            }
        }

        if(inBounds(gameWidth,gameHeight,velocity.getEnd().getX(),velocity.getEnd().getY()) && !isOnBlock(velocity.getDirection().getX(),velocity.getDirection().getY()))
        {
            body.setStroke(Color.GREEN);
            setPos(bodyPosition.add(velocity.getDirection())); // moving the player in the direction of vector velocity
            velocity.add(gravity); // adding gravity to velocity
            velocity.multiply(dt);
        }
        else
        {
            body.setStroke(Color.BLUE);
            currentJump = 0;
            if(shotsFired)
            {
                setPos(bodyPosition.add(velocity.getDirection().getX(),0));
                shotsFired = false;
            }
        }

        if(moveL) { moveLeft(gameWidth);}
        else if(moveR) {moveRight(gameWidth);}
        if(moveS) {moveShot(gameWidth);}

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

    private boolean isOnBlock(double stepX,double stepY)
    {
        for(Rectangle e : gameObj)
        {
            if(bodyPosition.getY() + body.getHeight() + stepY >= e.getY() && bodyPosition.getX() + body.getWidth()  > e.getX() && bodyPosition.getX()  < e.getX() + e.getWidth()
                && bodyPosition.getY() <= e.getY()  && velocity.getEnd().getY() >= 0)
            {
                return true;
            }
        }
        return false;
    }

    private void moveRight(double gameWidth) {
        if(bodyPosition.getX() + body.getWidth() + speedX <= gameWidth && allowedMoveRight())
        {
            setPos(bodyPosition.add(new Point2D(speedX,0)));
            face = Face.RIGHT;
        }
    }

    private void moveLeft(double gameWidth) {
        if(bodyPosition.getX() - speedX >= 0 && allowedMoveLeft())
        {
            setPos(bodyPosition.subtract(new Point2D(speedX,0)));
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
            velocity.setEnd(new Point2D(0,-1*jumpStrength));
            currentJump ++;
            if(currentJump == numberOfJumps)
            {
                currentJump = numberOfJumps;
            }
        }
    }

    private boolean allowedMoveLeft() // prevents player from getting stuck inside the block ;left side
    {
        for(Rectangle r : gameObj)
        {
            if(! (bodyPosition.getY() + body.getHeight() <= r.getY() || bodyPosition.getY() >= r.getY() + r.getHeight())
                    && bodyPosition.getX()   <= r.getX() + r.getWidth() && bodyPosition.getX() >= r.getX() && velocity.getEnd().getY() >= 0 )
            {
                return false;
            }
        }
        return true;
    }

    private boolean allowedMoveRight() // prevents player from getting stuck inside the block ;right side
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

    private void keepOnTheMap(double gameWidth, double gameHeight)
    {

    }

}
