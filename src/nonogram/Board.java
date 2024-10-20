package nonogram;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JPanel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adrien
 */
public class Board extends JPanel {
    private static final long serialVersionUID = 7148504598535036003L;
    
    private ArrayList<ArrayList<Square>> board = new ArrayList<>();
    private ArrayList<Clue> HorizontalClues = new ArrayList<>();
    private ArrayList<Clue> VerticalClues = new ArrayList<>();
    
    public Board(int dimensionX, int dimensionY, ArrayList<Clue> HorizontalClues, ArrayList<Clue> VerticalClues) {
        for (int y = 0; y<dimensionY; y++){
            ArrayList<Square> row = new ArrayList<>();
            for (int x = 0; x<dimensionX; x++){
                row.add(new Square());
            }
            board.add(row);
        }
        this.HorizontalClues = HorizontalClues;
        this.VerticalClues = VerticalClues;
    }

    public ArrayList<Clue> getHorizontalClues() {
        return HorizontalClues;
    }

    public ArrayList<Clue> getVerticalClues() {
        return VerticalClues;
    }

    public void fillSquare(int Xcoord, int Ycoord){
        board.get(Ycoord).get(Xcoord).fillSquare();
    }
    public void crossSquare(int Xcoord, int Ycoord){
        board.get(Ycoord).get(Xcoord).crossSquare();
    }
    
    public int[] getDimensions(){
        int[] dimensions = new int[]{board.get(0).size(), board.size()};
        return dimensions;
    }
    
    public Square getSquare(int x, int y){
        return board.get(y).get(x);
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i<board.size(); i++){
            string += HorizontalClues.get(i) + board.get(i).toString() + '\n';
        }
        return string;
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        var Xcount = board.get(0).size();
        var Ycount = board.size();
 
        // Calculate the font size based on the square length
        int fontSize = (int) ((getHeight() / Ycount) * 0.6);
        g.setFont(new Font("Courier New", Font.BOLD, fontSize));

        // Get FontMetrics to measure the size of the string
        FontMetrics metrics = g.getFontMetrics();
        
        //Find longest clue horizontally
        Clue maxHoriz = new Clue();
        for (Clue c : HorizontalClues){
            if (c.getClue().size()>maxHoriz.getClue().size()){
                maxHoriz = c;
            }
        }
        var maxClueWidth = metrics.stringWidth(maxHoriz.getClueString());
        
        //Find longest clue vertically
        Clue maxVert = new Clue();
        for (Clue c : VerticalClues){
            if (c.getClue().size()>maxVert.getClue().size()){
                maxVert = c;
            }
        }
        var maxClueHeight = maxVert.getClue().size()*metrics.getAscent();
             
        var squareLength = Integer.min((getHeight() - maxClueHeight) / Ycount , (getWidth() - maxClueWidth) / Xcount);
        var Xoffset = (getWidth() - (Xcount*squareLength+maxClueWidth)) / 2;
        var Yoffset = (getHeight() - (Ycount*squareLength+maxClueHeight)) / 2;       
        
        // Draw the grid
        for (int i = 0; i < Ycount; i++) {
            ArrayList<Square> row = board.get(i);
            for (int j = 0; j < Xcount; j++) {
                // Draw the squares
                g.setColor(Color.BLUE.darker());
                g.fillRect(maxClueWidth + Xoffset + squareLength * j, maxClueHeight + Yoffset + squareLength * i, squareLength, squareLength);
                if (row.get(j).isFilled()){
                    g.setColor(Color.BLACK);                    
                } else if (row.get(j).isCrossed()){
                    g.setColor(Color.GRAY.brighter());
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(maxClueWidth + 1 + Xoffset + squareLength * j, maxClueHeight + Yoffset + 1 + squareLength * i, squareLength - 2, squareLength - 2);
            }
        }
        
        g.setColor(Color.WHITE);
        
        //Draw the clues
        //Horizontally
        for (int j = 0; j < Xcount; j++){
            g.drawString(HorizontalClues.get(j).getClueString(), Xoffset + (maxClueWidth-metrics.stringWidth(HorizontalClues.get(j).getClueString())), maxClueHeight + Yoffset + (squareLength + metrics.getAscent())/2 + squareLength * j);
//            System.out.println("Drawing clue " + j);
        }
        //Vertically
        for (int k = 0; k < maxVert.getClue().size(); k++){
            for (int i = 0; i < Ycount; i++){    
                try{
                    g.drawString(VerticalClues.get(i).getClue().get(VerticalClues.get(i).getClue().size()-k-1).toString(), maxClueWidth + Xoffset + squareLength * i + (squareLength - metrics.stringWidth(VerticalClues.get(i).getClue().get(VerticalClues.get(i).getClue().size()-k-1).toString())) / 2, Yoffset + maxClueHeight - k*metrics.getAscent());
                } catch (IndexOutOfBoundsException e){
                    //Less than k numbers in clue
                }
                
            }
        }
        
    }    
    
}
