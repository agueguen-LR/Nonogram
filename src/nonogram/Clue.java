/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nonogram;

import java.util.ArrayList;

/**
 *
 * @author Adrien
 */
public class Clue {
    
    private ArrayList<Integer> clue = new ArrayList<>();

    public Clue() {
    }
    
    private Clue(ArrayList<Integer> clueCopy, int id){
        if (id==1234){
            this.clue = clueCopy;
        }
    }

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

    public ArrayList<Integer> getClue() {
        return clue;
    }
    
    public Clue copy(){
        ArrayList<Integer> tempClue = new ArrayList<Integer>();
        for(Integer i : this.clue){
            tempClue.add(Integer.valueOf(i));
        }
        return new Clue(tempClue, 1234);
    }
    
    
    public String getClueString(){
        String str = "";
        for (Integer num : clue){
            str += num.toString()+ " ";
        }
        return str.stripTrailing();
    }

    @Override
    public String toString() {
        return "clue=" + clue;
    }
    
}
