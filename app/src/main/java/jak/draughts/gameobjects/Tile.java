package jak.draughts.gameobjects;

abstract class Tile {
    protected Piece piece;

    public boolean isEmpty() {
        return piece == null;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void removePiece() {
        this.piece = null;
    }

    public abstract Piece getPiece();

    public abstract int getType();
}
