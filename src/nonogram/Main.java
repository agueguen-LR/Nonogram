/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package nonogram;

import nonogram.generator.BoardGenerator;
import nonogram.generator.Board;
import nonogram.solver.BoardCompleter;
import nonogram.solver.Solver;

/**
 *
 * @author agueguen-LR
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {

//        ActiveNonogram Camera = new ActiveNonogram("Camera.bmp", 1);
//        Board board = Camera.getBoard();
//        ActiveNonogram Test = new ActiveNonogram("Test.bmp", 1);
//        Board board = Test.getBoard();
//        ActiveNonogram Test2 = new ActiveNonogram("Test2.bmp", 1);
//        Board board = Test2.getBoard();
        BoardGenerator CatPot_Large = new BoardGenerator("resources/CatPot_Large.png", 12);
        Board board = CatPot_Large.getBoard();

        BoardCompleter solver = new BoardCompleter(board);
        solver.BeginSolving(1);

//        Solver solver = new Solver(board);
//        solver.BeginSolving(1);
        
    }
    
}
