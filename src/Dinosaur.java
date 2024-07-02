import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Dinosaur extends JPanel implements ActionListener, KeyListener {
    int boardHeight = 200;
    int boardWidth = 1200;
    ImageIcon dinosaurImgIcon;
    ImageIcon birdImgIcon;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;
    Image gameOverImg;

    class Block {
        int x;
        int y;
        int width;
        int height;
        ImageIcon imgIcon;
        Image img;

        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }

        Block(int x, int y, int width, int height, ImageIcon imgIcon) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.imgIcon = imgIcon;
        }
    }

    // DINOSAUR
    int dinosaurWidth = 90;
    int dinosaurHeight = 80;
    int dinosaurX = 50;
    int dinosaurY = boardHeight - dinosaurHeight - 20;

    // Cactuses 1, 2, 3
    int cactus1width = 34;
    int cactus2width = 69;
    int cactus3width = 102;
    int cactusHeight = 70;
    int cactusX = boardWidth;
    int cactusY = boardHeight - cactusHeight;
    ArrayList<Block> cactusArray;

    Block dinosaur;

    Timer gameLoop;
    Timer placeCactustimer;
    Timer scoreTimer;

    // PHYSICS
    int velocityY = 0; // vertically up and down
    int gravity = 1;
    int cactusSpeed = 5;

    boolean isGameOver = false;
    int score = 0;

    public Dinosaur() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(new Color(137,240,204));
        setFocusable(true);
        addKeyListener(this);

        dinosaurImgIcon = loadGif("dino-run.gif");
        birdImgIcon      = loadGif("bird.gif");
        dinosaurDeadImg = loadImage("dino-dead.png", dinosaurWidth, dinosaurHeight);
        dinosaurJumpImg = loadImage("dino-jump.png", dinosaurWidth, dinosaurHeight);
        cactus1Img = loadImage("cactus1.png", cactus1width, cactusHeight);
        cactus2Img = loadImage("cactus2.png", cactus2width, cactusHeight);
        cactus3Img = loadImage("cactus3.png", cactus3width, cactusHeight);
        gameOverImg = loadImage("game-over.png", 200, 200);

        // dinosaur
        dinosaur = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurImgIcon);

        cactusArray = new ArrayList<>();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        placeCactustimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCactus();
            }
        });
        placeCactustimer.start();

        scoreTimer = new Timer(50, new ActionListener() { // Increment score every half second
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    score++;
                }
            }
        });
        scoreTimer.start();
    }

    protected void placeCactus() {
        double placeCactusChance = Math.random();
        double placebird= Math.random();
        if (placeCactusChance > 0.90) {
            Block cactus = new Block(cactusX, cactusY, cactus3width, cactusHeight, cactus3Img);
            cactusArray.add(cactus);
        } else if (placeCactusChance > 0.70) {
            Block cactus = new Block(cactusX, cactusY, cactus2width, cactusHeight, cactus2Img);
            cactusArray.add(cactus);
        } else if (placeCactusChance > 0.50) {
            Block cactus = new Block(cactusX, cactusY, cactus1width, cactusHeight, cactus1Img);
            cactusArray.add(cactus);
        }
    }

    private Image loadImage(String path, int width, int height) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            return originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private ImageIcon loadGif(String path) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (dinosaur.imgIcon != null) {
            dinosaur.imgIcon.paintIcon(this, g, dinosaur.x, dinosaur.y);
        } else if (dinosaur.img != null) {
            g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, this);
        }

        for (int i = 0; i < cactusArray.size(); i++) {
            Block cactus = cactusArray.get(i);
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }

        if (isGameOver) {
            g.drawImage(gameOverImg, (boardWidth - gameOverImg.getWidth(this)) / 2, (boardHeight - gameOverImg.getHeight(this)) / 2, this);
        }

        // Draw score on the top right corner
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, boardWidth - 150, 30);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dinosaur Game");
        Dinosaur dinosaurPanel = new Dinosaur();
        frame.add(dinosaurPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void move() {
        if (isGameOver) {
            return;
        }

        // DINOSAUR
        velocityY += gravity;
        dinosaur.y += velocityY;
        if (dinosaur.y > dinosaurY) {
            dinosaur.y = dinosaurY;
            velocityY = 0;
            dinosaur.imgIcon = dinosaurImgIcon; // Set the running GIF when on the ground
            dinosaur.img = null;
        }

        // CACTUS
        for (int i = 0; i < cactusArray.size(); i++) {
            Block cactus = cactusArray.get(i);
            cactus.x -= cactusSpeed; // Move cactuses to the left
        }

        // Remove off-screen cactuses
        cactusArray.removeIf(cactus -> cactus.x + cactus.width < 0);

        // Check for collisions
        checkCollision();
    }

    private void checkCollision() {
        for (Block cactus : cactusArray) {
            Rectangle dinoRect = new Rectangle(dinosaur.x, dinosaur.y, dinosaur.width - 20, dinosaur.height - 20);
            Rectangle cactusRect = new Rectangle(cactus.x, cactus.y, cactus.width, cactus.height);
            if (dinoRect.intersects(cactusRect)) {
                gameOver();
                break;
            }
        }
    }

    private void gameOver() {
        isGameOver = true;
        gameLoop.stop();
        placeCactustimer.stop();
        scoreTimer.stop(); // Stop the score timer
        dinosaur.imgIcon = null;
        dinosaur.img = dinosaurDeadImg;
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint(); // this will call paintComponent method
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // Jump only if the dinosaur is on the ground
            if (dinosaur.y == dinosaurY && !isGameOver) {
                velocityY = -20;
                dinosaur.img = dinosaurJumpImg; // Set the jumping image when jumping
                dinosaur.imgIcon = null;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }
}
