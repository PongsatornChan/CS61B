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
        assertTrue(checkBoard(initialBoardState, myBoard));
    }

    @Test
    public void copyTest() {
        Board myBoard = new Board();
        myBoard.init();
        Board copyBoard = new Board();
        copyBoard.copy(myBoard);
        assertTrue(checkBoard(initialBoardState, copyBoard));
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
    public void makeMoveTest() {
        Board myBoard = new Board();
        myBoard.init();
        myBoard.makeMove(Square.sq("d1"), Square.sq("d3"));
        myBoard.makeMove(Square.sq("a4"), Square.sq("c4"));
        myBoard.makeMove(Square.sq("d5"), Square.sq("d8"));
        assertTrue(checkBoard(movedState, myBoard));
    }

    @Test
    public void undoTest() {
        Board myBoard = new Board();
        myBoard.init();
        myBoard.makeMove(Square.sq("d1"), Square.sq("d3"));
        assertTrue(checkBoard(movedState2, myBoard));
        myBoard.undo();
        assertTrue(checkBoard(initialBoardState, myBoard));

        myBoard.makeMove(Square.sq("d1"), Square.sq("d3"));
        myBoard.makeMove(Square.sq("d1"), Square.sq("d3"));

    }

    @Test
    public void normTest() {
        System.out.println(Integer.MAX_VALUE);
    }

    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - 1 - row][col];
                b.put(piece, Square.sq(col, row));
            }
        }
        System.out.println(b);
    }

    boolean checkBoard(Piece[][] state, Board board) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = state[Board.SIZE - 1 - row][col];
                if (board.get(Square.sq(col, row)) != piece) {
                    return false;
                }
            }
        }
        return true;
    }

    static final Piece E = Piece.EMPTY;
    static final Piece W = Piece.WHITE;
    static final Piece B = Piece.BLACK;
    static final Piece K = Piece.KING;

    static final Piece[][] initialBoardState = {
            {E, E, E, B, B, B, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, E, W, E, E, E, E},
            {B, E, E, E, W, E, E, E, B},
            {B, B, W, W, K, W, W, B, B},
            {B, E, E, E, W, E, E, E, B},
            {E, E, E, E, W, E, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, B, B, B, E, E, E},
    };

    static final Piece[][] movedState = {
            {E, E, E, B, B, B, E, E, E},
            {E, E, E, W, B, E, E, E, E},
            {E, E, E, E, W, E, E, E, E},
            {B, E, E, E, W, E, E, E, B},
            {B, B, W, E, K, W, W, B, B},
            {B, E, E, E, W, E, E, E, B},
            {E, E, E, B, W, E, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, E, B, B, E, E, E},
    };

    static final Piece[][] movedState2 = {
            {E, E, E, B, B, B, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, E, W, E, E, E, E},
            {B, E, E, E, W, E, E, E, B},
            {B, B, W, W, K, W, W, B, B},
            {B, E, E, E, W, E, E, E, B},
            {E, E, E, B, W, E, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, E, B, B, E, E, E},
    };

}
