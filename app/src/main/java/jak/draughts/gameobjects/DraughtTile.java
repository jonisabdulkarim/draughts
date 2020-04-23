package jak.draughts.gameobjects;

import jak.draughts.TileColor;

/**
 * Concrete sub-class of <tt>Tile</tt>, representing each individual tile
 * in a <tt>DraughtBoard</tt>.
 * <p>
 * Each <tt>DraughtTile</tt> object can only hold one <tt>DraughtPiece</tt> and
 * must be assigned a default colour when created.
 */
public class DraughtTile extends Tile {
    private final TileColor defaultColor;

    DraughtTile(DraughtPiece piece, TileColor defaultColor) {
        this.defaultColor = defaultColor;

        setPiece(piece);
        setTileColor(defaultColor);
    }

    /**
     * Returns the piece object contained in this tile, or null if there is no piece.
     *
     * @return a <tt>DraughtPiece</tt> object if it exists, null otherwise
     */
    @Override
    public DraughtPiece getPiece() {
        return (DraughtPiece) super.getPiece();
    }

    /**
     * Returns a unique number depending on what kind of <tt>DraughtPiece</tt> object
     * is contained within this tile
     *
     * @return 0 if empty, 1 for red man, 2 for red king, 3 for white man, 4 for white king
     */
    @Override
    public int getType() {
        if (isEmpty()) {
            return 0;
        }

        DraughtPiece piece = getPiece();
        if (piece.isRed() && !piece.isKing()) {
            return 1;
        } else if (piece.isRed() && piece.isKing()) {
            return 2;
        } else if (!piece.isRed() && !piece.isKing()) {
            return 3;
        } else if (!piece.isRed() && piece.isKing()) {
            return 4;
        } else {
            throw new IllegalStateException("There must only be four types of pieces in Draughts.");
        }
    }

    /**
     * Reset's the tile's background color to its default
     */
    public void clearSelection() {
        setTileColor(defaultColor);
    }
}
