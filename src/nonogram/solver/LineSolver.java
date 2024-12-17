package nonogram.solver;

import nonogram.generator.Clue;
import nonogram.generator.Square;

import java.util.ArrayList;
import java.util.Dictionary;

/**
 * LineSolver does as much as it can on a single line of the nonogram.
 *
 * @author agueguen-LR
 */
public class LineSolver {

    private LineSolverUtilities utilities = new LineSolverUtilities();
    private ArrayList<Square> line;
    private ArrayList<Integer> lineState;
    private ArrayList<Clue> clues;

    public LineSolver() {}

    public void setLine(ArrayList<Square> line, ArrayList<Clue> clues) {
        this.line = line;
        this.lineState = utilities.getLineState(this.line);
        this.clues = clues;
    }

    public Dictionary<Integer, Boolean> getNewSquareStates() {
        return null;
    }




}
