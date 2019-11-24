package Fakehalla.Game;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;


public class Texture {
    //todo
    private Image texture;

    public Texture(String filename)
    {
        texture = new Image("file:textures/donald.png");
    }

    public ImagePattern getTexture() {
        System.out.println(texture.getProgress());
        return new ImagePattern(this.texture);}
}
