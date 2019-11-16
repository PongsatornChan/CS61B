package tablut;

import java.util.List;

import static java.lang.Math.*;

import static tablut.Square.sq;
import static tablut.Board.THRONE;
import static tablut.Piece.*;

/** A Player that automatically generates moves.
 *  @author Pongsatorn Chanpanichravee
 */
class AI extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A position-score magnitude indicating a forced win in a subsequent
     *  move.  This differs from WINNING_VALUE to avoid putting off wins. */
    private static final int WILL_WIN_VALUE = Integer.MAX_VALUE - 40;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        return findMove().toString(); // FIXME

    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        _lastFoundMove = null;
        // FIXME
        if (myPiece() == WHITE) {
            findMove(b, 4, true, 1, -1 * INFTY, INFTY);
        } else {
            findMove(b, 4, true, -1, -1 * INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        // FIXME
        if (depth == 0) {
            return staticScore(board);
        } else {
            int bestSoFar = -1 * sense * INFTY;
            if (sense == 1) {
                for (Move m : board.legalMoves(board.turn())) {
                    board.makeMove(m);
                    int score = findMove(board, depth - 1, false, sense * -1, alpha, beta);
                    if (score >= bestSoFar) {
                        bestSoFar = score;
                        alpha = max(alpha, bestSoFar);
                        if (saveMove) {
                            _lastFoundMove = m;
                        }
                        if (beta <= alpha) {
                            board.undo();
                            break;
                        }
                    }
                    board.undo();
                }
            } else if (sense == -1) {
                for (Move m : board.legalMoves(board.turn())) {
                    board.makeMove(m);
                    int score = findMove(board, depth - 1, false, sense * -1, alpha, beta);
                    if (score <= bestSoFar) {
                        bestSoFar = score;
                        beta = min(beta, bestSoFar);
                        if (saveMove) {
                            _lastFoundMove = m;
                        }
                        if (beta <= alpha) {
                            board.undo();
                            break;
                        }
                    }
                    board.undo();
                }
            }
            return bestSoFar;
        }
    }


    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private static int maxDepth(Board board) {
        return _maxDepth; // FIXME?
    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        // FIXME
        if (board.winner() == WHITE) {
            return WINNING_VALUE;
        } else if (board.winner() == BLACK) {
            return -1 * WINNING_VALUE;
        } else if (willWin(board) != 0) {
            return willWin(board) * WILL_WIN_VALUE;
        }
        return 0;
    }

    // FIXME: More here.

    private int willWin(Board board) {
        List<Move> moveLst = board.legalMoves(WHITE);
        return 0;
    }

    private static int _maxDepth = 4;
}
