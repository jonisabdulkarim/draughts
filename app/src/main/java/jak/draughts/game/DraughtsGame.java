package jak.draughts.game;

import java.util.List;

import jak.draughts.Coordinates;
import jak.draughts.TileColor;
import jak.draughts.game.gameobjects.DraughtBoard;
import jak.draughts.game.gameobjects.DraughtPiece;

/**
 * This class serves as the base class for all draughts game modes.
 * An object of this class will follow the default rules of the game.
 *
 */
public class DraughtsGame extends Game {

    DraughtBoard board;
    DraughtPiece selectedPiece;

    boolean isMyTurn; // true if it's player's turn
    boolean isRed; // true if player controls red pieces
    boolean mustCapture; // true if player can/must capture

    DraughtsGame() {
        this(true);
    }

    DraughtsGame(boolean isRed) {
        board = new DraughtBoard();

        this.isMyTurn = this.isRed = isRed;

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
    public void updateBoard(List<Integer> dataSet, Coordinates movedPieceCoords) {

    }

    @Override
    public void resolveClick(Coordinates clickCoords) {
        if (!isMyTurn) return; // take no action

        if (hasSelectedPiece()) {
            // TODO: deselect, move, capture
        } else {
            // TODO: select, canAnyCapture
            canSelect(clickCoords);
        }
    }

    private boolean hasSelectedPiece() {
        return selectedPiece != null;
    }

    private void canSelect(Coordinates coords) {
        if (board.isEmpty(coords)) return; // cannot select empty tile

        DraughtPiece piece = board.getPiece(coords);
        if (isRed != piece.isRed()) return; // wrong team
        if (canAnyCapture()) return; // no select if player's pieces can capture
        if (canMove(piece)) {
            selectedPiece = piece;
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

        if (!piece.isRed() || piece.isKing()) {
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
