package jak.draughts;

public class Tile {

    private Piece piece;

    public Tile() {
        // empty for json
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void removePiece(Piece piece) {
        this.piece = null;
    }
}
