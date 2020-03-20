package jak.draughts;

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

    private int[][] board;

    Board() {
        board = new int[ROWS][COLUMNS];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (row > WHITE_ROW_END && row < RED_ROW_START)
                    continue; // skip middle rows

                fillBoard(row, col);
            }
        }
    }

    private void fillBoard(int row, int col) {
        if (row <= WHITE_ROW_END && isGreen(row, col)) {
            board[row][col] = 1;
        } else if (row >= RED_ROW_START && isGreen(row, col)) {
            board[row][col] = 2;
        } else {
            board[row][col] = 0;
        }
    }

    private boolean isGreen(int row, int col) {
        return row % 2 == 0 ^ col % 2 == 0;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
}
