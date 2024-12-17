package nonogram.solver;

import static org.junit.jupiter.api.Assertions.*;
import nonogram.generator.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineSolverUtilitiesTest {

    private LineSolverUtilities lineSolverUtilities;
    private ArrayList<Square> lineEmpty;
    private ArrayList<Square> lineFilled;
    private ArrayList<Square> lineCrossed;
    private ArrayList<Square> lineMixed;
    private ArrayList<Square> lineEmptySquares;

    @BeforeEach
    void setUp() {
        lineSolverUtilities = new LineSolverUtilities();

        lineEmpty = new ArrayList<>(Arrays.asList(new Square(), new Square(), new Square(), new Square()));

        lineFilled = new ArrayList<>(Arrays.asList(new Square(), new Square(), new Square(), new Square()));
        lineFilled.get(0).fill();
        lineFilled.get(1).fill();

        lineCrossed = new ArrayList<>(Arrays.asList(new Square(), new Square(), new Square(), new Square()));
        lineCrossed.get(2).cross();

        lineMixed = new ArrayList<>(Arrays.asList(new Square(), new Square(), new Square(), new Square()));
        lineMixed.get(0).fill();
        lineMixed.get(1).fill();
        lineMixed.get(2).cross();
        lineMixed.get(3).fill();

        lineEmptySquares = new ArrayList<>(Arrays.asList(new Square(), new Square(), new Square(), new Square()));
        lineEmptySquares.get(0).fill();
        lineEmptySquares.get(1).fill();
        lineEmptySquares.get(3).fill();
    }

    @Test
    void lineStateTests() {
        ArrayList<Integer> expectedEmpty = new ArrayList<>(List.of(-4));
        assertEquals(expectedEmpty, lineSolverUtilities.getLineState(lineEmpty));

        ArrayList<Integer> expectedFilled = new ArrayList<>(Arrays.asList(2, -2));
        assertEquals(expectedFilled, lineSolverUtilities.getLineState(lineFilled));

        ArrayList<Integer> expectedCrossed = new ArrayList<>(Arrays.asList(-2, 0, -1));
        assertEquals(expectedCrossed, lineSolverUtilities.getLineState(lineCrossed));

        ArrayList<Integer> expectedMixed = new ArrayList<>(Arrays.asList(2, 0, 1));
        assertEquals(expectedMixed, lineSolverUtilities.getLineState(lineMixed));

        ArrayList<Integer> expectedEmptySquares = new ArrayList<>(Arrays.asList(2, -1, 1));
        assertEquals(expectedEmptySquares, lineSolverUtilities.getLineState(lineEmptySquares));
    }
}