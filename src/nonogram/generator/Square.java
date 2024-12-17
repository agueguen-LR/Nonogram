/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nonogram.generator;

/**
 * Represents a square in the nonogram board.
 * A square can be in one of three states: EMPTY, CROSS, or FILLED.
 * The state of the square can be changed using the provided methods.
 * The state can also be queried to check if the square is empty, crossed, or filled.
 * The toString method provides a string representation of the square's state.
 *
 * @author agueguen-LR
 */
public class Square {

    /**
     * Enum representing the possible states of a square.
     */
    private enum State{
        EMPTY, CROSS, FILLED
    }

    /**
     * The current state of the square.
     */
    private State state;

    /**
     * Constructs a new Square with an initial state of EMPTY.
     */
    public Square() {
        state = State.EMPTY;
    }

    /**
     * Sets the state of the square to FILLED.
     */
    public void fill(){
        state = State.FILLED;
    }

    /**
     * Sets the state of the square to CROSS.
     */
    public void cross(){
        state = State.CROSS;
    }

    /**
     * Checks if the square is in the FILLED state.
     *
     * @return true if the square is FILLED, false otherwise
     */
    public boolean isFilled(){
        return state.equals(State.FILLED);
    }

    /**
     * Checks if the square is in the CROSS state.
     *
     * @return true if the square is CROSS, false otherwise
     */
    public boolean isCrossed(){
        return state.equals(State.CROSS);
    }

    /**
     * Checks if the square is in the EMPTY state.
     *
     * @return true if the square is EMPTY, false otherwise
     */
    public boolean isEmpty(){
        return state.equals(State.EMPTY);
    }

    /**
     * Returns a string representation of the square's state.
     *
     * @return "O" if the square is FILLED, "X" if the square is CROSS, and " " if the square is EMPTY
     */
    @Override
    public String toString() {
        if(state.equals(State.FILLED)){
            return "O";
        } else if (state.equals(State.CROSS)){
            return "X";
        }
        return " ";
    }

}