package Fakehalla.Game.Entity;

import Fakehalla.Game.Entity.Animations.PlayerSkin;
import Fakehalla.Game.Utils.Vector2D;
import Fakehalla.Settings.PlayerSettings;
import Fakehalla.Settings.Settings;
import Fakehalla.Settings.SettingsLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

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

    private Label playerLabel;

    private final double jumpStrength;
    private final int playerNameLimit = 8;


    public Player(double gameWidth, double gameHeight, double defaultPosX, double defaultPosY,
                  PlayerSettings playerSettings, Group group) throws IOException, ClassNotFoundException {
        super(new Texture(),new Point2D(defaultPosX,defaultPosY),Direction.DOWN,gameWidth/30,(gameWidth/30)*1.3);

        this.shootDelay = 200;
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
        checkName();
        initLabel();
        this.playerLabel.setFont(Font.font("Verdana",gameWidth*0.007));
        group.getChildren().add(this.playerLabel);

    }

    @Override
    public void update(long currentTime,double dt, double gameWidth, double gameHeight,Vector2D gravity, ArrayList<Updatable> objToInteract, LinkedList<Block> gameObj)
    {
        checkForCollision(objToInteract);

        this.timeStamp = currentTime;

        double dumping = dt /2; //dumping on the X axis
        if(dumping >= 1) {dumping = 0.9;} //checking ig dumping isn't too high
        this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX() * dumping,this.getVelocity().getDirection().getY())); //adding gravity to velocity
        setPosition(this.getPosition().add(this.getVelocity().getDirection())); //setting player to his new location

        if(!isOnBlock(this.getVelocity().getDirection().getY(),gravity.getDirection().getY(),gameObj)) //falling
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

        if(moveL) { moveLeft(dt);}
        else if(moveR) {moveRight(dt);}

        Texture oldDirection = this.getDefaultTexture();
        checkVelocity(this.maxVelocity);
        Texture newTexture = playerSkin.getTexture(this.getDirection(), (int) this.getPosition().getX());
        if(oldDirection != newTexture)
            this.setDefaultTexture(newTexture); //setting texture according to direcion and position

        this.playerLabel.setTranslateX(this.getPosition().getX());
        this.playerLabel.setTranslateY(this.getPosition().getY() - this.playerLabel.getHeight());
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

    public void setMoveR(boolean b) { this.moveR = b; }
    public void setMoveL(boolean b) { this.moveL = b; }


    private boolean isOnBlock(double stepY, double gravityY, LinkedList<Block> gameObj)
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

    private void moveRight(double speedX)
    {
        this.getVelocity().add(new Vector2D(new Point2D(speedX,0)));
        this.shotDirection = Direction.RIGHT;

    }

    private void moveLeft(double speedX)
    {
        this.getVelocity().add(new Vector2D(new Point2D(-1*speedX,0)));
        this.shotDirection = Direction.LEFT;
    }

    public Shot moveShot()
    {
        if(settings.isSound())
        {
            playSound("laser.wav", 0.1);
        }
        this.shootTime = this.timeStamp;
        return new Shot(this.getPosition(),this.shotDirection,this.getWidth(),this.getHeight() / 2,(this.getHeight() / 2)*1.61,this.getHeight(), this.animationResources);
    }

    public void moveJump()
    {
        if(currentJump < numberOfJumps)
        {
            this.getVelocity().setEnd(new Point2D(this.getVelocity().getDirection().getX(),-1*jumpStrength));
            currentJump ++;
            if(settings.isSound())
            {
                playSound("jump.wav", 1);
            }
        }
    }

    private boolean isHit(Shot shot)
    {
        return this.getPosition().getX() < shot.getPosition().getX() + shot.getWidth()
                && this.getPosition().getX() + this.getWidth() > shot.getPosition().getX()
                && this.getPosition().getY() < shot.getPosition().getY() + shot.getHeight()
                && this.getPosition().getY() + this.getHeight() > shot.getPosition().getY();
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

    private void initLabel()
    {
        this.playerLabel = new Label(this.playerName);
        this.playerLabel.setTextFill(new Color(1,1,1,0.5));
        this.playerLabel.setTranslateX(this.getPosition().getX());
        this.playerLabel.setTranslateY(this.getPosition().getY() - this.playerLabel.getHeight());
        this.playerLabel.setTextFill(Color.BLACK);
    }
    private void checkName()
    {
        this.playerName = this.playerName.replaceAll("\n","");
        if(this.playerName.length() > playerNameLimit)
        {
            this.playerName = this.playerName.substring(0,playerNameLimit-1);
        }
        else{
            int offset = playerNameLimit - this.playerName.length();
            StringBuilder sb = new StringBuilder();
            sb.append(this.playerName);
            for(int i = 0; i <= offset/2 + 1; i++)
            {
                sb.insert(i," ");
            }
            this.playerName = sb.toString();
        }
    }

}
