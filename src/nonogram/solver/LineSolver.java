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

    public LineSolver() {}

    /**
     * Returns the new states of the squares in the line after as much as possible has been solved.
     * @param line the line to solve
     * @param clue the clue for the line
     * @return HashMap of the new states of the squares, with the index of the square as the key and value as a boolean, true if filled, false if crossed
     */
    public HashMap<Integer, Boolean> getNewSquareStates(ArrayList<Square> line, Clue clue) {
        ArrayList<Integer> lineState = getLineState(line);

        HashMap<Integer, Boolean> newSquareStates = new HashMap<>();
        ArrayList<Integer> spaceFiller = SpaceFiller(lineState, clue);
        ArrayList<Integer> edgeExtender = edgeExtender(line, lineState, clue);
        ArrayList<Integer> edgeExtenderReversed = edgeExtender(new ArrayList<>(line.reversed()), new ArrayList<>(lineState.reversed()), clue.reverse());
        for(Integer squareIndex : spaceFiller){
            if(line.get(squareIndex).isEmpty()){
                newSquareStates.put(squareIndex, true);
            }
        }
        for(Integer squareIndex : edgeExtender){
            newSquareStates.put(squareIndex, true);
        }
//        for(Integer squareIndex : edgeExtenderReversed){
//            newSquareStates.put(line.size()-squareIndex, true);
//        }
        return newSquareStates;
    }

    /**
     * Returns the indexes of the squares that should be filled between each side.
     * Based upon the space between the edges and the total length of active clues.
     * Can contain squares already filled.
     * @return the indexes of the squares that should be filled
     */
    public ArrayList<Integer> SpaceFiller(ArrayList<Integer> lineState, Clue clue){
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

    /**
     * Extends the filled squares based on distance from the edge, goes leftwards
     * @param lineState the state of the line
     * @param clue the clue for the line
     * @return the indexes of the squares that should be filled
     */
    public ArrayList<Integer> edgeExtender(ArrayList<Square> line, ArrayList<Integer> lineState, Clue clue) {
        ArrayList<Integer> newSquares = new ArrayList<>();

        int currentClueIndex = firstIncompleteClue(clue, lineState);
        if (currentClueIndex == -1) { // no incomplete clues
            return newSquares;
        }

        int emptyIndex = nextEmptyIndex(0, lineState);
        int previousFilledIndex = previousFilledIndex(emptyIndex, lineState);
        int nextFilledIndex = nextFilledIndex(emptyIndex, lineState);

        if (emptyIndex == -1) { // line is already complete, theoretically impossible to reach because firstIncompleteClue would return -1
            return newSquares;
        }

        int AmountOfSquaresToFill;
        int squareIndex;

        if (previousFilledIndex != -1 && previousFilledIndex == emptyIndex - 1) { //Extension starts from filled square
            AmountOfSquaresToFill = clue.getClue().get(currentClueIndex) - lineState.get(emptyIndex - 1);
            squareIndex = stateIndexToSquareIndex(emptyIndex, lineState);

        } else if(nextFilledIndex == -1){ //No filled square to extend from
            return newSquares;

        }else{ // Extension has a gap at the start
            AmountOfSquaresToFill = clue.getClue().get(currentClueIndex) - (lineState.get(emptyIndex + 1)-lineState.get(emptyIndex));
            squareIndex = stateIndexToSquareIndex(emptyIndex+1, lineState) + lineState.get(emptyIndex+1);
        }

        if(AmountOfSquaresToFill <= 0){ // no squares can be filled with this method
            return newSquares;
        }

        while (squareIndex < line.size() && AmountOfSquaresToFill > 0){
            if(line.get(squareIndex).isCrossed()){ // hit an edge, fill backwards TO DO
                break;
            }
            newSquares.add(squareIndex);
            squareIndex++;
            AmountOfSquaresToFill--;
        }

        return newSquares;
    }
}
