package nonogram.generator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;

public class ClueTest {

    private Clue clue;

    @BeforeEach
    void setUp() {
        clue = new Clue();
    }

    @Test
    void initialStateIsEmpty() {
        assertTrue(clue.getClue().isEmpty());
    }

    @Test
    void constructFromBrightnessValues() {
        ArrayList<Float> brightnessLine = new ArrayList<>(Arrays.asList(0.1f, 0.1f, 0.6f, 0.1f, 0.1f, 0.1f));
        Clue clueFromBrightness = new Clue(brightnessLine);
        assertEquals(Arrays.asList(2, 3), clueFromBrightness.getClue());
    }

    @Test
    void copyCreatesIdenticalClue() {
        ArrayList<Float> brightnessLine = new ArrayList<>(Arrays.asList(0.1f, 0.1f, 0.6f, 0.1f, 0.1f, 0.1f));
        Clue originalClue = new Clue(brightnessLine);
        Clue copiedClue = originalClue.copy();
        assertEquals(originalClue.getClue(), copiedClue.getClue());
    }

    @Test
    void getClueStringReturnsCorrectString() {
        ArrayList<Float> brightnessLine = new ArrayList<>(Arrays.asList(0.1f, 0.1f, 0.6f, 0.1f, 0.1f, 0.1f));
        Clue originalClue = new Clue(brightnessLine);
        assertEquals("2 3", originalClue.getClueString());
    }

    @Test
    void toStringReturnsCorrectRepresentation() {
        ArrayList<Float> brightnessLine = new ArrayList<>(Arrays.asList(0.1f, 0.1f, 0.6f, 0.1f, 0.1f, 0.1f));
        Clue originalClue = new Clue(brightnessLine);
        assertEquals("clue=[2, 3]", originalClue.toString());
    }
}