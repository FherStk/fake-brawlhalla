package Fakehalla;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player implements Updatable {
    private Rectangle body;
    private Point2D bodyPosition;
    private Vector2D velocity;

    private boolean moveR;
    private boolean moveL;
    private boolean moveJ;

    private KeyCode moveRightKey;
    private KeyCode moveLeftKey;
    private KeyCode moveJumpKey;

    private static final double playerWidth = 50;
    private static final double playerHeight = playerWidth*1.6;
    private static final double speedX = 5;


    public Player(Color p,double gameWidth, double gameHeight, double defaultPosX, double defaultPosY)
    {
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

        moveL = moveR = moveJ = false;

        velocity = new Vector2D(new Point2D(0,1)); // direction of the gravity.. straight down (0,1) vector
    }

    public Player(Color p,double gameWidth, double gameHeight, double defaultPosX, double defaultPosY, KeyCode r, KeyCode l, KeyCode j)
    {
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

        moveL = moveR = moveJ = false;

        velocity = new Vector2D(new Point2D(0,1)); // direction of the gravity.. straight down (0,1) vector
    }

    @Override
    public void update(double gameWidth, double gameHeight) {
        if(!outOfBounds(gameWidth,gameHeight,velocity.getDirection().getX(),velocity.getDirection().getY())){
            setPos(bodyPosition.add(velocity.getDirection())); // moving the player in the direction of vector velocity
            velocity.setEnd(velocity.getDirection().add(new Point2D(0,gravity))); // adding gravity to velocity
        }
        else{
            body.setStroke(Color.RED);
        }

        if(moveL) { moveLeft(gameWidth);}
        else if(moveR) {moveRight(gameWidth);}
        /* debug
        System.out.println(gameWidth + " " + gameHeight);
        System.out.println("actual x and y: " + body.getX() + " " + body.getY());
        System.out.println("bodyposition: " + bodyPosition.getX() + " " + bodyPosition.getY());*/

    }

    @Override
    public boolean outOfBounds(double widthLimit, double heightLimit,double stepX, double stepY) // checking if the player is off the screen
    {
        return !(bodyPosition.getX() - stepX >= 0 && bodyPosition.getY() - stepY >= 0 && (bodyPosition.getX() + body.getWidth() + stepX) <= widthLimit
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
    public void setMoveJ(boolean b) {this.moveJ = b; }

    private void setPos(Point2D point)
    {
        this.bodyPosition = point;
        this.body.setX(bodyPosition.getX());
        this.body.setY(bodyPosition.getY());
    }

    private void moveRight(double gameWidth) {
        if(bodyPosition.getX() + body.getWidth() + speedX <= gameWidth) { setPos(bodyPosition.add(new Point2D(speedX,0))); }
    }

    private void moveLeft(double gameWidth) {
        if(bodyPosition.getX() - speedX >= 0) {  setPos(bodyPosition.subtract(new Point2D(speedX,0)));  }
    }
}
