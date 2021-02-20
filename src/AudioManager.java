import map.MapManager;

import javax.sound.sampled.*;
import java.io.File;

public class AudioManager {

    private static final AudioManager instance = new AudioManager();

    public static AudioManager getInstance(){
        return instance;
    }

    private boolean isMute = false;

    //reference: https://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
    public synchronized Clip playSound(final String url) {
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(url));
            clip.open(inputStream);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return clip;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }
}
