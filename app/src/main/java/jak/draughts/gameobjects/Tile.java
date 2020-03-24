package jak.draughts.gameobjects;

import jak.draughts.gameobjects.pieces.Piece;

public class Tile {

    private Piece piece;

    public Tile(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean hasPiece() {
        return piece != null;
    }

    public void removePiece() {
        this.piece = null;
    }

    public void getPieceId() {
        // TODO
    }
}
