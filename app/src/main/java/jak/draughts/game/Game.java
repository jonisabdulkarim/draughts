package jak.draughts.game;

import java.util.List;

import jak.draughts.Coordinates;
import jak.draughts.Room;
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

    private Room room;
    private GameDatabase database;
    private GameAdapter adapter;

    public static Game chooseGameMode(String mode, String roomId, int turn) {
        switch(mode) {
            case "GAYP":
            case "3MOVE":
                Game game = new DraughtsGame(roomId, turn);

                return game;
            default:
                throw new IllegalArgumentException("Invalid game mode");
        }
    }

    public void initialiseDatabase(String roomId) {
        setDatabase(new GameDatabase(this));
        getDatabase().getRoom(roomId);
        getDatabase().listenForChanges(roomId);
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public GameAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(GameAdapter adapter) {
        this.adapter = adapter;
    }

    public GameDatabase getDatabase() {
        return database;
    }

    public void setDatabase(GameDatabase database) {
        this.database = database;
    }

    public abstract List<Integer> getDataSet();

    public abstract List<TileColor> getBackgroundSet();

    /**
     * Determines first turn from this room. Called by the database
     * once the room object has been set.
     */
    public abstract void setFirstTurn();

    /**
     * Method called by the database that informs the game of new
     * changes made in the room. The method will examine the room's
     * contents, including the board, to see if there are any
     * significant changes, i.e. a change in turn.
     */
    public abstract void updateBoard();

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
     * Sends an updated dataSet to the database after each move. Does not
     * update the turn status. This method will be used in games that can
     * have multiple moves in one turn i.e. capturing moves in draughts.
     */
    public abstract void endMove();

    /**
     * Confirms whether the game has ended. Must be run at the beginning of
     * each new turn. For game over to be accepted, this method must return
     * true for both players.
     */
    public abstract boolean gameOver();

    /**
     * Cleans up resources before activity shutdown. Includes detaching
     * real-time listeners from the database.
     */
    public void stopGame() {
        getDatabase().detachListeners();
    }
}
