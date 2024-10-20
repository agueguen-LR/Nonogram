/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nonogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * The {@code Solver} class is responsible for solving a Nonogram puzzle using 
 * the provided {@code Board} object and associated clues. The solver 
 * interacts with the graphical user interface to display the solving 
 * process in real-time.
 * <p>
 * This class handles various solving strategies such as completing full rows 
 * or columns based on given clues and filling in the edges. The solving 
 * process is displayed using a graphical interface, which can be slowed down 
 * for visual effect.
 * </p>
 * <p>
 * The solver uses a copy of the original clues to avoid modifying the original 
 * clue lists during the solving process.
 * </p>
 * 
 * @author Adrien
 */
public class Solver {
    
    /**
     * The board on which the Nonogram puzzle is being solved.
     */
    private Board board;
    
    /**
     * The list of horizontal clues for each row of the Nonogram.
     */
    private ArrayList<Clue> horizClues = new ArrayList<>();
    
    /**
     * The list of vertical clues for each column of the Nonogram.
     */
    private ArrayList<Clue> vertClues = new ArrayList<>();
    
    /**
     * A copy of the original list of horizontal clues used to reference during solving.
     */
    private final ArrayList<Clue> horizCluesCopy;
    
    /**
     * A copy of the original list of vertical clues used to reference during solving.
     */
    private final ArrayList<Clue> vertCluesCopy;

    /**
     * Constructs a new {@code Solver} instance with the specified board and 
     * initializes the horizontal and vertical clues.
     * 
     * @param board the {@code Board} object representing the Nonogram puzzle to be solved
     */
    public Solver(Board board) {
        this.board = board;
        this.horizClues = board.getHorizontalClues();
        this.vertClues = board.getVerticalClues();
        horizCluesCopy = createCopy(horizClues);
        vertCluesCopy = createCopy(vertClues);
    }
    
    /**
     * Begins solving the Nonogram puzzle at the specified speed.
     * This method sets up the graphical interface and executes the 
     * solving algorithms.
     * 
     * Incomplete
     * 
     * @param speed the delay in milliseconds between each step of the solving process
     * @throws InterruptedException if the thread is interrupted during the solving process
     */
    public void BeginSolving(int speed) throws InterruptedException{
        var frame = new JFrame("Nonogram Board");
        SwingUtilities.invokeLater(() -> {
            board.setBackground(Color.BLUE.darker());            
            frame.setSize(1200, 1000);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.getContentPane().add(board, BorderLayout.CENTER);
            frame.setVisible(true);
        });
        FillAllRows(frame, speed);
        allColumnChecker(frame, speed);
        FillAllColumns(frame, speed);
        allRowChecker(frame, speed);
        allColumnChecker(frame, speed);
        allColumnEdgeExtend(frame, speed);
        allRowChecker(frame, speed);
        allColumnChecker(frame, speed);
        allRowEdgeExtend(frame, speed);
        

//        completeFullHints(frame, speed);
//        completeBorders(frame, speed);

    }
    
    /**
     * Fills out all rows and columns where the clue provides enough 
     * information to fill it in its entirety.
     * 
     * @param frame the JFrame displaying the Nonogram board
     * @param speed the delay in milliseconds between each step
     * @throws InterruptedException if the thread is interrupted during the process
     */
    private void completeFullHints(JFrame frame, int speed) throws InterruptedException{
        //Horizontal
        for (int i = 0; i<horizClues.size(); i++){           
            
            if (horizClues.get(i).getClue().size() == 0){
                for (int x = 0; x<board.getDimensions()[0]; x++){
                    slowProgression(frame, speed);
                    board.crossSquare(x, i);
                }
            }
            
            int clueSum = 0;
            for (Integer n : horizClues.get(i).getClue()){
                clueSum += n;
            }
            clueSum+=horizClues.get(i).getClue().size()-1;
            
            if (clueSum == board.getDimensions()[0]){
                int currentIndex = 0;
                for (Integer n : horizClues.get(i).getClue()){
                    for (int k = 0; k<n; k++){
                        slowProgression(frame, speed);
                        board.fillSquare(currentIndex, i);
                        currentIndex++;
                    }
                    if (currentIndex!=board.getDimensions()[0]){
                        slowProgression(frame, speed);
                        board.crossSquare(currentIndex, i);
                        currentIndex++;
                    }
                }
                horizClues.get(i).getClue().replaceAll(n -> 0);
            }
        }
        //Vertical
        for (int i = 0; i<vertClues.size(); i++){
            
            if (vertClues.get(i).getClue().size() == 0){
                for (int y = 0; y<board.getDimensions()[1]; y++){
                    slowProgression(frame, speed);
                    board.crossSquare(i, y);
                }
            }           
            
            int clueSum = 0;
            for (Integer n : vertClues.get(i).getClue()){
                clueSum += n;
            }
            clueSum+=vertClues.get(i).getClue().size()-1;
            
            if (clueSum == board.getDimensions()[1]){
                int currentIndex = 0;
                for (Integer n : vertClues.get(i).getClue()){
                    for (int k = 0; k<n; k++){
                        slowProgression(frame, speed);
                        board.fillSquare(i, currentIndex);
                        currentIndex++;
                    }
                    if (currentIndex!=board.getDimensions()[1]){
                        slowProgression(frame, speed);
                        board.crossSquare(i, currentIndex);
                        currentIndex++;
                    }
                }
                vertClues.get(i).getClue().replaceAll(n -> 0);
            }       
        }
        System.out.println("Finished Completing Full Hints");
    }
    
