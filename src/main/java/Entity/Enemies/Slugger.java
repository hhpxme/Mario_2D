package Entity.Enemies;

import Animation.Animation;
import TileMap.Map2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Slugger extends Enemy {
	private BufferedImage[] sprites;
	
	public Slugger(Map2D map2D) {
		super(map2D);
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		boxWidth = 20;
		boxHeight = 20;
		
		currentHP = maxHP = 1;
		damage = 1;
		
		//Load sprites
		try {
			BufferedImage spriteSheet = ImageIO.read(new File("src/main/resources/Sprites/Enemies/slugger.gif"));
			
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
		animation.setDelay(300);
		
		right = true;
		facingRight = true;
	}
	
	private void getNextPosition() {
		//Movement
		if (left) {
			dX -= moveSpeed;
			if (dX < -maxSpeed) {
				dX = -maxSpeed;
			}
		} else if (right) {
			dX += moveSpeed;
			if (dX > maxSpeed) {
				dX = maxSpeed;
			}
		}
		
		//Falling
		if (falling) {
			dY += fallSpeed;
		}
	}
	
	public void update() {
		//Update position
		getNextPosition();
		mapCollision();
		setPosition(xTemp, yTemp);

		//If touch a wall, go other direction
		if (right && dX == 0) {
			right = false;
			left = true;
			facingRight = false;
		} else if (left && dX == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		//Update animation
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
	}
	
}











