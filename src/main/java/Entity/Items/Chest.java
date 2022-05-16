package Entity.Items;

import Animation.Animation;
import Entity.Enemies.Enemy;
import Objects.MapObject;
import TileMap.Map2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Chest extends MapObject {
	private BufferedImage[] sprites;

	public Chest(Map2D map2D) {
		super(map2D);
		
		width = 30;
		height = 30;
		boxWidth = 25;
		boxHeight = 25;

		//load sprite sheet
		try {
			BufferedImage spriteSheet = ImageIO.read(new File("src/main/resources/Sprites/Items/chest.png"));

			sprites = new BufferedImage[3];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(100);

		right = true;
		facingRight = true;
	}
	
	public void update() {
		// update position
		mapCollision();
		//setPosition(0, 0);

		// update animation
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();

		super.draw(g);
	}
	
}











