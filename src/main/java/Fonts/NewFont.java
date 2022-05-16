package Fonts;

import java.awt.*;
import java.io.*;

public class NewFont {
    private String path;

    public NewFont(String path) {
        this.path = path;
    }

    public Font loadFont() {
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(path));
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            return font;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
