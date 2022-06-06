import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Game extends Thread {
    private int delay = 20;
    private long time;
    private int count;
    private int score;

    private Image player = new ImageIcon("src/image/Emolga.png").getImage();
    private int playerX, playerY;
    private int playerWidth = player.getWidth(null);
    private int playerHeight = player.getHeight(null);
    private int playerSpeed = 10;

    private boolean left, right;
    private boolean isOver;

    private ArrayList<Oran> oranList=new ArrayList<Oran>();
    private ArrayList<Leppa> leppaList=new ArrayList<Leppa>();
    private ArrayList<Wiki> wikiList=new ArrayList<Wiki>();

    private Oran oran;
    private Leppa leppa;

    private Wiki wiki;
    @Override
    public void run() {
        restart();
        while (true) {
            while(!isOver) {
                time = System.currentTimeMillis();
                if (System.currentTimeMillis() - time < delay) {
                    try {
                        Thread.sleep(delay - System.currentTimeMillis() + time);
                        wikiAppearProcess();
                        wikiMoveProcess();
                        KeyProcess();
                        count++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(100);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void wikiAppearProcess() {
        if(count%7==0) {
            wiki = new Wiki((int)(Math.random()*441), 0);
            wikiList.add(wiki);
        }
    }

    private void wikiMoveProcess() {
        for (int i = 0; i < wikiList.size(); i++) {
            wiki = wikiList.get(i);
            wiki.move();

            if (wiki.x + 15 < playerX + playerWidth && wiki.x + wiki.width > playerX + 15
                    // 에몽가가 위키열매에 맞을 때
                    && wiki.y < playerY + playerHeight - 40 && wiki.y + wiki.height > playerY)
            {
                wikiList.remove(wiki); // 위키 열매 삭제
                isOver = true;
            }
        }
    }

            public void restart() {	//다시하기
        isOver = false;
        count = 0;
        score = 0;

        playerX = (Main.SCREEN_WIDTH - playerWidth) / 2;
        playerY = (Main.SCREEN_HEIGHT - playerHeight) - 15;

        oranList.clear();
        leppaList.clear();
        wikiList.clear();
    }

    public void gameDraw(Graphics g) {
        playerDraw(g);
        BerryDraw(g);
        infoDraw(g);
    }

    public void playerDraw(Graphics g) {
        g.drawImage(player, playerX, playerY, null);
    }

    public void BerryDraw(Graphics g) {
        for(int i=0;i<wikiList.size();i++) {
            wiki = wikiList.get(i);
            g.drawImage(wiki.image, wiki.x, wiki.y, null);
        }
    }

    public void infoDraw(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial",Font.BOLD,20));
        g.drawString("SCORE : "+score,250,60);
        if(isOver) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial",Font.BOLD,40));
            g.drawString("Press Enter to restart",40,200);
        }
    }

    private void KeyProcess() {
        if (left && playerX - playerSpeed > 0)
            playerX -= playerSpeed;
        if (right && playerX + playerWidth + playerSpeed < Main.SCREEN_WIDTH)
            playerX += playerSpeed;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isOver() {
        return isOver;
    }

}
