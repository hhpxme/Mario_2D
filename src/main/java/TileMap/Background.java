package TileMap;

import Game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Background {
	private BufferedImage image;

	private double x;
	private double y;
	private double dX;
	private double dY;

	private double moveScale;

	public Background(String s, double moveScale) {
		try {
			image = ImageIO.read(new File(s));
			this.moveScale = moveScale;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}

	public void setVector(double dX, double dY) {
		this.dX = dX;
		this.dY = dY;
	}

	public void update() {
		x += dX;
		y += dY;
	}

	public void draw(Graphics2D g) {
		g.drawImage(image, (int) x, (int) y, null);
		if (x < 0) {
			g.drawImage(image, (int) x + GamePanel.WIDTH, (int) y, null);
		}
		if (x > 0) {
			g.drawImage(image, (int) x - GamePanel.WIDTH, (int) y, null);
		}
	}
}







