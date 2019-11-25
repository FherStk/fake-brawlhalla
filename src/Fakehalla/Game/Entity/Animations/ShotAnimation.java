package Fakehalla.Game.Entity.Animations;

import Fakehalla.Game.Entity.Direction;
import Fakehalla.Game.Entity.Texture;

public class ShotAnimation {
    private Texture right;

    public ShotAnimation(String filename){
        this.right = new Texture(filename);
    }

    public Texture getTexture(Direction direction){
        if(direction == Direction.LEFT) {
            if (!right.isMirrored())
                right.mirror();
            return right;
        }
        else  {
            if (right.isMirrored())
                right.mirror();
            return right;
    }
    }
}
