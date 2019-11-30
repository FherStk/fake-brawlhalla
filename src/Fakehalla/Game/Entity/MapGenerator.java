package Fakehalla.Game.Entity;

import javafx.geometry.Point2D;

import java.util.ArrayList;

public class MapGenerator {

    private  double blocksWidth;
    private  double blocksHeight;
    private final String filename = "src/resources/stoneMid.png";

    public MapGenerator() {}

    public ArrayList<Block> generateBlocks(double gameWidth,double gameHeight)
    {
        blocksWidth = gameWidth / 3;
        blocksHeight = blocksWidth * 0.1;
        ArrayList<Block> blocks = new ArrayList<>();

        double xcor = gameWidth / 2 - blocksWidth/2;
        double ycor = gameHeight / 2 + blocksHeight;

        Block newBlockCenter = new Block(new Texture(filename),new Point2D(xcor,ycor),blocksWidth,blocksHeight);
        Block newBlockLeft = new Block(new Texture(filename),new Point2D(xcor - blocksWidth*0.75,ycor - blocksHeight*2),blocksWidth*0.5,blocksHeight);
        Block newBlockRight = new Block(new Texture(filename),new Point2D(xcor + blocksWidth*1.25,ycor - blocksHeight*2),blocksWidth*0.5,blocksHeight);

        blocks.add(newBlockCenter);
        blocks.add(newBlockLeft);
        blocks.add(newBlockRight);

        return blocks;
    }
}
