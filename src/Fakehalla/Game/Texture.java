package Fakehalla.Game;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;


public class Texture {
    private Image texture;
    private String filename;

    public Texture(String filename)
    {
        this.filename = filename;
        texture = new Image("file:"+filename); //didnt work without "file:" before the filename
    }

    public Texture()
    {
        texture = new Image("file:" + null);
    }

    public ImagePattern getTexture()
    {
        return new ImagePattern(this.texture);
    }

    public void setTexture(String filename)
    {
        this.filename = filename;
        this.texture = new Image("file:" + this.filename);
    }
}
