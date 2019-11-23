package Fakehalla.Game;

import javafx.geometry.Point2D;

import static java.lang.Math.sqrt;

public class Vector2D {
    Point2D start;
    Point2D end;

    private double size;

    public Vector2D(Point2D start, Point2D end)
    {
        this.start = start;
        this.end = end;
    }

    public Vector2D(Point2D normalPoint)
    {
        start  = new Point2D(0,0);
        end = normalPoint;
    }

    @Override
    public String toString()
    {
        return "X: " + this.start + " Y: " + this.end;
    }

    //returning the size of the vector
    public double getSize() { return sqrt((end.getX() - start.getX()) * (end.getX() - start.getX()) + (end.getY() - start.getY())*end.getY() - start.getY());}

    public Point2D getStart() {return this.start;}

    public Point2D getEnd() {return this.end;}

    // returning vector in the default position (first point is (0,0))
    public Point2D getDirection() { return new Point2D(end.getX() - start.getX(),end.getY() - start.getY());}

    public void setStart(Point2D p) {this.start = p;}

    public void setEnd(Point2D p) {this.end = p;}

    public void add(Vector2D vec)
    {
        setStart(this.start.add(vec.getStart()));
        setEnd(this.end.add(vec.getEnd()));
    }

    public void multiply(double m)
    {
        this.start.multiply(m);
        this.end.multiply(m);
    }


    /* dev func ... delete later*/
    public void print()
    {
        System.out.println("X: " + (end.getX() - start.getX()));
        System.out.println("Y: " + (end.getY() - start.getY()));
    }
}
