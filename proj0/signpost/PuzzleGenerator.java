package signpost;

import java.util.Collections;
import java.util.Random;

import signpost.Model.Sq;
import static signpost.Place.PlaceList;
import static signpost.Utils.*;

/** A creator of random Signpost puzzles.
 *  @author Pongsatorn Chanpanichravee
 */
class PuzzleGenerator implements PuzzleSource {

    /** A new PuzzleGenerator whose random-number source is seeded
     *  with SEED. */
    PuzzleGenerator(long seed) {
        _random = new Random(seed);
    }

    @Override
    public Model getPuzzle(int width, int height, boolean allowFreeEnds) {
        Model model =
            new Model(makePuzzleSolution(width, height, allowFreeEnds));

        makeSolutionUnique(model);
        model.autoconnect();
        return model;
    }

    /** Return an array representing a WIDTH x HEIGHT Signpost puzzle.
     *  The first array index indicates x-coordinates (column numbers) on
     *  the board, and the second index represents y-coordinates (row numbers).
     *  Its values will be the sequence numbers (1 to WIDTH x HEIGHT)
     *  appearing in a sequence queen moves on the resulting board.
     *  Unless ALLOWFREEENDS, the first and last sequence numbers will
     *  appear in the upper-left and lower-right corners, respectively.
     *
     *  RETURN: Solution
     */
    private int[][] makePuzzleSolution(int width, int height,
                                       boolean allowFreeEnds) {
        _vals = new int[width][height];
        _successorCells = Place.successorCells(width, height);
        int last = width * height;
        int x0, y0, x1, y1;
        if (allowFreeEnds) {
            int r0 = _random.nextInt(last),
                r1 = (r0 + 1 + _random.nextInt(last - 1)) % (last);
            x0 = r0 / height; y0 = r0 % height;
            x1 = r1 / height; y1 = r1 % height;
        } else {
            x0 = 0; y0 = height - 1;
            x1 = width - 1; y1 = 0;
        }
        _vals[x0][y0] = 1;
        _vals[x1][y1] = last;


        boolean ok = findSolutionPathFrom(x0, y0);
        assert ok;
        return _vals;
    }

    /** Try to find a random path of queen moves through VALS from (X0, Y0)
     *  to the cell with number LAST.  Assumes that
     *    + The dimensions of VALS conforms to those of MODEL;
     *    + There are cells (separated by queen moves) numbered from 1 up to
     *      and including the number in (X0, Y0);
     *    + There is a cell numbered LAST;
     *    + All other cells in VALS contain 0.
     *  Does not change the contents of any non-zero cell in VALS.
     *  Returns true and leaves the path that is found in VALS.  Otherwise
     *  returns false and leaves VALS unchanged. Does not change MODEL. */
    private boolean findSolutionPathFrom(int x0, int y0) {
        int w = _vals.length, h = _vals[0].length;
        int v;
        int start = _vals[x0][y0] + 1;
        PlaceList moves = _successorCells[x0][y0][0];
        Collections.shuffle(moves, _random);
        for (Place p : moves) {
            v = _vals[p.x][p.y];
            if (v == 0) {
                _vals[p.x][p.y] = start;
                if (findSolutionPathFrom(p.x, p.y)) {
                    return true;
                }
                _vals[p.x][p.y] = 0;
            } else if (v == start && start == w * h) {
                return true;
            }
        }
        return false;
    }

    /** Extend unambiguous paths in MODEL (add all connections where there is
     *  a single possible successor or predecessor). Return 2 if any change
     *  was made, 1 if no change was made, 0 if unconnectable
     *  square encountered. */
    private int extendSimple(Model model) {
        int result;
        result = 1;
        while (true) {
            int cf = makeForwardConnections(model);
            if (cf == 0) {
                return 0;
            }
            int cb = makeBackwardConnections(model);
            if (cb == 0) {
                return 0;
            } else if (cb == 1 && cf == 1) {
                return result;
            }
            result = 2;
        }
    }

