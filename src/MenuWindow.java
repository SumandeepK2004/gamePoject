import javax.swing.*; // library code for using jframe,jpanel,jbutton.... etc


// Defining the MenuWindow class which extends JFrame
public class MenuWindow extends JFrame {
    private JButton startButton; // Declaring a private JButton variable named startButton

    // Constructor for the MenuWindow class
    public MenuWindow() {

        // Setting the title of the window
        setTitle("Minesweeper Menu");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centering the window on the screen

        JPanel panel = new JPanel();   // Creating a JPanel to hold GUI components


        startButton = new JButton("Start Game"); // start game button
        startButton.addActionListener(e -> startGame());
        panel.add(startButton);

        add(panel);

        setVisible(true);
    }

    // Method to start the game
    public void startGame() {
        dispose(); // Close the menu window
        new mineSweeper(); // Start the game
    }

}
