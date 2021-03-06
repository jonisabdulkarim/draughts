package jak.draughts.game.gameobjects;

import java.util.List;

import jak.draughts.Coordinates;
import jak.draughts.TileColor;

/**
 * This abstract class serves as a contract for game boards. Its only requirement is to
 * create a suitable 1D list of integers, so that the state of the board can be parsed by
 * both the database and RecyclerView Adapter.
 */
public abstract class Board {

    private List<Integer> dataSet;
    private List<TileColor> backgroundSet;

    /**
     * Abstract method that, when implemented, will create 1D list of integers from the
     * board and store it in the dataSet variable. The result must be stored using the
     * setDataSet(...) method.
     * <p>
     * The adapter and database must run this method before attempting to get the
     * dataSet or the backgroundSet, or else the data may be outdated.
     */
    public abstract void writeDataSet();

    public abstract void readDataSet(List<Integer> dataSet, Coordinates movedPieceCoords);

    public List<Integer> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<Integer> dataSet) {
        this.dataSet = dataSet;
    }

    public List<TileColor> getBackgroundSet() {
        return backgroundSet;
    }

    public void setBackgroundSet(List<TileColor> backgroundSet) {
        this.backgroundSet = backgroundSet;
    }
}
