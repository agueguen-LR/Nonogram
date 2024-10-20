/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package nonogram;

/**
 *
 * @author Adrien
 */
public class Nonogram {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        
//        Board board = new Board(20, 20);

//        ActiveNonogram Camera = new ActiveNonogram("Camera.bmp", 1);
//        Board board = Camera.getBoard();
//        ActiveNonogram Test = new ActiveNonogram("Test.bmp", 1);
//        Board board = Test.getBoard();
//        ActiveNonogram Test2 = new ActiveNonogram("Test2.bmp", 1);
//        Board board = Test2.getBoard();
        ActiveNonogram CatPot_Large = new ActiveNonogram("CatPot_Large.png", 6);
        Board board = CatPot_Large.getBoard();
        Solver solver = new Solver(board);
        solver.BeginSolving(0);

//        System.out.println("\nUP");
//        System.out.println("Column 0");
//        solver.currentSideLength(Solver.direction.UP, 0);
//        System.out.println("Column 1");
//        solver.currentSideLength(Solver.direction.UP, 1);
//        System.out.println("Column 2");
//        solver.currentSideLength(Solver.direction.UP, 2);
//        System.out.println("Column 18");
//        solver.currentSideLength(Solver.direction.UP, 18);
//        
//        System.out.println("\nDOWN");
//        System.out.println("Column 0");
//        solver.currentSideLength(Solver.direction.DOWN, 0);
//        System.out.println("Column 1");
//        solver.currentSideLength(Solver.direction.DOWN, 1);
//        System.out.println("Column 3");
//        solver.currentSideLength(Solver.direction.DOWN, 3);
//        System.out.println("Column 18");
//        solver.currentSideLength(Solver.direction.DOWN, 18);
//                
//        System.out.println("\nRIGHT");
//        System.out.println("Column 0");
//        solver.currentSideLength(Solver.direction.RIGHT, 0);
//        System.out.println("Column 1");
//        solver.currentSideLength(Solver.direction.RIGHT, 1);
//        System.out.println("Column 2");
//        solver.currentSideLength(Solver.direction.RIGHT, 2);
//        System.out.println("Column 18");
//        solver.currentSideLength(Solver.direction.RIGHT, 18);
//        
//        System.out.println("\nLEFT");
//        System.out.println("Column 0");
//        solver.currentSideLength(Solver.direction.LEFT, 0);
//        System.out.println("Column 1");
//        solver.currentSideLength(Solver.direction.LEFT, 1);
//        System.out.println("Column 3");
//        solver.currentSideLength(Solver.direction.LEFT, 3);
//        System.out.println("Column 18");
//        solver.currentSideLength(Solver.direction.LEFT, 18);
        
//        board.fillSquare(0,0);
//        board.crossSquare(1,0);
//        System.out.println(board);
//        System.out.println(Camera);
        

        
    }
    
}
