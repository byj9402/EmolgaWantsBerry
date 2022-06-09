import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Plane extends JFrame {
    private final Image mainScreen = new ImageIcon("src/image/background.png").getImage();
    private final Game game=new Game();

    public Plane() {
        super("나무열매를 먹어요");
        setSize(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);	// 크기 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(null);
        addKeyListener(new KeyListener());

        // resize 허용하지 않음
        setResizable(false);
        setVisible(true);
        game.start();
    }

    public void paint(Graphics g) {
        Image bufferImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        Graphics screenGraphic = bufferImage.getGraphics();
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
                // 왼쪽 화살표를 눌렀을 때
                case KeyEvent.VK_LEFT:
                    // 왼쪽으로 이동
                    game.setLeft(true);
                    // 오른쪽을 바라보던 이미지를 지우고
                    // 왼쪽을 바라보는 이미지로 표시
                    game.setRightImage(false);
                    game.setLeftImage(true);
                    break;
                // 오른쪽 화살표를 눌렀을 때
                case KeyEvent.VK_RIGHT:
                    // 오른쪽으로 이동
                    game.setRight(true);
                    // 왼쪽을 바라보던 이미지를 지우고
                    // 오른쪽을 바라보는 이미지로 표시
                    game.setRightImage(true);
                    game.setLeftImage(false);
                    break;
                // esc 키를 눌렀을 때
                case KeyEvent.VK_ESCAPE:
                    // 게임이 멈춰 있는 상태라면
                    // 게임을 시작한다
                    // 그렇지 않다면 게임을 멈춘다
                    game.setStop(!game.Stop());
                    break;
                // Enter 키를 눌렀을 때
                case KeyEvent.VK_ENTER:
                    // 게임이 종료된 상황이라면 다시 시작한다
                    if(game.isOver()) {game.restart();}
                    // 게임이 멈춰있는 상태라면 게임을 시작한다
                    else if (game.Stop()) {game.setStop(false);}
                    break;
            }
        }

        // 키를 뗐을 때
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
