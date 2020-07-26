//Joaquin Hidalgo - UTEP
package edu.utep.cs.cs4381.connectfour.model;
//Joaquin Hidalgo - UTEP

import java.util.ArrayList;
import java.util.List;

/**
 * Abstraction of the Connect Four game board. A game board consists
 * of seven columns, where each column has six places (rows) in which
 * a player's disc can be placed. A place is denoted by a pair of
 * a 0-based column and row indices. The origin (0,0) is the top left.
 */
public class Board  {


    private static final int NUM_OF_COLUMNS = 7;
    private static final int NUM_OF_ROWS = 6;
    private static final int NO_INDEX = -1;

    /** Places of this board. A place is represented as a player
     * whose disc is placed on it or null if it's empty. */
    private Player[][] places;

    /** Winning row of places. */
    private List<Place> winningRow = new ArrayList<>();

    /** Create a new board. */
    public Board() {

        places = new Player[NUM_OF_COLUMNS][NUM_OF_ROWS];
    }

    /** Clear this board by removing all discs. */
    public void clear() {
        winningRow.clear();
        for (int i = 0; i < NUM_OF_COLUMNS; i++) {
            for (int j = 0; j < NUM_OF_ROWS; j++) {
                places[i][j] = null;
            }
        }
    }

    /** Return the number of columns. */
    public int numOfColumns() {
        return NUM_OF_COLUMNS;
    }

    /** Return the number of rows. */
    public int numOfRows() {
        return NUM_OF_ROWS;
    }

    /**
     * Does the given column have an empty place?
     *
     * @param x 0-based column index
     */
    public boolean isColumnOpen(int x) {
        return places[x][0] == null;
    }

    /**
     *  Return true if no disc can be placed in the specified column.
     *
     * @param x 0-based column index
     */
    public boolean isColumnFull(int x) {
        return places[x][0] != null;
    }


    /** Are all places occupied? */
    public boolean isFull() {
        for (int x = 0; x < NUM_OF_COLUMNS; x++) {
            if (isColumnOpen(x)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Drop a disc of the given player in the specified column
     * and return the place (row) index of the placed disc.
     * The specified column should be open; if not, this
     * method has no effect.
     *
     * @param x 0-based column index
     * @param player Player whose disc is to be dropped
     */
    public int dropDisc(int x, Player player) {
        for (int y = NUM_OF_ROWS - 1; y >= 0; y--) {
            if (places[x][y] == null) {
                places[x][y] = player;
//                if (changeListener != null) {
//                    changeListener.discDropped(x, y, player);
//                }
                return y;
            }
        }
        return NO_INDEX;
    }

    /**
     * Is the specified place empty?
     *
     * @param x 0-based column index
     * @param y 0-based row index
     */
    public boolean isEmpty(int x, int y) {
        return places[x][y] == null;
    }

    /**
     * Is the specified place occupied?
     *
     * @param x 0-based slot (column) index
     * @param y 0-based place (row) index
     */
    public boolean isOccupied(int x, int y) {
        return places[x][y] != null;
    }

    /** Is the given place occupied? */
    public boolean isOccupied(Place place) {
        return isOccupied(place.x, place.y);
    }

    /**
     * Is the specified place occupied by the given player?
     *
     * @param x 0-based slot (column) index
     * @param y 0-based place (row) index
     */
    public boolean isOccupiedBy(int x, int y, Player player) {
        return places[x][y] == player;
    }

    /**
     * Return the player occupying the specified place; null if the place
     * is empty.
     *
     * @param x 0-based column index
     * @param y 0-based row index
     */
    public Player playerAt(int x, int y) {
        return places[x][y];
    }

    /** Return true if the given player has a winning row. */
    public boolean isWonBy(Player player) {
        for (int i = 0; i < NUM_OF_COLUMNS; i++) {
            for (int j = 0; j < NUM_OF_ROWS; j++) {
                boolean won = isWonBy(i, j, 1, 0, player); // horizontal
                won = won || isWonBy(i, j, 0, 1, player);  // vertical
                won = won || isWonBy(i, j, 1, 1, player);  // diagonal(\)
                won = won || isWonBy(i, j, 1, -1, player); // diagonal(/)
                if (won) {
                    return won;
                }
            }
        }
        return false;
    }

    /** Return the winning row. */
    public Iterable<Place> winningRow() {
        return winningRow;
    }

    /** Return true if this board has a winning row. */
    public boolean hasWinningRow() {
        return !winningRow.isEmpty();
    }

    /** Return true if the given player has a winning row in the specified
     * direction and containing the specified place. */
    private boolean isWonBy(int x, int y, int dx, int dy, Player player) {
        final int FOUR = 4;
        int[] row = new int[FOUR];

        // check left/lower side
        int cnt = 0;
        int sx = x;
        int sy = y;
        while (!(dx > 0 && sx < 0) && !(dx < 0 && sx >= NUM_OF_COLUMNS)
                && !(dy > 0 && sy < 0) && !(dy < 0 && sy >= NUM_OF_ROWS)
                && isOccupiedBy(sx, sy, player) && cnt < FOUR) {
            row[cnt++] = sx * 100 + sy;
            sx = sx - dx;
            sy = sy - dy;
        }
        // check right/higher side
        int ex = x + dx;
        int ey = y + dy;
        while (!(dx > 0 && ex >= NUM_OF_COLUMNS) && !(dx < 0 && ex < 0)
                && !(dy > 0 && ey >= NUM_OF_ROWS) && !(dy < 0 && ey < 0)
                && isOccupiedBy(ex, ey, player) && cnt < FOUR) {
            row[cnt++] = ex * 100 + ey;
            ex = ex + dx;
            ey = ey + dy;
        }
        if (cnt >= FOUR) {
            for (int xy: row) {
                winningRow.add(new Place(xy / 100, xy % 100));
//                Log.w("wX", ""+xy/100);
//                Log.w("wY", ""+xy%100);
            }
        }
        return cnt >= FOUR;
    }

    /** Clear the given place. */
    protected void clearPlace(int slot, int y) {
        places[slot][y] = null;
    }

    /**
     * Denote a place on a board by specifying its 0-based column (x)
     * and row y) indices.
     */
    public static class Place {
        /** 0-based column index of this place. */
        public final int x;

        /** 0-based row index of this place. */
        public final int y;

        /** Create a new place of the given indices.
         *
         * @param x 0-based column index
         * @param y 0-based row index
         */
        public Place(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }

    }
}