    /**
     * Fills out the borders of the grid where it can be determined based on the clues.
     * 
     * Incomplete
     * 
     * @param frame the JFrame displaying the Nonogram board
     * @param speed the delay in milliseconds between each step
     * @throws InterruptedException if the thread is interrupted during the process
     */
    private void completeBorders(JFrame frame, int speed) throws InterruptedException{
        
        int endPointer;
        int currentLength;
        ArrayList<Integer> activeClue;
        int currentClueNumber;
        
        //Horizontally
        for (int i = 0; i<horizClues.size(); i++){
                      
            activeClue = horizClues.get(i).getClue();
            System.out.println("Currently completing horizontally at row: " + i);
            
            //LEFT
            if(activeClue.stream().anyMatch(num -> num != 0)){ 
                
                endPointer = sideEndCoord(direction.LEFT, i);
                if (endPointer>-1){
                    if(board.getSquare(endPointer, i).isFilled()){
                                               
                        currentLength = currentSideLength(direction.LEFT, i);
                        currentClueNumber = activeClue.get(indexFirstNonZero(activeClue));
                        
                        //Fill squares until clue is fulfilled
                        while (currentLength != currentClueNumber){
                            endPointer++;
                            currentLength++;
                            slowProgression(frame, speed);
                            board.fillSquare(endPointer, i);
                        }
                        //If we're not at the edge, the next square can be crossed
                        if(endPointer+1 != board.getDimensions()[0]){
                            slowProgression(frame, speed);
                            board.crossSquare(endPointer+1, i);
                        }
                        activeClue.set(indexFirstNonZero(activeClue), 0);
                        slowProgression(frame, speed);
                    }                    
                }
            }
                
            //RIGHT
            if(activeClue.stream().anyMatch(num -> num != 0)){ 
                endPointer = sideEndCoord(direction.RIGHT, i);
                if (endPointer<board.getDimensions()[0]){
                    if(board.getSquare(endPointer, i).isFilled()){
                        
                        currentLength = currentSideLength(direction.RIGHT, i);
                        currentClueNumber = activeClue.get(indexLastNonZero(activeClue));
                        
                        //Fill squares until clue is fulfilled
                        while (currentLength != currentClueNumber){
                            endPointer--;
                            currentLength++;
                            slowProgression(frame, speed);
                            board.fillSquare(endPointer, i);
                        }
                        //If we're not at the edge, the next square can be crossed
                        if(endPointer-1 != -1){
                            slowProgression(frame, speed);
                            board.crossSquare(endPointer-1, i);
                        }
                        activeClue.set(indexLastNonZero(activeClue), 0);
                        slowProgression(frame, speed);
                    }                    
                }
            }         
        }
        
        allColumnClueRemover(frame, speed*10);
        
        //Vertically
        for (int i = 0; i<vertClues.size(); i++){
            
            activeClue = vertClues.get(i).getClue();
            System.out.println("Currently completing vertically at column: " + i);
            
            //DOWN
            if(activeClue.stream().anyMatch(num -> num != 0)){                             
                endPointer = sideEndCoord(direction.DOWN, i);
                if (endPointer>-1){
                    if(board.getSquare(i, endPointer).isFilled()){
                        
                        currentLength = currentSideLength(direction.DOWN, i);
                        currentClueNumber = activeClue.get(indexFirstNonZero(activeClue));
                        
                        //Fill squares until clue is fulfilled
                        while (currentLength != currentClueNumber){
                            endPointer++;
                            currentLength++;
                            slowProgression(frame, speed);
                            board.fillSquare(i, endPointer);
                        }
                        //If we're not at the edge, the next square can be crossed
                        if(endPointer+1 != board.getDimensions()[1]){                           
                            board.crossSquare(i, endPointer+1);
                            slowProgression(frame, speed);
                        }
                        activeClue.set(indexFirstNonZero(activeClue), 0);
                        slowProgression(frame, speed);
                    }                    
                }
            }
                
            //UP
            if(activeClue.stream().anyMatch(num -> num != 0)){ 
                endPointer = sideEndCoord(direction.UP, i);
                if (endPointer<board.getDimensions()[1]){
                    if(board.getSquare(i, endPointer).isFilled()){
                        
                        currentLength = currentSideLength(direction.UP, i);
                        currentClueNumber = activeClue.get(indexLastNonZero(activeClue));
                        
                        //Fill squares until clue is fulfilled
                        while (currentLength != currentClueNumber){
                            endPointer--;
                            currentLength++;
                            slowProgression(frame, speed);
                            board.fillSquare(i, endPointer);
                        }
                        //If we're not at the edge, the next square can be crossed
                        if(endPointer-1 != -1){
                            slowProgression(frame, speed);
                            board.crossSquare(i, endPointer-1);
                        }
                        activeClue.set(indexLastNonZero(activeClue), 0);
                        slowProgression(frame, speed);
                    }                    
                }
            }         
        }
        
        allRowClueRemover(frame, speed*10);
        
    }
    
    /**
     * Slows down the advancement of the solver by the specified delay.
     * This method also triggers the revalidation and repainting of the JFrame.
     * 
     * @param frame the JFrame displaying the Nonogram board
     * @param delay the delay in milliseconds between each step
     * @throws InterruptedException if the thread is interrupted during the delay
     */
    private void slowProgression(JFrame frame, int delay) throws InterruptedException{
        Thread.sleep(delay);
        SwingUtilities.invokeLater(() -> {
            frame.revalidate();
            frame.repaint();
        });
    }
       
    /**
     * Enum representing the possible directions for solving (UP, DOWN, LEFT, RIGHT).
     */
    private enum direction{
        UP, DOWN, LEFT, RIGHT
    }
    
    /**
     * Returns the coordinate of the last CROSS after only FILLEDs or CROSSes
     * Returns out-of-bounds value if empty in said direction
     * 
     * @param dir the direction to check for filled squares
     * @param coord the coordinate index to check along
     * @return the coordinate of the last filled square
     */
    private int sideEndCoord(direction dir, int coord){
        
        ArrayList<Integer> state;
        int index;
        int counter;
        int output;
        
        switch (dir){
            case UP:
                state = ColumnState(coord);
                if (state.getLast()<0){return board.getDimensions()[1];}
                index = state.size()-1;
                counter = 0;
                output = board.getDimensions()[1];
                while (index>0 && state.get(index)>-1){               
                    if (state.get(index).equals(0)){
                        output = board.getDimensions()[1]-counter-1;
                        counter++;
                    }
                    counter+=state.get(index);
                    index--;
                }
//                System.out.println("sideEndCoord returned " + output);
                return output;
                
            case DOWN:
                state = ColumnState(coord);
                if (state.getFirst()<0){return -1;}
                index = 0;
                counter = 0;
                output = -1;
                while (index<state.size() && state.get(index)>-1){               
                    if (state.get(index).equals(0)){
                        output = counter;
                        counter++;
                    }
                    counter+=state.get(index);
                    index++;
                }
//                System.out.println("sideEndCoord returned " + output);
                return output;
                
            case RIGHT:
                state = RowState(coord);
                if (state.getLast()<0){return board.getDimensions()[0];}
                index = state.size()-1;
                counter = 0;
                output = board.getDimensions()[0];
                while (index>0 && state.get(index)>-1){               
                    if (state.get(index).equals(0)){
                        output = board.getDimensions()[0]-counter-1;
                        counter++;
                    }
                    counter+=state.get(index);
                    index--;
                }
//                System.out.println("sideEndCoord returned " + output);
                return output;
                
            case LEFT:
                state = RowState(coord);
                if (state.getFirst()<0){return -1;}
                index = 0;
                counter = 0;
                output = -1;
                while (index<state.size() && state.get(index)>-1){ 
                    if (state.get(index).equals(0)){
                        output = counter;
                        counter++;
                    }
                    counter+=state.get(index);
                    index++;
                }
//                System.out.println("sideEndCoord returned " + output);
                return output;
                
            default:
                System.out.println("There has been an error in calculating Solver.sideEndCoord: An incorrect direction has been given?");
                return 0;
        }
    }
    
    /**
     * Outputs the length of the farthest full line (ending in a FILLED, 
     * CROSSED, or an edge) in the specified direction at the given coordinate.
     * 
     * @param dir the direction to check for line length
     * @param coord the coordinate index to check along
     * @return the length of the farthest full line
     */
    private int currentSideLength(direction dir, int coord){
        
        ArrayList<Integer> state;
        int index;
        int output = 0;
        int temp;
        
        switch (dir){
            case UP:
                state = ColumnState(coord);
                index = state.size();
                do {
                    index--;
                    if (index<0){return output;}
                    temp = state.get(index);
                    if (temp>0) {output = temp;}                
                } while (temp>-1);
                return output;
                
            case DOWN:
                state = ColumnState(coord);
                index = -1;
                do {
                    index++;
                    if (index>state.size()){return output;}
                    temp = state.get(index);
                    if (temp>0) {output = temp;}                
                } while (temp>-1);
                return output;  
                
            case RIGHT:
                state = RowState(coord);
                index = state.size();
                do {
                    index--;
                    if (index<0){return output;}
                    temp = state.get(index);
                    if (temp>0) {output = temp;}                
                } while (temp>-1);
                return output;
                
            case LEFT:
                state = RowState(coord);
                index = -1;
                do {
                    index++;
                    if (index>state.size()){return output;}
                    temp = state.get(index);
                    if (temp>0) {output = temp;}                
                } while (temp>-1);
                return output; 
                
            default:
                System.out.println("There has been an error in calculating Solver.currentSideLength: An incorrect direction has been given?");
                return 0;
        }
    }
    
