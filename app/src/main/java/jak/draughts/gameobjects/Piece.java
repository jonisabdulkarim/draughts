package jak.draughts.gameobjects;

import jak.draughts.Coordinates;

/**
 * This class represents any game objects placed on tiles. The abstract class only contains
 * the coordinates of each Piece, and each sub-type of Piece must create its own variables
 * to differentiate between different pieces in the same game.
 * <p>
 * The coordinates allow for quick access to a team's pieces, without searching the whole board.
 */
abstract class Piece {
    private Coordinates coordinates;

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
