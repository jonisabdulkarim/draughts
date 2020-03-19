package jak.draughts;

public class Piece {

    private char type;

    public Piece() {
        // empty for json
    }

    public Piece(char type) {
        this.type = type;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }
}
