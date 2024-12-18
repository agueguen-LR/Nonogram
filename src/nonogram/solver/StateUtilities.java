package nonogram.solver;

import nonogram.generator.Square;

import java.util.ArrayList;

interface StateUtilities {

    ArrayList<Integer> getLineState(ArrayList<Square> line);
    int stateIndexToSquareIndex(int stateIndex, ArrayList<Integer> lineState);

    int nextCrossIndex(int currentIndex, ArrayList<Integer> lineState);
    int previousCrossIndex(int currentIndex, ArrayList<Integer> lineState);
    int nextFilledIndex(int currentIndex, ArrayList<Integer> lineState);
    int previousFilledIndex(int currentIndex, ArrayList<Integer> lineState);
    int nextEmptyIndex(int currentIndex, ArrayList<Integer> lineState);
    int previousEmptyIndex(int currentIndex, ArrayList<Integer> lineState);

    int lengthOfSection(int startIndex, int endIndex, ArrayList<Integer> lineState);
    int lengthOfIncompleteSection(ArrayList<Integer> lineState);
    int lengthOfInterCrossSection(ArrayList<Integer> lineState);
}