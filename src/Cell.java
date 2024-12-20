/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #2
 * 1 - 5026221156 - Muhammad Ali Husain Ridwan
 * 2 - 5026221157 - Muhammad Afaf
 * 3 - 5026221162 - Raphael Andhika Pratama
 */
import java.awt.*;

/**
 * The Cell class models each individual cell of the game board.
 */
public class Cell {
    // Define named constants for drawing
    public static final int SIZE = 120; // cell width/height (square)
    public static final int PADDING = SIZE / 5; // Padding for the disc inside the cell
    public static final int SEED_SIZE = SIZE - PADDING * 2; // Size of the disc

    // Define properties (package-visible)
    /**
     * Content of this cell (Seed.EMPTY, Seed.CROSS, or Seed.NOUGHT)
     */
    Seed content;
    /**
     * Row and column of this cell
     */
    int row, col;

    /**
     * Constructor to initialize this cell with the specified row and col
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED; // Default content
    }

    /**
     * Reset this cell's content to EMPTY, ready for a new game
     */
    public void newGame() {
        content = Seed.NO_SEED;
    }

    /**
     * Paint itself on the graphics canvas, given the Graphics context
     */
    public void paint(Graphics g) {
        // Calculate the top-left corner of the cell
        int x = col * SIZE;
        int y = row * SIZE;

        // Set the color based on the content
        if (content == Seed.CROSS) { // Red disc for CROSS
            g.setColor(Color.RED);
        } else if (content == Seed.NOUGHT) { // Yellow disc for NOUGHT
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.WHITE); // Empty cell
        }

        // Draw the disc inside the cell with padding
        g.fillOval(x + PADDING, y + PADDING, SEED_SIZE, SEED_SIZE);
    }
}