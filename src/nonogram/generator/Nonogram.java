/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nonogram.generator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * The Nonogram class represents a nonogram puzzle generator.
 * It reads an image file, processes its brightness values, and generates clues for the puzzle.
 * The puzzle is represented by a Board object.
 *
 * @author agueguen-LR
 */
public class Nonogram {

    private BufferedImage img;
    private final Board board;

    /**
     * Constructs a Nonogram object by reading an image file and generating the puzzle clues.
     *
     * @param filename the name of the image file to read
     * @param pixelSize the size of the pixel block to reduce the image to
     */
    public Nonogram(String filename, int pixelSize) {
        try {
            img = ImageIO.read(new File(filename));
            System.out.println("Successfully Loaded Bitmap");
        } catch (IOException e) {
            System.out.println("Couldn't Load Bitmap");
        }

        ArrayList<ArrayList<Float>> BrightnessMap = new ArrayList<>();
        // for each pixel of the bitmap
        for(int y=0; y<img.getHeight(); y++){
            ArrayList<Float> BrightnessRow = new ArrayList<>();
            for(int x=0; x<img.getWidth(); x++){

                int color = img.getRGB(x, y);

                // extract each color component
                int red   = (color >>> 16) & 0xFF;
                int green = (color >>>  8) & 0xFF;
                int blue  = (color) & 0xFF;

                // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
                Float luminance = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;
                BrightnessRow.add(luminance);
            }
            BrightnessMap.add(BrightnessRow);
        }

        ArrayList<Clue> HorizontalClues = new ArrayList<>();
        ArrayList<Clue> VerticalClues = new ArrayList<>();

        BrightnessMap = ReduceArray(BrightnessMap, pixelSize);

        for (ArrayList<Float> row : BrightnessMap){
            HorizontalClues.add(new Clue(row));
        }
        for (int i = 0; i<BrightnessMap.size(); i++){
            ArrayList<Float> column = new ArrayList<>();
            for (ArrayList<Float> row : BrightnessMap){
                column.add(row.get(i));
            }
            VerticalClues.add(new Clue(column));
        }

        this.board = new Board(BrightnessMap.getFirst().size(), BrightnessMap.size(), HorizontalClues, VerticalClues);

    }

    /**
     * Reduces the size of a 2D array by averaging blocks of values.
     *
     * @param array the original 2D array to reduce
     * @param factor the factor by which to reduce the array
     * @return the reduced 2D array
     */
    ArrayList<ArrayList<Float>> ReduceArray(ArrayList<ArrayList<Float>> array, int factor){
        ArrayList<ArrayList<Float>> reducedArray = new ArrayList<>();
        Float sumValues = 0f;
        for (int y = 0; y < array.size(); y+=factor){
            ArrayList<Float> reducedRow = new ArrayList<>();
            for (int x = 0; x < array.getFirst().size(); x+=factor){
                for (int i = 0; i < factor; i++){
                    for (int j = 0; j < factor; j++){
                        sumValues += array.get(y+i).get(x+j);
                    }
                }
                reducedRow.add(sumValues/(factor*factor));
                sumValues = 0f;
            }
            reducedArray.add(reducedRow);
        }
        return reducedArray;
    }

    /**
     * Returns the Board object representing the nonogram puzzle.
     *
     * @return the Board object
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns a string representation of the Nonogram object.
     *
     * @return a string representation of the Nonogram object
     */
    @Override
    public String toString() {
        return "Nonogram{" + "board=" + board + '}';
    }

}