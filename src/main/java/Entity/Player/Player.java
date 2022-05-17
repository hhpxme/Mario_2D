package Entity.Player;

import Animation.Animation;
import Entity.Enemies.Enemy;
import Entity.Items.Chest;
import Objects.FireBall;
import Objects.MapObject;
import TileMap.Map2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Player extends MapObject {
	//Player stuff
	private int HP;
	private int maxHP;
	private int fire;
	private int maxFire;
	private boolean dead;
	private long deadTimer;
	private boolean flinching;
	private long flinchTimer;
	private boolean dance;
	private long danceTimer;
	
	//Fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	//Knocking (Gõ búa ý)
	private boolean knocking;
	private int knockDamage;
	private int knockRange;
	
	//Gliding
	private boolean gliding;
	
	//Animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		2, 8, 1, 6, 3, 4, 2, 2, 4
	};
	
	//Animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int KNOCKING = 6;
	private static final int DEAD = 7;
	private static final int DANCE = 8;

	//Win lose stt
	private boolean lose = false;
	private boolean win = false;
	private long time;

	public int getHP() {
		return HP;
	}
	public int getMaxHP() {
		return maxHP;
	}
	public int getFire() {
		return fire;
	}
	public int getMaxFire() {
		return maxFire;
	}
	public boolean isDead() {
		return dead;
	}
	public boolean isLose() { return lose; }
	public boolean isWin() { return win; }

	public void setFiring() {
		firing = true;
	}
	public void setKnocking() {
		knocking = true;
	}
	public void setGliding(boolean gliding) {
		this.gliding = gliding;
	}

	public Player(Map2D map2D) {
		super(map2D);

		width = 30;
		height = 30;
		boxWidth = 20;
		boxHeight = 20;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		dead = false;
		dance = false;
		
		facingRight = true;
		
		HP = maxHP = 5;
		fire = maxFire = 2500;
		
		fireCost = 200;
		fireBallDamage = 4;
		fireBalls = new ArrayList<FireBall>();
		
		knockDamage = 5;
		knockRange = 32;

		time = 0;
		
		//Load sprites
		try {
			BufferedImage spriteSheet = ImageIO.read(new File("src/main/resources/Sprites/Player/playersprites.png"));
			
			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < numFrames.length; i++) {
				BufferedImage[] bufferedImages = new BufferedImage[numFrames[i]];
				
				for (int j = 0; j < numFrames[i]; j++) {
					if (i != KNOCKING) {
						bufferedImages[j] = spriteSheet.getSubimage(j * width, i * height, width, height);
					} else {
						bufferedImages[j] = spriteSheet.getSubimage(j * width * 2, i * height, width * 2, height);
					}
				}
				
				sprites.add(bufferedImages);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(300);
	}

	public void checkAttack(ArrayList<Enemy> enemies) {
		//Loop through enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			
			//Knock attack
			if (knocking) {
				if (facingRight) {
					if (e.getX() > x && e.getX() < x + knockRange && e.getY() > y - height / 2 && e.getY() < y + height / 2) {
						e.hit(knockDamage);
					}
				} else {
					if (e.getX() < x && e.getX() > x - knockRange && e.getY() > y - height / 2 && e.getY() < y + height / 2) {
						e.hit(knockDamage);
					}
				}
			}
			
			//Fireballs
			for (int j = 0; j < fireBalls.size(); j++) {
				if (fireBalls.get(j).intersects(e)) {
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
					break;
				}
			}
			
			//Check enemy collision
			if (!dead) {
				if (intersects(e)) {
					hit(e.getDamage());
				}
			}
		}
	}

	//Check player touch a chest
	public void checkWin(Chest chest) {
		if (intersects(chest)) {
			dance = true;
			danceTimer = System.nanoTime();
			if (y <= 4725) {
				right = true;
			} else {
				left = true;
			}
		}
	}
	
	public void hit(int damage) {
		if (flinching)
			return;

		HP -= damage;
		if (HP < 0) {
			HP = 0;
		}
		if (HP == 0) {
			dead = true;
			deadTimer = System.nanoTime();
		}

		if (!dead) {
			flinching = true;
			flinchTimer = System.nanoTime();
		}
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
		} else {
			if (dX > 0) {
				dX -= stopSpeed;
				if (dX < 0) {
					dX = 0;
				}
			} else if (dX < 0) {
				dX += stopSpeed;
				if (dX > 0) {
					dX = 0;
				}
			}
		}
		
		//Cannot move while attacking and dead, except in air
		if ((currentAction == KNOCKING || currentAction == FIREBALL || currentAction == DEAD) && !(jumping || falling || dead)) {
			dX = 0;
		}
		
		//Jumping
		if (jumping && !falling) {
			dY = jumpStart;
			falling = true;
		}
		
		//Falling
		if (falling) {
			if (dY > 0 && gliding) {
				dY += fallSpeed * 0.1;
			} else dY += fallSpeed;
			
			if (dY > 0) {
				jumping = false;
			}
			if (dY < 0 && !jumping) {
				dY += stopJumpSpeed;
			}
			
			if (dY > maxFallSpeed) {
				dY = maxFallSpeed;
			}
		}
	}
	
	public void update() {
		//Update position
		getNextPosition();
		mapCollision();
		setPosition(xTemp, yTemp);

		//Check attack has stopped
		if (currentAction == KNOCKING) {
			if (animation.isPlay()) {
				knocking = false;
			}
		}
		if (currentAction == FIREBALL) {
			if (animation.isPlay()) {
				firing = false;
			}
		}
		
		//Fireball attack
		fire += 1;
		if (fire > maxFire) {
			fire = maxFire;
		}
		if (firing && currentAction != FIREBALL) {
			if (fire > fireCost) {
				fire -= fireCost;
				FireBall fb = new FireBall(map2D, facingRight);
				fb.setPosition(x, y);
				fireBalls.add(fb);
			}
		}
		
		//Update fireballs
		for (int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if (fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
		
		//Check done flinching
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 1000) {
				flinching = false;
			}
		}

		if (y >= 210) {
			HP = 0;
			dead = true;
			deadTimer = System.nanoTime();
		}

		//Check done dead anim
		if (dead) {
			long elapsed = (System.nanoTime() - deadTimer) / 1000000;
			System.out.println(elapsed);
			if (elapsed > 1200) {
				lose = true;
			}
			time++;
			if (time == 3 && elapsed < 30) {
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				lose = true;
			}
		}

		//Check done dance anim
		if (dance) {
			long elapsed = (System.nanoTime() - danceTimer) / 1000000;
			System.out.println(elapsed);
			if (elapsed > 200) {
				left = false;
				right = false;
			}
			if (elapsed > 1200) {
				win = true;
			}
		}
		
		//Set animation
		if (knocking) {
			if (currentAction != KNOCKING) {
				currentAction = KNOCKING;
				animation.setFrames(sprites.get(KNOCKING));
				animation.setDelay(75);
				width = 60;
			}
		} else if (firing) {
			if (currentAction != FIREBALL) {
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 30;
			}
		} else if (dead) {
			if (currentAction != DEAD || falling) {
				currentAction = DEAD;
				animation.setFrames(sprites.get(DEAD));
				animation.setDelay(100);
				width = 30;
			}
		} else if (dance) {
			if (currentAction != DANCE) {
				currentAction = DANCE;
				animation.setFrames(sprites.get(DANCE));
				animation.setDelay(100);
				width = 30;
			}
		} else if (dY > 0) {
			if (gliding) {
				if (currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 30;
				}
			} else if (currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		} else if (dY < 0) {
			if (currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(300);
				width = 30;
			}
		}
		animation.update();
		
		// set direction
		if (currentAction != KNOCKING && currentAction != FIREBALL) {
			if (right) {
				facingRight = true;
			}
			if (left) {
				facingRight = false;
			}
		}
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		
		//Draw fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}
		
		//Draw player when flinching
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0)
				return;
		}
		
		super.draw(g);
	}
}

















