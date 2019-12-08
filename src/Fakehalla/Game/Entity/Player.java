package Fakehalla.Game.Entity;

import Fakehalla.Game.Entity.Animations.PlayerSkin;
import Fakehalla.Game.Utils.Vector2D;
import Fakehalla.Settings.PlayerSettings;
import Fakehalla.Settings.Settings;
import Fakehalla.Settings.SettingsLoader;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity implements Updatable {
    private Point2D spawnPosition;
    private Vector2D maxVelocity;
    private Direction shotDirection; // passing this to shot constructor
    private Settings settings;
    private boolean moveR;
    private boolean moveL;
    private boolean justFell;
    private int numberOfJumps;
    private int currentJump;
    private int numberOfFells = 0;
    private String playerName;
    private String animationResources;
    private PlayerSkin playerSkin;
    private double shootDelay;

    private long timeStamp;
    private long shootTime;

    private KeyCode moveRightKey;
    private KeyCode moveLeftKey;
    private KeyCode moveJumpKey;
    private KeyCode moveShotKey;

    private  final double jumpStrength;


    public Player(double gameWidth, double gameHeight, double defaultPosX, double defaultPosY, PlayerSettings playerSettings) throws IOException, ClassNotFoundException {
        super(new Texture(),new Point2D(defaultPosX,defaultPosY),Direction.DOWN,gameWidth/30,(gameWidth/30)*1.3);

        this.shootDelay = 500;
        this.shootTime = 0;
        maxVelocity = new Vector2D(new Point2D(gameWidth / 150,gameHeight / 40));
        jumpStrength = gameHeight/ 100;
        moveRightKey = playerSettings.getRight();
        moveLeftKey = playerSettings.getLeft();
        moveJumpKey = playerSettings.getJump();
        moveShotKey = playerSettings.getShoot();
        numberOfJumps = 2;
        currentJump = numberOfJumps;
        moveL = moveR = justFell =  false;
        spawnPosition = this.getPosition();
        this.playerName = playerSettings.getName();
        this.animationResources = "src/resources/PlayerAnimation/"+playerSettings.getSkin()+"/";

        this.setVelocity(new Vector2D(new Point2D(0,1))); // direction of the gravity.. straight down (0,1) vector

        this.playerSkin = new PlayerSkin(this.animationResources);

        settings = new SettingsLoader().loadSettings("settings.txt");
    }

    @Override
    public void update(long currentTime,double dt, double gameWidth, double gameHeight,Vector2D gravity, ArrayList<Updatable> objToInteract, ArrayList<Block> gameObj)
    {
        checkForCollision(objToInteract);

        this.timeStamp = currentTime;

        double dumping = dt /2; //dumping on the X axis
        if(dumping >= 1) {dumping = 0.9;} //checking ig dumping isn't too high
        this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX() * dumping,this.getVelocity().getDirection().getY())); //adding gravity to velocity
        setPosition(this.getPosition().add(this.getVelocity().getDirection())); //setting player to his new location

        if(!isOnBlock(this.getVelocity().getDirection().getX(),this.getVelocity().getDirection().getY(),gravity.getDirection().getY(),gameObj)) //falling
        {
            justFell = true;
            this.getVelocity().add(gravity); // adding gravity vector to player's velocity vector
            this.getVelocity().multiply(dt); // adjusting gravity in respect to dt
        }
        else //not falling
        {
            if(justFell) // once player hits the block for the first time
            {
                this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX(),0)); // setting Y velocity to 0
            }

            this.justFell = false;
            this.currentJump = 0; //resetting jump
        }

        if(!inBounds(gameWidth,gameHeight,0)) // if player falls off
        {
            numberOfFells++; //counting how many times player fell off the map
            this.getVelocity().setEnd(new Point2D(0,0)); //resetting the velocity
            setPosition(new Point2D(spawnPosition.getX(),0));  //re-spawning
        }

        if(moveL) { moveLeft(dt,gameWidth,gameObj);}
        else if(moveR) {moveRight(dt,gameWidth,gameObj);}

        Texture oldDirection = this.getDefaultTexture();
        checkVelocity(this.maxVelocity);
        Texture newTexture = playerSkin.getTexture(this.getDirection(), (int) this.getPosition().getX());
        if(oldDirection != newTexture)
            this.setDefaultTexture(newTexture); //setting texture according to direcion and position
    }

    @Override
    public boolean inBounds(double widthLimit,double heightLimit, double stepY) // checking if the player is off the screen
    {
        return ((this.getPosition().getY() + this.getBody().getHeight() + stepY) <= heightLimit); // checking one step ahead (stepx and stepy)
    }

    public boolean getCanShoot()
    {
        return this.timeStamp - this.shootTime >= shootDelay;
    }

    public KeyCode getMoveRightKey() { return this.moveRightKey; }
    public KeyCode getMoveJumpKey() { return this.moveJumpKey; }
    public KeyCode getMoveLeftKey() { return this.moveLeftKey; }
    public KeyCode getMoveShotKey() { return moveShotKey; }


    public int getScore() { return this.numberOfFells;}

    public String getPlayerName() {
        return playerName;
    }


    public void setMoveRightKey(KeyCode key) { this.moveRightKey = key; }
    public void setMoveLeftKey(KeyCode key) { this.moveLeftKey = key; }
    public void setMoveJumpKey(KeyCode key) { this.moveJumpKey = key; }
    public void setMoveShotKey(KeyCode moveShotKey) { this.moveShotKey = moveShotKey; }

    public void setMoveR(boolean b) { this.moveR = b; }
    public void setMoveL(boolean b) { this.moveL = b; }


    private boolean isOnBlock(double stepX,double stepY,double gravityY,ArrayList<Block> gameObj)
    {
        for(Block e : gameObj)
        {
            if(this.getPosition().getY() + this.getBody().getHeight() + stepY >= e.getBody().getY() && this.getPosition().getX() + this.getBody().getWidth()  > e.getBody().getX() && this.getPosition().getX() + this.getWidth()*0.5  < e.getBody().getX() + e.getWidth()
                && this.getPosition().getY() + this.getBody().getHeight() <= e.getBody().getY() + gravityY && this.getVelocity().getEnd().getY() >= 0)
            {
                return true;
            }
        }
        return false;
    }

    private void moveRight(double speedX,double gameWidth,ArrayList<Block> gameObj) {
        if(this.getPosition().getX() + this.getBody().getWidth() + this.getVelocity().getDirection().getX() <= gameWidth )
        {
            this.getVelocity().add(new Vector2D(new Point2D(speedX,0)));
            this.shotDirection = Direction.RIGHT;
        }
    }

    private void moveLeft(double speedX, double gameWidth,ArrayList<Block> gameObj) {
        if(this.getPosition().getX() - this.getVelocity().getDirection().getX() >= 0 )
        {
            this.getVelocity().add(new Vector2D(new Point2D(-1*speedX,0)));
            this.shotDirection = Direction.LEFT;
        }
    }

    public Shot moveShot(double gameWidth)
    {
        if(settings.isSound())
        {
            playSound("laser.wav", 0.1);
        }
        this.shootTime = this.timeStamp;
        return new Shot(this.getPosition(),this.shotDirection,this.getWidth(),this.getHeight() / 2,(this.getHeight() / 2)*1.61,this.getHeight(), this.animationResources);
    }

    public void moveJump(double gameHeight)
    {
        if(this.getPosition().getY() >= 0 && currentJump < numberOfJumps)
        {
            this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX(),-1*jumpStrength));
            currentJump ++;
            if(currentJump == numberOfJumps)
            {
                currentJump = numberOfJumps;
            }
            if(settings.isSound())
            {
                playSound("jump.wav", 1);
            }
        }
    }

    private boolean isHit(Shot shot)
    {
        if(this.getBody().contains(new Point2D(shot.getPosition().getX() + shot.getBody().getWidth() / 2, shot.getPosition().getY())) ||
            this.getBody().contains(new Point2D(shot.getPosition().getX() + shot.getBody().getWidth() / 2, shot.getPosition().getY() + shot.getHeight())))
        {
            return true;
        }
        return false;
    }

    private void checkVelocity(Vector2D maxVelocity)
    {
        if(this.getVelocity().getDirection().getX() < 0.01 && this.getVelocity().getDirection().getX() > -0.01 ) { this.getVelocity().setEnd(new Point2D(0,this.getVelocity().getEnd().getY()));}

        double maxSpeedX = maxVelocity.getDirection().getX();
        double maxSpeedY = maxVelocity.getDirection().getY();
        if(this.getVelocity().getDirection().getX() > maxSpeedX)
        {
            this.getVelocity().setEnd(new Point2D(maxSpeedX,this.getVelocity().getDirection().getY()));
        }
        if(this.getVelocity().getDirection().getY() > maxSpeedY)
        {
            this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX(),maxSpeedY));
        }
        if(this.getVelocity().getDirection().getX() < -1*maxSpeedX)
        {
            this.getVelocity().setEnd(new Point2D(-1*maxSpeedX,this.getVelocity().getDirection().getY()));
        }
        if(this.getVelocity().getDirection().getY() < -1*maxSpeedY)
        {
            this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX(),-1*maxSpeedY));
        }


        if (this.getVelocity().getDirection().getY() == 0){
            if(this.getVelocity().getDirection().getX() > 0)
                setDirection(Direction.RIGHT);
            else if(this.getVelocity().getDirection().getX() < 0)
                setDirection(Direction.LEFT);
            else
                setDirection(Direction.NONE);
        }
        else if (this.getVelocity().getDirection().getY() > 0){
            if(this.getVelocity().getDirection().getX() > 0)
                setDirection(Direction.DOWNRIGHT);
            else if(this.getVelocity().getDirection().getX() < 0)
                setDirection(Direction.DOWNLEFT);
            else
                setDirection(Direction.DOWN);
        }
        else {
            if (this.getVelocity().getDirection().getX() > 0)
                setDirection(Direction.UPRIGHT);
            else if (this.getVelocity().getDirection().getX() < 0)
                setDirection(Direction.UPLEFT);
            else
                setDirection(Direction.UP);
        }
        //System.out.println(this.getVelocity().getDirection().getX());
    }

    private void checkForCollision(ArrayList<Updatable> objToInteract)
    {
        for(Updatable u : objToInteract) //interacting with shots
        {
            if(u instanceof Shot)
            {
                if(isHit((Shot)u) && ((Shot) u).isHit())
                {
                    this.getVelocity().add(new Vector2D(new Point2D(((Shot) u).getVelocity().getEnd().getX(),0)));
                    ((Shot) u).setHit(false);
                }
            }
        }
    }

    private void playSound(String path, double volume){
        AudioClip au = new AudioClip("file:src/resources/sounds/"+path);
        au.setVolume(volume);
        au.play();
    }
}
