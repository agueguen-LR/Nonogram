package nonogram.generator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.Serial;
import java.util.ArrayList;
import javax.swing.JPanel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Represents the nonogram board.
 * The board consists of a grid of squares and clues for each row and column.
 * The board can be painted on a JPanel.
 * The board can be queried for its dimensions and individual squares.
 * The board can also be updated by filling or crossing squares.
 * The toString method provides a string representation of the board.
 * The paintComponent method draws the board and clues on the JPanel.
 *
 * @see Square
 * @see Clue
 * @see JPanel
 *
 * @author agueguen-LR
 */
public class Board extends JPanel {
    @Serial
    private static final long serialVersionUID = 7148504598535036003L;

    /**
     * The grid of squares representing the board.
     */
    private final ArrayList<ArrayList<Square>> board = new ArrayList<>();

    /**
     * The list of horizontal clues.
     */
    private final ArrayList<Clue> HorizontalClues;

    /**
     * The list of vertical clues.
     */
    private final ArrayList<Clue> VerticalClues;

    /**
     * Constructs a new Board with the specified dimensions and clues.
     *
     * @param dimensionX the number of columns in the board
     * @param dimensionY the number of rows in the board
     * @param HorizontalClues the list of horizontal clues
     * @param VerticalClues the list of vertical clues
     */
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

    /**
     * Returns the list of horizontal clues.
     *
     * @return the list of horizontal clues
     */
    public ArrayList<Clue> getHorizontalClues() {
        return HorizontalClues;
    }

    /**
     * Returns the list of vertical clues.
     *
     * @return the list of vertical clues
     */
    public ArrayList<Clue> getVerticalClues() {
        return VerticalClues;
    }

    /**
     * Fills the square at the specified coordinates.
     *
     * @param Xcoord the x-coordinate of the square
     * @param Ycoord the y-coordinate of the square
     */
    public void fillSquare(int Xcoord, int Ycoord){
        board.get(Ycoord).get(Xcoord).fill();
    }

    /**
     * Crosses the square at the specified coordinates.
     *
     * @param Xcoord the x-coordinate of the square
     * @param Ycoord the y-coordinate of the square
     */
    public void crossSquare(int Xcoord, int Ycoord){
        board.get(Ycoord).get(Xcoord).cross();
    }

    /**
     * Returns the dimensions of the board.
     *
     * @return an array containing the number of columns and rows in the board
     */
    public int[] getDimensions(){
        return new int[]{board.getFirst().size(), board.size()};
    }

    /**
     * Returns the square at the specified coordinates.
     *
     * @param x the x-coordinate of the square
     * @param y the y-coordinate of the square
     * @return the square at the specified coordinates
     */
    public Square getSquare(int x, int y){
        return board.get(y).get(x);
    }

    /**
     * Returns a string representation of the board.
     * The string representation includes the horizontal clues and the state of each square.
     *
     * @return the string representation of the board
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i<board.size(); i++){
            string.append(HorizontalClues.get(i)).append(board.get(i).toString()).append('\n');
        }
        return string.toString();
    }

    /**
     * Paints the board and clues on the JPanel.
     *
     * @param g the Graphics object used for painting
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        var Xcount = board.getFirst().size();
        var Ycount = board.size();

        // Calculate the font size based on the square length
        int fontSize = (int) (((double) getHeight() / Ycount) * 0.6);
        g.setFont(new Font("Courier New", Font.BOLD, fontSize));

        // Get FontMetrics to measure the size of the string
        FontMetrics metrics = g.getFontMetrics();

        // Find the longest clue horizontally
        Clue maxHoriz = new Clue();
        for (Clue c : HorizontalClues){
            if (c.getClue().size()>maxHoriz.getClue().size()){
                maxHoriz = c;
            }
        }
        var maxClueWidth = metrics.stringWidth(maxHoriz.getClueString());

        // Find the longest clue vertically
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

        // Draw the clues
        // Horizontally
        for (int j = 0; j < Xcount; j++){
            g.drawString(HorizontalClues.get(j).getClueString(), Xoffset + (maxClueWidth-metrics.stringWidth(HorizontalClues.get(j).getClueString())), maxClueHeight + Yoffset + (squareLength + metrics.getAscent())/2 + squareLength * j);
        }
        // Vertically
        for (int k = 0; k < maxVert.getClue().size(); k++){
            for (int i = 0; i < Ycount; i++){
                try{
                    g.drawString(VerticalClues.get(i).getClue().get(VerticalClues.get(i).getClue().size()-k-1).toString(), maxClueWidth + Xoffset + squareLength * i + (squareLength - metrics.stringWidth(VerticalClues.get(i).getClue().get(VerticalClues.get(i).getClue().size()-k-1).toString())) / 2, Yoffset + maxClueHeight - k*metrics.getAscent());
                } catch (IndexOutOfBoundsException e){
                    // Less than k numbers in clue
                }
            }
        }
    }
}