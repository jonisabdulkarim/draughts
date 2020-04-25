package jak.draughts.game;

import java.util.List;

import jak.draughts.Coordinates;
import jak.draughts.TileColor;

/**
 * This abstract class serves as a base class for all game modes,
 * including draughts and chess. Implementing classes are free to
 * implement any game rules or mechanics.
 * <p>
 * Sub-classes must implement methods to send and receive board data
 * from both the adapter and database. This includes the both dataSets
 * in the Board class, as well as the resolveClick() method in the adapter
 * and the listenForChanges() method in the database class.
 */
public abstract class Game {

    public static Game chooseGameMode(char mode) {
        switch(mode) {
            case 'D':
                Game game = new DraughtsGame();

                return game;
            default:
                throw new IllegalArgumentException("Invalid game mode");
        }
    }

    public abstract List<Integer> getDataSet();

    public abstract List<TileColor> getBackgroundSet();

    /**
     * Method called by the database that informs the game of any
     * changes made in the opponent's board. Must include the coords
     * of the piece that was moved during a turn to ensure objects are
     * correctly removed.
     *
     * @param dataSet the updated board in a 1D list
     * @param movedPieceCoords old position of the piece that was moved
     */
    public abstract void updateBoard(List<Integer> dataSet, Coordinates movedPieceCoords);

    /**
     * Method called by the adapter informing the game that a
     * tile has been clicked on.
     *
     * @param clickCoords the tile that was pressed
     */
    public abstract void resolveClick(Coordinates clickCoords);

    /**
     * Clean-up method that updates the dataSets, clears any selections
     * and then informs the database and adapter of any changes made
     * during a turn.
     */
    public abstract void endTurn();


    /**
     * Confirms whether the game has ended. Must be run at the beginning of
     * each new turn. For game over to be accepted, this method must return
     * true for both players.
     */
    public abstract boolean gameOver();
}
