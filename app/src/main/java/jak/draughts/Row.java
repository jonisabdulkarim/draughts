package jak.draughts;

import java.util.ArrayList;
import java.util.List;

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
