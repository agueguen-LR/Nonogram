/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nonogram.generator;

import java.util.ArrayList;

/**
 * Represents a clue in the nonogram puzzle.
 * A clue is a sequence of integers that indicates the lengths of filled blocks in a row or column.
 * The clue can be constructed from a list of brightness values or copied from another clue.
 * The clue can be queried to get its string representation or a copy of the clue.
 * The toString method provides a string representation of the clue.
 *
 * @author agueguen-LR
 */
public class Clue {

    /**
     * The list of integers representing the clue.
     */
    private ArrayList<Integer> clue = new ArrayList<>();

    /**
     * Constructs an empty Clue.
     */
    public Clue() {
    }

    /**
     * Constructs a Clue by copying another clue.
     * This constructor is private and used internally for copying.
     *
     * @param clueCopy the list of integers to copy
     * @param id an identifier to validate the copy operation
     */
    private Clue(ArrayList<Integer> clueCopy, int id){
        if (id==1234){
            this.clue = clueCopy;
        }
    }

    /**
     * Constructs a Clue from a list of brightness values.
     * A brightness value less than 0.5 indicates a filled square.
     *
     * @param brightnessLine the list of brightness values
     */
    public Clue(ArrayList<Float> brightnessLine) {
        boolean previousIsFilled = false;
        for (Float num : brightnessLine){
            if (num<.5 && previousIsFilled){
                clue.set(clue.size()-1, clue.getLast()+1);
            } else if (num<.5){
                clue.add(1);
                previousIsFilled = true;
            } else{
                previousIsFilled = false;
            }
        }
    }

    /**
     * Returns the list of integers representing the clue.
     *
     * @return the list of integers representing the clue
     */
    public ArrayList<Integer> getClue() {
        return clue;
    }

    /**
     * Returns a string representation of the clue.
     * The string representation is a space-separated list of integers.
     *
     * @return the string representation of the clue
     */
    public String getClueString(){
        StringBuilder str = new StringBuilder();
        for (Integer num : clue){
            str.append(num.toString()).append(" ");
        }
        return str.toString().stripTrailing();
    }

    public Clue reverse(){
        ArrayList<Integer> reversedClue = new ArrayList<>(this.clue.reversed());
        return new Clue(reversedClue, 1234);
    }

    /**
     * Returns a string representation of the Clue object.
     *
     * @return the string representation of the Clue object
     */
    @Override
    public String toString() {
        return "clue=" + clue;
    }

}