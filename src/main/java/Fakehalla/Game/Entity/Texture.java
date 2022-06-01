package Fakehalla.Game.Entity;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;


public class Texture {
    private ImageView texture;
    private String filename;
    boolean mirrored = false; //saving if image in memory is mirrored or not.

    public Texture(String filename)
    {
        this.filename = filename;
        texture = new ImageView(new Image("file:"+filename)); //didnt work without "file:" before the filename
    }

    public Texture()
    {
        texture = new ImageView(new Image("file:" + null));
    }

    public ImagePattern getTexture()
    {
        return new ImagePattern(this.texture.getImage());
    }

    public void setTexture(String filename)
    {
        this.filename = filename;
        this.texture = new ImageView(new Image("file:" + this.filename));
    }

    public void mirror()
    {
        mirrored = !mirrored;
        //this.texture.setScaleY(-1);
        //System.out.println("Mirrored");
    }
    public boolean isMirrored(){
        return mirrored;

    }
}
