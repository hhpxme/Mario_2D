package GameState;

import Game.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {
	private Background background;

	private int currentSelection = 0;
	private String[] menu = {
			"Start",
			"Credits",
			"Quit"
	};

	private Color titleColor;
	private Color selectionColor;
	private Color nonSelectionColor;

	private Font titleFont;
	private Font font;

	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;

		try {
			background = new Background("src/main/resources/Backgrounds/menu_background.png", 1);
			background.setVector(-0.1, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		titleColor = new Color(242, 109, 125);
		selectionColor = new Color(246, 107, 114);
		nonSelectionColor = new Color(255, 255, 255);

		titleFont = new Font("Century Gothic", Font.PLAIN, 32);
		font = new Font("Arial", Font.PLAIN, 16);
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

		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Mario", GamePanel.WIDTH / 2 - g.getFontMetrics(titleFont).stringWidth("Mario") / 2, GamePanel.HEIGHT / 4);

		g.setFont(font);
		for (int i = 0; i < menu.length; i++) {
			if (i == currentSelection) {
				g.setColor(selectionColor);
			} else {
				g.setColor(nonSelectionColor);
			}
			g.drawString(menu[i], GamePanel.WIDTH / 2 - g.getFontMetrics(font).stringWidth(menu[i]) / 2, GamePanel.HEIGHT / 2 + i * 25);
		}
	}

	private void select() {
		if (currentSelection == 0) {
			//Set State Level1
			gsm.setState(GameStateManager.LEVEL1STATE);
		} else if (currentSelection == 1) {

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










