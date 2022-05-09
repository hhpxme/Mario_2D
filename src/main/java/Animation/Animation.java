package Animation;

import java.awt.image.BufferedImage;

public class Animation {
	private BufferedImage[] frames;
	private int currentFrame;
	
	private long startTime;
	private long delay;
	
	private boolean play;

	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public Animation() {
		play = false;
	}
	
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		play = false;
	}

	public void update() {
		if (delay == -1)
			return;
		
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if (elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if (currentFrame == frames.length) {
			currentFrame = 0;
			play = true;
		}
	}

	public BufferedImage getImage() { return frames[currentFrame]; }
	public boolean isPlay() { return play; }
}
