    /**
     * Removes all vertical clues that can be deduced from the current state of the board. 
     * The method iterates over each vertical clue, progressively removing clues based on 
     * the current state of the board using the {@link #columnClueRemover(int)} method.
     * 
     * @param frame The {@link JFrame} that displays the Nonogram board.
     * @param speed The delay in milliseconds between each step of the solving process 
     *              to visually show the solver's progression.
     * @throws InterruptedException If the thread executing this method is interrupted 
     *                              while sleeping.
     */
    private void allColumnClueRemover(JFrame frame, int speed) throws InterruptedException{
        for (int i = 0; i<vertClues.size(); i++){
            slowProgression(frame, speed);
            System.out.println("Currently checking Hints vertically at column: " + i);
            columnClueRemover(i);
        }
    }
    
    /**
     * Removes clues from {@code vertClues}, based on the state of the board and the original clues in {@code vertCluesCopy}.
     * 
     * @param coord the coordinate index to check along for removing clues
     */
    private void columnClueRemover(int coord){
        
        ArrayList<Integer> activeClue = vertClues.get(coord).getClue();
        
        if (!activeClue.stream().anyMatch(num -> num != 0)){
            return;
        }
        
        ArrayList<Integer> activeCopyClue = vertCluesCopy.get(coord).getClue();
        ArrayList<Integer> columnstate = ColumnState(coord);
        int stateIndex;
        int clueIndex;
        int nonZero;
        
        stateIndex = 0;
        clueIndex = 0;
        while (columnstate.get(stateIndex)>-1){              
            if (columnstate.get(stateIndex)>0){
                if (!isEnclosed(columnstate, stateIndex)){
                    break;
                }
                nonZero = indexFirstNonZero(activeClue);
                if (activeCopyClue.get(clueIndex).equals(activeClue.get(nonZero))){
                    activeClue.set(nonZero, 0);
                    if (!activeClue.stream().anyMatch(num -> num != 0)){
                        return;
                    }
                }
                clueIndex++;
            }
            stateIndex++;
            if (clueIndex>activeCopyClue.size()-1 || stateIndex>columnstate.size()-1){
                System.out.println("End of array reached in columnChecker, intended or not? forwards column: " + coord);
                return;
            }
        }
        
        stateIndex = columnstate.size()-1;
        clueIndex = activeCopyClue.size()-1;
        while (columnstate.get(stateIndex)>-1){              
            if (columnstate.get(stateIndex)>0){
                if (!isEnclosed(columnstate, stateIndex)){
                    break;
                }
                nonZero = indexLastNonZero(activeClue);
                if (activeCopyClue.get(clueIndex).equals(activeClue.get(nonZero))){
                    activeClue.set(nonZero, 0);
                    if (!activeClue.stream().anyMatch(num -> num != 0)){
                        return;
                    }
                }
                clueIndex--;
            }
            stateIndex--;
            if (clueIndex<0 || stateIndex<0){
                System.out.println("End of array reached in columnChecker, intended or not? backwards column: " + coord);
                return;
            }
        }        
    }
    
    /**
     * Removes all horizontal clues that can be deduced from the current state of the board. 
     * The method iterates over each horizontal clue, progressively removing clues based on 
     * the current state of the board using the {@link #rowClueRemover(int)} method.
     * 
     * @param frame The {@link JFrame} that displays the Nonogram board.
     * @param speed The delay in milliseconds between each step of the solving process 
     *              to visually show the solver's progression.
     * @throws InterruptedException If the thread executing this method is interrupted 
     *                              while sleeping.
     */
    private void allRowClueRemover(JFrame frame, int speed) throws InterruptedException{
        for (int i = 0; i<horizClues.size(); i++){
            slowProgression(frame, speed);
            System.out.println("Currently checking Hints horizontally at row: " + i);
            rowClueRemover(i);
        }
    }    
    
    /**
     * Removes clues from {@code horizClues}, based on the state of the row and the original clues in {@code horizCluesCopy}.
     * 
     * @param coord the coordinate index to check along for removing clues
     */
    private void rowClueRemover(int coord){
        
        ArrayList<Integer> activeClue = horizClues.get(coord).getClue();
        
        if (!activeClue.stream().anyMatch(num -> num != 0)){
            return;
        }
        
        ArrayList<Integer> activeCopyClue = horizCluesCopy.get(coord).getClue();
        ArrayList<Integer> rowState = RowState(coord);
        int stateIndex;
        int clueIndex;
        int nonZero;
        
        stateIndex = 0;
        clueIndex = 0;
        while (rowState.get(stateIndex)>-1){              
            if (rowState.get(stateIndex)>0){
                if (!isEnclosed(rowState, stateIndex)){
                    break;
                }
                nonZero = indexFirstNonZero(activeClue);
                if (activeCopyClue.get(clueIndex).equals(activeClue.get(nonZero))){
                    activeClue.set(nonZero, 0);
                    if (!activeClue.stream().anyMatch(num -> num != 0)){
                        return;
                    }
                }
                clueIndex++;
            }
            stateIndex++;
            if (clueIndex>activeCopyClue.size()-1 || stateIndex>rowState.size()-1){
                System.out.println("End of array reached in rowChecker, intended or not? forwards row: " + coord);
                return;
            }
        }
        
        stateIndex = rowState.size()-1;
        clueIndex = activeCopyClue.size()-1;
        while (rowState.get(stateIndex)>-1){              
            if (rowState.get(stateIndex)>0){
                if (!isEnclosed(rowState, stateIndex)){
                    break;
                }
                nonZero = indexLastNonZero(activeClue);
                if (activeCopyClue.get(clueIndex).equals(activeClue.get(nonZero))){
                    activeClue.set(nonZero, 0);
                    if (!activeClue.stream().anyMatch(num -> num != 0)){
                        return;
                    }
                }
                clueIndex--;
            }
            stateIndex--;
            if (clueIndex<0 || stateIndex<0){
                System.out.println("End of array reached in rowChecker, intended or not? backwards row: " + coord);
                return;
            }
        }     
    }    
    
    /**
     * Gives a comprehensive state of the row
     * 0 represents a CROSS
     * Positive numbers represent consecutive FILLED squares
     * Negative numbers represent consecutive EMPTY squares
     * 
     * @param coord the coordinate index of the row
     * @return ArrayList<Integer> containing the exact current state of the row
     */
    private ArrayList<Integer> RowState(int coord){
        
        ArrayList<Integer> rowState = new ArrayList<>();
        Integer stateCounter = 0;
        boolean increasing = false;
        boolean decreasing = false;
        
        for (int x = 0; x<board.getDimensions()[0]; x++){
            if (board.getSquare(x, coord).isFilled()){
                if (increasing){
                    rowState.set(rowState.size()-1, rowState.getLast()+1);
                } else{
                    rowState.add(1);
                    increasing=true;
                    decreasing = false;
                }
            }
            if (board.getSquare(x, coord).isEmpty()){
                if (decreasing){
                    rowState.set(rowState.size()-1, rowState.getLast()-1);
                } else{
                    rowState.add(-1);
                    decreasing=true;
                    increasing = false;
                }
            }
            if (board.getSquare(x, coord).isCrossed()){
                rowState.add(0);
                increasing = false;
                decreasing = false;
            }
        }
        if (stateCounter>0){
            rowState.add(stateCounter);
        }
        
        return rowState;
    }
    
