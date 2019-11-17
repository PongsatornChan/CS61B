package tablut;

import java.util.List;

import static java.lang.Math.*;

import static tablut.Board.WTHRONE;
import static tablut.Board.STHRONE;
import static tablut.Board.ETHRONE;
import static tablut.Board.NTHRONE;
import static tablut.Square.SQUARE_LIST;
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
        Move aiMove = findMove();
        _controller.reportMove(aiMove);
        return aiMove.toString();
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
        if (myPiece() == WHITE) {
            findMove(b, 4, true, 1, -1 * WILL_WIN_VALUE, WILL_WIN_VALUE);
        } else {
            findMove(b, 4, true, -1, -1 * WILL_WIN_VALUE, WILL_WIN_VALUE);
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
        if (depth == 1 || board.winner() != null) {
            return staticScore(board);
        } else {
            int bestSoFar = -1 * sense * INFTY;
            if (sense == 1) {
                for (Move m : board.legalMoves(board.turn())) {
                    board.makeMove(m);
                    int score = findMove(board, depth - 1,
                            false, sense * -1, alpha, beta);
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
                    int score = findMove(board, depth - 1,
                            false, sense * -1, alpha, beta);
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
        return _maxDepth;
    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        if (board.winner() == WHITE) {
            return WINNING_VALUE;
        } else if (board.winner() == BLACK) {
            return -1 * WINNING_VALUE;
        } else {
            int sum = 0;
            sum += kingCondition(board);
            for (Square s : SQUARE_LIST) {
                if (board.get(s) == BLACK) {
                    sum -= _blackValue;
                } else if (board.get(s) == WHITE) {
                    sum += _whiteValue;
                }
            }
            return sum;
        }
    }

    /** Return a heuristic value considering the
     *  position and surrounding of KING in
     *  BOARD. (Experimental)
     */
    private int kingCondition(Board board) {
        int sum = 0;
        Square king = board.kingPosition();
        if (king == THRONE
                || king == NTHRONE
                || king == ETHRONE
                || king == STHRONE
                || king == WTHRONE) {
            sum += _safeBonus;
            for (int i = 0; i < 4; i++) {
                List<Square> rookMoves = Square.ROOK_SQUARES[king.index()][i];
                for (Square s : rookMoves) {
                    if (board.get(s) == WHITE) {
                        sum += 5;
                    } else if (board.get(s) == BLACK) {
                        sum -= 5;
                    }
                }
            }
        } else {
            int edges = 0;
            for (int i = 0; i < 4; i++) {
                List<Square> rookMoves = Square.ROOK_SQUARES[king.index()][i];
                for (Square s : rookMoves) {
                    if (board.get(s) == WHITE) {
                        sum += 5;
                        break;
                    } else if (board.get(s) == BLACK) {
                        sum -= _blackValue * 2;
                        break;
                    } else if (s.isEdge()) {
                        edges++;
                    }
                }
            }
            if (edges > 1) {
                sum += _edgeScore;
            }
        }
        return sum;
    }

    /** force win straetrgy. */
    private static int _edgeScore = 100 * 100;

    /** max depth for which the search should stop. */
    private static int _maxDepth = 4;

    /** heuristic value for KING in CASTLE area. */
    private final int _safeBonus = 10;

    /** heuristic value of a black piece. */
    private final int _blackValue = 10;

    /** heuristic value of a white piece. */
    private final int _whiteValue = 20;
}
