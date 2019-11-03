package Fakehalla;

public interface Updatable { // interface of every object that is going to be moving (move = update + draw)
    public final double gravity = 0.2;
    public void update(double dt,double gameWidth, double gameHeight);
    public boolean inBounds(double widthLimit, double heightLimit,double stepX, double stepY);
}
