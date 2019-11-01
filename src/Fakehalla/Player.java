package Fakehalla;

import javafx.scene.shape.Circle;

public class Player implements Entity {

    private Point2D position;
    private Circle body;

    private double radius;
    private double speedX;
    private double speedY;

    private boolean pressedLeft;
    private boolean pressedRight;
    private boolean pressedJump;

    private int index;



    public Player(int xCor,int yCor,int size)
    {
        position = new Point2D(xCor,yCor);
        speedY = 1;
        speedX = 3;
        index = 0;
        pressedLeft = pressedRight = pressedJump = false;
        radius = size;
        body = new Circle();
        body.setCenterX(position.getX());
        body.setCenterY(position.getY());
        body.setRadius(radius);
    }


    public Circle getBody() {return body;}
    public int getX() { return position.getX();}
    public int getY() { return position.getY();}

    public boolean getPressedLeft() {return pressedLeft;}
    public boolean getPressedRight() {return pressedRight;}
    public boolean getPressedJump() {return pressedJump;}

    public void setPressedLeft(boolean pressed ) { pressedLeft = pressed;}
    public void setPressedRight(boolean pressed ) { pressedRight = pressed;}
    public void setPressedJump(boolean pressed ) { pressedJump = pressed;}
    public void incrementIndex() { index ++;}
    @Override
    public void setPos(double x, double y)
    {
        body.setCenterX(x);
        body.setCenterY(y);
    }

    @Override
    public void fall(double width, double height)
    {
        if(body.getCenterY() + radius + speedY <= height)
        {
            setPos(body.getCenterX(), body.getCenterY() + speedY);
            speedY+= gravity;
        }
        else
        {
            index = 0;
            speedY = 1;
        }
    }

    public void moveRight()
    {
        setPos(body.getCenterX() + speedX, body.getCenterY());
    }

    public void moveLeft()
    {
        setPos(body.getCenterX() - speedX, body.getCenterY());
    }

    public void Jump()
    {
        if (index < 2) { speedY = -5;}
    }

}
