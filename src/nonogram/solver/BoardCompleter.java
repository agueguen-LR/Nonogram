package nonogram.solver;

import nonogram.generator.Board;
import nonogram.generator.Clue;
import nonogram.generator.Square;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class BoardCompleter {

    /**
     * The board on which the Nonogram puzzle is being solved.
     */
    private Board board;

    /**
     * The list of horizontal clues for each row of the Nonogram.
     */
    private ArrayList<Clue> horizClues = new ArrayList<>();

    /**
     * The list of vertical clues for each column of the Nonogram.
     */
    private ArrayList<Clue> vertClues = new ArrayList<>();

    /**
     * Constructs a new {@code Solver} instance with the specified board and
     * initializes the horizontal and vertical clues.
     *
     * @param board the {@code Board} object representing the Nonogram puzzle to be solved
     */
    public BoardCompleter(Board board) {
        this.board = board;
        this.horizClues = board.getHorizontalClues();
        this.vertClues = board.getVerticalClues();
    }

    public void BeginSolving(int speed) throws InterruptedException{
        var frame = new JFrame("Nonogram Board");
        SwingUtilities.invokeLater(() -> {
            board.setBackground(Color.BLUE.darker());
            frame.setSize(1200, 1000);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.getContentPane().add(board, BorderLayout.CENTER);
            frame.setVisible(true);
        });
        fillBoard(frame, speed);
    }

    /**
     * Slows down the advancement of the solver by the specified delay.
     * This method also triggers the revalidation and repainting of the JFrame.
     *
     * @param frame the JFrame displaying the Nonogram board
     * @param delay the delay in milliseconds between each step
     * @throws InterruptedException if the thread is interrupted during the delay
     */
    private void slowProgression(JFrame frame, int delay) throws InterruptedException{
        Thread.sleep(delay);
        SwingUtilities.invokeLater(() -> {
            frame.revalidate();
            frame.repaint();
        });
    }

    public void fillBoard(JFrame frame, int delay) throws InterruptedException {
        int[] dimensions = board.getDimensions();
        LineSolver solver = new LineSolver();

        for (int i = 0; i < dimensions[0]; i++) {
            ArrayList<Square> row = board.getRow(i);
            solver.setLine(row, horizClues.get(i));
            ArrayList<Integer> squaresToFill = solver.FillEmptySquares();
            for (int squareIndex : squaresToFill) {
                row.get(squareIndex).fill();
                slowProgression(frame, delay);
            }
        }

        for (int i = 0; i < dimensions[1]; i++) {
            ArrayList<Square> column = board.getColumn(i);
            solver.setLine(column, vertClues.get(i));
            ArrayList<Integer> squaresToFill = solver.FillEmptySquares();
            for (int squareIndex : squaresToFill) {
                column.get(squareIndex).fill();
                slowProgression(frame, delay);
            }
        }
    }
}
