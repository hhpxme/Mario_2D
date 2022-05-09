package Objects;

import Animation.Animation;
import Game.GamePanel;
import TileMap.Map2D;
import TileMap.Tile;

import java.awt.*;

public abstract class MapObject {
	// tile stuff
	protected Map2D map2D;
	protected int tileSize;
	protected double xMap;
	protected double yMap;
	
	// position and vector
	protected double x;
	protected double y;
	protected double dX;
	protected double dY;
	
	// dimensions
	protected int width;
	protected int height;
	
	// collision box
	protected int boxWidth;
	protected int boxHeight;
	
	// collision
	protected int currRow;
	protected int currCol;
	protected double xDest;
	protected double yDest;
	protected double xTemp;
	protected double yTemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	// animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	// constructor
	public MapObject(Map2D map2D) {
		this.map2D = map2D;
		tileSize = map2D.getTileSize();
	}
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	public Rectangle getRectangle() {
		return new Rectangle((int)x - boxWidth, (int)y - boxHeight, boxWidth, boxHeight);
	}
	
	public void boxCorners(double x, double y) {
		int leftTile = (int)(x - boxWidth / 2) / tileSize;
		int rightTile = (int)(x + boxWidth / 2 - 1) / tileSize;
		int topTile = (int)(y - boxHeight / 2) / tileSize;
		int bottomTile = (int)(y + boxHeight / 2 - 1) / tileSize;
		
		int tl = map2D.getType(topTile, leftTile);
		int tr = map2D.getType(topTile, rightTile);
		int bl = map2D.getType(bottomTile, leftTile);
		int br = map2D.getType(bottomTile, rightTile);
		
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
	}
	
	public void mapCollision() {
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xDest = x + dX;
		yDest = y + dY;
		
		xTemp = x;
		yTemp = y;
		
		boxCorners(x, yDest);
		if (dY < 0) {
			if (topLeft || topRight) {
				dY = 0;
				yTemp = currRow * tileSize + boxHeight / 2;
			} else {
				yTemp += dY;
			}
		}
		if (dY > 0) {
			if (bottomLeft || bottomRight) {
				dY = 0;
				falling = false;
				yTemp = (currRow + 1) * tileSize - boxHeight / 2;
			} else {
				yTemp += dY;
			}
		}
		
		boxCorners(xDest, y);
		if (dX < 0) {
			if (topLeft || bottomLeft) {
				dX = 0;
				xTemp = currCol * tileSize + boxWidth / 2;
			} else {
				xTemp += dX;
			}
		}
		if (dX > 0) {
			if (topRight || bottomRight) {
				dX = 0;
				xTemp = (currCol + 1) * tileSize - boxWidth / 2;
			} else {
				xTemp += dX;
			}
		}
		
		if (!falling) {
			boxCorners(x, yDest + 1);
			if (!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setMapPosition() {
		xMap = map2D.getX();
		yMap = map2D.getY();
	}
	
	public void draw(java.awt.Graphics2D g) {
		if (facingRight) {
			g.drawImage(animation.getImage(), (int)(x + xMap - width / 2), (int)(y + yMap - height / 2), null);
		} else {
			g.drawImage(animation.getImage(), (int)(x + xMap - width / 2 + width), (int)(y + yMap - height / 2), -width, height, null);
		}
	}
}
















