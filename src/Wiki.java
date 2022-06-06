import java.awt.*;
import javax.swing.*;

public class Wiki extends Berry{
    Image image = new ImageIcon("src/image/Wiki.png").getImage();
    int width = image.getWidth(null);
    int height = image.getHeight(null);

    public Wiki(int x, int y) {
        super(x, y);
    }

    public void move() {
        this.y += 10;
    }
}
