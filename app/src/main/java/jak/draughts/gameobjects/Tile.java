package jak.draughts.gameobjects;

import androidx.annotation.CallSuper;

/**
 * Each object of this class represents an individual tile in a <tt>Board</tt>.
 * <p>
 * The class must be implemented so that it returns the correct sub-type of <tt>Piece</tt> (i.e.
 * <tt>DraughtPiece</tt> when playing Draughts), and to ensure that each different type of
 * <tt>Piece</tt> is assigned a unique number.
 * <p>
 * Each tile can only hold one <tt>Piece</tt>.
 */
abstract class Tile {
    private Piece piece;

    public boolean isEmpty() {
        return piece == null;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void removePiece() {
        this.piece = null;
    }

    /**
     * Method that must be overridden so that it returns the correct sub-type of
     * <tt>Piece</tt> that this tile contains.
     *
     * @return an object of sub-type <tt>Piece</tt>
     */
    @CallSuper
    public Piece getPiece() {
        return piece;
    }

    /**
     * Abstract method that must be implemented so that it returns a unique identifier for each
     * type of piece (i.e. 0 for empty tiles, 1 for red men in draughts, etc).
     *
     * @return a number specifying the type of Piece
     */
    public abstract int getType();
}
