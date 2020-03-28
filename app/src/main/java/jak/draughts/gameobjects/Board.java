package jak.draughts.gameobjects;

import java.util.ArrayList;
import java.util.List;

import jak.draughts.Coordinates;

/**
 * This class serves as the logical representation of
 * a draughtboard and includes core mechanics such as
 * calculating permitted moves.
 */
public class Board {

    // constants for board tiles
    private static final int ROWS = 8;
    private static final int COLUMNS = 8;
    private static final int RED_ROW_START = 2;
    private static final int WHITE_ROW_END = 5;

    // constants for tile pieces
    private static final int EMPTY_TILE = 0;
    private static final int RED_MAN = 1;
    private static final int WHITE_MAN = 2;

    private List<List<Integer>> board;
    private boolean turn; // true for red's turn, false for white
    private boolean hasSelectedPiece; // indicates if there is a piece selected
    private Coordinates selectedPiece; // indicates position selected piece

    /**
     * Returns a Board object, containing a 2D list of integers.
     * By default, the board is filled out with the set-up of a
     * regular draught game, with red at the top and white at
     * the bottom.
     */
    public Board() {
        createBoard();
        turn = true; // red starts first
        hasSelectedPiece = false; // no piece is selected
        selectedPiece = null;
    }

    private void createBoard() {
        board = new ArrayList<>(ROWS);
        for (int row = 0; row < ROWS; row++) {
            board.add(new ArrayList<Integer>(COLUMNS));
            for (int col = 0; col < COLUMNS; col++) {
                fillBoard(row, col);
            }
        }
    }

    private void fillBoard(int row, int col) {
        if (row <= RED_ROW_START && Coordinates.isGreen(row, col)) {
            board.get(row).add(RED_MAN);
        } else if (row >= WHITE_ROW_END && Coordinates.isGreen(row, col)) {
            board.get(row).add(WHITE_MAN);
        } else {
            board.get(row).add(EMPTY_TILE);
        }
    }

    /**
     * resolveClick() is called by the adapter to notify the Board object
     * that a user has pressed a tile in that board. The method will confirm
     * if the tile selected leads to any changes, and if so, will call the
     * relevant methods.
     *
     * The BoardActivity class will continually check for changes in Board
     * and send an updated dataSet to the adapter when necessary.
     * @param position integer between 0 and 63; the 1D tile position
     */
    public void resolveClick(int position) {
        Coordinates coords = new Coordinates(position);

        // clicked on white/unplayable tiles -> deselect
        if (!coords.isGreen()) {
            deselect();
            return;
        }

        int piece = getPiece(coords);
        if (hasSelectedPiece) {
            if (piece == EMPTY_TILE) {
                deselect();
            }
        } else {

        }
        // TODO: called by adapter
    }

    private void deselect() {
        hasSelectedPiece = false;
        selectedPiece = null;
    }

    private Integer getPiece(Coordinates coordinates) {
        return board.get(coordinates.getX()).get(coordinates.getY());
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

    public List<List<Integer>> getBoard() {
        return board;
    }

    public void setBoard(List<List<Integer>> board) {
        this.board = board;
    }
}
