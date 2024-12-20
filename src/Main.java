import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JPanel {
    private static final long serialVersionUID = 1L;
    private JButton undoButton;

    // Define colors, fonts, etc.
    public static final String TITLE = "Connect-Four";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;
    private GameMode currentMode = GameMode.PLAYER_VS_PLAYER;  // Only Player vs Player mode is available now
    private Seed playerSeed;

    public Main() {
        super.setLayout(new BorderLayout());

        // Create the Undo button
        undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> {
            board.undoMove();  // Undo the last move
            repaint();         // Repaint the board after undo
        });

        // Add the undo button to the bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(undoButton);
        super.add(bottomPanel, BorderLayout.PAGE_END);

        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = board.stepGame(currentPlayer, col); // Pass only the column
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;

                        if (currentState == State.PLAYING) {
                            SoundEffect.EAT_FOOD.play();
                        } else {
                            SoundEffect.DIE.play();
                        }
                    }
                } else {
                    newGame();
                }
                repaint();
            }
        });

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        initGame();
        newGame();
        super.setFocusable(true);  // Make sure the panel can listen to key events
    }

    public void initGame() {
        board = new Board();
    }

    public void newGame() {
        chooseGameMode();  // Only Player vs Player mode will be available
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
        playerSeed = Seed.CROSS;
        currentState = State.PLAYING;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);

        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'X' Won! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click to play again.");
        }
    }

    public void chooseGameMode() {
        String[] options = {"Player vs Player"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose Game Mode:",
                "Game Mode",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        // Only Player vs Player mode will be available
        if (choice == 0) {
            currentMode = GameMode.PLAYER_VS_PLAYER;
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
                frame.setContentPane(new Main());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}