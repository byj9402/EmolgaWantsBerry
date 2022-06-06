import java.awt.*;
import javax.swing.*;

public class Leppa extends Berry{
    Image image = new ImageIcon("src/image/Leppa.png").getImage();
    int width = image.getWidth(null);
    int height = image.getHeight(null);

    public Leppa(int x, int y) {
        super(x, y);
    }

    public void move() {
        this.y += 15;
    }
}
