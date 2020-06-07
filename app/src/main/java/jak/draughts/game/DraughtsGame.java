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
    private Coordinates lastMovedPieceCoords;

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
        lastMovedPieceCoords = room.getMovedPiece();
        if (roomDataSet != null) {
            board.readDataSet(roomDataSet, lastMovedPieceCoords);
            adapter.update();
            logBoard(roomDataSet);
        }
    }


    /**
     * Debugging method that displays the board according to the database.
     * Also displays who's turn it is, what turn the player can move in, and
     * whether it is currently their turn or not.
     *
     * @param roomDataSet integer list representing the board
     */
    private void logBoard(List<Integer> roomDataSet) {
        Log.d(TAG, "Room turn: " + room.getTurn() + ", game turn: " + turn
                + ", isMyTurn = " + isMyTurn + ".");
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
                lastMovedPieceCoords = board.capture(selectedPiece, coords);
                deSelect();
                endTurn(); // TODO: multiple captures
                break;
            case SELECTED:
                lastMovedPieceCoords = board.move(selectedPiece, coords);
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
                if (!canAnyCapture()) { // any piece can capture
                    if (canMove(piece)) {
                        selectedPiece = piece;
                        board.writeDataSet();
                    }
                } else {
                    if (canCapture(piece, true)) { // this piece can capture
                        selectedPiece = piece;
                        board.writeDataSet();
                    }
                }
            }
        }
    }

    private boolean canAnyCapture() {
        mustCapture = false;
        List<DraughtPiece> teamPieces = board.getTeamPieces(isRed);
        for (DraughtPiece piece : teamPieces) {
            if (canCapture(piece, false)) {
                mustCapture = true;
            }
        }
        return mustCapture;
    }

    private boolean canCapture(DraughtPiece piece, boolean alsoSelect) {
        boolean canCaptureResult = false;

        if (piece.isRed() || piece.isKing()) {
            // down-left
            Coordinates removeCoords = piece.getCoordinates().moveDownLeft(1);
            Coordinates putCoords = piece.getCoordinates().moveDownLeft(2);
            canCaptureResult = canCapture(removeCoords, putCoords, alsoSelect);

            // down-right
            removeCoords = piece.getCoordinates().moveDownRight(1);
            putCoords = piece.getCoordinates().moveDownRight(2);
            canCaptureResult = canCaptureResult || canCapture(removeCoords, putCoords, alsoSelect);
        }

        if (!piece.isRed() || piece.isKing()) {
            // up-left
            Coordinates removeCoords = piece.getCoordinates().moveUpLeft(1);
            Coordinates putCoords = piece.getCoordinates().moveUpLeft(2);
            canCaptureResult = canCaptureResult || canCapture(removeCoords, putCoords, alsoSelect);

            // up-right
            removeCoords = piece.getCoordinates().moveUpRight(1);
            putCoords = piece.getCoordinates().moveUpRight(2);
            canCaptureResult = canCaptureResult || canCapture(removeCoords, putCoords, alsoSelect);
        }

        return canCaptureResult;
    }

    private boolean canCapture(Coordinates removeCoords, Coordinates putCoords, boolean alsoSelect) {
        if (board.inRange(putCoords) && board.isEmpty(putCoords)
                && !board.isEmpty(removeCoords)
                && board.getPiece(removeCoords).isRed() != isRed) {
            if (alsoSelect) {
                captureSelect(putCoords);
            }
            return true;
        } else {
            return false;
        }
    }

    private void captureSelect(Coordinates putCoords) {
        board.selectTile(putCoords, TileColor.CAPTURE_SELECT);
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
        room.setMovedPiece(lastMovedPieceCoords);
        database.setRoom(room);
    }

    @Override
    public boolean gameOver() {
        return false;
    }
}
