package Audio;

import javax.sound.sampled.*;
import java.io.*;

public class SFX {
    private Clip clip;

    public SFX (String s) {
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(s));
//            AudioInputStream ais = AudioSystem.getAudioInputStream(inputStream);
            AudioInputStream ais = AudioSystem.getAudioInputStream(inputStream);
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, -1.0F, 64, 2, 4, -1.0F, true);
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
