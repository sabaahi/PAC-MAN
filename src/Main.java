import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) throws Exception {
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        JFrame frame = new JFrame("Pac Man Created by Muhammad Akbar");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Game pacmanGame = new Game();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);

    }
}
