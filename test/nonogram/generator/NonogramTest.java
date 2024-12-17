package nonogram.generator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NonogramTest {

    private Nonogram Nonogram;

    @BeforeEach
    void setUp() {
        Nonogram = new Nonogram("resources/Test.bmp", 1);
    }

    @Test
    void boardIsInitializedCorrectly() {
        Board board = Nonogram.getBoard();
        assertNotNull(board);
    }

    @Test
    void toStringReturnsCorrectRepresentation() {
        String expected = "Nonogram{board=" + Nonogram.getBoard().toString() + "}";
        assertEquals(expected, Nonogram.toString());
    }

    @Test
    void reduceArrayReducesCorrectly() {
        ArrayList<ArrayList<Float>> array = new ArrayList<>();
        array.add(new ArrayList<>(Arrays.asList(1.0f, 2.0f, 3.0f, 4.0f)));
        array.add(new ArrayList<>(Arrays.asList(5.0f, 6.0f, 7.0f, 8.0f)));
        array.add(new ArrayList<>(Arrays.asList(9.0f, 10.0f, 11.0f, 12.0f)));
        array.add(new ArrayList<>(Arrays.asList(13.0f, 14.0f, 15.0f, 16.0f)));

        ArrayList<ArrayList<Float>> reducedArray = Nonogram.ReduceArray(array, 2);
        ArrayList<ArrayList<Float>> expectedArray = new ArrayList<>();
        expectedArray.add(new ArrayList<>(Arrays.asList(3.5f, 5.5f)));
        expectedArray.add(new ArrayList<>(Arrays.asList(11.5f, 13.5f)));

        assertEquals(expectedArray, reducedArray);
    }

    @Test
    void reduceArrayHandlesEmptyArray() {
        ArrayList<ArrayList<Float>> array = new ArrayList<>();
        ArrayList<ArrayList<Float>> reducedArray = Nonogram.ReduceArray(array, 2);
        assertTrue(reducedArray.isEmpty());
    }

    @Test
    void reduceArrayHandlesSingleElementArray() {
        ArrayList<ArrayList<Float>> array = new ArrayList<>();
        array.add(new ArrayList<>(List.of(1.0f)));

        ArrayList<ArrayList<Float>> reducedArray = Nonogram.ReduceArray(array, 1);
        ArrayList<ArrayList<Float>> expectedArray = new ArrayList<>();
        expectedArray.add(new ArrayList<>(List.of(1.0f)));

        assertEquals(expectedArray, reducedArray);
    }
}