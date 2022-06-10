import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import javax.swing.*;

public class Game extends Thread {
    private int count;
    private int score;
    private int life = 3;

    // player(이하 '에몽가') left 이미지
    private final Image player = new ImageIcon("src/image/EmolgaLeft.png").getImage();
    // 에몽가 right 이미지
    private final Image playerRight = new ImageIcon("src/image/EmolgaRight.png").getImage();
    // 멈췄을 때 보여질 게임 rule
    private final Image notice = new ImageIcon("src/image/pause.png").getImage();
    // gameover일 때 보일 에몽가 이미지
    private final Image fail = new ImageIcon("src/image/sadmolga.png").getImage();

    private int playerX, playerY;
    private final int playerWidth = player.getWidth(null);
    private final int playerHeight = player.getHeight(null);

    private boolean left, right;
    private boolean leftImage, rightImage;
    private boolean isOver;
    private boolean Stop;

    // Berry를 저장할 각자의 ArrayList
    private final ArrayList<Oran> oranList = new ArrayList<>();
    private final ArrayList<Leppa> leppaList = new ArrayList<>();
    private final ArrayList<Wiki> wikiList = new ArrayList<>();

    private Oran oran;
    private Leppa leppa;
    private Wiki wiki;

    public Game(){
        int[] temp = new int[10];
        for(int start = 0; start< temp.length; start++){
            temp[start]=start;
        }
    }

    /* Thread 제작에 참고한 소스 코드
        public class SingleThreadEx2 implements Runnable{

            private int[] temp;

            public SingleThreadEx2(){
            temp = new int[10];

            for(int start=0;start<temp.length;start++){
                temp[start]=start;
            }
            }

            @Override
            public void run() {
            // TODO Auto-generated method stub
            for(int start:temp){
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                    // TODO: handle exception
                }

                System.out.println("스레드이름:"+Thread.currentThread().getName());
                System.out.println("temp value :"+start);
            }
            }

            public static void main(String[] args) {

            SingleThreadEx2 ct = new SingleThreadEx2();
            Thread t = new Thread(ct,"첫번째");

            t.start();
            }
        }
        출처 : https://coding-factory.tistory.com/279
     */

    @Override
    public void run() {
        restart();
        while(!isOver&&!Stop) {
            try {
                Thread.sleep(30);
                // Berry들을 나타내고 움직임
                wikiAppearProcess();
                wikiMoveProcess();
                oranAppearProcess();
                oranMoveProcess();
                leppaAppearProcess();
                leppaMoveProcess();
                // ~
                KeyProcess();
            } catch (InterruptedException|ConcurrentModificationException|NullPointerException e) {
                e.printStackTrace();
            } finally {
                count++;
            }
        }
    }

    private void wikiAppearProcess() {
        if(count%20==0) {
            wiki = new Wiki((int)(Math.random()*470) + 20, 0);
            wikiList.add(wiki);
        }
    }

    private void wikiMoveProcess() {
        for (int i = 0; i < wikiList.size(); i++) {
            wiki = wikiList.get(i);
            wiki.move();

            /*
            충돌 범위 (hitbox) 생성에 참고한 소스 코드
                function hitTestRectangle(rect1, rect2) {
                    if( rect1.x < rect2.x + rect2.width  &&
                        rect1.x + rect1.width > rect2.x  &&
                        rect1.y < rect2.y + rect2.height &&
                        rect1.height < rect1.y > rect2.y  ) { return true; }
                    return false;
                }
                출처: https://boycoding.tistory.com/57
            */

            // 위의 소스코드에 사용된 조건에서
            // 충돌 범위를 가시적으로 판단하기 쉽게 하기 위해
            // 조건에 투명화된 이미지 배경만큼의 가중치를 부여
            // 이하 Berry들의 히트박스에 동일하게 적용

            if (wiki.x + 5 < playerX + playerWidth && wiki.x + wiki.width > playerX + 5
                    // 에몽가가 위키 열매에 맞을 때
                    && wiki.y < playerY + playerHeight - 10 && wiki.y + wiki.height > playerY)
            {
                wikiList.remove(wiki); // 위키 열매 삭제
                life--;
                if (life < 1) { isOver = true; }
            }
            // 데이터가 쌓여 과부하되는 현상을 막기 위해 화면 밖을 벗어나면 객체를 삭제함
            if (wiki.y > 820) {
                wikiList.remove(wiki);
            }
        }
    }

    private void oranAppearProcess() {
        if(count%15==0) {
            oran = new Oran((int)(Math.random()*470) + 20, 0);
            oranList.add(oran);
        }
    }

