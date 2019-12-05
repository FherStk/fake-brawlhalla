package Fakehalla.Game.Entity.Animations;

import Fakehalla.Game.Entity.Direction;
import Fakehalla.Game.Entity.Texture;

public class PlayerSkin {
    private Texture[] walk = new Texture[11];
    private Texture stand; //front.png
    private Texture fall; //hurt.png
    private Texture jump; //jump.png
    private Texture oldTexture;
    private int index = 0;


    public PlayerSkin(String animationResources){
        for (int i = 0; i < 11; i++) {
            this.walk[i] = new Texture(animationResources+"walk/walk"+Integer.toString(i+1)+".png");
        }
        this.stand = new Texture(animationResources+"front.png");
        this.fall = new Texture(animationResources+"hurt.png");
        this.jump = new Texture(animationResources+"jump.png");
        System.out.println(animationResources);
    }
    public Texture getTexture(Direction direction, int index){ //return texture according to direction
        if(direction == Direction.NONE)
            return oldTexture = stand;
        else if(direction == Direction.UPRIGHT) {
            if (jump.isMirrored()) //all animations facing left must be mirrored
                jump.mirror();
            return oldTexture = jump;
        }
        else if(direction == Direction.UPLEFT) {
            if (!jump.isMirrored())
                jump.mirror();
            return oldTexture = jump;
        }
        else if(direction == Direction.DOWNRIGHT) {
            if (fall.isMirrored())
                fall.mirror();
            return oldTexture = fall;
        }
        else if(direction == Direction.DOWNLEFT) {
            if (!fall.isMirrored())
                fall.mirror();
            return oldTexture = fall;
        }
        else {
            if(index>100000) index=0;
            if(direction == Direction.RIGHT) {
                if(walk[(index/7)%11].isMirrored())
                    walk[(index/7)%11].mirror();
                return oldTexture = walk[(index/7)%11];
            }
            else{
                if(!walk[(index/7)%11].isMirrored())
                    walk[(index/7)%11].mirror();
                return oldTexture = walk[(index/7)%11];
            }

        }
    }
}
