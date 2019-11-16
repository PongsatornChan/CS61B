package tablut;

import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;
import java.util.List;


public class BoardTest {

    public static void main(String[] ignored) {
        textui.runClasses(BoardTest.class);
    }

    @Test
    public void initTest() {
        Board myBoard = new Board();
        myBoard.init();
        System.out.println(myBoard.toString());

    }

    @Test
    public void copyTest() {
        Board myBoard = new Board();
        myBoard.init();
        Board copyBoard = new Board();
        copyBoard.copy(myBoard);
        System.out.println(copyBoard.toString());
    }

    @Test
    public void isUnblockedMoveTest() {
        Board myBoard = new Board();
        myBoard.init();
        Square from = Square.sq("d1");
        Square to1 = Square.sq("a1");
        Square to2 = Square.sq("d4");
        Square to3 = Square.sq("d5");
        Square to4 = Square.sq("d7");
        assertTrue(myBoard.isUnblockedMove(from, to1));
        assertTrue(myBoard.isUnblockedMove(from,to2));
        assertFalse(myBoard.isUnblockedMove(from, to3));
        assertFalse(myBoard.isUnblockedMove(from, to4));
    }

    @Test
    public void legalMovesTest() {
        Board myBoard = new Board();
        myBoard.init();
        System.out.println(myBoard.toString());
        System.out.println(myBoard.legalMoves(Piece.WHITE));
    }

    @Test
    public void makeMoveTest() {
        Board myBoard = new Board();
        myBoard.init();
        myBoard.makeMove(Square.sq("d1"), Square.sq("d3"));
        try{
            myBoard.makeMove(Square.sq("a4"), Square.sq("c4"));
        } catch (AssertionError e) {
            System.out.println(myBoard);
        }
//        myBoard.makeMove(Square.sq("c5"), Square.sq("c3"));
//        System.out.println(myBoard);

        myBoard.makeMove(Square.sq("d5"), Square.sq("d8"));
        System.out.println(myBoard);
    }

    @Test
    public void undoTest() {
        Board myBoard = new Board();
        myBoard.init();
        System.out.println(myBoard.encodedBoard());
        myBoard.makeMove(Square.sq("d1"), Square.sq("d3"));
        System.out.println(myBoard);
        myBoard.undo();
        System.out.println(myBoard);

    }

    @Test
    public void normTest() {
        System.out.println(Integer.MAX_VALUE);
    }

}
