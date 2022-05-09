package Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		JFrame jFrame = new JFrame("MARIO");
		jFrame.setContentPane(new GamePanel());
		jFrame.pack();
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setResizable(false);
		jFrame.setVisible(true);
		jFrame.setIconImage(icon());
	}

	private static Image icon() {
		Image img = null;
		try {
			img = ImageIO.read(new File("src/main/resources/Icon/icon.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return img;
	}
}
