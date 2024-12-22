package nonogram.solver;

import nonogram.generator.Clue;

import java.util.ArrayList;

interface ClueUtilities {
    int activeClueLength(Clue clue, ArrayList<Integer> lineState);

    int firstIncompleteClue(Clue clue, ArrayList<Integer> lineState);
    int lastIncompleteClue(Clue clue, ArrayList<Integer> lineState);

    ArrayList<Integer> removeCompletedClues(Clue clue, ArrayList<Integer> lineState);
}
