package Fakehalla.Game.Entity;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {

    private final double blocksWidth;
    private final double blocksHeight;
    private final String filename = "src/resources/stoneMid.png";

    private double gameWidth;
    private double gameHeight;

    public MapGenerator(double gameWidth, double gameHeight)
    {
        blocksWidth = gameWidth / 3;
        blocksHeight = blocksWidth * 0.1;


        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;
    }

    public ArrayList<Block> generateBlocks(int numberOfBlocks)
    {
        ArrayList<Block> blocks = new ArrayList<>();
        Random r = new Random();
        for(int i = 0; i < numberOfBlocks; i++)
        {
            System.out.println((i+1) + ". block created!");
            double blockHeight = blocksHeight;
            double xcor = gameWidth / 2 - blocksWidth/2;
            double ycor = gameHeight / 2 - blocksHeight;

            Block newBlock = new Block(new Texture(filename),new Point2D(xcor,ycor),blocksWidth,blocksHeight);
            System.out.println(" block position: " + newBlock.getPosition());
            blocks.add(newBlock);
        }
        return blocks;
    }
}
