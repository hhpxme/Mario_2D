package GameState.Levels;

import Entity.Enemies.Duckeshell;
import Entity.Enemies.Slugger;
import Entity.Enemies.Enemy;
import Entity.Items.Chest;
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

	//Boss room
	private boolean lockBossRoom;
	private double pPos;
	private ArrayList<Enemy> listMiniBoss;
	private int turn;
	private boolean finishBoss;

	//Chest
	private double chestX;
	private double chestY;
	private Chest chest;

//	private SFX backgroundMusic;
	
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
		player.setPosition(4600, 110);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);

		lockBossRoom = false;
		turn = 0;
		finishBoss = false;

//		backgroundMusic = new SFX("src/main/resources/Music/bgmusic_1.wav");
//		backgroundMusic.play();
	}
	
	private void populateEnemies() {
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		Duckeshell d;

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
			double numRandom = Math.random();
			numRandom = numRandom * 100;
			System.out.println((int)numRandom);
			if ((int)numRandom % 2 == 0) {
				s = new Slugger(map2D);
				s.setPosition(points[i].x, points[i].y);
				enemies.add(s);
			} else {
				d = new Duckeshell(map2D);
				d.setPosition(points[i].x, points[i].y);
				enemies.add(d);
			}
		}
	}

	private void miniBoss() {
		listMiniBoss = new ArrayList<Enemy>();

		Slugger s;
		Duckeshell d;
		Point[] points = new Point[] {
				new Point(4850, 140),
				new Point(4800, 140),
				new Point(4900, 140)
		};
		for (int i = 0; i < points.length; i++) {
			double numRandom = Math.random();
			numRandom = numRandom * 100;
			if ((int)numRandom % 2 == 0) {
				s = new Slugger(map2D);
				s.setPosition(points[i].x, points[i].y);
				listMiniBoss.add(s);
			} else {
				d = new Duckeshell(map2D);
				d.setPosition(points[i].x, points[i].y);
				listMiniBoss.add(d);
			}
		}
		turn += 1;
	}

	@Override
	public void update() {
		//Ipdate player
		player.update();
		map2D.setPosition(GamePanel.WIDTH / 2 - player.getX(), GamePanel.HEIGHT / 2 - player.getY());
		
		//Set background
		background.setPosition(map2D.getX(), map2D.getY());

		//Attack enemies
		player.checkAttack(enemies);

		//System.out.println(player.getX() + ", " + player.getY());
		//System.out.println(map2D.getX() + ", " + map2D.getY());

		//Set next state
		if (player.isLose()) {
			gsm.setState(GameStateManager.LOSESTATE);
		}
		if (player.isWin()) {
			gsm.setState(GameStateManager.WINSTATE);
		}
		
		//Update all enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			enemy.update();
			if (enemy.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion((int)enemy.getX(), (int)enemy.getY()));
			}
		}
		
		//Update explosions
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if (explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}

		//Create boss room
		if (lockBossRoom == false) {
			pPos = player.getX();
		} else {
			pPos = 0;
			player.checkAttack(listMiniBoss);
			if (listMiniBoss.size() == 0) {
				if (turn < 3) {
					miniBoss();
				}
				if (finishBoss) {
					chest = new Chest(map2D);
					chest.setPosition(chestX, chestY);
					System.out.println(chestX + ", " + chestY);
					finishBoss = false;
				}
			}
			if (turn <= 3) {
				for (int i = 0; i < listMiniBoss.size(); i++) {
					Enemy enemy = listMiniBoss.get(i);
					enemy.update();
					if (enemy.isDead()) {
						listMiniBoss.remove(i);
						i--;
						explosions.add(new Explosion((int)enemy.getX(), (int)enemy.getY()));
					}
					if (listMiniBoss.size() == 1 && turn == 3) {
						chestX = enemy.getX();
						chestY = enemy.getY();
						finishBoss = true;
					}
				}
			}
		}

		//Lock boss room
		if (pPos >= 4650) {
			lockBossRoom = true;
			map2D.loadMap("src/main/resources/Maps/map2.map");
			miniBoss();
		}

		//Update chest
		player.checkWin(chest);
	}
	
	public void draw(Graphics2D g) {
		background.draw(g);
		map2D.draw(g);
		hud.draw(g);
		player.draw(g);

		//Draw enemies
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}

		//Draw enemies in boss room
		for (int i = 0; i < listMiniBoss.size(); i++) {
			listMiniBoss.get(i).draw(g);
		}

		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int)map2D.getX(), (int)map2D.getY());
			explosions.get(i).draw(g);
		}

		//Draw chest
		chest.draw(g);
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












