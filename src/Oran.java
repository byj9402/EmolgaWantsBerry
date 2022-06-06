import java.awt.*;
import javax.swing.*;

public class Bomb {
    Image image=new ImageIcon("src/image/bomb.png").getImage();
    int x,y;
    int width=image.getWidth(null);
    int height=image.getHeight(null);

    public Bomb(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public void move() {
        // 폭탄의 움직임
    }
}
