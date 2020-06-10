package jak.draughts.game.gameobjects;

import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jak.draughts.Coordinates;
import jak.draughts.TileColor;

public class DraughtBoard extends Board {

    // constants for board tiles
    private static final int ROWS = 8;
    private static final int COLUMNS = 8;
    private static final int RED_KING_ROW = 0;
    private static final int RED_ROW_END = 2;
    private static final int WHITE_ROW_END = 5;
    private static final int WHITE_KING_ROW = 7;

    private List<List<DraughtTile>> board;

    public DraughtBoard() {
        createBoard();
        writeDataSet();
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

            if (row <= RED_ROW_END) {
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
    public void writeDataSet() {
        List<Integer> dataSet = new ArrayList<>(COLUMNS * ROWS);
        List<TileColor> backgroundSet = new ArrayList<>(COLUMNS * ROWS);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                dataSet.add(board.get(row).get(col).getType());
                backgroundSet.add(board.get(row).get(col).getTileColor());
            }
        }

        setDataSet(dataSet);
        setBackgroundSet(backgroundSet);
    }

    @Override
    public void readDataSet(List<Integer> dataSet, Coordinates movedPieceCoords) {
        DraughtPiece movedPiece = getPiece(movedPieceCoords);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Coordinates coords = new Coordinates(row, col);
                int newType = dataSet.get(coords.getPosition());
                int currentType = getTile(coords).getType();

                if (currentType != newType) {
                    if (newType == 0) {
                        remove(coords);
                    } else {
                        put(movedPiece, coords);
                        upgradeToKing(movedPiece);
                    }
                }
            }
        }

        setDataSet(dataSet);
    }

    /**
     * Checks if the given piece can be upgraded to king,
     * and if true, upgrades it.
     *
     * @param piece given piece to check
     */
    public void upgradeToKing(DraughtPiece piece) {
        if (!piece.isKing()) {
            int row = piece.getCoordinates().getX();
            if (piece.isRed() && row == WHITE_KING_ROW) {
                piece.setKing(true);
            } else if (!piece.isRed() && row == RED_KING_ROW) {
                piece.setKing(true);
            }
        }
    }

    /**
     * Sets the tile's background to the specified color
     *
     * @param coords tile coordinates
     * @param tileColor new tile color
     */
    public void selectTile(Coordinates coords, TileColor tileColor) {
        getTile(coords).setTileColor(tileColor);
    }

    /**
     * Convenience method that moves the capturing piece to the given
     * coordinate, and removes the captured piece in between. Returns
     * the coordinate of the given piece before it was moved.
     *
     * @param piece the capturing piece
     * @param newCoords coordinates of destination tile
     * @return coordinates of the tile the capturing piece is moved from
     */
    public Coordinates capture(DraughtPiece piece, Coordinates newCoords) {
        Coordinates oldCoords = piece.getCoordinates();
        remove(newCoords.middle(oldCoords));
        move(piece, newCoords);
        return oldCoords;
    }

    /**
     * Convenience method that removes the piece from its old location and puts it
     * in the new location. Returns the coordinates it was moved from.
     *
     * @param piece     the piece to be moved
     * @param newCoords location of the tile to be moved to
     * @throws IllegalStateException if tile already contains a piece
     * @return Coordinates last position of the tile
     */
    public Coordinates move(DraughtPiece piece, Coordinates newCoords) {
        Coordinates oldCoords = piece.getCoordinates();
        remove(piece);
        put(piece, newCoords);
        return oldCoords;
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

    /**
     * Checks if coordinates are within range of this board
     *
     * @param coords coordinate to be checked
     * @return true if within range, false otherwise
     */
    public boolean inRange(Coordinates coords) {
        return 0 <= coords.getX() && coords.getX() <= 7
                && 0 <= coords.getY() && coords.getY() <= 7;
    }

    /**
     * Removes any highlighted tiles by setting all tiles back
     * to their default TileColor
     */
    public void deselect() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                board.get(row).get(col).clearSelection();
            }
        }
    }

    /**
     * Returns the piece at the given location. Returns null
     * if tile is empty
     *
     * @param coordinates location of tile
     * @return piece at the given tile
     */
    public DraughtPiece getPiece(Coordinates coordinates) {
        return getTile(coordinates).getPiece();
    }

    /**
     * Returns the tile object at the given location.
     *
     * @param coordinates location of tile
     * @return tile object
     */
    public DraughtTile getTile(Coordinates coordinates) {
        return board.get(coordinates.getX()).get(coordinates.getY());
    }

    /**
     * Returns all pieces on the board that matches the given
     * isRed value, i.e. red pieces if set to true.
     *
     * @param isRed the team of this player
     * @return list of red pieces if isRed is true, white pieces otherwise
     */
    public List<DraughtPiece> getTeamPieces(boolean isRed) {
        List<DraughtPiece> pieces = new LinkedList<>();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                DraughtPiece piece = getTeamPieces(isRed, new Coordinates(row, col));
                if (piece != null) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    private DraughtPiece getTeamPieces(boolean isRed, Coordinates coords) {
        DraughtPiece piece = getPiece(coords);
        if (piece != null && piece.isRed() == isRed) {
            return piece;
        } else {
            return null;
        }
    }

    @VisibleForTesting
    public List<List<DraughtTile>> getBoard() {
        return board;
    }
}
