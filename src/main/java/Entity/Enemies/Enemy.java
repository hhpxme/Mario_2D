package Entity.Enemies;

import Objects.MapObject;
import TileMap.Map2D;

public class Enemy extends MapObject {
	protected int currentHP;
	protected int maxHP;
	protected boolean dead;
	protected int damage;
	
	protected boolean flinching;
	protected long flinchTimer;

	public boolean isDead() {
		return dead;
	}

	public int getDamage() {
		return damage;
	}

	public Enemy(Map2D map2D) {
		super(map2D);
	}
	
	public void hit(int damage) {
		if (dead || flinching)
			return;

		currentHP -= damage;
		if (currentHP < 0) {
			currentHP = 0;
		}
		if (currentHP == 0) {
			dead = true;
		}

		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void update() {}
}














