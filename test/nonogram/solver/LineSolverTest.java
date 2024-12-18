package nonogram.solver;

import static org.junit.jupiter.api.Assertions.*;

import nonogram.generator.Clue;
import nonogram.generator.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineSolverTest {
    
    private LineSolver lineSolver;
    private ArrayList<Square> lineEmpty;
    private ArrayList<Square> lineFilled;
    private ArrayList<Square> lineCrossed;
    private ArrayList<Square> lineMixed;
    private ArrayList<Square> lineEmptySquares;
    private ArrayList<Square> line574;
    private Clue clue212;
    private Clue clue574;

    @BeforeEach
    void setUp() {
        lineSolver = new LineSolver();

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

        line574 = new ArrayList<>(Arrays.asList(new Square(), new Square(), new Square(), new Square(),new Square(), new Square(), new Square(), new Square(),new Square(), new Square(), new Square(), new Square(),new Square(), new Square(), new Square(), new Square(),new Square(), new Square(), new Square(), new Square()));

        ArrayList<Float> brightnessLine = new ArrayList<>(Arrays.asList(0.1f, 0.1f, 0.6f, 0.1f, 0.6f, 0.1f, 0.1f));
        clue212 = new Clue(brightnessLine);

        brightnessLine = new ArrayList<>(Arrays.asList(0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.6f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.6f, 0.1f, 0.1f, 0.1f, 0.1f));
        clue574 = new Clue(brightnessLine);
    }

    @Test
    void lineStateTests() {
        ArrayList<Integer> expectedEmpty = new ArrayList<>(List.of(-4));
        assertEquals(expectedEmpty, lineSolver.getLineState(lineEmpty));

        ArrayList<Integer> expectedFilled = new ArrayList<>(Arrays.asList(2, -2));
        assertEquals(expectedFilled, lineSolver.getLineState(lineFilled));

        ArrayList<Integer> expectedCrossed = new ArrayList<>(Arrays.asList(-2, 0, -1));
        assertEquals(expectedCrossed, lineSolver.getLineState(lineCrossed));

        ArrayList<Integer> expectedMixed = new ArrayList<>(Arrays.asList(2, 0, 1));
        assertEquals(expectedMixed, lineSolver.getLineState(lineMixed));

        ArrayList<Integer> expectedEmptySquares = new ArrayList<>(Arrays.asList(2, -1, 1));
        assertEquals(expectedEmptySquares, lineSolver.getLineState(lineEmptySquares));
    }

    @Test
    void testFillEmptySquares(){
        lineSolver.setLine(line574, clue574);
        ArrayList<Integer> filledSquares = lineSolver.FillEmptySquares();
        ArrayList<Integer> expectedFilledSquares = new ArrayList<>(Arrays.asList(2, 3, 4, 8, 9, 10, 11, 12, 16, 17));
        assertEquals(expectedFilledSquares, filledSquares);


    }
    
}