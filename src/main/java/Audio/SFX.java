package Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SFX {
    private Clip clip;

    public SFX (String s) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(s));
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
            clip = AudioSystem.getClip();
            clip.open(dais);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip == null)
            return;
        stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public void stop() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    public void loop() {
        if (clip == null)
            return;
        stop();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void close() {
        stop();
        clip.close();
    }
}
