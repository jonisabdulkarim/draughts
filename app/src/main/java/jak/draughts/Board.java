package jak.draughts;

import java.util.*;

/**
 * This class serves as the logical representation of
 * a draughtboard and includes core mechanics such as
 * calculating permitted moves.
 */
public class Board {

    private static final int ROWS = 8;
    private static final int COLUMNS = 8;

    private int[][] board;

    Board() {
        board = new int[ROWS][COLUMNS];
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    private boolean isGreen(int x, int y) {
        return x % 2 == 0 ^ y % 2 == 0;
    }
}
