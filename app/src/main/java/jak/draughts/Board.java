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
    private static final int RED_ROW_START = 2;
    private static final int WHITE_ROW_END = 5;
    private static final int EMPTY_TILE = 0;
    private static final int RED_MAN = 1;
    private static final int WHITE_MAN = 2;

    private List<List<Integer>> board;

    public Board() {
        board = new ArrayList<>(ROWS);
        for (int row = 0; row < ROWS; row++) {
            board.add(new ArrayList<Integer>(COLUMNS));
            for (int col = 0; col < COLUMNS; col++) {
                fillBoard(row, col);
            }
        }
    }

    private void fillBoard(int row, int col) {
        if (row <= RED_ROW_START && isGreen(row, col)) {
            board.get(row).add(RED_MAN);
        } else if (row >= WHITE_ROW_END && isGreen(row, col)) {
            board.get(row).add(WHITE_MAN);
        } else {
            board.get(row).add(EMPTY_TILE);
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

    /**
     * This method converts the board into a 1D int array,
     * which is required to update the disc.
     * @return The board in a one dimensional integer array
     */
    public List<Integer> convertBoard() {
        List<Integer> convertedBoard = new ArrayList<>(COLUMNS * ROWS);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                convertedBoard.add(board.get(i).get(j));
            }
        }

        return convertedBoard;
    }
}
