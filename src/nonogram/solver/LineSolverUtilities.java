package nonogram.solver;

import nonogram.generator.Clue;
import nonogram.generator.Square;

import java.util.ArrayList;
import java.util.HashMap;

abstract class LineSolverUtilities implements StateUtilities, ClueUtilities{

    /**
     * Gives a comprehensive state of the line
     * 0 represents a CROSS
     * Positive numbers represent consecutive FILLED squares
     * Negative numbers represent consecutive EMPTY squares
     *
     * @return ArrayList<Integer> containing the exact current state of the line
     */
    public ArrayList<Integer> getLineState(ArrayList<Square> line) {

        ArrayList<Integer> lineState = new ArrayList<>();
        boolean increasing = false;
        boolean decreasing = false;

        for (Square square : line) {
            if (square.isFilled()){
                if (increasing){
                    lineState.set(lineState.size()-1, lineState.getLast()+1);
                } else{
                    lineState.add(1);
                    increasing = true;
                    decreasing = false;
                }
            }
            if (square.isEmpty()){
                if (decreasing){
                    lineState.set(lineState.size()-1, lineState.getLast()-1);
                } else{
                    lineState.add(-1);
                    decreasing = true;
                    increasing = false;
                }
            }
            if (square.isCrossed()){
                lineState.add(0);
                increasing = false;
                decreasing = false;
            }
        }

        return lineState;
    }

    /**
     * Converts the index of an element in the lineState to the index of the square in the line.
     * @param stateIndex the index of the element in the lineState
     * @param lineState the lineState
     * @return the index of the square in the line
     */
    public int stateIndexToSquareIndex(int stateIndex, ArrayList<Integer> lineState){
        int squareIndex = 0;
        int i = 0;
        while (i < stateIndex){
            int stateValue = lineState.get(i);
            if (stateValue == 0){
                squareIndex++;
            } else if (stateValue > 0){
                squareIndex += stateValue;
            } else {
                squareIndex -= stateValue;
            }
            i++;
        }
        return squareIndex;
    }

