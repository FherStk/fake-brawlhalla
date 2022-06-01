package Fakehalla.Game.Entity.Animations;

import Fakehalla.Game.Entity.Direction;
import Fakehalla.Game.Entity.Texture;

public class ShotAnimation { //Same as PlayersSkin but with shot
    private Texture texture;

    public ShotAnimation(String filename){
        this.texture = new Texture(filename);
    }

    public Texture getTexture(Direction direction){
        if(direction == Direction.LEFT) {
            if (!texture.isMirrored())
                texture.mirror();
            return texture;
        }
        else  {
            if (texture.isMirrored())
                texture.mirror();
            return texture;
    }
    }
}
