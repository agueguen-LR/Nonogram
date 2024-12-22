/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package nonogram;

import nonogram.generator.BoardGenerator;
import nonogram.generator.Board;
import nonogram.solver.BoardCompleter;

/**
 *
 * @author agueguen-LR
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {

//        BoardGenerator Camera = new BoardGenerator("resources/Camera.bmp", 1);
//        Board board = Camera.getBoard();
        BoardGenerator CatPot_Large = new BoardGenerator("resources/CatPot_Large.png", 20);
        Board board = CatPot_Large.getBoard();

        BoardCompleter solver = new BoardCompleter(board);
        solver.BeginSolving(3);

    }
    
}
