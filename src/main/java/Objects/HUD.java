package Objects;

import Entity.Player.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class HUD {
	private Player player;
	
	private BufferedImage image;
	private Font font;
	
	public HUD(Player player) {
		this.player = player;
		try {
			image = ImageIO.read(new File("src/main/resources/HUD/hud.gif"));
			font = new Font("Arial", Font.PLAIN, 14);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image, 0, 10, null);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(player.getHP() + "/" + player.getMaxHP(), 30, 25);
		g.drawString(player.getFire() / 100 + "/" + player.getMaxFire() / 100, 30, 45);
	}
}













