import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, KeyListener {
    class Block {
        int x, y, width, height, startX, startY, velocityX, velocityY;
        Image image;
        char direction = 'U';

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction) {
            try {
                char prevDirection = this.direction;
                this.direction = direction;
                updateVelocity();
                this.x += this.velocityX;
                this.y += this.velocityY;
                for (Block wall : walls) {
                    if (collision(this, wall)) {
                        this.x -= this.velocityX;
                        this.y -= this.velocityY;
                        this.direction = prevDirection;
                        updateVelocity();
                    }
                }
            } catch (Exception e) {
                reset();
            }
        }

        void updateVelocity() {
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -tileSize / 4;
            } else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = tileSize / 4;
            } else if (this.direction == 'L') {
                this.velocityX = -tileSize / 4;
                this.velocityY = 0;
            } else if (this.direction == 'R') {
                this.velocityX = tileSize / 4;
                this.velocityY = 0;
            }
        }

        void reset() {
            this.x = this.startX;
            this.y = this.startY;
        }
    }

    private int rowCount = 21, columnCount = 19, tileSize = 32, boardWidth = columnCount * tileSize, boardHeight = rowCount * tileSize;
    private Image wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, pacmanUpImage, pacmanDownImage, pacmanLeftImage, pacmanRightImage;
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };
    HashSet<Block> walls, foods, ghosts;
    Block pacman;
    Timer gameLoop;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0, lives = 3;
    boolean gameOver = false;

    Game() {
        try {
            setPreferredSize(new Dimension(boardWidth, boardHeight));
            setBackground(Color.BLACK);
            addKeyListener(this);
            setFocusable(true);
            wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
            blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
            orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
            pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
            redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
            pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
            pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
            pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
            pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();
            loadMap();
            for (Block ghost : ghosts) {
                ghost.updateDirection(directions[random.nextInt(4)]);
            }
            gameLoop = new Timer(50, this);
            gameLoop.start();
        } catch (Exception e) {
            gameOver = true;
        }
    }

    public void loadMap() {
        try {
            walls = new HashSet<>();
            foods = new HashSet<>();
            ghosts = new HashSet<>();
            for (int r = 0; r < rowCount; r++) {
                for (int c = 0; c < columnCount; c++) {
                    char tile = tileMap[r].charAt(c);
                    int x = c * tileSize, y = r * tileSize;
                    if (tile == 'X') walls.add(new Block(wallImage, x, y, tileSize, tileSize));
                    else if (tile == 'b') ghosts.add(new Block(blueGhostImage, x, y, tileSize, tileSize));
                    else if (tile == 'o') ghosts.add(new Block(orangeGhostImage, x, y, tileSize, tileSize));
                    else if (tile == 'p') ghosts.add(new Block(pinkGhostImage, x, y, tileSize, tileSize));
                    else if (tile == 'r') ghosts.add(new Block(redGhostImage, x, y, tileSize, tileSize));
                    else if (tile == 'P') pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                    else if (tile == ' ') foods.add(new Block(null, x + 14, y + 14, 4, 4));
                }
            }
        } catch (Exception e) {
            gameOver = true;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        try {
            g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);
            for (Block ghost : ghosts) g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
            for (Block wall : walls) g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
            g.setColor(Color.WHITE);
            for (Block food : foods) g.fillRect(food.x, food.y, food.width, food.height);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString(gameOver ? "Game Over: " + score : "x" + lives + " Score: " + score, tileSize / 2, tileSize / 2);
        } catch (Exception e) {
            gameOver = true;
        }
    }

    public void move() {
        try {
            pacman.x += pacman.velocityX;
            pacman.y += pacman.velocityY;
    
            for (Block wall : walls) {
                if (collision(pacman, wall)) {
                    pacman.x -= pacman.velocityX;
                    pacman.y -= pacman.velocityY;
                    break;
                }
            }
    
            for (Block ghost : ghosts) {
                if (collision(ghost, pacman)) {
                    lives--;
                    if (lives == 0) {
                        gameOver = true;
                        return;
                    }
                    resetPositions();
                }
    
                ghost.x += ghost.velocityX;
                ghost.y += ghost.velocityY;
    
                if (ghost.x < 0) {
                    ghost.x = 0;
                    ghost.updateDirection(directions[random.nextInt(4)]);
                } else if (ghost.x + ghost.width > boardWidth) {
                    ghost.x = boardWidth - ghost.width;
                    ghost.updateDirection(directions[random.nextInt(4)]);
                }
    
                if (ghost.y < 0) {
                    ghost.y = 0;
                    ghost.updateDirection(directions[random.nextInt(4)]);
                } else if (ghost.y + ghost.height > boardHeight) {
                    ghost.y = boardHeight - ghost.height;
                    ghost.updateDirection(directions[random.nextInt(4)]);
                }
    
                for (Block wall : walls) {
                    if (collision(ghost, wall)) {
                        ghost.x -= ghost.velocityX;
                        ghost.y -= ghost.velocityY;
                        ghost.updateDirection(directions[random.nextInt(4)]);
                    }
                }
            }
    
            Block foodEaten = null;
            for (Block food : foods) {
                if (collision(pacman, food)) {
                    foodEaten = food;
                    score += 10;
                }
            }
            foods.remove(foodEaten);
    
            if (foods.isEmpty()) {
                loadMap();
                resetPositions();
            }
    
        } catch (Exception e) {
            gameOver = true;
        }
    }
    
    public boolean collision(Block a, Block b) {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    public void resetPositions() {
        pacman.reset();
        for (Block ghost : ghosts) ghost.reset();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            move();
            repaint();
            if (gameOver) gameLoop.stop();
        } catch (Exception ex) {
            gameOver = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }
    System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }

        if (pacman.direction == 'U') {
            pacman.image = pacmanUpImage;
        }
        else if (pacman.direction == 'D') {
            pacman.image = pacmanDownImage;
        }
        else if (pacman.direction == 'L') {
            pacman.image = pacmanLeftImage;
        }
        else if (pacman.direction == 'R') {
            pacman.image = pacmanRightImage;
        }
    }
}
