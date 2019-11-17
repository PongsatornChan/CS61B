package tablut;

import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;
import java.util.List;

/** Junit tests for our Tablut Board class.
 *  @author Vivant Sakore
 */
public class TablutTests {

    /** Run the JUnit tests in this package. */
    public static void main(String[] ignored) {
        textui.runClasses(TablutTests.class);
    }

    /**
     * Tests legalMoves for white pieces to make sure
     * it returns all legal Moves.
     * This method needs to be finished and may need to be changed
     * based on your implementation.
     */
    @Test
    public void testLegalWhiteMoves() {
        Board b = new Board();
        b.init();
        buildBoard(b, initialBoardState);

        List<Move> movesList = b.legalMoves(Piece.WHITE);

        assertEquals(56, movesList.size());

        assertFalse(movesList.contains(Move.mv("e7-8")));
        assertFalse(movesList.contains(Move.mv("e8-f")));

        assertTrue(movesList.contains(Move.mv("e6-f")));
        assertTrue(movesList.contains(Move.mv("f5-8")));
    }

    /**
     * Tests legalMoves for black pieces to make sure
     * it returns all legal Moves. This method needs
     * to be finished and may need to be changed
     * based on your implementation.
     */
    @Test
    public void testLegalBlackMoves() {
        Board b = new Board();
        b.init();
        buildBoard(b, initialBoardState);

        List<Move> movesList = b.legalMoves(Piece.BLACK);

        assertEquals(80, movesList.size());

        assertFalse(movesList.contains(Move.mv("e8-7")));
        assertFalse(movesList.contains(Move.mv("e7-8")));

        assertTrue(movesList.contains(Move.mv("f9-i")));
        assertTrue(movesList.contains(Move.mv("h5-1")));
    }

    @Test
    public void testCapture() {
        Board b = new Board();
        b.init();
        buildBoard(b, kingCap);
        b.makeMove(Square.sq("e8"), Square.sq("e6"));
        System.out.print(b);
        assertTrue(b.winner() == Piece.BLACK);

        b.init();
        buildBoard(b, kingCap2);
        b.makeMove(Square.sq("e2"), Square.sq("e3"));
        System.out.print(b);
        assertTrue(b.winner() == Piece.BLACK);

        b.init();
        buildBoard(b, kingCap3);
        b.makeMove(Square.sq("d4"), Square.sq("e4"));
        System.out.print(b);
        assertTrue(b.winner() == Piece.BLACK);
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

    static final Piece E = Piece.EMPTY;
    static final Piece W = Piece.WHITE;
    static final Piece B = Piece.BLACK;
    static final Piece K = Piece.KING;

    static Piece[][] initialBoardState = {
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

    static Piece[][] kingCap = {
            {E, E, E, B, B, B, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, E, E, E, E, E, E},
            {B, E, E, E, E, E, E, E, B},
            {B, B, W, B, K, B, W, B, B},
            {B, E, E, E, B, E, E, E, B},
            {E, E, E, E, W, E, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, B, B, B, E, E, E},
    };

    static Piece[][] kingCap2 = {
            {E, E, E, B, B, B, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, E, E, E, E, E, E},
            {B, E, E, E, E, E, E, E, B},
            {B, B, W, W, E, W, W, B, B},
            {B, E, E, B, K, B, E, E, B},
            {E, E, E, E, E, E, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, B, B, B, E, E, E},
    };

    static Piece[][] kingCap3 = {
            {E, E, E, B, B, B, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, E, E, E, E, E, E},
            {B, E, E, E, E, E, E, E, B},
            {B, B, W, W, E, W, W, B, B},
            {B, E, E, B, E, K, B, E, B},
            {E, E, E, E, E, E, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, B, B, B, E, E, E},
    };
}
