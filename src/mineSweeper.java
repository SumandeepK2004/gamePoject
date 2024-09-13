import java.awt.*; //it's a library code for importing abstract window toolkit
import java.awt.event.*; //it's a library code for listen the actions of user in the GUI
import java.util.ArrayList; // it is used to store all the tiles with the mines
import java.util.Random; // library code for random data (int,double,float.etc....) (in this code it is used to place mines in random tiles)
import javax.swing.*; // library code for using jframe,jpanel,jbutton.... etc


public class mineSweeper {



    public class MineTile extends JButton // creating button for the game

    {
        // These are like memory slots to remember which row and column this button is in
        int r; // Row
        int c; // Column

        // Constructor for MineTile class, takes row and column
        public MineTile(int r, int c) {
            // Now we're telling the MineTile to remember the row and column
            this.r = r; // To remember the row
            this.c = c; // To remember the column
        }
    }

    // specifying varibles
    int tileSize = 70; // specifying that each tile will be 70 px
    int numRows = 10;  // number of rows set to 10
    int numCols = numRows;
    int boardWidth = numCols * tileSize; // specifying that the no.of board width
    int boardHeight = numRows * tileSize; // specifying that the no.of board height

    JFrame frame = new JFrame("Mine Sweeper");  // creating the title of the window mineSweeper

    // JLabel is a component used to display text or an image on a graphical user interface (GUI)
    JLabel textLabel = new JLabel(); //This line declares a new JLabel object named textLabel. This label will be used to display text in the user interface

    JPanel textPanel = new JPanel(); // container to hold the buttons
    JPanel boardPanel = new JPanel(); // to hold the game board for the Mine Sweeper game

    int mineCount = 20; // the no.of mines in the game
    MineTile[][] board = new MineTile[numRows][numCols]; // creating 2d array of mine tiles
    ArrayList<MineTile> mineList;
    // Declare an ArrayList named mineList to store MineTile objects
    Random random = new Random(); // creating random class for generate random numbers

    int tilesClicked = 0; //goal is to click all tiles except the ones containing mines
    boolean gameOver = false;


    // creating constructor mineSweeper
    mineSweeper() {
        frame.setSize(boardWidth, boardHeight); // setting up the board-width and height
        frame.setLocationRelativeTo(null); // this code is used to open thw window of the game ath the centre of the screen
        frame.setResizable(false); // setting up the window as non-resizable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when we press on the X button on the window it will terminate the program
        frame.setLayout(new BorderLayout()); //This line sets the layout manager for the frame to be a BorderLayout

        textLabel.setFont(new Font("Arial", Font.BOLD, 25)); // setting up the font and style for text-label
        textLabel.setHorizontalAlignment(JLabel.CENTER); // to center the text
        textLabel.setText("Mines no: " + Integer.toString(mineCount)); // printing the number of mines to the user
        //Integer.toString() is a method used to convert an integer value to its corresponding string representation

        textLabel.setOpaque(true); //This line sets the opaque property of the textLabel to true

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH); // Add textPanel to the frame top position

        boardPanel.setLayout(new GridLayout(numRows, numCols)); // creating the grid layout 10x10

        frame.add(boardPanel); // add board-panel to the frame

        // for statement for fill the grid with mine tiles
        for (int r = 0; r < numRows; r++) { // in rows
            for (int c = 0; c < numCols; c++) { // in columns
                MineTile tile = new MineTile(r, c); // creating mine tile object
                board[r][c] = tile; //  newly created MineTile object is then assigned to the corresponding position in the board array

                // properties for the tile
                tile.setFocusable(false); // This line is used to make the tile component un-focusable
                tile.setMargin(new Insets(0, 0, 0, 0)); // setting margin of tile
                tile.setFont(new Font("Arial Unicode MS", Font.BOLD, 45)); // font properties, unicode MS enable us to use emojies


                tile.addMouseListener(new MouseAdapter() {  // adding listener to take values from mouse clicks
                    // It allows us to perform actions when the mouse is pressed on the tile.
                    @Override
                    public void mousePressed(MouseEvent e) { //This method is called when the mouse button is pressed on the tile
                        if (gameOver) {
                            return;
                        }
                        MineTile tile = (MineTile) e.getSource(); // declares the varibale tiles and assigns it the result of MouseEvent to MineTile object

                        //left click for opening the tile
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (tile.getText() == "") {
                                // Check if the tile is a mine
                                if (mineList.contains(tile)) {
                                    revealMines(); // game-over revealing all the mines
                                }
                                else {
                                    // If the tile is not a mine, check adjacent tiles for mines
                                    checkMine(tile.r, tile.c);
                                }
                            }
                        }
                        //right click for setting the flag
                        else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (tile.getText() == "" && tile.isEnabled()) {
                                tile.setText("🚩");
                            }
                            else if (tile.getText() == "🚩") {
                                tile.setText("");
                            }
                        }
                    }
                });

                boardPanel.add(tile);

            }
        }

        frame.setVisible(true); // setting the frame visible

        setMines(); // to initialize the game board by randomly placing mines on it
    }

    void setMines() {
        mineList = new ArrayList<MineTile>(); // creating array list for mine tiles


        int mineLeft = mineCount;
        while (mineLeft > 0) {
            int r = random.nextInt(numRows); //0-7
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];
            if (!mineList.contains(tile)) {
                mineList.add(tile);
                mineLeft -= 1;
            }
        }
    }

    void revealMines() { // to reveal all the mines on the game board when a mine is clicked on by the player
        for (int i = 0; i < mineList.size(); i++) {
            MineTile tile = mineList.get(i);
            tile.setText("💥");  // Set the text of the MineTile to a blast emoji 💥
        }

        gameOver = true; // boolean for game-over
        textLabel.setText("Game Over!");
    }

    void checkMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return;
        }

        MineTile tile = board[r][c];
        if (!tile.isEnabled()) {
            return;
        }
        tile.setEnabled(false);
        tilesClicked += 1;

        int minesFound = 0;

        //top 3
        minesFound += countMine(r-1, c-1);  //top left
        minesFound += countMine(r-1, c);    //top
        minesFound += countMine(r-1, c+1);  //top right

        //left and right
        minesFound += countMine(r, c-1);    //left
        minesFound += countMine(r, c+1);    //right

        //bottom 3
        minesFound += countMine(r+1, c-1);  //bottom left
        minesFound += countMine(r+1, c);    //bottom
        minesFound += countMine(r+1, c+1);  //bottom right

        if (minesFound > 0) {
            tile.setText(Integer.toString(minesFound));
        }
        else {
            tile.setText("");

            //top 3
            checkMine(r-1, c-1);    //top left
            checkMine(r-1, c);      //top
            checkMine(r-1, c+1);    //top right

            //left and right
            checkMine(r, c-1);      //left
            checkMine(r, c+1);      //right

            //bottom 3
            checkMine(r+1, c-1);    //bottom left
            checkMine(r+1, c);      //bottom
            checkMine(r+1, c+1);    //bottom right
        }

        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true;
            textLabel.setText("Mines Cleared!");
        }

    }

    int countMine(int r, int c) { // This method counts the number of mines adjacent to a given tile located at row 'r' and column 'c'
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {  // Check if the given tile is out of bounds

            // If the tile is out of bounds, return 0 (indicating no adjacent mines)
            return 0;
        }

        // Check if the given tile contains a mine
        if (mineList.contains(board[r][c])) {

            // If the tile contains a mine, return 1 (indicating one adjacent mine)
            return 1;
        }

        // If the given tile is within bounds and does not contain a mine, return 0
        return 0;
    }
}


