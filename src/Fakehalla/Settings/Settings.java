package Fakehalla.Settings;

import javafx.scene.input.KeyCode;

import java.io.Serializable;

public class Settings implements Serializable {
    private int width, height;
    private boolean fullscreen;
    private KeyCode player1Jump, player1Shoot, player1Left, player1Right;
    private KeyCode player2Jump, player2Shoot, player2Left, player2Right;

    public void setResolution(int width, int height, boolean fullscreen){
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setPlayer1Jump(KeyCode player1Jump) {
        this.player1Jump = player1Jump;
    }

    public void setPlayer1Shoot(KeyCode player1Shoot) {
        this.player1Shoot = player1Shoot;
    }

    public void setPlayer1Left(KeyCode player1Left) {
        this.player1Left = player1Left;
    }

    public void setPlayer1Right(KeyCode player1Right) {
        this.player1Right = player1Right;
    }

    public void setPlayer2Jump(KeyCode player2Jump) {
        this.player2Jump = player2Jump;
    }

    public void setPlayer2Shoot(KeyCode player2Shoot) {
        this.player2Shoot = player2Shoot;
    }

    public void setPlayer2Left(KeyCode player2Left) {
        this.player2Left = player2Left;
    }

    public void setPlayer2Right(KeyCode player2Right) {
        this.player2Right = player2Right;
    }

    public void setPlayer1Controls(KeyCode up, KeyCode left, KeyCode shoot, KeyCode right)
    {
        this.player1Jump = up;
        this.player1Left = left;
        this.player1Shoot = shoot;
        this.player1Right = right;
    }

    public void setPlayer2Controls(KeyCode up, KeyCode left, KeyCode shoot, KeyCode right)
    {
        this.player2Jump = up;
        this.player2Left = left;
        this.player2Shoot = shoot;
        this.player2Right = right;
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
        return player1Jump;
    }

    public KeyCode getPlayer1Shoot() {
        return player1Shoot;
    }

    public KeyCode getPlayer1Left() {
        return player1Left;
    }

    public KeyCode getPlayer1Right() {
        return player1Right;
    }

    public KeyCode getPlayer2Jump() {
        return player2Jump;
    }

    public KeyCode getPlayer2Shoot() {
        return player2Shoot;
    }

    public KeyCode getPlayer2Left() {
        return player2Left;
    }

    public KeyCode getPlayer2Right() {
        return player2Right;
    }
}
