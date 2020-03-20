package jak.draughts;

import java.util.ArrayList;
import java.util.List;

/**
 * The Row class is used to store each row of tiles in a board.
 * Since Firebase Firestore does not allow for nested arrays, a
 * custom object must be used to store 2D arrays such as boards.
 */
public class Row {

    private List<Tile> tiles;

    Row() {
        tiles = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            tiles.add(new Tile());
        }
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }
}
