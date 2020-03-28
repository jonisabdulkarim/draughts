package jak.draughts.gameobjects;

import jak.draughts.Coordinates;

public class Piece {

    /**
     * Specifies what piece is in this tile.
     * A single letter:
     *  - A: red man
     *  - B: red king
     *  - C: white man
     *  - D: white king
     */
    private char type;
    private Coordinates coords;

    public Piece(char type, Coordinates coords) {
        this.type = type;
        this.coords = coords;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public Coordinates getCoords() {
        return coords;
    }

    public void setCoords(Coordinates coords) {
        this.coords = coords;
    }
}