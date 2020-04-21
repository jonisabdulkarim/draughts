package jak.draughts.gameobjects;

import jak.draughts.Coordinates;

/**
 * A concrete sub-class of <tt>Piece</tt>, representing all the different types of pieces in
 * Draughts. Each type is differentiated based on two factors: the colour (red or white) and
 * status (man or king). In total, there are four types of pieces in Draughts.
 */
public class DraughtPiece extends Piece {
    private boolean isRed;
    private boolean isKing;

    public DraughtPiece(boolean isRed, boolean isKing, Coordinates coords) {
        setRed(isRed);
        setKing(isKing);
        setCoordinates(coords);
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean red) {
        isRed = red;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing(boolean king) {
        isKing = king;
    }
}
