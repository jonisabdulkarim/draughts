package jak.draughts.game;

import java.util.List;

import jak.draughts.Coordinates;
import jak.draughts.TileColor;
import jak.draughts.game.gameobjects.DraughtBoard;

/**
 * This class serves as the base class for all draughts game modes.
 * An object of this class will follow the default rules of the game.
 *
 */
public class DraughtsGame extends Game {

    DraughtBoard board;

    boolean isMyTurn; // true if it's player's turn
    boolean isRed; // true if player controls red pieces
    boolean mustCapture; // true if player can/must capture
    boolean selectedPiece; // true if a piece is selected

    DraughtsGame() {
        this(true);
    }

    DraughtsGame(boolean isRed) {
        board = new DraughtBoard();

        this.isMyTurn = this.isRed = isRed;

        mustCapture = false;
        selectedPiece = false;
    }

    @Override
    public List<Integer> getDataSet() {
        return board.getDataSet();
    }

    @Override
    public List<TileColor> getBackgroundSet() {
        return board.getBackgroundSet();
    }

    @Override
    public void updateBoard(List<Integer> dataSet, Coordinates movedPieceCoords) {

    }

    @Override
    public void resolveClick(Coordinates clickCoords) {

    }

    @Override
    public void endTurn() {

    }

    @Override
    public boolean gameOver() {
        return false;
    }
}
