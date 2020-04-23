package jak.draughts.gameobjects;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static jak.draughts.TileColor.*;

public class DraughtTileTest {

    private final DraughtPiece redMan = new DraughtPiece(true, false, null);
    private final DraughtPiece redKing = new DraughtPiece(true, true, null);
    private final DraughtPiece whiteMan = new DraughtPiece(false, false, null);
    private final DraughtPiece whiteKing = new DraughtPiece(false, true, null);

    private DraughtTile emptyTile;
    private DraughtTile redManTile;
    private DraughtTile redKingTile;
    private DraughtTile whiteManTile;
    private DraughtTile whiteKingTile;

    @Before
    public void setUp() {
        emptyTile = new DraughtTile(null, WHITE);
        redManTile = new DraughtTile(redMan, GREEN);
        redKingTile = new DraughtTile(redKing, GREEN);
        whiteManTile = new DraughtTile(whiteMan, GREEN);
        whiteKingTile = new DraughtTile(whiteKing, GREEN);
    }

    @Test
    public void isEmpty() {
        assertTrue(emptyTile.isEmpty());
        assertFalse(redManTile.isEmpty());
        assertFalse(redKingTile.isEmpty());
        assertFalse(whiteManTile.isEmpty());
        assertFalse(whiteKingTile.isEmpty());
    }

    @Test
    public void setPiece() {
        assertTrue(emptyTile.isEmpty());
        emptyTile.setPiece(redMan);
        assertFalse(emptyTile.isEmpty());
        assertEquals(emptyTile.getPiece(), redMan);
    }

    @Test
    public void removePiece() {
        emptyTile.removePiece();
        assertFalse(redManTile.isEmpty());
        redManTile.removePiece();
        assertTrue(redManTile.isEmpty());
    }

    @Test
    public void getPiece() {
        assertNull(emptyTile.getPiece());
        assertEquals(redMan, redManTile.getPiece());
        assertEquals(redKing, redKingTile.getPiece());
        assertEquals(whiteMan, whiteManTile.getPiece());
        assertEquals(whiteKing, whiteKingTile.getPiece());
    }

    @Test
    public void getType() {
        assertEquals(0, emptyTile.getType());
        assertEquals(1, redManTile.getType());
        assertEquals(2, redKingTile.getType());
        assertEquals(3, whiteManTile.getType());
        assertEquals(4, whiteKingTile.getType());
    }

    @Test
    public void getTileColor() {
        assertEquals(WHITE, emptyTile.getTileColor());
        assertEquals(GREEN, redManTile.getTileColor());
    }

    @Test
    public void setTileColor() {
        assertNotEquals(GREEN, emptyTile.getTileColor());
        emptyTile.setTileColor(GREEN);
        assertEquals(GREEN, emptyTile.getTileColor());
    }

    @Test
    public void clearSelection() {
        assertNotEquals(GREEN, emptyTile.getTileColor());
        emptyTile.setTileColor(GREEN);
        assertEquals(GREEN, emptyTile.getTileColor());
        emptyTile.clearSelection();
        assertEquals(WHITE, emptyTile.getTileColor());
    }
}