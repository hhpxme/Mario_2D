package GameState;

import Game.GamePanel;
import TileMap.Background;
import Fonts.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class MenuState extends GameState {
	private Background background;

	private int currentSelection = 0;
	private String[] menu = {
			"Start",
			"Credits",
			"Quit"
	};

	private Color selectionColor;
	private Color nonSelectionColor;

	private Font selectionFont;

	private NewFont newFont;

	private BufferedImage logo;

	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;

		try {
			background = new Background("src/main/resources/Backgrounds/menubg.png", 0);
			background.setVector(0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Color
		selectionColor = new Color(246, 107, 114);
		nonSelectionColor = new Color(0, 0, 0);

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
		g.drawImage(logo, GamePanel.WIDTH / 2 - logo.getWidth() / 2, GamePanel.HEIGHT / 6, null);

		g.setFont(selectionFont.deriveFont(Font.PLAIN, 14));
		for (int i = 0; i < menu.length; i++) {
			if (i == currentSelection) {
				g.setColor(selectionColor);
			} else {
				g.setColor(nonSelectionColor);
			}
			g.drawString(menu[i], GamePanel.WIDTH / 2 - g.getFontMetrics(selectionFont.deriveFont(Font.PLAIN, 14)).stringWidth(menu[i]) / 2, GamePanel.HEIGHT / 2 + 2 + i * 27);
		}
	}

	private void select() {
		if (currentSelection == 0) {
			//Set State Level1
			gsm.setState(GameStateManager.LEVEL1STATE);
		} else if (currentSelection == 1) {
			gsm.setState(GameStateManager.CREDITSSTATE);
		} else if (currentSelection == 2) {
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










