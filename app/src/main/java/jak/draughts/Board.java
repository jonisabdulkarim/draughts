package jak.draughts;

import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as the logical representation of
 * a draughtboard and includes core mechanics such as
 * calculating permitted moves.
 */
public class Board {

    private static final int ROWS = 8;
    private static final int COLUMNS = 8;
    private static final int WHITE_ROW_END = 2;
    private static final int RED_ROW_START = 5;

    //private int[][] board;
    private List<List<Integer>> board;

    Board() {
        //board = new int[ROWS][COLUMNS];
        board = new ArrayList<>(ROWS);

        for (int row = 0; row < ROWS; row++) {
            board.add(new ArrayList<Integer>(COLUMNS));
            for (int col = 0; col < COLUMNS; col++) {
                fillBoard(row, col);
            }
        }
    }


    private void fillBoard(int row, int col) {
        if (row <= WHITE_ROW_END && isGreen(row, col)) {
            // board[row][col] = 1;
            board.get(row).add(1);
        } else if (row >= RED_ROW_START && isGreen(row, col)) {
            // board[row][col] = 2;
            board.get(row).add(2);
        } else {
            // board[row][col] = 0;
            board.get(row).add(0);
        }
    }

    private boolean isGreen(int row, int col) {
        return row % 2 == 0 ^ col % 2 == 0;
    }

    public List<List<Integer>> getBoard() {
        return board;
    }

    public void setBoard(List<List<Integer>> board) {
        this.board = board;
    }

    /*
    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
    */
}
