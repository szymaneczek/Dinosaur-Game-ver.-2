import javax.swing.*;


public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 750;
        int boardHeight = 300;

         JFrame frame = new JFrame("Dinosaur Game ver.2 ");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DinosaurGame dinosaurGame = new DinosaurGame();
        frame.add(dinosaurGame);
        frame.pack();
        dinosaurGame.requestFocus();
        frame.setVisible(true);
    }
}