    /** Make all unique forward connections in MODEL (those in which there is
     *  a single possible successor).  Return 2 if changes made, 1 if no
     *  changes made, 0 if a non-final square with no possible connections
     *  encountered. */
    private int makeForwardConnections(Model model) {
        int w = model.width(), h = model.height();
        int result;
        result = 1;
        for (Sq sq : model) {
            Sq found;
            found = null;
            int nFound;
            nFound = 0;
            if (sq.successor() == null && sq.direction() != 0) {

                PlaceList successors = sq.successors();
                PlaceList cSuccessors = new PlaceList();
                for (Place x: successors) {
                    if (sq.connectable(model.get(x))) {
                        nFound++;
                        cSuccessors.add(x);
                        found = model.get(x);
                    }
                }
                if (sq.sequenceNum() != 0) {
                    for (Place x : cSuccessors) {
                        if (model.get(x).sequenceNum() != 0) {
                            assert (model.get(x).sequenceNum()
                                    == sq.sequenceNum() + 1);
                            nFound = 1;
                            found = model.get(x);
                        }
                    }
                }
                if (nFound == 0) {
                    return 0;
                } else if (nFound == 1) {
                    sq.connect(found);
                    result = 2;
                }
            }
        }
        return result;
    }

    /** Make all unique backward connections in MODEL (those in which there is
     *  a single possible predecessor).  Return 2 if changes made, 1 if no
     *  changes made, 0 if a non-final square with no possible connections
     *  encountered. */
    private int makeBackwardConnections(Model model) {
        int w = model.width(), h = model.height();
        int result;
        result = 1;
        for (Sq sq : model) {
            Sq found;
            int nFound;
            found = null;
            nFound = 0;
            if (sq.predecessor() == null && sq.sequenceNum() != 1) {

                PlaceList predecessors = sq.predecessors();
                PlaceList cPredecessors = new PlaceList();
                for (Place x: predecessors) {
                    if (model.get(x).connectable(sq)) {
                        nFound++;
                        cPredecessors.add(x);
                        found = model.get(x);
                    }
                }
                if (sq.sequenceNum() != 0) {
                    for (Place x : cPredecessors) {
                        if (model.get(x).sequenceNum() != 0) {
                            assert (model.get(x).sequenceNum()
                                    == sq.sequenceNum() - 1);
                            nFound = 1;
                            found = model.get(x);
                        }
                    }
                }

                if (nFound == 0) {
                    return 0;
                } else if (nFound == 1) {
                    if (found.sequenceNum() == 0 && found.group() == 0) {
                        System.out.println("Error 1");
                    }
                    if (sq.sequenceNum() == 0 && sq.group() == 0) {
                        System.out.println("Error 1");
                    }
                    found.connect(sq);
                    result = 2;
                }
            }
        }
        return result;
    }

    /** Remove all links in MODEL and unfix numbers (other than the first and
     *  last) that do not affect solvability.  Not all such numbers are
     *  necessarily removed. */
    private void trimFixed(Model model) {
        int w = model.width(), h = model.height();
        boolean changed;
        do {
            changed = false;
            for (Sq sq : model) {
                if (sq.hasFixedNum() && sq.sequenceNum() != 1
                    && sq.direction() != 0) {
                    model.restart();
                    int n = sq.sequenceNum();
                    sq.unfixNum();
                    extendSimple(model);
                    if (model.solved()) {
                        changed = true;
                    } else {
                        sq.setFixedNum(n);
                    }
                }
            }
        } while (changed);
    }

    /** Fix additional numbers in MODEL to make the solution from which
     *  it was formed unique.  Need not result in a minimal set of
     *  fixed numbers. */
    private void makeSolutionUnique(Model model) {
        model.restart();
        AddNum:
        while (true) {
            extendSimple(model);
            if (model.solved()) {
                trimFixed(model);
                model.restart();
                return;
            }
            PlaceList unnumbered = new PlaceList();
            for (Sq sq : model) {
                if (sq.sequenceNum() == 0) {
                    unnumbered.add(sq.pl);
                }
            }
            Collections.shuffle(unnumbered, _random);
            for (Place p : unnumbered) {
                Model model1 = new Model(model);
                model1.get(p).setFixedNum(model.solution()[p.x][p.y]);
                if (extendSimple(model1) == 2) {
                    model.get(p).setFixedNum(model1.get(p).sequenceNum());
                    continue AddNum;
                }
            }
            throw badArgs("no solution found");
        }
    }

    @Override
    public void setSeed(long seed) {
        _random.setSeed(seed);
    }

    /** Solution board currently being filled in by findSolutionPathFrom. */
    private int[][] _vals;
    /** Mapping of positions and directions to lists of queen moves on _vals. */
    private PlaceList[][][] _successorCells;

    /** My PNRG. */
    private Random _random;

}
