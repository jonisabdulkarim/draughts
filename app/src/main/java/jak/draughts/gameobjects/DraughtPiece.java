package jak.draughts.gameobjects;

import jak.draughts.Coordinates;

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
