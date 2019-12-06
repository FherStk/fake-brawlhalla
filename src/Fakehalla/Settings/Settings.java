package Fakehalla.Settings;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;

import java.io.Serializable;

public class Settings implements Serializable {
    private int width, height;
    private boolean fullscreen, sound;
    private PlayerSettings player1;
    private PlayerSettings player2;

    public PlayerSettings getPlayer1() {
        return player1;
    }

    public PlayerSettings getPlayer2() {
        return player2;
    }

    public Settings() {
        this.player1 = new PlayerSettings();
        this.player2 = new PlayerSettings();
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean isSound() {
        return sound;
    }

    public void setResolution(int width, int height, boolean fullscreen){
        if(fullscreen){
            Rectangle2D screenBounds = Screen.getPrimary().getBounds();
            this.width = (int) screenBounds.getMaxX();
            this.height = (int) screenBounds.getMaxY();
        }
        else {
            this.width = width;
            this.height = height;
        }
        this.fullscreen = fullscreen;
    }

    public void setPlayer1Jump(KeyCode player1Jump) {
        this.player1.setJump(player1Jump);
    }

    public void setPlayer1Shoot(KeyCode player1Shoot) {
        this.player1.setShoot(player1Shoot);
    }

    public void setPlayer1Left(KeyCode player1Left) {
        this.player1.setLeft(player1Left);
    }

    public void setPlayer1Right(KeyCode player1Right) {
        this.player1.setRight(player1Right);
    }

    public void setPlayer2Jump(KeyCode player2Jump) {
        this.player2.setJump(player2Jump);
    }

    public void setPlayer2Shoot(KeyCode player2Shoot) {
        this.player2.setShoot(player2Shoot);
    }

    public void setPlayer2Left(KeyCode player2Left) {
        this.player2.setLeft(player2Left);
    }

    public void setPlayer2Right(KeyCode player2Right) {
        this.player2.setRight(player2Right);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public KeyCode getPlayer1Jump() {
        return player1.getJump();
    }

    public KeyCode getPlayer1Shoot() {
        return player1.getShoot();
    }

    public KeyCode getPlayer1Left() {
        return player1.getLeft();
    }

    public KeyCode getPlayer1Right() {
        return player1.getRight();
    }

    public KeyCode getPlayer2Jump() {
        return player2.getJump();
    }

    public KeyCode getPlayer2Shoot() {
        return player2.getShoot();
    }

    public KeyCode getPlayer2Left() {  return player2.getLeft();  }

    public KeyCode getPlayer2Right() {
        return player2.getRight();
    }

    public void setPlayer1Controls(KeyCode jump, KeyCode left, KeyCode right, KeyCode shoot) {
        this.player1.set(jump, shoot, left, right);
    }

    public void setPlayer2Controls(KeyCode jump, KeyCode left, KeyCode right, KeyCode shoot) {
        this.player2.set(jump, shoot, left, right);
    }
}
