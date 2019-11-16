package tablut;

import java.util.*;

import static tablut.Move.ROOK_MOVES;
import static tablut.Piece.*;
import static tablut.Square.*;
import static tablut.Move.mv;


/** The state of a Tablut Game.
 *  @author Pongsatorn Chanpanichravee
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 9;

    /** The throne (or castle) square and its four surrounding squares.. */
    static final Square THRONE = sq(4, 4),
        NTHRONE = sq(4, 5),
        STHRONE = sq(4, 3),
        WTHRONE = sq(3, 4),
        ETHRONE = sq(5, 4);

    /** Initial positions of attackers. */
    static final Square[] INITIAL_ATTACKERS = {
        sq(0, 3), sq(0, 4), sq(0, 5), sq(1, 4),
        sq(8, 3), sq(8, 4), sq(8, 5), sq(7, 4),
        sq(3, 0), sq(4, 0), sq(5, 0), sq(4, 1),
        sq(3, 8), sq(4, 8), sq(5, 8), sq(4, 7)
    };

    /** Initial positions of defenders of the king. */
    static final Square[] INITIAL_DEFENDERS = {
        NTHRONE, ETHRONE, STHRONE, WTHRONE,
        sq(4, 6), sq(4, 2), sq(2, 4), sq(6, 4)
    };

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        if (model == this) {
            return;
        }
        init();

        pieceMap.clear();
        clearUndo();

        kingLocation = model.kingPosition();
        _winner = model.winner();
        _repeated = model.repeatedPosition();
        _turn = model.turn();
        _moveCount = model.moveCount();
        _maxMove = model.getMaxMove();
        pieceMap.putAll(model.getPieceMap());
        prevBoard = model.getPrevBoard();
    }

    /** Clears the board to the initial position. */
    void init() {

        _moveCount = 0;
        _maxMove = Integer.MAX_VALUE;
        _turn = BLACK;
        _repeated = false;
        _winner = null;
        pieceMap = new HashMap<Square, Piece>();
        prevBoard = new ArrayList<String>();
        kingLocation = null;
        for (Square s : SQUARE_LIST) {
            put(EMPTY, s);
        }
        for (int i = 0; i < INITIAL_ATTACKERS.length; i++) {
            put(BLACK, INITIAL_ATTACKERS[i]);
        }
        for (int i = 0; i < INITIAL_DEFENDERS.length; i++) {
            put(WHITE, INITIAL_DEFENDERS[i]);
        }
        revPut(KING, THRONE);
    }

    /** Set the move limit to LIM.  It is an error if 2*LIM <= moveCount(). */
    void setMoveLimit(int n) {
        _maxMove = n;
    }

    /** Return a Piece representing whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the winner in the current position, or null if there is no winner
     *  yet. */
    Piece winner() {
        return _winner;
    }

    /** Returns true iff this is a win due to a repeated position. */
    boolean repeatedPosition() {
        return _repeated;
    }

    /** Record current position and set winner() next mover if the current
     *  position is a repeat. */
    private void checkRepeated() {

        if (prevBoard.contains(encodedBoard())) {
            _repeated = true;
            _winner = turn().opponent();
        }
        prevBoard.add(encodedBoard());

    }

    /** Return the number of moves since the initial position that have not been
     *  undone. */
    int moveCount() {
        return _moveCount;
    }

    /** Return location of the king. */
    Square kingPosition() {
        for (Square s : SQUARE_LIST) {
            if (get(s) == KING) {
                if (s != kingLocation) {
                    System.out.println("kingLocation is wrong." + kingLocation);
                    kingLocation = s;
                }
                return s;
            }
        }
        System.out.println("Cannot find KING");
        return kingLocation;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return pieceMap.get(sq(col, row));
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        pieceMap.put(s, p);
        if (p == KING) {
            kingLocation = s;
            if (s.isEdge()) {
                _winner = WHITE;
            }
        }
    }

    /** Set square S to P and record for undoing. */
    final void revPut(Piece p, Square s) {
        put(p, s);

        checkRepeated();
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, sq(col - 'a', row - '1'));
    }

    /** Return true iff FROM - TO is an unblocked rook move on the current
     *  board.  For this to be true, FROM-TO must be a rook move and the
     *  squares along it, other than FROM, must be empty. */
    boolean isUnblockedMove(Square from, Square to) {
        assert from.isRookMove(to);
        int dir = from.direction(to);
        Square curr = from.rookMove(dir, 1);
        for (int i = 2; curr != to; i++) {
            if (pieceMap.get(curr) != EMPTY) {
                return false;
            }
            curr = from.rookMove(dir, i);
        }
        return pieceMap.get(curr) == EMPTY;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return get(from).side() == _turn;
    }

    /** Return true iff FROM-TO is a valid move. */
    boolean isLegal(Square from, Square to) {
        if (!isLegal(from)) {
            return false;
        }
        if (get(from) == KING) {
            return isUnblockedMove(from, to);
        }
        return to != THRONE && isUnblockedMove(from, to);
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to());
    }

    boolean isHostile(Piece side, Square sq) {
        if (side == KING) {
            side = WHITE;
        }
        if (sq == null) {
            return false;
        }
        if (sq == THRONE) {
            if (get(sq) == EMPTY) {
                return true;
            } else {
                return side == BLACK;
            }
        } else if (get(sq) != EMPTY) {
            return side != get(sq);
        } else {
            return false;
        }
    }

    /** Move FROM-TO, assuming this is a legal move. */
    void makeMove(Square from, Square to) {
        if (!isLegal(from, to)) {
            //System.out.println("Illegal move!");
            return;
        }
        _moveCount++;
        Piece curr = get(from);
        put(EMPTY, from);
        for (int i = 0; i < 4; i++) {
            Square other = to.rookMove(i, 2);
            if (isHostile(_turn.opponent(), other)) {
                Square between = to.between(other);
                if (get(between).side() == _turn.opponent()) {
                    if (get(between) == KING &&
                            (between == THRONE ||
                             between == NTHRONE ||
                             between == ETHRONE ||
                             between == STHRONE ||
                             between == WTHRONE))  {
                        if (isHostile(_turn.opponent(),
                                to.diag1(between)) &&
                            isHostile(_turn.opponent(),
                                    to.diag2(between))) {
                            capture(to, other);
                        }
                    } else {
                        capture(to, other);
                    }
                }
            }
        }
        revPut(curr, to);
        if (kingPosition() != null && kingPosition().isEdge()) {
            _winner = WHITE;
        }
        if (_winner == null && _moveCount/2 >= _maxMove) {
            _winner = turn();
        }
        _turn = _turn.opponent();
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to());
    }

    /** Capture the piece between SQ0 and SQ2, assuming a piece just moved to
     *  SQ0 and the necessary conditions are satisfied. */
    private void capture(Square sq0, Square sq2) {
        if (get(sq0.between(sq2)) == KING) {
            _winner = BLACK;
            kingLocation = null;
        }
        put(EMPTY, sq0.between(sq2));
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (_moveCount > 0) {
            undoPosition();
            _repeated = false;
            _winner = null;
            _moveCount--;
            _turn = turn().opponent();
            kingLocation = null;
            String prevStr = prevBoard.get(prevBoard.size() - 1);
            assert _turn.toString().charAt(0) == prevStr.charAt(0);
            for (int i = 0; i < NUM_SQUARES; i++) {
                if (prevStr.charAt(i + 1) == 'W')
                    put(WHITE, SQUARE_LIST.get(i));
                else if (prevStr.charAt(i + 1) == 'B')
                    put(BLACK, SQUARE_LIST.get(i));
                else if (prevStr.charAt(i + 1) == 'K')
                    put(KING, SQUARE_LIST.get(i));
                else
                    put(EMPTY, SQUARE_LIST.get(i));
            }
        }
    }

    /** Remove record of current position in the set of positions encountered,
     *  unless it is a repeated position or we are at the first move. */
    private void undoPosition() {

        if (_moveCount > 0) {
            prevBoard.remove(prevBoard.size() - 1);
            pieceMap.clear();
        }
    }

    /** Clear the undo stack and board-position counts. Does not modify the
     *  current position or win status. */
    void clearUndo() {

        prevBoard.clear();
    }

    /** Return a new mutable list of all legal moves on the current board for
     *  SIDE (ignoring whose turn it is at the moment). */
    List<Move> legalMoves(Piece side) {
        ArrayList<Move> MoveList = new ArrayList<Move>();
        HashSet<Square> locations = pieceLocations(side);
        Piece currSide = _turn;
        _turn = side;
        for (Square s : locations) {
            if (s == null) {
                System.out.println("s is null!!");
                System.out.println(locations.size());
            }
            for (int i = 0; i < 4; i++) {
                ArrayList<Move> temp = ROOK_MOVES[s.index()][i];
                for (Move m : temp) {
                    if (isLegal(m)) {
                        MoveList.add(m);
                    }
                }
            }

        }
        _turn = currSide;
        return MoveList;
    }

    /** Return true iff SIDE has a legal move. */
    boolean hasMove(Piece side) {
        return !legalMoves(side).isEmpty();
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /** Return a text representation of this Board.  If COORDINATES, then row
     *  and column designations are included along the left and bottom sides.
     */
    String toString(boolean coordinates) {
        Formatter out = new Formatter();
        for (int r = SIZE - 1; r >= 0; r -= 1) {
            if (coordinates) {
                out.format("%2d", r + 1);
            } else {
                out.format("  ");
            }
            for (int c = 0; c < SIZE; c += 1) {
                out.format(" %s", get(c, r));
            }
            out.format("%n");
        }
        if (coordinates) {
            out.format("  ");
            for (char c = 'a'; c <= 'i'; c += 1) {
                out.format(" %c", c);
            }
            out.format("%n");
        }
        return out.toString();
    }

    /** Return the locations of all pieces on SIDE. */
    private HashSet<Square> pieceLocations(Piece side) {
        assert side != EMPTY;
        HashSet<Square> locations = new HashSet<Square>();
        for (Square s : SQUARE_LIST) {
            if (get(s) == side) {
                locations.add(s);
            }
        }
        if (side == WHITE) {
            if (kingPosition() != null) {
                locations.add(kingPosition());
            }
        }
        return locations;
    }

    /** Return the contents of _board in the order of SQUARE_LIST as a sequence
     *  of characters: the toString values of the current turn and Pieces. */
    String encodedBoard() {
        char[] result = new char[Square.SQUARE_LIST.size() + 1];
        result[0] = turn().toString().charAt(0);
        for (Square sq : SQUARE_LIST) {
            result[sq.index() + 1] = get(sq).toString().charAt(0);
        }
        return new String(result);
    }

    public HashMap<Square, Piece> getPieceMap() {
        return pieceMap;
    }

    public ArrayList<String> getPrevBoard() {
        return prevBoard;
    }

    public int getMaxMove() {
        return _maxMove;
    }

    /** Piece whose turn it is (WHITE or BLACK). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;
    /** Number of (still undone) moves since initial position. */
    private int _moveCount;
    /** True when current board is a repeated position (ending the game). */
    private boolean _repeated;


    private Square kingLocation;

    private int _maxMove;

    private HashMap<Square, Piece> pieceMap;

    private ArrayList<String> prevBoard;

}
