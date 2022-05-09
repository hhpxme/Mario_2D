package GameState.Levels;

import Entity.Enemies.Slugger;
import Entity.Enemies.Enemy;
import GameState.*;
import Objects.Explosion;
import Objects.HUD;
import Entity.Player.Player;
import Game.GamePanel;
import TileMap.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level1State extends GameState {
	private Map2D map2D;
	private Background background;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {
		map2D = new Map2D(30);
		map2D.loadTiles("src/main/resources/Tilesets/grasstileset.gif");
		map2D.loadMap("src/main/resources/Maps/level1-1.map");
		map2D.setPosition(0, 0);
		
		background = new Background("src/main/resources/Backgrounds/ing_background.png", 0.1);
		
		player = new Player(map2D);
		player.setPosition(100, 100);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
	}
	
	private void populateEnemies() {
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		Point[] points = new Point[] {
			new Point(200, 100),
			new Point(860, 200),
			new Point(1525, 200),
			new Point(1680, 200),
			new Point(1800, 200)
		};
		for (int i = 0; i < points.length; i++) {
			s = new Slugger(map2D);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
	}

	@Override
	public void update() {
		// update player
		player.update();
		map2D.setPosition(GamePanel.WIDTH / 2 - player.getX(), GamePanel.HEIGHT / 2 - player.getY());
		
		// set background
		background.setPosition(map2D.getX(), map2D.getY());
		
		// attack enemies
		player.checkAttack(enemies);

		//System.out.println(player.getX() + ", " + player.getY());
		System.out.println(map2D.getX() + ", " + map2D.getY());

		if (player.isDead()) {
			gsm.setState(GameStateManager.LOSESTATE);
		}

		if (map2D.getX() == -2890) {
			gsm.setState(GameStateManager.WINSTATE);
		}
		
		// update all enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			enemy.update();
			if (enemy.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion((int)enemy.getX(), (int)enemy.getY()));
			}
		}
		
		// update explosions
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if (explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
	}
	
	public void draw(Graphics2D g) {
		background.draw(g);
		map2D.draw(g);
		hud.draw(g);
		player.draw(g);

		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}

		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int)map2D.getX(), (int)map2D.getY());
			explosions.get(i).draw(g);
		}
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_LEFT) {
			player.setLeft(true);
		}
		if (k == KeyEvent.VK_RIGHT) {
			player.setRight(true);
		}
		if (k == KeyEvent.VK_UP) {
			player.setUp(true);
		}
		if (k == KeyEvent.VK_DOWN) {
			player.setDown(true);
		}
		if (k == KeyEvent.VK_W) {
			player.setJumping(true);
		}
		if (k == KeyEvent.VK_E) {
			player.setGliding(true);
		}
		if (k == KeyEvent.VK_R) {
			player.setKnocking();
		}
		if (k == KeyEvent.VK_F) {
			player.setFiring();
		}
	}

	@Override
	public void keyReleased(int k) {
		if (k == KeyEvent.VK_LEFT) {
			player.setLeft(false);
		}
		if (k == KeyEvent.VK_RIGHT) {
			player.setRight(false);
		}
		if (k == KeyEvent.VK_UP) {
			player.setUp(false);
		}
		if (k == KeyEvent.VK_DOWN) {
			player.setDown(false);
		}
		if (k == KeyEvent.VK_W) {
			player.setJumping(false);
		}
		if (k == KeyEvent.VK_E) {
			player.setGliding(false);
		}
	}
}












