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

    private boolean moveR;
    private boolean moveL;
    private int numberOfJumps;
    private int currentJump;

    private KeyCode moveRightKey;
    private KeyCode moveLeftKey;
    private KeyCode moveJumpKey;

    private  final double playerWidth;
    private  final double playerHeight;
    private  final double speedX = 5;
    private  final double jumpStrength = 10;


    public Player(Color p, double gameWidth, double gameHeight, double defaultPosX, double defaultPosY, ArrayList<Rectangle> gameObj)
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
        numberOfJumps = 2;
        currentJump = 0;

        moveL = moveR = false;

        velocity = new Vector2D(new Point2D(0,1)); // direction of the gravity.. straight down (0,1) vector
    }

    public Player(Color p,double gameWidth, double gameHeight, double defaultPosX, double defaultPosY, KeyCode r, KeyCode l, KeyCode j, ArrayList<Rectangle> gameObj)
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
        numberOfJumps = 2;
        currentJump = 0;

        moveL = moveR  = false;

        velocity = new Vector2D(new Point2D(0,1)); // direction of the gravity.. straight down (0,1) vector
    }

    @Override
    public void update(double dt,double gameWidth, double gameHeight)
    {
        if(inBounds(gameWidth,gameHeight,velocity.getEnd().getX(),velocity.getEnd().getY()) && !isOnBlock(velocity.getDirection().getX(),velocity.getDirection().getY()))
        {
            body.setStroke(Color.GREEN);
            setPos(bodyPosition.add(velocity.getDirection())); // moving the player in the direction of vector velocity
            velocity.setEnd(velocity.getDirection().add(new Point2D(0,gravity))); // adding gravity to velocity
        }
        else
        {
            body.setStroke(Color.BLUE);
            currentJump = 0;
            velocity.setEnd(new Point2D(0,1));
        }


        if(moveL) { moveLeft(gameWidth);}
        else if(moveR) {moveRight(gameWidth);}

        /*
                        debug
        System.out.println(gameWidth + " " + gameHeight);
        System.out.println("actual x and y: " + body.getX() + " " + body.getY());
        System.out.println("bodyposition: " + bodyPosition.getX() + " " + bodyPosition.getY());
        */

    }

    @Override
    public boolean inBounds(double widthLimit, double heightLimit,double stepX, double stepY) // checking if the player is off the screen
    {
        return (bodyPosition.getX() - stepX >= 0  && (bodyPosition.getX() + body.getWidth() + stepX) <= widthLimit
                && (bodyPosition.getY() + body.getHeight() + stepY) <= heightLimit); // checking one step ahead (stepx and stepy)
    }

    public Rectangle getBody() { return this.body;}

    public KeyCode getMoveRightKey() { return this.moveRightKey; }
    public KeyCode getMoveJumpKey() { return moveJumpKey; }
    public KeyCode getMoveLeftKey() { return moveLeftKey; }

    public void setMoveRightKey(KeyCode key) {moveRightKey = key; }
    public void setMoveLeftKey(KeyCode key) {moveLeftKey = key; }
    public void setMoveJumpKey(KeyCode key) {moveJumpKey = key; }

    public void setMoveR(boolean b) { this.moveR = b; }
    public void setMoveL(boolean b) { this.moveL = b; }

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
            // todo
            if(bodyPosition.getY() + body.getHeight() + stepY >= e.getY() && bodyPosition.getX() + body.getWidth()  > e.getX() && bodyPosition.getX()  < e.getX() + e.getWidth()
                && bodyPosition.getY() <= e.getY() + e.getHeight())
            {
                return true;
            }
        }
        return false;
    }

    private void moveRight(double gameWidth) {
        if(bodyPosition.getX() + body.getWidth() + speedX <= gameWidth && allowedMoveRight()) { setPos(bodyPosition.add(new Point2D(speedX,0))); }
    }

    private void moveLeft(double gameWidth) {
        if(bodyPosition.getX() - speedX >= 0 && allowedMoveLeft()) {  setPos(bodyPosition.subtract(new Point2D(speedX,0)));  }
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
                    && bodyPosition.getX()   <= r.getX() + r.getWidth() && bodyPosition.getX() >= r.getX() )
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
                    && bodyPosition.getX() + body.getWidth() >= r.getX() && bodyPosition.getX() <= r.getX() + r.getWidth())
            {
                return false;
            }
        }
        return true;
    }



}
