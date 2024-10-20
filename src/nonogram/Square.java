/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nonogram;

/**
 *
 * @author Adrien
 */
public class Square {
    
    private enum State{
        EMPTY, CROSS, FILLED
    }
    private State state;

    public Square() {
        state = State.EMPTY;
    }
    
    public void fillSquare(){
        state = State.FILLED;
    }
    
    public void crossSquare(){
        state = State.CROSS;
    }
    
    public boolean isFilled(){
        return state.equals(State.FILLED);
    }
    
    public boolean isCrossed(){
        return state.equals(State.CROSS);
    }
        
    public boolean isEmpty(){
        return state.equals(State.EMPTY);
    }

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
