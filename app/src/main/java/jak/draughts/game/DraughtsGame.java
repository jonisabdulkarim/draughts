package jak.draughts.game;

import android.util.Log;

import java.util.List;

import jak.draughts.Coordinates;
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

    private String TAG;

    private DraughtBoard board;
    private DraughtPiece selectedPiece;

    private int turn;
    private boolean isMyTurn; // true if it's player's turn
    private boolean isRed; // true if player controls red pieces
    private boolean mustCapture; // true if player can/must capture

    DraughtsGame(String roomId, int turn) {
        TAG = getClass().getName();
        Log.d(TAG, "GAME IS STARTED.");

        initialiseDatabase(roomId);
        board = new DraughtBoard();

        this.turn = turn;

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
        Log.d(TAG, "BOARD IS UPDATED.");

        if (room.getTurn() == this.turn) {
            isMyTurn = true;
        }

        List<Integer> roomDataSet = room.getDataSet();
        if (roomDataSet != null) {
            board.readDataSet(roomDataSet);
            adapter.update();
            logBoard(roomDataSet);
        }
    }

    private void logBoard(List<Integer> roomDataSet) {
        Log.d(TAG, "Room turn: " + room.getTurn() + ", game turn: " + turn
                + ", isMyTurn = " + isMyTurn + ".");
        roomDataSet = getDataSet();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append("[");
            for (int j = 0; j < 8; j++) {
                sb.append(roomDataSet.get((i*8)+j));
                if (j != 7)
                    sb.append(", ");
                else
                    sb.append("]");
            }
            sb.append("\n");
        }
        Log.d("BOARD", "\n" + sb.toString());
    }

    public void setFirstTurn() {
        Log.d(TAG, "FIRST TURN IS SET.");
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
        adapter.update();
    }

    private void makeMove(Coordinates coords) {
        DraughtTile tile = board.getTile(coords);
        switch (tile.getTileColor()) {
            case CAPTURE_SELECT:
                // TODO: capturing moves
            case SELECTED:
                board.move(selectedPiece, coords);
                deSelect();
                endTurn();
                break;
            default:
                deSelect();
        }
    }

    private void deSelect() {
        selectedPiece = null;
        board.deselect();
        board.writeDataSet();
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
                        board.writeDataSet();

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
        Log.d(TAG, "TURN IS ENDED.");
        isMyTurn = false;
        room.setTurn(room.getTurn() == 0 ? 1 : 0);
        room.setDataSet(board.getDataSet());
        database.setRoom(room);
    }

    @Override
    public boolean gameOver() {
        return false;
    }
}
