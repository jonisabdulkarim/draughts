package jak.draughts.gameobjects;

import jak.draughts.Coordinates;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DraughtPieceTest {

    // initial coords
    private static final Coordinates DUMMY_COORDINATES = new Coordinates(0, 0);

    // coords in second and third green tiles
    private static final Coordinates ZERO_TWO_COORDINATES = new Coordinates(0, 2);
    private static final Coordinates ZERO_FOUR_COORDINATES = new Coordinates(0, 4);

    private DraughtPiece redMan;
    private DraughtPiece redKing;
    private DraughtPiece whiteMan;
    private DraughtPiece whiteKing;

    @Before
    public void setUp() {
        redMan = new DraughtPiece(true, false, DUMMY_COORDINATES);
        redKing = new DraughtPiece(true, true, DUMMY_COORDINATES);
        whiteMan = new DraughtPiece(false, false, DUMMY_COORDINATES);
        whiteKing = new DraughtPiece(false, true, DUMMY_COORDINATES);
    }

    @Test
    public void getCoordinates() {
        assertEquals(redMan.getCoordinates(), DUMMY_COORDINATES);
        assertEquals(redKing.getCoordinates(), DUMMY_COORDINATES);
        assertEquals(whiteKing.getCoordinates(), DUMMY_COORDINATES);
        assertEquals(whiteMan.getCoordinates(), DUMMY_COORDINATES);
    }

    @Test
    public void setCoordinates() {
        redMan.setCoordinates(ZERO_TWO_COORDINATES);
        assertNotEquals(redMan.getCoordinates(), DUMMY_COORDINATES);
        assertEquals(redMan.getCoordinates(), ZERO_TWO_COORDINATES);
    }

    @Test
    public void isRed() {
        assertTrue(redMan.isRed());
        assertTrue(redKing.isRed());
        assertFalse(whiteMan.isRed());
        assertFalse(whiteKing.isRed());
    }

    @Test
    public void setRed() {
        redMan.setRed(false);
        assertFalse(redMan.isRed());
        redMan.setRed(true);
        assertTrue(redMan.isRed());
    }

    @Test
    public void isKing() {
        assertFalse(redMan.isKing());
        assertFalse(whiteMan.isKing());

        assertTrue(redKing.isKing());
        assertTrue(whiteKing.isKing());
    }

    @Test
    public void setKing() {
        assertFalse(redMan.isKing());

        redMan.setKing(false);
        assertFalse(redMan.isKing());

        redMan.setKing(true);
        assertTrue(redMan.isKing());

        redMan.setKing(false);
        assertFalse(redMan.isKing());
    }
}