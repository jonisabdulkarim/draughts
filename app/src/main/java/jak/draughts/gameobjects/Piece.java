package jak.draughts.gameobjects;

import jak.draughts.Coordinates;

abstract class Piece {
    private Coordinates coordinates;

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