    /**
     * Gives a comprehensive state of the column
     * 0 represents a CROSS
     * Positive numbers represent consecutive FILLED squares
     * Negative numbers represent consecutive EMPTY squares
     * 
     * @param coord the coordinate index of the column
     * @return ArrayList<Integer> containing the exact current state of the column
     */
    private ArrayList<Integer> ColumnState(int coord){
        
        ArrayList<Integer> columnState = new ArrayList<>();
        Integer stateCounter = 0;
        boolean increasing = false;
        boolean decreasing = false;
        
        for (int y = 0; y<board.getDimensions()[1]; y++){
            if (board.getSquare(coord, y).isFilled()){
                if (increasing){
                    columnState.set(columnState.size()-1, columnState.getLast()+1);
                } else{
                    columnState.add(1);
                    increasing=true;
                    decreasing = false;
                }
            }
            if (board.getSquare(coord, y).isEmpty()){
                if (decreasing){
                    columnState.set(columnState.size()-1, columnState.getLast()-1);
                } else{
                    columnState.add(-1);
                    decreasing=true;
                    increasing = false;
                }
            }
            if (board.getSquare(coord, y).isCrossed()){
                columnState.add(0);
                increasing = false;
                decreasing = false;
            }
        }
        if (stateCounter>0){
            columnState.add(stateCounter);
        }
        
        return columnState;
    }
    
    
    
    
    
    
    
    
    
    /**
     * Creates a copy of the specified clue list to be used for reference 
     * during the solving process.
     * 
     * @param clueList the list of clues to be copied
     * @return a copy of the provided clue list
     */
    private ArrayList<Clue> createCopy(ArrayList<Clue> clueList){
        ArrayList<Clue> temp = new ArrayList<>();
        for (Clue c : clueList){
            temp.add(c.copy());
        }
        return temp;
    }
    
