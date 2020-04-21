package jak.draughts.gameobjects;

import java.util.List;

/**
 * This abstract class serves as a contract for game boards. Its only requirement is to
 * create a suitable 1D list of integers, so that the state of the board can be parsed by
 * both the database and RecyclerView Adapter.
 */
public abstract class Board {

    private List<Integer> dataSet;

    /**
     * Abstract method that when implemented, will create 1D list of integers from the
     * board and store it in the dataSet variable.
     */
    public abstract void createDataSet();

    public List<Integer> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<Integer> dataSet) {
        this.dataSet = dataSet;
    }
}
