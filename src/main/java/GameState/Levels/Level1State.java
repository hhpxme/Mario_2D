package GameState.Levels;

import Audio.SFX;
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
	private ArrayList<Enemy> enemiesInBossRoom;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;

	//private SFX backgroundMusic;
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {
		map2D = new Map2D(30);
		map2D.loadTiles("src/main/resources/Tilesets/grasstileset.gif");
		map2D.loadMap("src/main/resources/Maps/map1.map");
		map2D.setPosition(0, 0);
		
		background = new Background("src/main/resources/Backgrounds/ing_background.png", 0.1);
		
		player = new Player(map2D);
		player.setPosition(100, 100);
		
		populateEnemies();

		if (player.getX() > 4700) {
			enemiesBossRoom();
		}
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);

		//backgroundMusic = new SFX("src/main/resources/Music/bgmusic_1.wav");
		//backgroundMusic.play();
	}
	
	private void populateEnemies() {
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		Point[] points = new Point[] {
				new Point(200, 200),
				new Point(525, 110),
				new Point(1150, 170),
				new Point(1300, 200),
				new Point(1675, 170),
				new Point(2525, 140),
				new Point(2675, 170),
				new Point(2925, 200),
				new Point(2800, 170),
				new Point(4300, 200),
		};
		for (int i = 0; i < points.length; i++) {
			s = new Slugger(map2D);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
	}

	private void enemiesBossRoom() {
		enemiesInBossRoom = new ArrayList<Enemy>();

		Slugger s;
		Point[] points = new Point[] {
				new Point(4850, 150),
				new Point(4800, 150),
				new Point(4900, 150)
		};
		for (int i = 0; i < points.length; i++) {
			s = new Slugger(map2D);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
	}

	private void closeBossRoom() {
		if (player.getX() >= 4670) {
			map2D.loadMap("src/main/resources/Maps/map2.map");
		}
		if (player.getX() >= 4700) {
			// update all enemies
			for (int k = 0; k < 3; k++) {
				for (int i = 0; i < enemiesInBossRoom.size(); i++) {
					Enemy enemy = enemiesInBossRoom.get(i);
					enemy.update();
					if (enemy.isDead()) {
						enemiesInBossRoom.remove(i);
						i--;
						explosions.add(new Explosion((int)enemy.getX(), (int)enemy.getY()));
					}
				}
				if (k == 3) {
					if (enemiesInBossRoom.size() == 1) {

					}
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

		System.out.println(player.getX() + ", " + player.getY());
		//System.out.println(map2D.getX() + ", " + map2D.getY());

		if (player.isLose()) {
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

		closeBossRoom();
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
		if (!player.isDead()) {
			if (k == KeyEvent.VK_LEFT) { player.setLeft(true); }
			if (k == KeyEvent.VK_RIGHT) { player.setRight(true); }
			if (k == KeyEvent.VK_UP) { player.setUp(true); }
			if (k == KeyEvent.VK_DOWN) { player.setDown(true); }
			if (k == KeyEvent.VK_W) { player.setJumping(true); }
			if (k == KeyEvent.VK_E) { player.setGliding(true); }
			if (k == KeyEvent.VK_R) { player.setKnocking(); }
			if (k == KeyEvent.VK_F) { player.setFiring(); }
		} else {
			if (k == KeyEvent.VK_LEFT) { player.setLeft(false); }
			if (k == KeyEvent.VK_RIGHT) { player.setRight(false); }
			if (k == KeyEvent.VK_UP) { player.setUp(false); }
			if (k == KeyEvent.VK_DOWN) { player.setDown(false); }
			if (k == KeyEvent.VK_W) { player.setJumping(false); }
			if (k == KeyEvent.VK_E) { player.setGliding(false); }
		}
	}

	@Override
	public void keyReleased(int k) {
		if (k == KeyEvent.VK_LEFT) { player.setLeft(false); }
		if (k == KeyEvent.VK_RIGHT) { player.setRight(false); }
		if (k == KeyEvent.VK_UP) { player.setUp(false); }
		if (k == KeyEvent.VK_DOWN) { player.setDown(false); }
		if (k == KeyEvent.VK_W) { player.setJumping(false); }
		if (k == KeyEvent.VK_E) { player.setGliding(false); }
	}
}












