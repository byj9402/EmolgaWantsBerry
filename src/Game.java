import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Game extends Thread {
    private int delay = 20;
    private long time;
    private long playTime;
    private int count;
    private int score;

    // 에몽가 이미지
    // left, right에 따라 변경하기
    private Image player = new ImageIcon("src/image/EmolgaLeft.png").getImage();
    private Image playerRight = new ImageIcon("src/image/EmolgaRight.png").getImage();
    private int playerX, playerY;
    private int playerWidth = player.getWidth(null);
    private int playerHeight = player.getHeight(null);
    private int playerSpeed = 10;

    private boolean left, right;
    private boolean leftImage, rightImage;
    private boolean isOver;
    private boolean Stop;

    private ArrayList<Oran> oranList = new ArrayList<Oran>();
    private ArrayList<Leppa> leppaList = new ArrayList<Leppa>();
    private ArrayList<Wiki> wikiList = new ArrayList<Wiki>();

    // Leppa 열매
    // AppearProcess & MoveProcess 추가
    // Leppa : 점수, 남은 시간 ++

    private Oran oran;
    private Leppa leppa;
    private Wiki wiki;
    @Override
    public void run() {
        restart();
        while (true) {
            while(!isOver&&!Stop) {
                // time 감소 시키기
                time = System.currentTimeMillis();
                if (System.currentTimeMillis() - time < delay) {
                    try {
                        Thread.sleep(delay - System.currentTimeMillis() + time);
                        // Leppa, Oran 열매 추가
                        wikiAppearProcess();
                        wikiMoveProcess();
                        oranAppearProcess();
                        oranMoveProcess();
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
        if(count%20==0) {
            wiki = new Wiki((int)(Math.random()*480), 0);
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

    private void oranAppearProcess() {
        if(count%15==0) {
            oran = new Oran((int)(Math.random()*480), 0);
            oranList.add(oran);
        }
    }

    private void oranMoveProcess() {
        for (int i = 0; i < oranList.size(); i++) {
            oran = oranList.get(i);
            oran.move();
            if (oran.x + 15 < playerX + playerWidth && oran.x + oran.width > playerX + 15
                    // 에몽가가 오랭열매에 맞을 때
                    && oran.y < playerY + playerHeight - 40 && oran.y + oran.height > playerY)
            {
                score = score + 10;
                oranList.remove(oran); // 오랭 열매 삭제
            }
        }
    }

    public void restart() {	//다시하기
        isOver = false;
        Stop = false;
        leftImage = true;
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
        if (leftImage) { g.drawImage(player, playerX, playerY, null); }
        else if (rightImage) { g.drawImage(playerRight, playerX, playerY, null); }
    }

    public void BerryDraw(Graphics g) {
        // Leppa 열매 추가
        for(int i=0;i<wikiList.size();i++) {
            wiki = wikiList.get(i);
            g.drawImage(wiki.image, wiki.x, wiki.y, null);
        }
        for(int i=0;i<oranList.size();i++) {
            oran = oranList.get(i);
            g.drawImage(oran.image, oran.x, oran.y, null);
        }
        for(int i=0;i<leppaList.size();i++) {
            leppa = leppaList.get(i);
            g.drawImage(leppa.image, leppa.x, leppa.y, null);
        }
    }

    public void infoDraw(Graphics g) {
        // info GUI 변경
        g.setColor(Color.BLACK);
        g.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        g.drawString("SCORE : " + score, 360, 60);
        g.drawString("TIME: " + playTime, 250, 60);
        if(isOver) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("맑은 고딕",Font.BOLD,40));
            g.drawString("Press Enter to restart",40,200);
        }
        if(Stop) {
            //ESC 눌렀을 때 멈추고 설명 띄움
        }
    }

    private void KeyProcess() {
        if (left && playerX - playerSpeed > 0)
            playerX -= playerSpeed;
        if (right && playerX + playerWidth + playerSpeed < Main.SCREEN_WIDTH)
            playerX += playerSpeed;
    }

    public void setLeft(boolean left) {this.left = left;}
    public void setRight(boolean right) {this.right = right;}
    public void setLeftImage(boolean leftImage) {this.leftImage = leftImage;}
    public void setRightImage(boolean rightImage) {this.rightImage = rightImage;}

    public void setStop(boolean Stop) {this.Stop = Stop;}

    public boolean isOver() {
        return isOver;
    }
    public boolean Stop() {
        return Stop;
    }

}
