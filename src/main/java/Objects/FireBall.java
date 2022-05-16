package Objects;

import Animation.Animation;
import TileMap.Map2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class FireBall extends MapObject {
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	
	public FireBall(Map2D map2D, boolean right) {
		super(map2D);
		
		facingRight = right;
		
		moveSpeed = 3.8;
		if (right) {
			dX = moveSpeed;
		} else {
			dX = -moveSpeed;
		}
		
		width = 30;
		height = 30;
		boxWidth = 15;
		boxHeight = 15;
		
		// load sprites
		try {
			BufferedImage spriteSheet = ImageIO.read(new File("src/main/resources/Sprites/Player/fireball.gif"));
			
			sprites = new BufferedImage[4];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
			}
			
			hitSprites = new BufferedImage[3];
			for (int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spriteSheet.getSubimage(i * width, height, width, height);
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setHit() {
		if (hit)
			return;

		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dX = 0;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {
		mapCollision();
		setPosition(xTemp, yTemp);
		
		if (dX == 0 && !hit) {
			setHit();
		}
		
		animation.update();
		if (hit && animation.isPlay()) {
			remove = true;
		}
		
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
	}
}


