    private void oranMoveProcess() {
        for (int i = 0; i < oranList.size(); i++) {
            oran = oranList.get(i);
            oran.move();
            if (oran.x + 5 < playerX + playerWidth && oran.x + oran.width > playerX + 5
                    // 에몽가가 오랭 열매에 맞을 때
                    && oran.y < playerY + playerHeight - 10 && oran.y + oran.height > playerY)
            {
                score = score + 10;
                oranList.remove(oran); // 오랭 열매 삭제
            }
            // 데이터가 쌓여 과부하되는 현상을 막기 위해 화면 밖을 벗어나면 객체를 삭제함
            if (oran.y > 820) {
                oranList.remove(oran);
            }
        }
    }

    private void leppaAppearProcess() {
        if(count%100==0) {
            leppa = new Leppa((int)(Math.random()*470) + 20, 0);
            leppaList.add(leppa);
        }
    }

    private void leppaMoveProcess() {
        for (int i = 0; i < leppaList.size(); i++) {
            leppa = leppaList.get(i);
            leppa.move();
            if (leppa.x + 5 < playerX + playerWidth && leppa.x + leppa.width > playerX + 5
                    // 에몽가가 과사 열매에 맞을 때
                    && leppa.y < playerY + playerHeight - 10 && leppa.y + leppa.height > playerY)
            {
                // life 1 증가 (최대 3)
                if (life>0 && life<3) { life++; }
                score = score - 100;
                // score가 0이하가 되면 게임 종료
                if (score < 0) { isOver = true; }
                leppaList.remove(leppa); // 과사 열매 삭제
            }
            // 데이터가 쌓여 과부하되는 현상을 막기 위해 화면 밖을 벗어나면 객체를 삭제함
            if (leppa.y > 820) {
                leppaList.remove(leppa);
            }
        }
    }

    // 재시작 시 변수 초기화
    public void restart() {
        isOver = false;
        leftImage = true;
        count = 0;
        score = 0;
        life = 3;

        playerX = (Main.SCREEN_WIDTH - playerWidth) / 2;
        playerY = (Main.SCREEN_HEIGHT - playerHeight) - 15;

        oranList.clear();
        leppaList.clear();
        wikiList.clear();
    }

    public void gameDraw(Graphics g) {
        playerDraw(g);
        BerryDraw(g);
        lifeDraw(g);
        infoDraw(g);
    }

    public void playerDraw(Graphics g) {
        if (leftImage) { g.drawImage(player, playerX, playerY, null); }
        else if (rightImage) { g.drawImage(playerRight, playerX, playerY, null); }
    }

    public void BerryDraw(Graphics g) {
        for (Wiki value : wikiList) {
            wiki = value;
            g.drawImage(wiki.image, wiki.x, wiki.y, null);
        }
        for (Oran value : oranList) {
            oran = value;
            g.drawImage(oran.image, oran.x, oran.y, null);
        }
        for (Leppa value : leppaList) {
            leppa = value;
            g.drawImage(leppa.image, leppa.x, leppa.y, null);
        }
    }

    public void infoDraw(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        g.drawString("SCORE : " + score, 360, 60);
        if(isOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("맑은 고딕",Font.BOLD,40));
            g.drawString("실패...",100,320);
            g.drawImage(fail, 30, 280, null);
            g.setColor(Color.black);
            g.setFont(new Font("맑은 고딕",Font.BOLD,20));
            g.drawString("다시 하려면 ENTER",120,350);
            g.drawString("도움말은 ESC",120,370);
        }
        if(Stop) {
            //ESC를 눌렀을 때 설명 이미지를 띄움
            g.drawImage(notice, 0, 0, null);
        }
    }

    // 남은 생명에 따라 이미지 개수가 달라짐
    // 최대 3, 최소 0 (게임 오버)
    public void lifeDraw(Graphics g) {
        // NullPointerException 오류를 제거하기 위해 Leppa 열매 이미지를 자체적으로 불러옴
        Image leppa = new ImageIcon("src/image/Leppa.png").getImage();
        if (life > 0) {
            g.drawImage(leppa, 10, 35, null);
        }
        if (life > 1) {
            g.drawImage(leppa, 40, 35, null);
        }
        if (life > 2) {
            g.drawImage(leppa, 70, 35, null);
        }
    }

    // key가 눌리는 것에 따라 player를 움직임
    private void KeyProcess() {
        int playerSpeed = 10;
        if (left && playerX - playerSpeed > 0) {
            playerX -= playerSpeed;
        }
        if (right && playerX + playerWidth + playerSpeed < Main.SCREEN_WIDTH) {
            playerX += playerSpeed;
        }
    }

    public void setLeft(boolean left) {this.left = left;}
    public void setRight(boolean right) {this.right = right;}
    public void setLeftImage(boolean leftImage) {this.leftImage = leftImage;}
    public void setRightImage(boolean rightImage) {this.rightImage = rightImage;}
    public void setStop(boolean Stop) { this.Stop = Stop; }

    public boolean isOver() { return isOver; }
    public boolean Stop() { return Stop; }
}
