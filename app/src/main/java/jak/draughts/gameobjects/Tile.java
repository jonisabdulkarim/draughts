package jak.draughts.gameobjects;

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

    /**
     * This method is used in a loop to convert the board
     * of characters into numbers
     * @return integer between 0 and 5
     */
    public int getPieceId() {
        if (!hasPiece()) return 0;
        switch (getPiece().getType()) {
            case 'A':
                return 1;
            case 'B':
                return 2;
            case 'C':
                return 3;
            case 'D':
                return 4;
            default:
                throw new IllegalStateException();
        }
    }
}
