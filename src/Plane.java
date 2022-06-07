import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Plane extends JFrame {
    private Image bufferImage;
    private Graphics screenGraphic;
    private Image mainScreen = new ImageIcon("src/image/background.png").getImage();
    private Game game=new Game();

    public Plane() {
        super("나무열매를 먹어요");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);	//스크린 크기 설정

        setLayout(null);
        addKeyListener(new KeyListener());

        setResizable(false);
        setVisible(true);
        game.start();
    }

    public void paint(Graphics g) {
        bufferImage = createImage(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);
        screenGraphic = bufferImage.getGraphics();
        screenDraw(screenGraphic);
        g.drawImage(bufferImage,0,0,null);
    }

    public void screenDraw(Graphics g) {
        g.drawImage(mainScreen,0,75,null);
        game.gameDraw(g);
        this.repaint();
    }

    class KeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    game.setLeft(true);
                    game.setRightImage(false);
                    game.setLeftImage(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    game.setRight(true);
                    game.setRightImage(true);
                    game.setLeftImage(false);
                    break;
                case KeyEvent.VK_ESCAPE:
                    if(game.Stop()) game.setStop(false);
                    else game.setStop(true);
                    break;
                case KeyEvent.VK_ENTER:
                    if(game.isOver()) game.restart();
                    break;
            }
        }
        public void keyReleased(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    game.setLeft(false);
                    break;
                case KeyEvent.VK_RIGHT:
                    game.setRight(false);
                    break;
            }
        }
    }
}
