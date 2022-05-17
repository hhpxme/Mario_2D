package Objects;

import Animation.Animation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Explosion {
	private int x;
	private int y;
	private int xMap;
	private int yMap;
	
	private int width;
	private int height;
	
	private Animation animation;
	private BufferedImage[] sprites;
	
	private boolean remove;
	
	public Explosion(int x, int y) {
		this.x = x;
		this.y = y;
		
		width = 30;
		height = 30;

		//Load sprite
		try {
			BufferedImage spriteSheet = ImageIO.read(new File("src/main/resources/Sprites/Enemies/explosion.gif"));
			
			sprites = new BufferedImage[6];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(70);
	}
	
	public void update() {
		animation.update();
		if (animation.isPlay()) {
			remove = true;
		}
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void setMapPosition(int xMap, int yMap) {
		this.xMap = xMap;
		this.yMap = yMap;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(animation.getImage(), x + xMap - width / 2, y + yMap - height / 2, null);
	}
}

















