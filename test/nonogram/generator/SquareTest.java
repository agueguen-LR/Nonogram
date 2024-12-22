package nonogram.generator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SquareTest {

    private Square square;

    @BeforeEach
    void setUp() {
        square = new Square();
    }

    @Test
    void initialStateIsEmpty() {
        assertTrue(square.isEmpty());
        assertFalse(square.isFilled());
        assertFalse(square.isCrossed());
    }

    @Test
    void fillChangesStateToFilled() {
        square.fill();
        assertTrue(square.isFilled());
        assertFalse(square.isEmpty());
        assertFalse(square.isCrossed());
    }

    @Test
    void crossChangesStateToCrossed() {
        square.cross();
        assertTrue(square.isCrossed());
        assertFalse(square.isEmpty());
        assertFalse(square.isFilled());
    }

    @Test
    void toStringReturnsCorrectRepresentation() {
        assertEquals(" ", square.toString());
        square.fill();
        assertEquals("O", square.toString());
        square.cross();
        assertEquals("X", square.toString());
    }
}