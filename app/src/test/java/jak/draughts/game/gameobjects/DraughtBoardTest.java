package jak.draughts.game.gameobjects;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import jak.draughts.Coordinates;
import jak.draughts.TileColor;

import static jak.draughts.TileColor.GREEN;
import static jak.draughts.TileColor.SELECTED;
import static jak.draughts.TileColor.WHITE;
import static org.junit.Assert.*;

public class DraughtBoardTest {

    // test values
    private static final Integer[] EMPTY_BOARD_ARRAY = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
    };
    private static final Integer[] STARTER_BOARD_ARRAY = {
            0, 1, 0, 1, 0, 1, 0, 1,
            1, 0, 1, 0, 1, 0, 1, 0,
            0, 1, 0, 1, 0, 1, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            3, 0, 3, 0, 3, 0, 3, 0,
            0, 3, 0, 3, 0, 3, 0, 3,
            3, 0, 3, 0, 3, 0, 3, 0,
    };
    private static final TileColor[] STARTED_BOARD_BACKGROUND_ARRAY = {
            WHITE, GREEN, WHITE, GREEN, WHITE, GREEN, WHITE, GREEN,
            GREEN, WHITE, GREEN, WHITE, GREEN, WHITE, GREEN, WHITE,
            WHITE, GREEN, WHITE, GREEN, WHITE, GREEN, WHITE, GREEN,
            GREEN, WHITE, GREEN, WHITE, GREEN, WHITE, GREEN, WHITE,
            WHITE, GREEN, WHITE, GREEN, WHITE, GREEN, WHITE, GREEN,
            GREEN, WHITE, GREEN, WHITE, GREEN, WHITE, GREEN, WHITE,
            WHITE, GREEN, WHITE, GREEN, WHITE, GREEN, WHITE, GREEN,
            GREEN, WHITE, GREEN, WHITE, GREEN, WHITE, GREEN, WHITE
    };

    private List<Integer> emptyBoard;
    private List<Integer> starterBoard;
    private List<TileColor> starterBoardBackground;
    private DraughtPiece redManPiece;
    private DraughtPiece whiteManPiece;
    private DraughtBoard board;
    private Coordinates redManOccupiedCoords;
    private Coordinates whiteManOccupiedCoords;
    private Coordinates redManPieceCoords;
    private Coordinates whiteManPieceCoords;

    @Before
    public void setUp() {
        board = new DraughtBoard();

        emptyBoard = Arrays.asList(EMPTY_BOARD_ARRAY);
        starterBoard = Arrays.asList(STARTER_BOARD_ARRAY);
        starterBoardBackground = Arrays.asList(STARTED_BOARD_BACKGROUND_ARRAY);

        redManPiece = new DraughtPiece(true, false, null);
        whiteManPiece = new DraughtPiece(false, false, null);

        // empty tiles, suitable for placing above pieces
        redManPieceCoords = new Coordinates(3, 2);
        whiteManPieceCoords = new Coordinates(4, 6);

        // coords which are already occupied
        redManOccupiedCoords = new Coordinates(2, 1);
        whiteManOccupiedCoords = new Coordinates(5, 4);
    }

    @Test
    public void getDataSet() {
        assertEquals(board.getDataSet(), starterBoard);
    }

    @Test
    public void setDataSet() {
        assertEquals(board.getDataSet(), starterBoard);
        board.setDataSet(emptyBoard);
        assertNotEquals(board.getDataSet(), starterBoard);
        assertEquals(board.getDataSet(), emptyBoard);
    }

    @Test
    public void createDataSet() {
        put(); // places redManPiece on 3/2 coordinates
        assertEquals(board.getDataSet(), starterBoard);
        assertEquals(0,
                board.getDataSet().get(redManPieceCoords.getPosition()).intValue());

        board.writeDataSet();

        assertNotEquals(board.getDataSet(), starterBoard);
        assertEquals(1,
                board.getDataSet().get(redManPieceCoords.getPosition()).intValue());
    }

    @Test
    public void movePiece() {
        put();
        assertEquals(redManPiece, board.getBoard()
                .get(redManPieceCoords.getX())
                .get(redManPieceCoords.getY()).getPiece());

        board.move(redManPiece, whiteManPieceCoords);

        // is removed
        assertNull(board.getBoard()
                .get(redManPieceCoords.getX())
                .get(redManPieceCoords.getY()).getPiece());

        // new place
        assertEquals(redManPiece, board.getBoard()
                .get(whiteManPieceCoords.getX())
                .get(whiteManPieceCoords.getY()).getPiece());

        // piece's coords updated
        assertEquals(redManPiece.getCoordinates(), whiteManPieceCoords);
    }

    @Test
    public void moveCoords() {
        DraughtPiece piece = board.getBoard()
                .get(whiteManOccupiedCoords.getX())
                .get(whiteManOccupiedCoords.getY()).getPiece();

        board.move(whiteManOccupiedCoords, whiteManPieceCoords);

        assertNull(board.getBoard()
                .get(whiteManOccupiedCoords.getX())
                .get(whiteManOccupiedCoords.getY()).getPiece());

        assertEquals(piece, board.getBoard()
                .get(whiteManPieceCoords.getX())
                .get(whiteManPieceCoords.getY()).getPiece());
    }

    @Test
    public void put() {
        board.put(redManPiece, redManPieceCoords);
        assertEquals(redManPieceCoords, redManPiece.getCoordinates());
        assertEquals(redManPiece, board.getBoard().get(3).get(2).getPiece());
    }

    @Test
    public void removePiece() {
        put(); // places redManPiece on 3/2 coordinates
        board.remove(redManPiece);
        assertNull(redManPiece.getCoordinates());
        assertNull(board.getBoard().get(3).get(2).getPiece());
    }

    @Test
    public void removeCoordinates() {
        DraughtPiece piece = board.getBoard().get(2).get(1).getPiece(); // also checks null
        board.remove(redManOccupiedCoords);
        assertNull(piece.getCoordinates());
        assertNull(board.getBoard().get(2).get(1).getPiece());
    }

    @Test
    public void isEmpty() {
        Coordinates coords = new Coordinates(2, 1);
        assertFalse(board.isEmpty(coords));
        coords = new Coordinates(3, 2);
        assertTrue(board.isEmpty(coords));

    }

    @Test
    public void selectTile() {
        assertEquals(starterBoardBackground, board.getBackgroundSet());
        Coordinates coords = new Coordinates(0);
        board.selectTile(coords, SELECTED);
        board.writeDataSet();
        assertNotEquals(starterBoardBackground, board.getBackgroundSet());
    }

    @Test
    public void inRange() {
        assertTrue(board.inRange(redManOccupiedCoords));
        assertTrue(board.inRange(whiteManOccupiedCoords));
        assertTrue(board.inRange(redManPieceCoords));
        assertTrue(board.inRange(whiteManPieceCoords));

        Coordinates outOfRangeCoords = new Coordinates(0, -1);
        assertFalse(board.inRange(outOfRangeCoords));
    }

    @Test
    public void getPiece() {
        DraughtPiece piece = board.getBoard().get(redManOccupiedCoords.getX())
                .get(redManOccupiedCoords.getY()).getPiece();

        assertEquals(piece, board.getPiece(redManOccupiedCoords));
    }
}