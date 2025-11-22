package com.brickbreaker;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {

    public static Clip loadClip(String fileName) {
        try {
            URL url = SoundManager.class.getResource("/sounds/" + fileName);
            if (url == null) {
                System.out.println("Sound not found: " + fileName);
                return null;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void play(Clip clip) {
        if (clip == null) return;
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
}
