package Fakehalla.Game.Entity.Animations;


import javafx.scene.media.AudioClip;

public class Sound {
    private AudioClip background;
    private AudioClip dnb;
    private boolean soundIsOn, eventIsOn = false;

    public Sound(boolean soundIsOn){
        background = new AudioClip("file:src/resources/sounds/sound.mp3");
        dnb = new AudioClip("file:src/resources/sounds/dnb.m4a");
        this.soundIsOn = soundIsOn;
        if (soundIsOn)
            background.play();
    }

    public void activatedEvent(boolean eventIsOn){
        if (!this.soundIsOn)
            return;

        if(!this.eventIsOn && eventIsOn){
            this.eventIsOn = true;
            background.stop();
            dnb.play();
            return;
        }
        if(this.eventIsOn && !eventIsOn){
            this.eventIsOn = false;
            dnb.stop();
            background.play();
        }


    }
    public void stop(){
        if (!this.soundIsOn)
            return;
        background.stop();
        dnb.stop();
    }
}
