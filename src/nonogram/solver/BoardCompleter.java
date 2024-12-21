package nonogram.solver;

import nonogram.generator.Board;
import nonogram.generator.Clue;
import nonogram.generator.Square;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class BoardCompleter {

    /**
     * The board on which the Nonogram puzzle is being solved.
     */
    private final Board board;

    /**
     * The list of horizontal clues for each row of the Nonogram.
     */
    private final ArrayList<Clue> horizClues;

    /**
     * The list of vertical clues for each column of the Nonogram.
     */
    private final ArrayList<Clue> vertClues;

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

        int loop = 0;
        while(loop < 2) {
            //for all rows
            for (int i = 0; i < dimensions[0]; i++) {
                ArrayList<Square> row = board.getRow(i);
                drawNewStates(frame, delay, i, row, horizClues);
            }

            //for all columns
            for (int i = 0; i < dimensions[1]; i++) {
                ArrayList<Square> column = board.getColumn(i);
                drawNewStates(frame, delay, i, column, vertClues);
            }
            loop++;
        }
    }

    private void drawNewStates(JFrame frame, int delay, int clueIndex, ArrayList<Square> line, ArrayList<Clue> clues) throws InterruptedException {
        LineSolver solver = new LineSolver();
        HashMap<Integer, Boolean> newSquareStates = solver.getNewSquareStates(line, clues.get(clueIndex));
        for (int squareIndex : newSquareStates.keySet()) {
            if (newSquareStates.get(squareIndex)) {
                line.get(squareIndex).fill();
            }
            else {
                line.get(squareIndex).cross();
            }
            slowProgression(frame, delay);
        }
    }
}
