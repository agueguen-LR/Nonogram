/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nonogram;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Adrien
 */
public class ActiveNonogram {
    
    private BufferedImage img;
    private Board board;
        
    public ActiveNonogram(String filename, int pixelSize) {        
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
                int blue  = (color >>>  0) & 0xFF;

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
                
        this.board = new Board(BrightnessMap.get(0).size(), BrightnessMap.size(), HorizontalClues, VerticalClues);
        
    }

    private ArrayList<ArrayList<Float>> ReduceArray(ArrayList<ArrayList<Float>> array, int factor){
        ArrayList<ArrayList<Float>> reducedArray = new ArrayList<ArrayList<Float>>();
        Float sumValues = 0f;
        for (int y = 0; y < array.size(); y+=factor){
            ArrayList<Float> reducedRow = new ArrayList<>();
            for (int x = 0; x < array.get(0).size(); x+=factor){                
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
    
    public Board getBoard() {
        return board;
    }
    
    @Override
    public String toString() {
        return "ActiveNonogram{" + "img=" + img + '}';
    }  
    
}
