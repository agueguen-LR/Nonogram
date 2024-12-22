package nonogram.solver;

import static org.junit.jupiter.api.Assertions.*;

import nonogram.generator.Clue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineSolverUtilitiesTest {

    private LineSolver lineSolver;
    private Clue clue212;

    @BeforeEach
    void setUp() {
        lineSolver = new LineSolver();

        ArrayList<Float> brightnessLine = new ArrayList<>(Arrays.asList(0.1f, 0.1f, 0.6f, 0.1f, 0.6f, 0.1f, 0.1f));
        clue212 = new Clue(brightnessLine);
    }

    @Test
    void testLengthOfSection() {
        ArrayList<Integer> lineState = new ArrayList<>(Arrays.asList(2, -1, 1, 0, -2));
        int length = lineSolver.lengthOfSection(0, 2, lineState);
        assertEquals(4, length);

        length = lineSolver.lengthOfSection(1, 4, lineState);
        assertEquals(5, length);
    }

    @Test
    void testLengthOfIncompleteSection() {
        ArrayList<Integer> lineState = new ArrayList<>(Arrays.asList(2, -1, 1, 0, -2));
        int length = lineSolver.lengthOfIncompleteSection(lineState);
        assertEquals(5, length);

        lineState = new ArrayList<>(Arrays.asList(2, -1, 1, 0, 2));
        length = lineSolver.lengthOfIncompleteSection(lineState);
        assertEquals(1, length);

        lineState = new ArrayList<>(Arrays.asList(2, 0, -3, 2, -2, 0, 1));
        length = lineSolver.lengthOfIncompleteSection(lineState);
        assertEquals(7, length);

        lineState = new ArrayList<>(Arrays.asList(2, 1, 0, 1, 2));
        length = lineSolver.lengthOfIncompleteSection(lineState);
        assertEquals(0, length);
    }

    @Test
    void testLengthOfInterCrossSection() {
        ArrayList<Integer> lineState = new ArrayList<>(Arrays.asList(2, 0, 1, 0, -2));
        int length = lineSolver.lengthOfInterCrossSection(lineState);
        assertEquals(2, length);

        lineState = new ArrayList<>(Arrays.asList(2, -1, 1, 0, 2));
        length = lineSolver.lengthOfInterCrossSection(lineState);
        assertEquals(4, length);

        lineState = new ArrayList<>(Arrays.asList(2, 0, -3, 2, -2, 0, 1));
        length = lineSolver.lengthOfInterCrossSection(lineState);
        assertEquals(7, length);

        lineState = new ArrayList<>(Arrays.asList(2, 1, 0, 1, 2));
        length = lineSolver.lengthOfInterCrossSection(lineState);
        assertEquals(0, length);
    }

    @Test
    void testFirstIncompleteClue() {
        ArrayList<Integer> lineState = new ArrayList<>(Arrays.asList(2, 0, 1, 0, -3));
        assertEquals(2, lineSolver.firstIncompleteClue(clue212, lineState));

        lineState = new ArrayList<>(Arrays.asList(2, 0, 1, 0, 2));
        assertEquals(-1, lineSolver.firstIncompleteClue(clue212, lineState));

        lineState = new ArrayList<>(Arrays.asList(2, 0, 1, 0, 2, -1));
        assertEquals(-1, lineSolver.firstIncompleteClue(clue212, lineState));

        lineState = new ArrayList<>(Arrays.asList(2, 0, -3, 2, -2, 0, 1));
        assertEquals(1, lineSolver.firstIncompleteClue(clue212, lineState));

        lineState = new ArrayList<>(List.of(-5));
        assertEquals(0, lineSolver.firstIncompleteClue(clue212, lineState));

        lineState = new ArrayList<>(Arrays.asList(1, 0, 1, 0, -3));
        assertEquals(0, lineSolver.firstIncompleteClue(clue212, lineState));
    }

    @Test
    void testLastIncompleteClue() {
        ArrayList<Integer> lineState = new ArrayList<>(Arrays.asList(2, 0, 1, 0, -3));
        assertEquals(2, lineSolver.lastIncompleteClue(clue212, lineState));

        lineState = new ArrayList<>(Arrays.asList(2, 0, 1, 0, 2));
        assertEquals(-1, lineSolver.lastIncompleteClue(clue212, lineState));

        lineState = new ArrayList<>(Arrays.asList(-1, 2, 0, 1, 0, 2));
        assertEquals(-1, lineSolver.lastIncompleteClue(clue212, lineState));

        lineState = new ArrayList<>(Arrays.asList(2, 0, -3, 2, -2, 0, 1));
        assertEquals(2, lineSolver.lastIncompleteClue(clue212, lineState));

        lineState = new ArrayList<>(List.of(-5));
        assertEquals(2, lineSolver.lastIncompleteClue(clue212, lineState));

        lineState = new ArrayList<>(Arrays.asList(-1, 0, 1, 0, 2));
        assertEquals(0, lineSolver.lastIncompleteClue(clue212, lineState));
    }

    @Test
    void testActiveClueLength() {
        ArrayList<Integer> lineState = new ArrayList<>(Arrays.asList(2, 0, 1, 0, -3));
        assertEquals(2, lineSolver.activeClueLength(clue212, lineState));

        lineState = new ArrayList<>(Arrays.asList(2, 0, 1, 0, 2));
        assertEquals(0, lineSolver.activeClueLength(clue212, lineState));

        lineState = new ArrayList<>(Arrays.asList(2, 0, -3, 2, -2, 0, 1));
        assertEquals(4, lineSolver.activeClueLength(clue212, lineState));

        lineState = new ArrayList<>(List.of(-5));
        assertEquals(7, lineSolver.activeClueLength(clue212, lineState));

        lineState = new ArrayList<>(Arrays.asList(1, 0, 1, 0, -3));
        assertEquals(7, lineSolver.activeClueLength(clue212, lineState));
    }

    @Test
    void testRemoveCompletedClues() {
        ArrayList<Integer> lineState = new ArrayList<>(Arrays.asList(2, 0, 1, 0, -3));
        ArrayList<Integer> activeClues = lineSolver.removeCompletedClues(clue212, lineState);
        assertEquals(new ArrayList<>(List.of(2)), activeClues);

        lineState = new ArrayList<>(Arrays.asList(2, 0, 1, 0, 2));
        activeClues = lineSolver.removeCompletedClues(clue212, lineState);
        assertEquals(new ArrayList<>(), activeClues);

        lineState = new ArrayList<>(Arrays.asList(2, 0, -3, 2, -2, 0, 1));
        activeClues = lineSolver.removeCompletedClues(clue212, lineState);
        assertEquals(new ArrayList<>(Arrays.asList(1, 2)), activeClues);

        lineState = new ArrayList<>(List.of(-5));
        activeClues = lineSolver.removeCompletedClues(clue212, lineState);
        assertEquals(new ArrayList<>(Arrays.asList(2, 1, 2)), activeClues);

        lineState = new ArrayList<>(Arrays.asList(-1, 1, 0, 1, 0, 2));
        activeClues = lineSolver.removeCompletedClues(clue212, lineState);
        assertEquals(new ArrayList<>(List.of(2)), activeClues);
    }

}