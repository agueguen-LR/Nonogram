package nonogram.solver;

import nonogram.generator.Clue;
import nonogram.generator.Square;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * LineSolver does as much as it can on a single line of the nonogram.
 *
 * @author agueguen-LR
 */
public class LineSolver extends LineSolverUtilities{

    private ArrayList<Integer> lineState;
    private Clue clue;

    public LineSolver() {}

    public void setLine(ArrayList<Square> line, Clue clue) {
        this.lineState = getLineState(line);
        this.clue = clue;
    }

    public HashMap<Integer, Boolean> getNewSquareStates() {
        HashMap<Integer, Boolean> newSquareStates = new HashMap<>();
        ArrayList<Integer> newFilledSquares = FillEmptySquares();
        for(Integer squareIndex : newFilledSquares){
            newSquareStates.put(squareIndex, true);
        }
        return newSquareStates;
    }

    /**
     * Returns the indexes of the squares that should be filled between each side.
     * Can contain squares already filled.
     * @return the indexes of the squares that should be filled
     */
    public ArrayList<Integer> FillEmptySquares(){
        ArrayList<Integer> FilledSquares = new ArrayList<>();
        int incompleteLength = lengthOfInterCrossSection(lineState);

        //line is already complete
        if(incompleteLength == 0){
            return FilledSquares;
        }

        int stateIndex = nextEmptyIndex(0, lineState);

        int clueLength = activeClueLength(clue, lineState);
        int notCompletableLength = incompleteLength - clueLength;
        int alreadyFilled = 0;

        if (stateIndex > 0){
            if (lineState.get(stateIndex-1) > 0){
                alreadyFilled = lineState.get(stateIndex-1);
            }
        }

        int squareIndex = stateIndexToSquareIndex(stateIndex, lineState) - alreadyFilled;

        if (clueLength > incompleteLength/2){
            ArrayList<Integer> activeClues = removeCompletedClues(clue, lineState);
            for (Integer clueValue : activeClues){
                if (clueValue > notCompletableLength){
                    for(int i = 0; i < clueValue-notCompletableLength; i++){
                        FilledSquares.add(squareIndex + notCompletableLength + i);
                    }
                }
                squareIndex += clueValue + 1;
            }
        }
        return FilledSquares;
    }
}
