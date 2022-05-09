package TileMap;

import Entity.Player.Player;
import Game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Map2D {
	// position
	private double x;
	private double y;

	//Hole Pos
	private double holeX;
	private double holeY;
	
	// bounds
	private int xMin;
	private int yMin;
	private int xMax;
	private int yMax;
	
	private double tweening;
	
	// map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;

	//death in last row
	private boolean instantDeath;

	public boolean isInstantDeath() {
		return instantDeath;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getHoleY() {
		return holeY;
	}

	public int getTileSize() {
		return tileSize;
	}

	public Map2D(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tweening = 0.1;
	}
	
	public void loadTiles(String s) {
		try {
			tileset = ImageIO.read(new File(s));
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[3][numTilesAcross];
			
			BufferedImage tileImage;
			for (int col = 0; col < numTilesAcross; col++) {
				tileImage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(tileImage, Tile.NORMAL);

				tileImage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(tileImage, Tile.BLOCKED);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String s) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(s)));
			
			numCols = Integer.parseInt(bufferedReader.readLine());
			numRows = Integer.parseInt(bufferedReader.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xMin = GamePanel.WIDTH - width;
			xMax = 0;
			yMin = GamePanel.HEIGHT - height;
			yMax = 0;

			for (int row = 0; row < numRows; row++) {
				String line = bufferedReader.readLine();
				String[] tokens = line.split("\\s+");
				for (int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	
	public void setPosition(double x, double y) {
		this.x += (x - this.x) * tweening;
		this.y += (y - this.y) * tweening;
		
		fixBounds();
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
	}
	
	private void fixBounds() {
		if (x < xMin) {
			x = xMin;
		}
		if (y < yMin) {
			y = yMin;
		}
		if (x > xMax) {
			x = xMax;
		}
		if (y > yMax) {
			y = yMax;
		}
	}
	
	public void draw(Graphics2D g) {
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			if (row >= numRows)
				break;
			for (int col = colOffset; col < colOffset + numColsToDraw; col++) {
				if (col >= numCols)
					break;
				
				if (map[row][col] == 0) {
					if (col == 7) {
						holeY = 227.5;
					} else {
						continue;
					}
				}
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				g.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize, (int)y + row * tileSize, null);
			}
		}
	}
}



















