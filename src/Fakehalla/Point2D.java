package Fakehalla;

public class Point2D {
    private int xCor;
    private int yCor;

    public Point2D(int x,int y)
    {
        xCor = x; yCor = y;
    }

    public Point2D getPoint() { return this;}
    public int getX() {return xCor;}
    public int getY() {return yCor;}
    public void setPos(int x, int y) { xCor = x; yCor = y;}
    public void setX(int x) { xCor = x; }
    public void setY(int y) {yCor = y;}

}
