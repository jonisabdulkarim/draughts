package jak.draughts.game.gameobjects;

import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

import jak.draughts.Coordinates;
import jak.draughts.TileColor;

public class DraughtBoard extends Board {

    // constants for board tiles
    private static final int ROWS = 8;
    private static final int COLUMNS = 8;
    private static final int RED_ROW_START = 2;
    private static final int WHITE_ROW_END = 5;

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
        TileColor color;

        if (coords.isGreen()) {
            color = TileColor.GREEN;

            if (row <= RED_ROW_START) {
                piece = new DraughtPiece(true, false, coords);
            } else if (row >= WHITE_ROW_END) {
                piece = new DraughtPiece(false, false, coords);
            } else {
                piece = null;
            }
        } else {
            color = TileColor.WHITE;
            piece = null;
        }

        board.get(row).add(new DraughtTile(piece, color));
    }

    @Override
    public void createDataSet() {
        List<Integer> dataSet = new ArrayList<>(COLUMNS * ROWS);
        List<TileColor> backgroundSet = new ArrayList<>(COLUMNS * ROWS);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                dataSet.add(board.get(i).get(j).getType());
                backgroundSet.add(board.get(i).get(j).getTileColor());
            }
        }

        setDataSet(dataSet);
        setBackgroundSet(backgroundSet);
    }

    /**
     * Convenience method that removes the piece from its old location and puts it
     * in the new location
     *
     * @param piece     the piece to be moved
     * @param newCoords location of the tile to be moved to
     * @throws IllegalStateException if tile already contains a piece
     */
    public void move(DraughtPiece piece, Coordinates newCoords) {
        remove(piece);
        put(piece, newCoords);
    }

    /**
     * Convenience method that removes the piece from its old location and puts it
     * in the new location
     *
     * @param oldCoords location of the tile to be moved from
     * @param newCoords location of the tile to be moved to
     */
    public void move(Coordinates oldCoords, Coordinates newCoords) {
        DraughtPiece piece = remove(oldCoords);
        put(piece, newCoords);
    }

    /**
     * Places the piece at the tile with the given coordinates. The piece's
     * coordinates will be updated. This method does not remove the piece
     * from its previous location, use the move() method instead. The tile must
     * be empty, else an <tt>IllegalStateException</tt> will be raised.
     *
     * @param piece  the piece to be placed on the board
     * @param coords location of the tile
     * @throws IllegalStateException if tile already contains a piece
     */
    public void put(DraughtPiece piece, Coordinates coords) {
        DraughtTile tile = getTile(coords);
        if (tile.isEmpty()) {
            tile.setPiece(piece);
            piece.setCoordinates(coords);
        } else {
            throw new IllegalStateException("Tile" +
                    coords + " already contains a piece");
        }
    }

    /**
     * Removes and returns a piece from the tile at the given coordinates.
     * Also sets the piece's coordinates to null. If no piece is present at
     * the tile, null is returned.
     *
     * @param coordinates location of the tile
     * @return the removed piece if it was present, null otherwise
     */
    public DraughtPiece remove(Coordinates coordinates) {
        DraughtTile tile = getTile(coordinates);
        DraughtPiece piece = tile.getPiece();
        tile.removePiece();
        piece.setCoordinates(null);
        return piece;
    }

    /**
     * Removes the given piece from the board. Also sets the piece's
     * coordinates to null.
     *
     * @param piece the piece to be removed from the board
     */
    public void remove(DraughtPiece piece) {
        Coordinates coordinates = piece.getCoordinates();
        DraughtTile tile = getTile(coordinates);
        tile.removePiece();
        piece.setCoordinates(null);
    }

    /**
     * Returns true if the given tile is empty, else it returns false.
     *
     * @param coordinates location of the tile
     * @return true if tile is empty, false otherwise
     */
    public boolean isEmpty(Coordinates coordinates) {
        DraughtTile tile = getTile(coordinates);
        return tile.isEmpty();
    }

    private DraughtTile getTile(Coordinates coordinates) {
        return board.get(coordinates.getX()).get(coordinates.getY());
    }

    @VisibleForTesting
    public List<List<DraughtTile>> getBoard() {
        return board;
    }
}
