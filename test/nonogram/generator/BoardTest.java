package nonogram.generator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class BoardTest {

    private Board board;
    private ArrayList<Clue> horizontalClues;
    private ArrayList<Clue> verticalClues;

    @BeforeEach
    void setUp() {
        horizontalClues = new ArrayList<>();
        horizontalClues.add(new Clue(new ArrayList<>(List.of(0.1f, 0.1f, 0.6f, 0.1f, 0.1f, 0.1f))));
        horizontalClues.add(new Clue(new ArrayList<>(List.of(0.1f, 0.1f, 0.6f, 0.1f, 0.1f, 0.1f))));

        verticalClues = new ArrayList<>();
        verticalClues.add(new Clue(new ArrayList<>(List.of(0.1f, 0.1f, 0.6f, 0.1f, 0.1f, 0.1f))));
        verticalClues.add(new Clue(new ArrayList<>(List.of(0.1f, 0.1f, 0.6f, 0.1f, 0.1f, 0.1f))));
        board = new Board(2, 2, horizontalClues, verticalClues);
    }

    @Test
    void initialBoardStateIsEmpty() {
        assertTrue(board.getSquare(0, 0).isEmpty());
        assertTrue(board.getSquare(1, 0).isEmpty());
        assertTrue(board.getSquare(0, 1).isEmpty());
        assertTrue(board.getSquare(1, 1).isEmpty());
    }

    @Test
    void fillSquareChangesStateToFilled() {
        board.fillSquare(0, 0);
        assertTrue(board.getSquare(0, 0).isFilled());
    }

    @Test
    void crossSquareChangesStateToCrossed() {
        board.crossSquare(1, 1);
        assertTrue(board.getSquare(1, 1).isCrossed());
    }

    @Test
    void getDimensionsReturnsCorrectDimensions() {
        assertArrayEquals(new int[]{2, 2}, board.getDimensions());
    }

    @Test
    void getHorizontalCluesReturnsCorrectClues() {
        assertEquals(horizontalClues, board.getHorizontalClues());
    }

    @Test
    void getVerticalCluesReturnsCorrectClues() {
        assertEquals(verticalClues, board.getVerticalClues());
    }

    @Test
    void toStringReturnsCorrectRepresentation() {
        String expected = "clue=[2, 3][ ,  ]\nclue=[2, 3][ ,  ]\n";
        assertEquals(expected, board.toString());
    }
}