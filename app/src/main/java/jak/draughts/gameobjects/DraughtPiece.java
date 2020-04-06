package jak.draughts.gameobjects;

public class DraughtPiece extends Piece {
    private boolean isRed;
    private boolean isKing;

    public DraughtPiece(boolean isRed, boolean isKing) {
        this.isRed = isRed;
        this.isKing = isKing;
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
