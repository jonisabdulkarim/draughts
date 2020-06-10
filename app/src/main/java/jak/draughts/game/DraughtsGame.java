package jak.draughts.game;

import android.util.Log;

import java.util.List;

import jak.draughts.Coordinates;
import jak.draughts.Room;
import jak.draughts.TileColor;
import jak.draughts.game.gameobjects.DraughtBoard;
import jak.draughts.game.gameobjects.DraughtPiece;
import jak.draughts.game.gameobjects.DraughtTile;

/**
 * This class serves as the base class for all draughts game modes.
 * An object of this class will follow the default rules of the game,
 * otherwise known as "Go As You Please" or "GAYP" for short.
 */
public class DraughtsGame extends Game {

    private String TAG;

    private DraughtBoard board;
    private DraughtPiece selectedPiece;
    private Coordinates lastMovedPieceCoords;

    private int turn; // turn number
    private boolean isMyTurn; // true if it's player's turn
    private boolean isRed; // true if player controls red pieces

    DraughtsGame(String roomId, int turn) {
        TAG = getClass().getName();

        initialiseDatabase(roomId);
        board = new DraughtBoard();

        this.turn = turn;
        this.selectedPiece = null;
    }

    @Override
    public void updateBoard() {
        if (room.getTurn() == this.turn) {
            isMyTurn = true;
            Log.d(TAG, "playerBlocked(): " + playerBlocked() + ", canAnyMove():" + canAnyMove() + ", canAnyCapture():" + canAnyCapture());
            gameOver(); // check gameOver at beginning of round
        }

        updateBoardData();
    }

    private void updateBoardData() {
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
        for (int row = 0; row < 8; row++) {
            sb.append("[");
            for (int col = 0; col < 8; col++) {
                sb.append(roomDataSet.get(new Coordinates(row, col).getPosition()));
                if (col != 7)
                    sb.append(", ");
                else
                    sb.append("]");
            }
            sb.append("\n");
        }
        Log.d(TAG, "Board: \n" + sb.toString());
    }

