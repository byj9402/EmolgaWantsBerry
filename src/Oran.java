import java.awt.*;
import javax.swing.*;

public class Oran extends Berry {
    Image image = new ImageIcon("src/image/Oran.png").getImage();
    int width = image.getWidth(null);
    int height = image.getHeight(null);

    public Oran(int x, int y) {
        super(x, y);
    }

    public void move() {
        this.y += 7;
    }
}
