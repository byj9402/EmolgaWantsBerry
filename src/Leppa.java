import javax.swing.*;
import java.awt.*;

public class Leppa {
    Image image=new ImageIcon("src/image/Leppa.png").getImage();
    int x,y;
    int width=image.getWidth(null);
    int height=image.getHeight(null);

    public Leppa(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public void move() {
        // 과사열매의 움직임
    }
}