    @Override
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
            if (!board.isEmpty(clickCoords)) {
                deSelect();
            } else {
                makeMove(clickCoords); // move/capture if possible
            }
        } else {
            select(clickCoords); // select piece/highlight tiles
        }

        adapter.update();
    }

    private void makeMove(Coordinates coords) {
        DraughtTile tile = board.getTile(coords);
        switch (tile.getTileColor()) {
            case CAPTURE_SELECT:
                lastMovedPieceCoords = board.capture(selectedPiece, coords);
                captureAgain();
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

    private void captureAgain() {
        board.deselect();
        if (canCapture(selectedPiece, true)) {
            board.writeDataSet();
            endMove();
        } else {
            deSelect();
            endTurn();
        }
    }

    private void deSelect() {
        board.upgradeToKing(selectedPiece);
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
                    if (canMove(piece, true)) {
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

    /**
     * Checks if any of this team's pieces can capture.
     *
     * @return true if any team piece can capture, false otherwise
     * @see DraughtsGame#canCapture(DraughtPiece, boolean)
     */
    private boolean canAnyCapture() {
        boolean mustCapture = false;
        List<DraughtPiece> teamPieces = board.getTeamPieces(isRed);

        for (DraughtPiece piece : teamPieces) {
            mustCapture = mustCapture || canCapture(piece, false);
        }

        return mustCapture;
    }

    /**
     * Checks if the given piece can capture in any of the four possible
     * directions. If alsoSelect is set to true, it will also highlight
     * the tiles that the given piece can be moved to.
     *
     * @param piece      the capturing piece
     * @param alsoSelect true to highlight tiles, false otherwise
     * @return true if it can capture in at least one direction, false otherwise
     * @see DraughtsGame#canCapture(Coordinates, Coordinates, boolean)
     */
    private boolean canCapture(DraughtPiece piece, boolean alsoSelect) {
        boolean result;
        boolean anyResult = false;

        if (piece.isRed() || piece.isKing()) {
            // down-left
            Coordinates removeCoords = piece.getCoordinates().moveDownLeft(1);
            Coordinates putCoords = piece.getCoordinates().moveDownLeft(2);
            result = canCapture(removeCoords, putCoords, alsoSelect);
            anyResult = result;

            // down-right
            removeCoords = piece.getCoordinates().moveDownRight(1);
            putCoords = piece.getCoordinates().moveDownRight(2);
            result = canCapture(removeCoords, putCoords, alsoSelect);
            anyResult = anyResult || result;
        }

        if (!piece.isRed() || piece.isKing()) {
            // up-left
            Coordinates removeCoords = piece.getCoordinates().moveUpLeft(1);
            Coordinates putCoords = piece.getCoordinates().moveUpLeft(2);
            result = canCapture(removeCoords, putCoords, alsoSelect);
            anyResult = anyResult || result;

            // up-right
            removeCoords = piece.getCoordinates().moveUpRight(1);
            putCoords = piece.getCoordinates().moveUpRight(2);
            result = canCapture(removeCoords, putCoords, alsoSelect);
            anyResult = anyResult || result;
        }

        return anyResult;
    }

    /**
     * Checks if a piece can capture the specified piece. If alsoSelect is
     * set to true, it will also highlight the tiles.
     * <p>
     * While three coordinates are needed to check for captures, only two
     * are given since the third can be deducted by taking into account
     * the direction of the move.
     *
     * @param removeCoords location of opponent's piece to capture
     * @param putCoords    destination of player's piece
     * @param alsoSelect   true to highlight tiles, false otherwise
     * @return true if it can capture, false otherwise
     */
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
    private boolean canMove(DraughtPiece piece, boolean alsoSelect) {
        boolean result;
        boolean anyResult = false;

        if (piece.isRed() || piece.isKing()) {
            // down-left
            Coordinates movedCoords = piece.getCoordinates().moveDownLeft(1);
            result = canMove(movedCoords, alsoSelect);
            anyResult = result;

            // down-right
            movedCoords = piece.getCoordinates().moveDownRight(1);
            result = canMove(movedCoords, alsoSelect);
            anyResult = anyResult || result;
        }

        if (!piece.isRed() || piece.isKing()) {
            // up-left
            Coordinates movedCoords = piece.getCoordinates().moveUpLeft(1);
            result = canMove(movedCoords, alsoSelect);
            anyResult = anyResult || result;

            // up-right
            movedCoords = piece.getCoordinates().moveUpRight(1);
            result = canMove(movedCoords, alsoSelect);
            anyResult = anyResult || result;
        }

        return anyResult;
    }

    private boolean canMove(Coordinates movedCoords, boolean alsoSelect) {
        if (board.inRange(movedCoords) && board.isEmpty(movedCoords)) {
            if (alsoSelect) {
                board.selectTile(movedCoords, TileColor.SELECTED);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean playerBlocked() {
        return !canAnyCapture() && !canAnyMove();
    }

    private boolean canAnyMove() {
        boolean result = false;
        List<DraughtPiece> teamPieces = board.getTeamPieces(isRed);

        for (DraughtPiece piece : teamPieces) {
            result = result || canMove(piece, false);
        }

        return result;
    }

    @Override
    public void endMove() {
        room.setDataSet(board.getDataSet());
        room.setMovedPiece(lastMovedPieceCoords);
        database.setRoom(room);
    }

    @Override
    public void endTurn() {
        isMyTurn = false;
        room.setTurn(room.getTurn() == 0 ? 1 : 0);
        room.setDataSet(board.getDataSet());
        room.setMovedPiece(lastMovedPieceCoords);
        database.setRoom(room);
    }

    @Override
    public boolean gameOver() {
        if (playerBlocked()) {
            room.setTurn(Room.STOP);
            room.setStatus(Room.RESULT);
            endGame();
            return true;
        } else {
            return false;
        }
    }

    private void endGame() {
        Log.d(TAG, "Game is over!");
    }

    @Override
    public List<Integer> getDataSet() {
        return board.getDataSet();
    }

    @Override
    public List<TileColor> getBackgroundSet() {
        return board.getBackgroundSet();
    }
}
