package Fakehalla.Game.Entity;

import Fakehalla.Game.Utils.Vector2D;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Event implements Updatable{
    private int duration;
    private int frequency;
    private long start;
    private boolean on = true;
    private int current;
    private final Direction direction = Direction.LEFT;
    private final String resource = "src\\resources\\asteroid.png";
    private double speed;
    private Updatable objToAdd;

    public Event(int min, int max,double playerWidth)
    {
        this.start = 0;
        this.duration = 0;
        this.frequency = 5; // higher the number, lesser the asteroids
        this.speed = (playerWidth - 1);
        this.on = false;
    }

    public Event(double playerWidth)
    {
        this(10000,15000,playerWidth);
    }

    public void start()
    {

        this.start = System.currentTimeMillis();
        this.duration = ThreadLocalRandom.current().nextInt(10000, 15000);
        this.on = true;
        System.out.println(" event started for: " + this.duration+ " [ms] ");

    }

    @Override
    public void update(long currentTime, double dt, double gameWidth, double gameHeight, Vector2D gravity, ArrayList<Updatable> objToInteract, ArrayList<Block> gameObj) {
        if(on)
        {
            this.current = (int)(currentTime - this.start);
            on = this.current < duration;

            if(this.current % frequency == 0)
            {
                int x = (int)gameWidth;
                int y = ThreadLocalRandom.current().nextInt((int)gameHeight/10, (int)(gameHeight - gameHeight/7));
                Point2D randomTempPoint = new javafx.geometry.Point2D(x,y);
                int randomTempSize = ThreadLocalRandom.current().nextInt((int)gameWidth/45,(int)gameWidth/15);
                int randomTilt = ThreadLocalRandom.current().nextInt(10,50);

                Shot temp = new Shot(randomTempPoint,direction,randomTempSize,randomTempSize,this.speed,randomTilt/10,resource,(int)gameWidth/15);
                this.objToAdd = temp;
            }
            else
            {
                this.objToAdd = null;
            }
        }
        else
        {
            this.objToAdd = null;
        }
    }

    @Override
    public boolean inBounds(double widthLimit, double heightLimit, double stepY) {
        return false;
    }

    public Updatable getObjToAdd() { return this.objToAdd; }

    public boolean isOn() {
        return this.on;
    }

    public int getDuration() { return this.duration; }
}