    /**
     * Gets the index of the next cross in lineState after currentIndex.
     * @param currentIndex the index of the current element in the line
     * @param lineState the lineState
     * @return the index of the next cross in lineState after currentIndex, -1 if there is no cross
     */
    public int nextCrossIndex(int currentIndex, ArrayList<Integer> lineState){
        int i = currentIndex;
        while (i < lineState.size()){
            if (lineState.get(i) == 0){
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Gets the index of the previous cross in lineState before currentIndex.
     * @param currentIndex the index of the current element in the line
     * @param lineState the lineState
     * @return the index of the previous cross in lineState before currentIndex, -1 if there is no cross
     */
    public int previousCrossIndex(int currentIndex, ArrayList<Integer> lineState){
        int i = currentIndex;
        while (i >= 0){
            if (lineState.get(i) == 0){
                return i;
            }
            i--;
        }
        return -1;
    }

    /**
     * Gets the index of the next filled section in lineState after currentIndex.
     * @param currentIndex the index of the current element in the line
     * @param lineState the lineState
     * @return the index of the next filled section in lineState after currentIndex, -1 if there is no filled section
     */
    public int nextFilledIndex(int currentIndex, ArrayList<Integer> lineState) {
        int i = currentIndex;
        while (i < lineState.size()) {
            if (lineState.get(i) > 0) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Gets the index of the previous filled section in lineState before currentIndex.
     * @param currentIndex the index of the current element in the line
     * @param lineState the lineState
     * @return the index of the previous filled section in lineState before currentIndex, -1 if there is no filled section
     */
    public int previousFilledIndex(int currentIndex, ArrayList<Integer> lineState) {
        int i = currentIndex;
        while (i >= 0) {
            if (lineState.get(i) > 0) {
                return i;
            }
            i--;
        }
        return -1;
    }

    /**
     * Gets the index of the next empty section in lineState after currentIndex.
     * @param currentIndex the index of the current element in the line
     * @param lineState the lineState
     * @return the index of the next empty section in lineState after currentIndex, -1 if there is no empty section
     */
    public int nextEmptyIndex(int currentIndex, ArrayList<Integer> lineState) {
        int i = currentIndex;
        while (i < lineState.size()) {
            if (lineState.get(i) < 0) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Gets the index of the previous empty section in lineState before currentIndex.
     * @param currentIndex the index of the current element in the line
     * @param lineState the lineState
     * @return the index of the previous empty section in lineState before currentIndex, -1 if there is no empty section
     */
    public int previousEmptyIndex(int currentIndex, ArrayList<Integer> lineState) {
        int i = currentIndex;
        while (i >= 0) {
            if (lineState.get(i) < 0) {
                return i;
            }
            i--;
        }
        return -1;
    }

    /**
     * Returns the length of the section from startIndex to endIndex in lineState.
     * @param startIndex the index of the start of the section
     * @param endIndex the index of the end of the section
     * @param lineState the lineState
     * @return the length of the section
     */
    public int lengthOfSection(int startIndex, int endIndex, ArrayList<Integer> lineState) {
        int length = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            length += Math.abs(lineState.get(i));
            if(lineState.get(i) == 0){
                length++;
            }
        }
        return length;
    }

    /**
     * Returns the length of the incomplete section in lineState.
     * @param lineState the lineState
     * @return the length of the incomplete section
     */
    public int lengthOfIncompleteSection(ArrayList<Integer> lineState) {
        int startIndex = nextEmptyIndex(0, lineState);
        int endIndex = previousEmptyIndex(lineState.size()-1, lineState);
        if (startIndex == -1 || endIndex == -1){
            return 0;
        }
        return lengthOfSection(startIndex, endIndex, lineState);
    }

    /**
     * Returns the length of the section between crosses in lineState.
     * @param lineState the lineState
     * @return the length of the section between crosses
     */
    public int lengthOfInterCrossSection(ArrayList<Integer> lineState) {
        int startIndex = nextEmptyIndex(0, lineState);
        int endIndex = previousEmptyIndex(lineState.size()-1, lineState);
        if (startIndex == -1 || endIndex == -1) {
            return 0;
        }

        int startCrossIndex = previousCrossIndex(startIndex, lineState);
        int endCrossIndex = nextCrossIndex(endIndex, lineState);
        if(startCrossIndex == -1 && endCrossIndex == -1){
            return lengthOfSection(0, lineState.size()-1, lineState);
        } else if (startCrossIndex == -1){
            return lengthOfSection(0, endCrossIndex-1, lineState);
        } else if (endCrossIndex == -1){
            return lengthOfSection(startCrossIndex+1, lineState.size()-1, lineState);
        }
        return lengthOfSection(startCrossIndex+1, endCrossIndex-1, lineState);
    }

    public ArrayList<Integer> updateLineState(ArrayList<Square> line, HashMap<Integer, Boolean> newSquareStates){
        ArrayList<Square> updatedLine = copyLine(line);
        for (Integer index : newSquareStates.keySet()){
            if (newSquareStates.get(index)){
                updatedLine.get(index).fill();
            } else {
                updatedLine.get(index).cross();
            }
        }
        return getLineState(updatedLine);
    }

    private ArrayList<Square> copyLine(ArrayList<Square> line){
        ArrayList<Square> copy = new ArrayList<>();
        for (Square square : line){
            Square newSquare = new Square();
            if(square.isFilled()){
                newSquare.fill();
            } else if (square.isCrossed()){
                newSquare.cross();
            }
            copy.add(newSquare);
        }
        return copy;
    }

    public ArrayList<Integer> stateSectionToSquareIndexes(int sectionIndex, ArrayList<Integer> lineState){
        ArrayList<Integer> squareIndexes = new ArrayList<>();
        for (int i = 0; i < Math.abs(lineState.get(sectionIndex)); i++){
            squareIndexes.add(stateIndexToSquareIndex(sectionIndex, lineState) + i);
        }
        return squareIndexes;
    }



    /**
     * Returns the index of the first incomplete clue in the Clue.
     * @param clue the clue
     * @return the index of the first incomplete clue
     */
    public int firstIncompleteClue(Clue clue, ArrayList<Integer> lineState){
        int filledIndex = nextFilledIndex(0, lineState);
        int emptyIndex = nextEmptyIndex(0, lineState);

        //No empty squares means the line is complete and all clues are filled
        if(emptyIndex == -1){
            return -1;
        }
        //Nothing is filled so the first clue is incomplete
        if (filledIndex == -1){
            return 0;
        }

        int stateValue = lineState.get(filledIndex);
        int clueIndex = 0;

        //while filled sections are equal to clues and no empty square has been passed
        while(filledIndex < emptyIndex && stateValue == clue.getClue().get(clueIndex)){

            clueIndex++;
            //all clues are complete
            if (clueIndex == clue.getClue().size()){
                return -1;
            }

            filledIndex = nextFilledIndex(filledIndex+stateValue, lineState);
            //no more filled squares
            if (filledIndex == -1){
                return clueIndex;
            }
            stateValue = lineState.get(filledIndex);
        }
        //clue is incomplete or empty square has been passed
        return clueIndex;
    }

    /**
     * Returns the index of the last incomplete clue in the Clue.
     * @param clue the clue
     * @return the index of the last incomplete clue
     */
    public int lastIncompleteClue(Clue clue, ArrayList<Integer> lineState) {
        int filledIndex = previousFilledIndex(lineState.size()-1, lineState);
        int emptyIndex = previousEmptyIndex(lineState.size()-1, lineState);

        //No empty squares means the line is complete and all clues are filled
        if(emptyIndex == -1){
            return -1;
        }
        //Nothing is filled so the last clue is incomplete
        if (filledIndex == -1){
            return clue.getClue().size()-1;
        }

        int stateValue = lineState.get(filledIndex);
        int clueIndex = clue.getClue().size()-1;

        //while filled sections are equal to clues and no empty square has been passed
        while(filledIndex > emptyIndex && stateValue == clue.getClue().get(clueIndex)){

            clueIndex--;
            //all clues are complete
            if (clueIndex == -1){
                return -1;
            }

            filledIndex = previousFilledIndex(filledIndex-stateValue, lineState);
            //no more filled squares
            if (filledIndex == -1){
                return clueIndex;
            }
            stateValue = lineState.get(filledIndex);
        }
        //clue is incomplete or empty square has been passed
        return clueIndex;
    }

    /**
     * Returns the length of the filled squares + in between crosses inferred by the clue
     * between the first and last incomplete clues.
     * @param clue the clue
     * @return the total length
     */
    public int activeClueLength(Clue clue, ArrayList<Integer> lineState) {
        int firstIncomplete = firstIncompleteClue(clue, lineState);
        int lastIncomplete = lastIncompleteClue(clue, lineState);

        if (firstIncomplete == -1 || lastIncomplete == -1){
            return 0;
        }

        int length = 0;
        for (int i = firstIncomplete; i <= lastIncomplete; i++){
            length += clue.getClue().get(i);
        }
        return length + lastIncomplete - firstIncomplete;
    }


    /**
     * Removes the completed clues from the clue.
     * @param clue the clue
     * @param lineState the lineState
     * @return the clue without the completed clues
     */
    public ArrayList<Integer> removeCompletedClues(Clue clue, ArrayList<Integer> lineState) {
        int startIndex = firstIncompleteClue(clue, lineState);
        int endIndex = lastIncompleteClue(clue, lineState);
        if (startIndex == -1 || endIndex == -1){
            return new ArrayList<>();
        }
        return new ArrayList<>(clue.getClue().subList(startIndex, endIndex + 1));
    }

}
