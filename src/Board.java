import java.awt.*;

public class Board {
    // Define constants for Connect-Four grid dimensions
    public static final int ROWS = 6;  // 6 rows
    public static final int COLS = 7;  // 7 columns

    // Define constants for drawing
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // Canvas width
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS; // Canvas height
    public static final int GRID_WIDTH = 8;  // Grid line width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Half-width for alignment
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // Grid color
    public static final int Y_OFFSET = 1;  // Fine-tune grid alignment

    // 2D array of cells representing the board
    Cell[][] cells;

    /** Constructor to initialize the board */
    public Board() {
        initGame();
    }

    /** Initialize the game board */
    public void initGame() {
        cells = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    /** Reset the board for a new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame();
            }
        }
    }

    /**
     * Handles a player's move. The piece "drops" to the lowest empty cell in the selected column.
     * @param player The current player (CROSS or NOUGHT).
     * @param selectedCol The column where the player wants to drop their piece.
     * @return The new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed player, int selectedCol) {
        // Drop the piece in the lowest available cell in the column
        for (int row = ROWS - 1; row >= 0; --row) {
            if (cells[row][selectedCol].content == Seed.NO_SEED) {
                cells[row][selectedCol].content = player;

                // Check for a win after the move
                if (hasWon(player, row, selectedCol)) {
                    return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
                }

                // Check for a draw (all cells filled)
                boolean isDraw = true;
                for (int r = 0; r < ROWS; ++r) {
                    for (int c = 0; c < COLS; ++c) {
                        if (cells[r][c].content == Seed.NO_SEED) {
                            isDraw = false;
                            break;
                        }
                    }
                }
                return isDraw ? State.DRAW : State.PLAYING;
            }
        }
        return State.PLAYING; // Column is full, should not happen if input is validated
    }

    /** Check if the player has won after their move */
    private boolean hasWon(Seed player, int row, int col) {
        return checkDirection(player, row, col, 1, 0)   // Horizontal
                || checkDirection(player, row, col, 0, 1)   // Vertical
                || checkDirection(player, row, col, 1, 1)   // Diagonal (\)
                || checkDirection(player, row, col, 1, -1); // Diagonal (/)
    }

    /**
     * Check a specific direction for 4 consecutive pieces.
     * @param player The current player.
     * @param row The starting row.
     * @param col The starting column.
     * @param deltaRow The row increment (e.g., 1 for down, 0 for horizontal).
     * @param deltaCol The column increment (e.g., 1 for right, -1 for left).
     * @return True if the player has 4 in a row in the given direction.
     */
    private boolean checkDirection(Seed player, int row, int col, int deltaRow, int deltaCol) {
        int count = 0;

        // Count pieces in the positive direction
        for (int i = 0; i < 4; ++i) {
            int r = row + i * deltaRow;
            int c = col + i * deltaCol;
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS && cells[r][c].content == player) {
                count++;
            } else {
                break;
            }
        }

        // Count pieces in the negative direction
        for (int i = 1; i < 4; ++i) {
            int r = row - i * deltaRow;
            int c = col - i * deltaCol;
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS && cells[r][c].content == player) {
                count++;
            } else {
                break;
            }
        }

        return count >= 4; // Win condition
    }

    /** Paint the board */
    public void paint(Graphics g) {
        // Draw grid lines
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }

        // Draw cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);
            }
        }
    }
}