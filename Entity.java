package Fakehalla;


public interface Entity {
    public final double gravity = 0.2;
    public void setPos(double x, double y);
    public void fall(double width, double height);
}
