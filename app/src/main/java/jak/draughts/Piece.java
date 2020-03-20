package jak.draughts;


/**
 * This class represents the units of a board game, which are
 * stored in Tile objects as part of a Board object. An empty
 * tile has a null value for its piece object.
 */
public class Piece {

    private String type;

    /**
     * A default empty method, should only be used for
     * converting objects into Firestore data.
     */
    public Piece() { }

    public Piece(String type) {
        this.type = type;
    }

    /**
     * This getter is to be used only for storing data in
     * Firebase
     *
     * @return single letter string denoting type of piece
     * in this tile
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setType(char type) { this.type = String.valueOf(type); }
}
