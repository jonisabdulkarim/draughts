package jak.draughts.gameobjects;

import java.util.ArrayList;
import java.util.List;

import jak.draughts.Coordinates;

public class DraughtBoard extends Board {

    // constants for board tiles
    private static final int ROWS = 8;
    private static final int COLUMNS = 8;
    private static final int RED_ROW_START = 2;
    private static final int WHITE_ROW_END = 5;

    // constants for tile pieces
    private static final int EMPTY_TILE = 0;
    private static final int RED_MAN = 1;
    private static final int WHITE_MAN = 2;

    private List<List<DraughtTile>> board;

    public DraughtBoard() {
        createBoard();
        createDataSet();
    }

    private void createBoard() {
        board = new ArrayList<>(ROWS);

        for (int row = 0; row < ROWS; row++) {
            List<DraughtTile> rowTiles = new ArrayList<>(COLUMNS);
            board.add(rowTiles);

            for (int col = 0; col < COLUMNS; col++) {
                fillBoard(row, col);
            }
        }
    }

    private void fillBoard(int row, int col) {
        Coordinates coords = new Coordinates(row, col);
        DraughtPiece piece;

        if (row <= RED_ROW_START && coords.isGreen()) {
            piece = new DraughtPiece(true, false, coords);
        } else if (row >= WHITE_ROW_END && coords.isGreen()) {
            piece = new DraughtPiece(false, false, coords);
        } else {
            piece = null;
        }

        board.get(row).add(new DraughtTile(piece));
    }

    @Override
    public void createDataSet() {
        List<Integer> convertedBoard = new ArrayList<>(COLUMNS * ROWS);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                convertedBoard.add(board.get(i).get(j).getType());
            }
        }

        setDataSet(convertedBoard);
    }

    public List<List<DraughtTile>> getBoard() {
        return board;
    }
}
