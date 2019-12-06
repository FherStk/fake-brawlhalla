package Fakehalla.Settings;

import javafx.scene.input.KeyCode;

import java.io.Serializable;

public class PlayerSettings implements Serializable {
    private KeyCode jump, shoot, left, right;
    private String name;
    private String skin;

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getSkin() {
        return skin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PlayerSettings(KeyCode jump, KeyCode shoot, KeyCode left, KeyCode right) {
        this.jump = jump;
        this.shoot = shoot;
        this.left = left;
        this.right = right;
    }

    public PlayerSettings() {
    }

    public void set(KeyCode jump, KeyCode shoot, KeyCode left, KeyCode right) {
        this.jump = jump;
        this.shoot = shoot;
        this.left = left;
        this.right = right;
    }

    public void setJump(KeyCode jump) {
        this.jump = jump;
    }

    public void setShoot(KeyCode shoot) {
        this.shoot = shoot;
    }

    public void setLeft(KeyCode left) {
        this.left = left;
    }

    public void setRight(KeyCode right) {
        this.right = right;
    }

    public KeyCode getJump() {
        return jump;
    }

    public KeyCode getShoot() {
        return shoot;
    }

    public KeyCode getLeft() {
        return left;
    }

    public KeyCode getRight() {
        return right;
    }
}
