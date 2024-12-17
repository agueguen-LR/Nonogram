package nonogram.solver;

import nonogram.generator.Square;

import java.util.ArrayList;

public class LineSolverUtilities {

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



}
