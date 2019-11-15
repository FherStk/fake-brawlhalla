package Fakehalla.Settings;

import java.io.Serializable;

public class Settings implements Serializable {
    private int width, height;
    private boolean fullscreen;
    private char player1Up, player1Down, player1Left, player1Right;
    private char player2Up, player2Down, player2Left, player2Right;

    public void setResolution(int width, int height, boolean fullscreen){
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;
    }

    public void setPlayer1Controls(char up, char left, char down, char right)
    {
        this.player1Up = up;
        this.player1Left = left;
        this.player1Down = down;
        this.player1Right = right;
    }

    public void setPlayer2Controls(char up, char left, char down, char right)
    {
        this.player2Up = up;
        this.player2Left = left;
        this.player2Down = down;
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

    public char getPlayer1Up() {
        return player1Up;
    }

    public char getPlayer1Down() {
        return player1Down;
    }

    public char getPlayer1Left() {
        return player1Left;
    }

    public char getPlayer1Right() {
        return player1Right;
    }

    public char getPlayer2Up() {
        return player2Up;
    }

    public char getPlayer2Down() {
        return player2Down;
    }

    public char getPlayer2Left() {
        return player2Left;
    }

    public char getPlayer2Right() {
        return player2Right;
    }
}
