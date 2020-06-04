package jak.draughts.game;

import java.util.List;

import jak.draughts.Coordinates;
import jak.draughts.Room;
import jak.draughts.TileColor;
import jak.draughts.game.gameobjects.DraughtBoard;
import jak.draughts.game.gameobjects.DraughtPiece;
import jak.draughts.game.gameobjects.DraughtTile;

/**
 * This class serves as the base class for all draughts game modes.
 * An object of this class will follow the default rules of the game.
 *
 */
public class DraughtsGame extends Game {

    private DraughtBoard board;
    private DraughtPiece selectedPiece;

    private int turn;
    private boolean isMyTurn; // true if it's player's turn
    private boolean isRed; // true if player controls red pieces
    private boolean mustCapture; // true if player can/must capture

    DraughtsGame(String roomId, int turn) {
        initialiseDatabase(roomId);
        board = new DraughtBoard();

        this.turn = turn;
        this.isMyTurn = this.isRed = false; // todo: change

        mustCapture = false;
        selectedPiece = null;
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
    public void updateBoard() {
        if (room.getTurn() == this.turn) {
            // my turn
        } else {
            // not my turn - ignore update
        }
    }

    public void setFirstTurn() {
        if (room.getTurn() == this.turn) {
            isRed = true;
            isMyTurn = true;
        } else {
            isRed = false;
            isMyTurn = false;
        }
    }

    @Override
    public void resolveClick(Coordinates clickCoords) {
        if (!isMyTurn) return; // take no action

        if (hasSelectedPiece()) {
            // TODO: capture
            if (!board.isEmpty(clickCoords)) {
                deSelect();
            } else {
                makeMove(clickCoords);
            }
        } else {
            // TODO: canAnyCapture
            select(clickCoords);
        }
        // TODO: endTurn
        // endTurn();
    }

    private void makeMove(Coordinates coords) {
        DraughtTile tile = board.getTile(coords);
        switch (tile.getTileColor()) {
            case CAPTURE_SELECT:
                // TODO: capturing moves
            case SELECTED:
                board.move(selectedPiece, coords);
            default:
                deSelect(); // must be run in all cases
        }
    }

    private void deSelect() {
        selectedPiece = null;
        board.deselect();
        board.createDataSet();
    }

    private boolean hasSelectedPiece() {
        return selectedPiece != null;
    }

    private void select(Coordinates coords) {
        if (!board.isEmpty(coords)) { // tile is not empty
            DraughtPiece piece = board.getPiece(coords);

            if (isRed == piece.isRed()) { // same team
                if (!canAnyCapture()) { // TODO
                    if (canMove(piece)) {
                        selectedPiece = piece;
                        board.createDataSet();

                    }
                }
            }
        }
    }

    private boolean canAnyCapture() {
        return false;
    }

    /**
     * Checks if the given piece can move in any of the four directions,
     * and if true, sets the destination tile's color accordingly.
     *
     * @param piece the piece to be moved
     * @return true if it can move in at least one direction, false otherwise
     */
    private boolean canMove(DraughtPiece piece) {
        boolean canMove = false;

        if (piece.isRed() || piece.isKing()) {
            // down-left
            Coordinates movedCoords = piece.getCoordinates().moveDownLeft(1);
            if (board.inRange(movedCoords) && board.isEmpty(movedCoords)) {
                board.selectTile(movedCoords, TileColor.SELECTED);
                canMove = true;
            }

            // down-right
            movedCoords = piece.getCoordinates().moveDownRight(1);
            if (board.inRange(movedCoords) && board.isEmpty(movedCoords)) {
                board.selectTile(movedCoords, TileColor.SELECTED);
                canMove = true;
            }
        }

        if (!piece.isRed() || piece.isKing()) {
            // up-left
            Coordinates movedCoords = piece.getCoordinates().moveUpLeft(1);
            if (board.inRange(movedCoords) && board.isEmpty(movedCoords)) {
                board.selectTile(movedCoords, TileColor.SELECTED);
                canMove = true;
            }

            // up-right
            movedCoords = piece.getCoordinates().moveUpRight(1);
            if (board.inRange(movedCoords) && board.isEmpty(movedCoords)) {
                board.selectTile(movedCoords, TileColor.SELECTED);
                canMove = true;
            }
        }

        return canMove;
    }

    @Override
    public void endTurn() {
        board.createDataSet();
        isMyTurn = false;
    }

    @Override
    public boolean gameOver() {
        return false;
    }
}
