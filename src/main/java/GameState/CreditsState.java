package GameState;

import Fonts.NewFont;
import Game.GamePanel;
import TileMap.Background;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CreditsState extends GameState {
	private Background background;

	private int currentSelection = 0;
	private String[] menu = {
			"Hoang Hong Phong",
			"Truong Quoc Thinh",
			"Mai Ngoc Han"
	};

	private Color titleColor;
	private Color memberColor;

	private Font selectionFont;

	private NewFont newFont;

	private BufferedImage logo;

	public CreditsState(GameStateManager gsm) {
		this.gsm = gsm;

		try {
			background = new Background("src/main/resources/Backgrounds/lw_background.png", 0);
			background.setVector(0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Color
		titleColor = new Color(255, 255, 150);
		memberColor = new Color(255, 255, 255);

		newFont = new NewFont("src/main/resources/Fonts/pixel.ttf");
		selectionFont = newFont.loadFont();

		try {
			logo = ImageIO.read(new File("src/main/resources/Icon/logo_320x240.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {

	}

	@Override
	public void update() {
		background.update();
	}

	@Override
	public void draw(Graphics2D g) {
		background.draw(g);
		g.drawImage(logo, GamePanel.WIDTH / 2 - logo.getWidth() / 2, GamePanel.HEIGHT / 10, null);
		g.setFont(selectionFont.deriveFont(Font.PLAIN, 14));

		g.setColor(titleColor);
		g.drawString("DEVELOPMENT TEAM", GamePanel.WIDTH / 2 - g.getFontMetrics(selectionFont.deriveFont(Font.PLAIN, 14)).stringWidth("DEVELOPMENT TEAM") / 2, GamePanel.HEIGHT / 2 - 20);

		g.setFont(selectionFont.deriveFont(Font.PLAIN, 12));
		g.setColor(memberColor);
		for (int i = 0; i < menu.length; i++) {
			g.drawString(menu[i], GamePanel.WIDTH / 2 - g.getFontMetrics(selectionFont.deriveFont(Font.PLAIN, 12)).stringWidth(menu[i]) / 2, GamePanel.HEIGHT / 2 + 10 + i * 30);
		}

		g.setFont(selectionFont.deriveFont(Font.PLAIN, 8));
		g.setColor(Color.BLACK);
		g.drawString("Press ESC to Menu", GamePanel.WIDTH / 2 - g.getFontMetrics(selectionFont.deriveFont(Font.PLAIN, 8)).stringWidth("Press ESC to Menu") / 2, GamePanel.HEIGHT - 10);
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ESCAPE) {
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}

	@Override
	public void keyReleased(int k) {

	}
}










