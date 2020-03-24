package jak.draughts.gameobjects.pieces;

public class Piece {

    private char side;

    public Piece(char side) {
        this.side = side;
    }

    public char getSide() {
        return side;
    }

    public void setSide(char side) {
        this.side = side;
    }
}
