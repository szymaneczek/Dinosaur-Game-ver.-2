import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;


public class DinosaurGame extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 750;
    int boardHeight = 300;

    //images
    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image tree1Img;
    Image tree2Img;
    Image tree3Img;
    Image tree4Img;
    Image cloudImg;

    Image backgroundImg;

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image img;

        Block(int x, int y, int width, int height, Image img){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    //dinosaur
    int dinosaurWidth = 120;
    int dinosaurHeight = 130;
    int dinosaurX = 50;
    int dinosaurY = boardHeight - dinosaurHeight;

    Block dinosaur;

    //trees
    int tree1Width = 69;
    int tree2Width = 78;
    int tree3Width = 66;
    int tree4Width = 73;

    int treeHeight = 95;
    int treeX = 700;
    int treeY = boardHeight - treeHeight;
    ArrayList<Block> treeArray;

    // physics
    int velocityX = -12; // tree moving left speed
    int velocityY = 0; //dinosaur jump speed
    int gravity = 1;

    boolean gameOver = false;
    int score=0;

    Timer gameLoop;
    Timer placeTreeTimer;


    public DinosaurGame (){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.LIGHT_GRAY);
        setFocusable(true);
        addKeyListener(this);

        dinosaurImg = new ImageIcon(getClass().getResource("./img/dino-run.gif")).getImage();
        dinosaurDeadImg = new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        dinosaurJumpImg = new ImageIcon(getClass().getResource("./img/dino-jump.png")).getImage();
        tree1Img = new ImageIcon(getClass().getResource("./img/tree1.png")).getImage();
        tree2Img = new ImageIcon(getClass().getResource("./img/tree2.png")).getImage();
        tree3Img = new ImageIcon(getClass().getResource("./img/tree3.png")).getImage();
        tree4Img = new ImageIcon(getClass().getResource("./img/tree4.png")).getImage();
        cloudImg = new ImageIcon(getClass().getResource("./img/cloud.png")).getImage();
        backgroundImg = new ImageIcon(getClass().getResource("./img/dinobackground.png")).getImage();

        //dinosaur
        dinosaur = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurImg);

        //tree
        treeArray = new ArrayList<Block>();

        //game timer
        gameLoop = new Timer(1000/60,this);
        gameLoop.start();


        // place tree timer
        placeTreeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeTree();
            }
        });
        placeTreeTimer.start();

    }

    void placeTree() {
        if (gameOver){
            return;
        }

        double placeTreeChance = Math.random();
        if (placeTreeChance > .90) {
            Block tree = new Block(treeX, treeY, tree4Width, treeHeight, tree4Img);
            treeArray.add(tree);
        } else if (placeTreeChance > .70) {
            Block tree = new Block(treeX, treeY, tree3Width, treeHeight, tree3Img);
            treeArray.add(tree);
        } else if (placeTreeChance > .50) {
            Block tree = new Block(treeX, treeY, tree2Width, treeHeight, tree2Img);
            treeArray.add(tree);
        } else if (placeTreeChance > .30) {
            Block tree = new Block(treeX, treeY, tree1Width, treeHeight, tree1Img);
            treeArray.add(tree);
        }

        if (treeArray.size() > 10){
            treeArray.remove(0);
        }
    }

       public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        // dinosaur
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null);

        // tree
        for (int i=0; i < treeArray.size(); i++){
            Block tree = treeArray.get(i);
            g.drawImage(tree.img, tree.x, tree.y, tree.width, tree.height, null);
        }

        // score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Courier", Font.PLAIN, 32));
        if (gameOver){
            g.drawString("Game Over: " + String.valueOf(score), 10, 35);
        }
        else {
            g.drawString(String.valueOf(score), 10, 35);
        }

    }

    public void move(){
        //dinosaur
        velocityY += gravity;
        dinosaur.y += velocityY;

        if (dinosaur.y > dinosaurY){ //stop the dinosaur from falling past the ground
            dinosaur.y = dinosaurY;
            velocityY = 0;
            dinosaur.img = dinosaurImg;
        }

        //tree
        for (int i = 0; i <treeArray.size(); i++){
            Block tree = treeArray.get(i);
            tree.x += velocityX;

            if (collision(dinosaur, tree)){
                gameOver = true;
                dinosaur.img = dinosaurDeadImg;
            }
        }
        //score
        score++;
    }

    boolean collision (Block a, Block b){
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }
    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if (gameOver){
            placeTreeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e){
    }

    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (dinosaur.y == dinosaurY) {
                velocityY = -20;
                dinosaur.img = dinosaurJumpImg;
            }

            if (gameOver){
                dinosaur.y = dinosaurY;
                dinosaur.img = dinosaurImg;
                velocityY = 0;
                treeArray.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placeTreeTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e){
    }
}
