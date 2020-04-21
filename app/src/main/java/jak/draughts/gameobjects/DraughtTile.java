package jak.draughts.gameobjects;

/**
 * Concrete sub-class of <tt>Tile</tt>, representing each individual tile
 * in a <tt>DraughtBoard</tt>.
 * <p>
 * Each <tt>DraughtTile</tt> object can only hold <tt>DraughtPiece</tt>.
 */
public class DraughtTile extends Tile {
    DraughtTile(DraughtPiece piece) {
        setPiece(piece);
    }

    /**
     * Returns the piece object contained in this tile, or null if there is no piece.
     *
     * @return a <tt>DraughtPiece</tt> object if it exists, null otherwise
     */
    @Override
    public DraughtPiece getPiece() {
        return (DraughtPiece) super.piece;
    }

    /**
     * Returns a unique number depending on whether a <tt>DraughtPiece</tt> object is contained
     * within this tile, and if true,
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
}
