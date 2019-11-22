package Fakehalla;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {

    private final double blocksWidth;
    private final double blocksHeight;

    private double gameWidth;
    private double gameHeight;

    public MapGenerator(double gameWidth, double gameHeight)
    {
        blocksWidth = gameWidth / 3;
        blocksHeight = gameHeight / 20;


        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;
    }

    public ArrayList<Rectangle> generateBlocks(int numberOfBlocks)
    {
        ArrayList<Rectangle> blocks = new ArrayList<>();
        Random r = new Random();
        for(int i = 0; i < numberOfBlocks; i++)
        {
            System.out.println((i+1) + ". block created!");
            double blockHeight = blocksHeight;
            double xcor = gameWidth / 2 - blocksWidth/2;
            double ycor = gameHeight / 2 - blocksHeight;

            Rectangle newRec = new Rectangle();
            newRec.setX(xcor);
            newRec.setY(ycor);
            newRec.setWidth(blocksWidth);
            newRec.setHeight(blocksHeight);
            newRec.setFill(Color.BLACK);

            blocks.add(newRec);
        }
        return blocks;
    }
}
