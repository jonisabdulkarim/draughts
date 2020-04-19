package jak.draughts.gameobjects;

public class DraughtTile extends Tile {
    DraughtTile(DraughtPiece piece) {
        setPiece(piece);
    }

    @Override
    public DraughtPiece getPiece() {
        return (DraughtPiece) super.piece;
    }

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
        } else {
            return 4;
        }
    }
}
