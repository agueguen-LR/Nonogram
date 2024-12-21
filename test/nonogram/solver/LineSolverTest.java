package nonogram.solver;

import static org.junit.jupiter.api.Assertions.*;

import nonogram.generator.Clue;
import nonogram.generator.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class LineSolverTest {
    
    private LineSolver lineSolver;

    private ArrayList<Square> lineEmpty;
    private ArrayList<Square> lineFilled;
    private ArrayList<Square> lineCrossed;
    private ArrayList<Square> lineMixed;
    private ArrayList<Square> lineEmptySquares;

    private ArrayList<Square> line574;
    private ArrayList<Square> line44;
    private ArrayList<Square> line44filledStart;
    private ArrayList<Square> line44crossedStart;
    private ArrayList<Square> line44noPossibleExtension;
    private ArrayList<Square> line44noNeededExtension;

    private Clue clue574;
    private Clue clue44;
    private Clue clue21;

    @BeforeEach
    void setUp() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<Clue> clueConstructor = Clue.class.getDeclaredConstructor(ArrayList.class, int.class);
        clueConstructor.setAccessible(true);

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
        clue574 = clueConstructor.newInstance(new ArrayList<>(Arrays.asList(5, 7, 4)), 1234);

        line44 = new ArrayList<>(Arrays.asList(new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square()));
        line44.get(1).fill();
        line44.get(8).fill();
        line44filledStart = new ArrayList<>(Arrays.asList(new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square()));
        line44filledStart.get(0).fill();
        line44filledStart.get(9).fill();
        line44crossedStart = new ArrayList<>(Arrays.asList(new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square()));
        line44crossedStart.get(0).cross();
        line44crossedStart.get(2).fill();
        line44noPossibleExtension = new ArrayList<>(Arrays.asList(new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square()));
        line44noPossibleExtension.get(3).fill();
        line44noNeededExtension = new ArrayList<>(Arrays.asList(new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square(), new Square()));
        line44noNeededExtension.get(0).fill();
        line44noNeededExtension.get(1).fill();
        line44noNeededExtension.get(2).fill();
        line44noNeededExtension.get(3).fill();
        clue44 = clueConstructor.newInstance(new ArrayList<>(Arrays.asList(4, 4)), 1234);

        clue21 = clueConstructor.newInstance(new ArrayList<>(Arrays.asList(2, 1)), 1234);


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
        ArrayList<Integer> filledSquares = lineSolver.SpaceFiller(lineSolver.getLineState(line574), clue574);
        ArrayList<Integer> expectedFilledSquares = new ArrayList<>(Arrays.asList(2, 3, 4, 8, 9, 10, 11, 12, 16, 17));
        assertEquals(expectedFilledSquares, filledSquares);
    }

    @Test
    void testEdgeExtender(){
        ArrayList<Integer> filledSquares = lineSolver.edgeExtender(line44, lineSolver.getLineState(line44), clue44);
        ArrayList<Integer> expectedFilledSquares = new ArrayList<>(Arrays.asList(2, 3));
        assertEquals(expectedFilledSquares, new ArrayList<>(filledSquares));

        filledSquares = lineSolver.edgeExtender(line44filledStart, lineSolver.getLineState(line44filledStart), clue44);
        expectedFilledSquares = new ArrayList<>(Arrays.asList(1, 2, 3));
        assertEquals(expectedFilledSquares, new ArrayList<>(filledSquares));

        filledSquares = lineSolver.edgeExtender(lineMixed, lineSolver.getLineState(lineMixed), clue21);
        expectedFilledSquares = new ArrayList<>();
        assertEquals(expectedFilledSquares, new ArrayList<>(filledSquares));

        filledSquares = lineSolver.edgeExtender(lineEmpty, lineSolver.getLineState(lineEmpty), clue21);
        expectedFilledSquares = new ArrayList<>();
        assertEquals(expectedFilledSquares, new ArrayList<>(filledSquares));

        filledSquares = lineSolver.edgeExtender(line44crossedStart,lineSolver.getLineState(line44crossedStart), clue44);
        expectedFilledSquares = new ArrayList<>(Arrays.asList(3, 4));
        assertEquals(expectedFilledSquares, new ArrayList<>(filledSquares));

        filledSquares = lineSolver.edgeExtender(line44noPossibleExtension, lineSolver.getLineState(line44noPossibleExtension), clue44);
        expectedFilledSquares = new ArrayList<>();
        assertEquals(expectedFilledSquares, new ArrayList<>(filledSquares));

        filledSquares = lineSolver.edgeExtender(lineFilled, lineSolver.getLineState(lineFilled), clue21);
        expectedFilledSquares = new ArrayList<>();
        assertEquals(expectedFilledSquares, new ArrayList<>(filledSquares));

        filledSquares = lineSolver.edgeExtender(line44noNeededExtension, lineSolver.getLineState(line44noNeededExtension), clue44);
        expectedFilledSquares = new ArrayList<>();
        assertEquals(expectedFilledSquares, new ArrayList<>(filledSquares));
    }
    
}