    private int indexFirstNonZero(ArrayList<Integer> arrayList){
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) != 0) {
                return i;
            }
        }
        System.out.println("Couldn't find a first non zero");
        return -1;
    }
    
    private int indexLastNonZero(ArrayList<Integer> arrayList){
        for (int i = arrayList.size()-1; i > -1; i--) {
            if (arrayList.get(i) != 0) {
                return i;
            }
        }
        System.out.println("Couldn't find a last non zero");
        return -1;
    }
    
    private boolean isEnclosed(ArrayList<Integer> arrayList, int index){
        boolean result;
        if(!(index-1<0)){
            result = arrayList.get(index-1).equals(0);
        } else{
            result = true;
        }
        if(!(index+1>arrayList.size()-1)){
            return result && arrayList.get(index+1).equals(0);
        } 
        return result;
    }
    
    private int distanceToEdge(ArrayList<Integer> arrayList, int index, boolean forward){
        int value;
        int distance = 0;       
        if (forward){
            while (index+1<arrayList.size()){
                value = arrayList.get(index+1);
                if (value==0){
                    return distance;
                } else{
                    distance+=Math.abs(value);
                }
                index++;
            } return distance;
        } else{
            while (index-1>-1){
                value = arrayList.get(index-1);
                if (value==0){
                    return distance;
                } else{
                    distance+=Math.abs(value);
                }
                index--;
            } return distance;
        }
    }
    
    /**
     * Executes the sizeFillRow method for all rows
     * 
     * @param frame The {@link JFrame} that displays the Nonogram board.
     * @param speed The delay in milliseconds between each step of the solving process 
     *              to visually show the solver's progression.
     * @throws InterruptedException If the thread executing this method is interrupted 
     *                              while sleeping.
     */
    private void FillAllRows(JFrame frame, int speed) throws InterruptedException{
        for (int y = 0; y<horizClues.size(); y++){
            rowSizeFiller(y, frame, speed);
        }
    }
    
    /**
    * Fills a row on the board by placing clues in appropriate positions the empty space available 
    * between the left and right endpoints. 
    * If the total length of the clues exceeds half of the empty distance, this method attempts 
    * to fill the row accordingly.
    *
    * @param coord The coordinate of the row to fill.
    * @param frame The JFrame object representing the GUI frame where the operation is visualized.
    * @param speed The speed of the visual progression when filling squares on the board.
    * @throws InterruptedException If the thread executing the method is interrupted during sleep or wait.
    */
   private void rowSizeFiller(int coord, JFrame frame, int speed) throws InterruptedException {
       // Calculate the left and right end coordinates for the given row
       int leftEndCoord = sideEndCoord(direction.LEFT, coord);
       int rightEndCoord = sideEndCoord(direction.RIGHT, coord);

       // Calculate the empty distance between the left and right ends
       int emptyDistance = Math.abs((rightEndCoord - 1) - (leftEndCoord + 1)) + 1;

       // Retrieve the active clues for the given row
       ArrayList<Integer> activeClue = horizClues.get(coord).getClue();

       // Find the index of the first and last non-zero clues
       int nonZeroLeft = indexFirstNonZero(activeClue);
       int nonZeroRight = indexLastNonZero(activeClue);

       // Calculate the total length of the clues plus the gaps between them
       int totalLength = 0;
       for (Integer k : activeClue) {
           totalLength += k;
       }
       totalLength += nonZeroRight - nonZeroLeft;

       // If the total length exceeds half of the empty distance, start filling the row
       if (totalLength > emptyDistance / 2) {
           int offset = emptyDistance - totalLength;
           int clueCounter = 0;
           int clueStep = nonZeroLeft;

           for (int x = leftEndCoord + 1; x < rightEndCoord - offset; x++) {
               // Skip clues that are smaller than or equal to the offset
               if (activeClue.get(clueStep) <= offset) {
                   if (clueStep + 1 == activeClue.size() || activeClue.get(clueStep) + offset >= rightEndCoord) {
                       return;
                   }
                   x += activeClue.get(clueStep);
                   clueStep++;
                   continue;
               }

               // Handle the filling process based on the clue counter and current clue
               if (clueCounter >= activeClue.get(clueStep)) {
                   clueCounter = 0;
                   clueStep++;
                   continue;
               } else if (clueCounter >= offset) {
                   slowProgression(frame, speed);
                   board.fillSquare(x, coord);
               }
               clueCounter++;
           }
       }
   }
   
   /**
     * Executes the sizeFillColumn method for all columns
     * 
     * @param frame The {@link JFrame} that displays the Nonogram board.
     * @param speed The delay in milliseconds between each step of the solving process 
     *              to visually show the solver's progression.
     * @throws InterruptedException If the thread executing this method is interrupted 
     *                              while sleeping.
     */
    private void FillAllColumns(JFrame frame, int speed) throws InterruptedException{
        for (int x = 0; x<vertClues.size(); x++){
            columnSizeFiller(x, frame, speed);
        }
    }
    
    /**
    * Fills a column on the board by placing clues in appropriate positions the empty space available 
    * between the up and down endpoints. 
    * If the total length of the clues exceeds half of the empty distance, this method attempts 
    * to fill the column accordingly.
    *
    * @param coord The coordinate of the column to fill.
    * @param frame The JFrame object representing the GUI frame where the operation is visualized.
    * @param speed The speed of the visual progression when filling squares on the board.
    * @throws InterruptedException If the thread executing the method is interrupted during sleep or wait.
    */
   private void columnSizeFiller(int coord, JFrame frame, int speed) throws InterruptedException {
       // Calculate the left and right end coordinates for the given column
       int leftEndCoord = sideEndCoord(direction.DOWN, coord);
       int rightEndCoord = sideEndCoord(direction.UP, coord);

       // Calculate the empty distance between the left and right ends
       int emptyDistance = Math.abs((rightEndCoord - 1) - (leftEndCoord + 1)) + 1;

       // Retrieve the active clues for the given row
       ArrayList<Integer> activeClue = vertClues.get(coord).getClue();

       // Find the index of the first and last non-zero clues
       int nonZeroLeft = indexFirstNonZero(activeClue);
       int nonZeroRight = indexLastNonZero(activeClue);

       // Calculate the total length of the clues plus the gaps between them
       int totalLength = 0;
       for (Integer k : activeClue) {
           totalLength += k;
       }
       totalLength += nonZeroRight - nonZeroLeft;

       // If the total length exceeds half of the empty distance, start filling the row
       if (totalLength > emptyDistance / 2) {
           int offset = emptyDistance - totalLength;
           int clueCounter = 0;
           int clueStep = nonZeroLeft;

           for (int y = leftEndCoord + 1; y < rightEndCoord - offset; y++) {
               // Skip clues that are smaller than or equal to the offset
               if (activeClue.get(clueStep) <= offset) {
                   if (clueStep + 1 == activeClue.size() || activeClue.get(clueStep) + offset >= rightEndCoord) {
                       return;
                   }
                   y += activeClue.get(clueStep);
                   clueStep++;
                   continue;
               }

               // Handle the filling process based on the clue counter and current clue
               if (clueCounter >= activeClue.get(clueStep)) {
                   clueCounter = 0;
                   clueStep++;
                   continue;
               } else if (clueCounter >= offset) {
                   slowProgression(frame, speed);
                   board.fillSquare(coord, y);
               }
               clueCounter++;
           }
       }
   }

   /**
     * Removes all vertical clues that can be deduced from the current state of the board,
     * and places all CROSSes that can be deduced
     * The method iterates over each vertical clue, based on 
     * the current state of the board using the {@link #columnChecker(int)} method.
     * 
     * @param frame The {@link JFrame} that displays the Nonogram board.
     * @param speed The delay in milliseconds between each step of the solving process 
     *              to visually show the solver's progression.
     * @throws InterruptedException If the thread executing this method is interrupted 
     *                              while sleeping.
     */
    private void allColumnChecker(JFrame frame, int speed) throws InterruptedException{
        for (int i = 0; i<vertClues.size(); i++){
            slowProgression(frame, speed);
            System.out.println("Currently checking Hints vertically at column: " + i);
            columnChecker(i);
        }
    }
   
   /**
     * Removes clues from {@code vertClues}, based on the state of the board and the original clues in {@code vertCluesCopy}.
     * Also places CROSSes when applicable
     * 
     * @param coord the coordinate index to check along for removing clues
     */
    private void columnChecker(int coord){
        
        ArrayList<Integer> activeClue = vertClues.get(coord).getClue();
        
        if (!activeClue.stream().anyMatch(num -> num != 0)){
            return;
        }
        
        ArrayList<Integer> activeCopyClue = vertCluesCopy.get(coord).getClue();
        ArrayList<Integer> columnState = ColumnState(coord);
        int stateIndex;
        int clueIndex;
        int boardIndex;
//        int nonZero;
        
        stateIndex = 0;
        clueIndex = 0;
        boardIndex = 0;
        while (columnState.get(stateIndex)>-1){              
            if (columnState.get(stateIndex).equals(activeCopyClue.get(clueIndex))){
                
//                nonZero = indexFirstNonZero(activeClue);
                activeClue.set(clueIndex, 0);
                boardIndex+=columnState.get(stateIndex);
                if (!(boardIndex>=board.getDimensions()[1])){                   
                    board.crossSquare(coord, boardIndex);
                    columnState = ColumnState(coord);
                }
                if (!activeClue.stream().anyMatch(num -> num != 0)){
                    return;
                }
                clueIndex++;
            }
            stateIndex++;
            if (clueIndex>activeCopyClue.size()-1 || stateIndex>columnState.size()-1){
                System.out.println("End of array reached in columnChecker, intended or not? forwards column: " + coord);
                return;
            }
            if (columnState.get(stateIndex).equals(0)){boardIndex++;}
        }
        
        stateIndex = columnState.size()-1;
        clueIndex = activeCopyClue.size()-1;
        boardIndex = board.getDimensions()[1]-1;
        while (columnState.get(stateIndex)>-1){              
            if (columnState.get(stateIndex).equals(activeCopyClue.get(clueIndex))){
                
//                nonZero = indexLastNonZero(activeClue);
                activeClue.set(clueIndex, 0);
                boardIndex-=columnState.get(stateIndex);
                if (!(boardIndex<=-1)){                   
                    board.crossSquare(coord, boardIndex);
                    columnState = ColumnState(coord);
                }
                if (!activeClue.stream().anyMatch(num -> num != 0)){
                    return;
                }
                clueIndex--;
            }
            stateIndex--;
            if (clueIndex<0 || stateIndex<0){
                System.out.println("End of array reached in columnChecker, intended or not? backwards column: " + coord);
                return;
            }
            if (columnState.get(stateIndex).equals(0)){boardIndex--;}
        }        
    }
    
    /**
     * Removes all horizontal clues that can be deduced from the current state of the board,
     * and places all CROSSes that can be deduced
     * The method iterates over each horizontal clue, based on 
     * the current state of the board using the {@link #rowChecker(int)} method.
     * 
     * @param frame The {@link JFrame} that displays the Nonogram board.
     * @param speed The delay in milliseconds between each step of the solving process 
     *              to visually show the solver's progression.
     * @throws InterruptedException If the thread executing this method is interrupted 
     *                              while sleeping.
     */
    private void allRowChecker(JFrame frame, int speed) throws InterruptedException{
        for (int i = 0; i<horizClues.size(); i++){
            slowProgression(frame, speed);
            System.out.println("Currently checking Hints horizontally at row: " + i);
            rowChecker(i);
        }
    }
   
   /**
     * Removes clues from {@code horizClues}, based on the state of the board and the original clues in {@code horizCluesCopy},
     * places CROSSes when applicable at the end of rows.
     * 
     * @param coord the coordinate index to check along for removing clues
     */
    private void rowChecker(int coord){
        
        ArrayList<Integer> activeClue = horizClues.get(coord).getClue();
        
        if (!activeClue.stream().anyMatch(num -> num != 0)){
            return;
        }
        
        ArrayList<Integer> activeCopyClue = horizCluesCopy.get(coord).getClue();
        ArrayList<Integer> rowState = RowState(coord);
        int stateIndex;
        int clueIndex;
        int boardIndex;
//        int nonZero;
        
        stateIndex = 0;
        clueIndex = 0;
        boardIndex = 0;
        while (rowState.get(stateIndex)>-1){   //While Not Empty           
            if (rowState.get(stateIndex).equals(activeCopyClue.get(clueIndex))){ //If currentState == current clue
                
//                nonZero = indexFirstNonZero(activeClue);
                activeClue.set(clueIndex, 0); //Remove completed clue
                boardIndex+=rowState.get(stateIndex); //Go to boardIndex of next Empty/Cross square
                if (!(boardIndex>=board.getDimensions()[0])){ //If within bounds             
                    board.crossSquare(boardIndex, coord); //Cross square
                    rowState = RowState(coord); //Update rowState
                }
                if (!activeClue.stream().anyMatch(num -> num != 0)){ //If no more clues, return
                    return;
                }
                clueIndex++; //Go to next clue
            }
            stateIndex++;
            if (clueIndex>activeCopyClue.size()-1 || stateIndex>rowState.size()-1){
                System.out.println("End of array reached in rowChecker, intended or not? forwards row: " + coord);
                return;
            }
            if (rowState.get(stateIndex).equals(0)){boardIndex++;}
        }
        
        stateIndex = rowState.size()-1;
        clueIndex = activeCopyClue.size()-1;
        boardIndex = board.getDimensions()[0]-1;
        while (rowState.get(stateIndex)>-1){              
            if (rowState.get(stateIndex).equals(activeCopyClue.get(clueIndex))){
                
//                nonZero = indexLastNonZero(activeClue);
                activeClue.set(clueIndex, 0);
                boardIndex-=rowState.get(stateIndex);
                if (!(boardIndex<=-1)){                   
                    board.crossSquare(boardIndex, coord);
                    rowState = RowState(coord);
                }
                if (!activeClue.stream().anyMatch(num -> num != 0)){
                    return;
                }
                clueIndex--;
            }
            stateIndex--;
            if (clueIndex<0 || stateIndex<0){
                System.out.println("End of array reached in rowChecker, intended or not? backwards row: " + coord);
                return;
            }
            if (rowState.get(stateIndex).equals(0)){
                boardIndex--;
            }
        }        
    }
    
    /**
     * This method extends filled squares based on the distance from the edges and the clues provided for each row, for all rows in the puzzle grid.
     * It iterates through all horizontal clues, calling the `rowEdgeExtender` method for each row.
     * Should be used after one or both checkers
     * 
     * @param frame The JFrame that displays the board.
     * @param speed The speed at which the board is updated during the extension process.
     * @throws InterruptedException If the thread running the method is interrupted during the process.
     */
    private void allRowEdgeExtend(JFrame frame, int speed) throws InterruptedException{
        for (int i = 0; i<horizClues.size(); i++){
            System.out.println("Currently extending row: " + i);
            rowEdgeExtender(i, frame, speed);
        }
    }
    
    /**
    * This method extends filled squares based on the distance from the edges and the clues provided the given row.
    * It first checks the row from left to right, then from right to left, filling squares and crossing squares
    * where necessary according to the clues.
    * Doesn't set completed clues to 0, as doing so will likely become obsolete
    * 
    * @param coord The index of the row to be extended.
    * @param frame The JFrame that displays the board.
    * @param speed The speed at which the board is updated during the extension process.
    * @throws InterruptedException If the thread running the method is interrupted during the process.
    */
    private void rowEdgeExtender(int coord, JFrame frame, int speed) throws InterruptedException{
        
        ArrayList<Integer> activeCopyClue = horizCluesCopy.get(coord).getClue();
        ArrayList<Integer> rowState = RowState(coord);
        int clueIndex;
        int boardIndex;
        int distanceFromEdge;
        
        System.out.println("beginning forwards");
        clueIndex = 0;
        boardIndex = 0;
        
        //for every number in rowState ascending, maybe shoulda been a while but honestly too complex for my feeble mind
        for (int stateIndex = 0; stateIndex<rowState.size(); stateIndex++){
            
            //if number is positive (Square is FILLED) AND number is lower than current clue -> extend rightwards
            if (rowState.get(stateIndex) > 0 && rowState.get(stateIndex)<activeCopyClue.get(clueIndex)){
                
                //Left is directly board edge -> go full distance of clue
                if (stateIndex == 0){
                    if (rowState.get(stateIndex+1)<0){                        
                        for (int x = rowState.get(stateIndex); x < activeCopyClue.get(clueIndex); x++){                   
                            board.fillSquare(x, coord);
                            slowProgression(frame, speed);
                        }
                        //if not at edge, CROSS next square
                        if (activeCopyClue.get(clueIndex) != board.getDimensions()[0]){
                            board.crossSquare(activeCopyClue.get(clueIndex), coord);
                            slowProgression(frame, speed);
                        }
                        if (clueIndex+1 < activeCopyClue.size()){clueIndex++;} else {break;}//go to next clue unless all clues done, then break
                    }
                }    
                //Left is CROSS or distant edge-> go distance equal to 'current clue'-'distance form edge'
                else if (stateIndex+1<rowState.size()){
                    if (rowState.get(stateIndex+1)<0){
                        distanceFromEdge = distanceToEdge(rowState, stateIndex, false);
                        // If I'm close enough to the edge the current clue can fill some squares
                        if (distanceFromEdge + rowState.get(stateIndex)<activeCopyClue.get(clueIndex)){
                            for (int x = boardIndex+rowState.get(stateIndex); x<boardIndex + activeCopyClue.get(clueIndex) - distanceFromEdge; x++){
                                board.fillSquare(x, coord);
                                slowProgression(frame, speed);                           
                            }
                            //if not at edge and next to edge, CROSS next square
                            if (boardIndex + activeCopyClue.get(clueIndex) - distanceFromEdge != board.getDimensions()[0] && distanceFromEdge == 0){
                                board.crossSquare(boardIndex + activeCopyClue.get(clueIndex) - distanceFromEdge, coord);
                                slowProgression(frame, speed);
                            }      
                            if (clueIndex+1 < activeCopyClue.size()){clueIndex++;} else {break;}//go to next clue unless all clues done, then break
                        } 
                        //Possibility to handle special case where distanceFromEdge + rowState.get(stateIndex)==activeCopyClue.get(clueIndex) here
                        //also possibility to handle special cases where based on the clues and the distance we can tell if it's possible to keep track of the clue we are on (advanced logic, might be too lacking in usefulness)
                        else {break;} //Found a filled square too far from an edge too fill anything, thus, it is impossible to know if the filled Square we are on actually corresponds to the current clue
                    }
                }        
                
                rowState = RowState(coord); //update rowState
                if (rowState.stream().allMatch(num -> num < 0)){return;} //if row doesn't contain any EMPTY squares, return
                
                boardIndex+=rowState.get(stateIndex);
                
            // if Number is equal to current clue, go to next clue
            } else if (rowState.get(stateIndex).equals(activeCopyClue.get(clueIndex))){
                if (clueIndex+1 < activeCopyClue.size()){clueIndex++;} else {break;}//go to next clue unless all clues done, then break
                boardIndex+=rowState.get(stateIndex);
            } 
            //if Number is <= 0 then update boardIndex and continue
            else{
                if (rowState.get(stateIndex)==0) {boardIndex++;}//advance on CROSS
                else{
                    //If we meet an EMPTY space larger than the current clue, we cannot be sure the next FILLED we find corresponds to the current clue, thus, break;
                    //(maybe change inequality if further logic is added)
                    if (-rowState.get(stateIndex)>activeCopyClue.get(clueIndex)){break;}
                    boardIndex-=(rowState.get(stateIndex));//advance by amount of EMPTY (is negative)
                }
            }
        }
        
        System.out.println("beginning backwards");
        clueIndex = activeCopyClue.size()-1;
        boardIndex = board.getDimensions()[0]-1;
        
        rowState = new ArrayList<Integer>(RowState(coord).reversed());
        //for every number in rowState descending
        for (int stateIndex = 0; stateIndex<rowState.size(); stateIndex++){
            
            //if number is positive (Square is FILLED) AND number is lower than current clue -> extend rightwards
            if (rowState.get(stateIndex) > 0 && rowState.get(stateIndex)<activeCopyClue.get(clueIndex)){
                
                //Left is board edge -> go full distance of clue
                if (stateIndex == 0){
                    if (rowState.get(stateIndex+1)<0){                        
                        for (int x = board.getDimensions()[0]-1 - rowState.get(stateIndex); x > board.getDimensions()[0]-1 - activeCopyClue.get(clueIndex); x--){                   
                            board.fillSquare(x, coord);
                            slowProgression(frame, speed);
                        }
                        //if not at edge, CROSS next square
                        if (board.getDimensions()[0]-1 - activeCopyClue.get(clueIndex) != -1){
                            board.crossSquare(board.getDimensions()[0]-1 - activeCopyClue.get(clueIndex), coord);
                            slowProgression(frame, speed);
                        }
                        if (clueIndex-1 > -1){clueIndex--;} else {return;}//go to next clue unless all clues done, then return
                    }
                }    
                //Left is CROSS -> go distance equal to 'current clue'-'distance form edge'
                else if (stateIndex+1<rowState.size()){
                    if (rowState.get(stateIndex+1)<0){
                        distanceFromEdge = distanceToEdge(rowState, stateIndex, false);
                        // If I'm close enough to the edge the current clue can fill some squares
                        if (distanceFromEdge + rowState.get(stateIndex)<activeCopyClue.get(clueIndex)){
                            for (int x = boardIndex-rowState.get(stateIndex); x>boardIndex - activeCopyClue.get(clueIndex) + distanceFromEdge; x--){
                                board.fillSquare(x, coord);
                                slowProgression(frame, speed);                           
                            }
                            //if not at edge and next to edge, CROSS next square
                            if (boardIndex - activeCopyClue.get(clueIndex) + distanceFromEdge != board.getDimensions()[0] && distanceFromEdge == 0){
                                board.crossSquare(boardIndex - activeCopyClue.get(clueIndex) + distanceFromEdge, coord);
                                slowProgression(frame, speed);
                            }      
                            if (clueIndex-1 > -1){clueIndex--;} else {return;}//go to next clue unless all clues done, then return
                        } 
                        //Possibility to handle special case where distanceFromEdge + rowState.get(stateIndex)==activeCopyClue.get(clueIndex) here
                        //also possibility to handle special cases where based on the clues and the distance we can tell if it's possible to keep track of the clue we are on (advanced logic, might be too lacking in usefulness)
                        else {break;} //Found a filled square too far from an edge too fill anything, thus, it is impossible to know if the filled Square we are on actually corresponds to the current clue
                    }
                }        
                
                rowState = RowState(coord); //update rowState
                rowState = new ArrayList<Integer>(rowState.reversed());
                if (rowState.stream().allMatch(num -> num < 0)){return;} //if row doesn't contain any EMPTY squares, return
                
                boardIndex-=rowState.get(stateIndex);
                
            // if Number is equal to current clue, go to next clue
            } else if (rowState.get(stateIndex).equals(activeCopyClue.get(clueIndex))){
                if (clueIndex-1 > -1){clueIndex--;} else {return;}//go to next clue unless all clues done, then return
                boardIndex-=rowState.get(stateIndex);
            } 
            //if Number is <= 0 then update boardIndex and continue
            else{
                if (rowState.get(stateIndex)==0) {boardIndex--;}//advance on CROSS
                else{
                    //If we meet an EMPTY space larger than the current clue, we cannot be sure the next FILLED we find corresponds to the current clue, thus, return;
                    //(maybe change inequality if further logic is added)
                    if (-rowState.get(stateIndex)>activeCopyClue.get(clueIndex)){return;}
                    boardIndex+=(rowState.get(stateIndex));//advance by amount of EMPTY (is negative)
                }
            }
        } 
    }
    
    /**
     * This method extends filled squares based on the distance from the edges and the clues provided for each column, for all columns in the puzzle grid.
     * It iterates through all vertical clues, calling the `columnEdgeExtender` method for each row.
     * Should be used after one or both checkers
     * 
     * @param frame The JFrame that displays the board.
     * @param speed The speed at which the board is updated during the extension process.
     * @throws InterruptedException If the thread running the method is interrupted during the process.
     */
    private void allColumnEdgeExtend(JFrame frame, int speed) throws InterruptedException{
        for (int i = 0; i<vertClues.size(); i++){
            System.out.println("Currently extending column: " + i);
            columnEdgeExtender(i, frame, speed);
        }
    }
    
    /**
    * This method extends filled squares based on the distance from the edges and the clues provided the given column.
    * It first checks the column from top to bottom, then from bottom to top, filling squares and crossing squares
    * where necessary according to the clues.
    * Doesn't set completed clues to 0, as doing so will likely become obsolete
    * 
    * @param coord The index of the column to be extended.
    * @param frame The JFrame that displays the board.
    * @param speed The speed at which the board is updated during the extension process.
    * @throws InterruptedException If the thread running the method is interrupted during the process.
    */
    private void columnEdgeExtender(int coord, JFrame frame, int speed) throws InterruptedException{
        
        ArrayList<Integer> activeCopyClue = vertCluesCopy.get(coord).getClue();
        ArrayList<Integer> columnState = ColumnState(coord);
        int clueIndex;
        int boardIndex;
        int distanceFromEdge;
        
        System.out.println("beginning downwards");
        clueIndex = 0;
        boardIndex = 0;
        
        //for every number in columnState ascending, maybe shoulda been a while but honestly too complex for my feeble mind
        for (int stateIndex = 0; stateIndex<columnState.size(); stateIndex++){
            
            //if number is positive (Square is FILLED) AND number is lower than current clue -> extend downwards
            if (columnState.get(stateIndex) > 0 && columnState.get(stateIndex)<activeCopyClue.get(clueIndex)){
                
                //Up is directly board edge -> go full distance of clue
                if (stateIndex == 0){
                    if (columnState.get(stateIndex+1)<0){                        
                        for (int y = columnState.get(stateIndex); y < activeCopyClue.get(clueIndex); y++){                   
                            board.fillSquare(coord, y);
                            slowProgression(frame, speed);
                        }
                        //if not at edge, CROSS next square
                        if (activeCopyClue.get(clueIndex) != board.getDimensions()[1]){
                            board.crossSquare(coord, activeCopyClue.get(clueIndex));
                            slowProgression(frame, speed);
                        }
                        if (clueIndex+1 < activeCopyClue.size()){clueIndex++;} else {break;}//go to next clue unless all clues done, then break
                    }
                }    
                //Up is CROSS or distant edge-> go distance equal to 'current clue'-'distance from edge'
                else if (stateIndex+1<columnState.size()){
                    if (columnState.get(stateIndex+1)<0){
                        distanceFromEdge = distanceToEdge(columnState, stateIndex, false);
                        // If I'm close enough to the edge the current clue can fill some squares
                        if (distanceFromEdge + columnState.get(stateIndex)<activeCopyClue.get(clueIndex)){
                            for (int y = boardIndex+columnState.get(stateIndex); y<boardIndex + activeCopyClue.get(clueIndex) - distanceFromEdge; y++){
                                board.fillSquare(coord, y);
                                slowProgression(frame, speed);                           
                            }
                            //if not at edge and next to edge, CROSS next square
                            if (boardIndex + activeCopyClue.get(clueIndex) - distanceFromEdge != board.getDimensions()[0] && distanceFromEdge == 0){
                                board.crossSquare(coord, boardIndex + activeCopyClue.get(clueIndex) - distanceFromEdge);
                                slowProgression(frame, speed);
                            }      
                            if (clueIndex+1 < activeCopyClue.size()){clueIndex++;} else {break;}//go to next clue unless all clues done, then break
                        } 
                        //Possibility to handle special case where distanceFromEdge + columnState.get(stateIndex)==activeCopyClue.get(clueIndex) here
                        //also possibility to handle special cases where based on the clues and the distance we can tell if it's possible to keep track of the clue we are on (advanced logic, might be too lacking in usefulness)
                        else {break;} //Found a filled square too far from an edge too fill anything, thus, it is impossible to know if the filled Square we are on actually corresponds to the current clue
                    }
                }        
                
                columnState = ColumnState(coord); //update columnState
                if (columnState.stream().allMatch(num -> num < 0)){return;} //if column doesn't contain any EMPTY squares, return
                
                boardIndex+=columnState.get(stateIndex);
                
            // if Number is equal to current clue, go to next clue
            } else if (columnState.get(stateIndex).equals(activeCopyClue.get(clueIndex))){
                if (clueIndex+1 < activeCopyClue.size()){clueIndex++;} else {break;}//go to next clue unless all clues done, then break
                boardIndex+=columnState.get(stateIndex);
            } 
            //if Number is <= 0 then update boardIndex and continue
            else{
                if (columnState.get(stateIndex)==0) {boardIndex++;}//advance on CROSS
                else{
                    //If we meet an EMPTY space larger than the current clue, we cannot be sure the next FILLED we find corresponds to the current clue, thus, break;
                    //(maybe change inequality if further logic is added)
                    if (-columnState.get(stateIndex)>activeCopyClue.get(clueIndex)){break;}
                    boardIndex-=(columnState.get(stateIndex));//advance by amount of EMPTY (is negative)
                }
            }
        }
        
        System.out.println("beginning backwards");
        clueIndex = activeCopyClue.size()-1;
        boardIndex = board.getDimensions()[1]-1;
        
        columnState = new ArrayList<Integer>(ColumnState(coord).reversed());
        //for every number in columnState descending
        for (int stateIndex = 0; stateIndex<columnState.size(); stateIndex++){
            
            //if number is positive (Square is FILLED) AND number is lower than current clue -> extend upwards
            if (columnState.get(stateIndex) > 0 && columnState.get(stateIndex)<activeCopyClue.get(clueIndex)){
                
                //Down is board edge -> go full distance of clue
                if (stateIndex == 0){
                    if (columnState.get(stateIndex+1)<0){                        
                        for (int y = board.getDimensions()[0]-1 - columnState.get(stateIndex); y > board.getDimensions()[0]-1 - activeCopyClue.get(clueIndex); y--){                   
                            board.fillSquare(coord, y);
                            slowProgression(frame, speed);
                        }
                        //if not at edge, CROSS next square
                        if (board.getDimensions()[1]-1 - activeCopyClue.get(clueIndex) != -1){
                            board.crossSquare(coord, board.getDimensions()[1]-1 - activeCopyClue.get(clueIndex));
                            slowProgression(frame, speed);
                        }
                        if (clueIndex-1 > -1){clueIndex--;} else {return;}//go to next clue unless all clues done, then return
                    }
                }    
                //Down is CROSS -> go distance equal to 'current clue'-'distance form edge'
                else if (stateIndex+1<columnState.size()){
                    if (columnState.get(stateIndex+1)<0){
                        distanceFromEdge = distanceToEdge(columnState, stateIndex, false);
                        // If I'm close enough to the edge the current clue can fill some squares
                        if (distanceFromEdge + columnState.get(stateIndex)<activeCopyClue.get(clueIndex)){
                            for (int y = boardIndex-columnState.get(stateIndex); y>boardIndex - activeCopyClue.get(clueIndex) + distanceFromEdge; y--){
                                board.fillSquare(coord, y);
                                slowProgression(frame, speed);                           
                            }
                            //if not at edge and next to edge, CROSS next square
                            if (boardIndex - activeCopyClue.get(clueIndex) + distanceFromEdge != board.getDimensions()[1] && distanceFromEdge == 0){
                                board.crossSquare(coord, boardIndex - activeCopyClue.get(clueIndex) + distanceFromEdge);
                                slowProgression(frame, speed);
                            }      
                            if (clueIndex-1 > -1){clueIndex--;} else {return;}//go to next clue unless all clues done, then return
                        } 
                        //Possibility to handle special case where distanceFromEdge + rowState.get(stateIndex)==activeCopyClue.get(clueIndex) here
                        //also possibility to handle special cases where based on the clues and the distance we can tell if it's possible to keep track of the clue we are on (advanced logic, might be too lacking in usefulness)
                        else {break;} //Found a filled square too far from an edge too fill anything, thus, it is impossible to know if the filled Square we are on actually corresponds to the current clue
                    }
                }        
                
                columnState = ColumnState(coord); //update columnState
                columnState = new ArrayList<Integer>(columnState.reversed());
                if (columnState.stream().allMatch(num -> num < 0)){return;} //if column doesn't contain any EMPTY squares, return
                
                boardIndex-=columnState.get(stateIndex);
                
            // if Number is equal to current clue, go to next clue
            } else if (columnState.get(stateIndex).equals(activeCopyClue.get(clueIndex))){
                if (clueIndex-1 > -1){clueIndex--;} else {return;}//go to next clue unless all clues done, then return
                boardIndex-=columnState.get(stateIndex);
            } 
            //if Number is <= 0 then update boardIndex and continue
            else{
                if (columnState.get(stateIndex)==0) {boardIndex--;}//advance on CROSS
                else{
                    //If we meet an EMPTY space larger than the current clue, we cannot be sure the next FILLED we find corresponds to the current clue, thus, return;
                    //(maybe change inequality if further logic is added)
                    if (-columnState.get(stateIndex)>activeCopyClue.get(clueIndex)){return;}
                    boardIndex+=(columnState.get(stateIndex));//advance by amount of EMPTY (is negative)
                }
            }
        } 
    }
    
}
