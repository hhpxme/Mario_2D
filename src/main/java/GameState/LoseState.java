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

public class LoseState extends GameState {
	private Background background;

	private int currentSelection = 0;
	private String[] menu = {
			"Replay",
			"Quit"
	};

	private Color selectionColor;
	private Color nonSelectionColor;

	private Font selectionFont;

	private NewFont newFont;

	private BufferedImage logo;

	public LoseState(GameStateManager gsm) {
		this.gsm = gsm;

		try {
			background = new Background("src/main/resources/Backgrounds/lw_background.png", 0);
			background.setVector(0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Color
		selectionColor = new Color(255, 115, 115);
		nonSelectionColor = new Color(255, 255, 255);

		newFont = new NewFont("src/main/resources/Fonts/pixel.ttf");
		selectionFont = newFont.loadFont();

		try {
			logo = ImageIO.read(new File("src/main/resources/Icon/game_over.png"));
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
		g.drawImage(logo, GamePanel.WIDTH / 2 - logo.getWidth() / 2, GamePanel.HEIGHT / 5, null);

		g.setFont(selectionFont.deriveFont(Font.PLAIN, 14));
		for (int i = 0; i < menu.length; i++) {
			if (i == currentSelection) {
				g.setColor(selectionColor);
			} else {
				g.setColor(nonSelectionColor);
			}
			g.drawString(menu[i], GamePanel.WIDTH / 2 - g.getFontMetrics(selectionFont.deriveFont(Font.PLAIN, 14)).stringWidth(menu[i]) / 2, GamePanel.HEIGHT / 2 + 20 + i * 35);
		}
	}

	private void select() {
		if (currentSelection == 0) {
			//Set State Level1
			gsm.setState(GameStateManager.LEVEL1STATE);
		} else if (currentSelection == 1) {
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_UP) {
			currentSelection--;
			if (currentSelection == -1) {
				currentSelection = menu.length - 1;
			}
		}

		if (k == KeyEvent.VK_DOWN) {
			currentSelection++;
			if (currentSelection == menu.length) {
				currentSelection = 0;
			}
		}

		if (k == KeyEvent.VK_ENTER) {
			select();
		}
	}

	@Override
	public void keyReleased(int k) {

	}
}










