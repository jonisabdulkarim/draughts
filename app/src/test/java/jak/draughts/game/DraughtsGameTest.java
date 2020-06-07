package jak.draughts.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import jak.draughts.Coordinates;
import jak.draughts.TileColor;
import jak.draughts.game.gameobjects.DraughtPiece;

import static org.junit.Assert.*;

public class DraughtsGameTest {

    private Game game;
    private Coordinates redManOccupiedCoords = new Coordinates(2, 1);
    private String roomId;

    @Before
    public void setUp() {
        game = Game.chooseGameMode("GAYP", roomId, 0);
    }

    @Test
    public void chooseGameMode() {
        // throw exception for illegal game modes
        assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                Game.chooseGameMode("GAYP", roomId, -1);
            }
        });

        // create draught's game
        game = Game.chooseGameMode("GAYP", roomId, 0);
        assertTrue(game instanceof DraughtsGame);
    }

    @Test
    public void updateBoard() {
    }

    @Test
    public void resolveClick() {
        // select non-playable tiles results in no change
        Coordinates coords = new Coordinates(0, 0);
        game.resolveClick(coords);
        assertEquals(64, game.getDataSet().size());
        assertEquals(64, game.getBackgroundSet().size());

        // selecting movable pieces results in dataSet changes
        game.resolveClick(redManOccupiedCoords);
        assertEquals(64, game.getDataSet().size());
        assertEquals(64, game.getBackgroundSet().size());

        // down-left should be selectable...
        Coordinates downLeft = redManOccupiedCoords.moveDownLeft(1);
        assertEquals(TileColor.SELECTED,
                game.getBackgroundSet().get(downLeft.getPosition()));

        // ...and down-right too
        Coordinates downRight = redManOccupiedCoords.moveDownRight(1);
        assertEquals(TileColor.SELECTED,
                game.getBackgroundSet().get(downRight.getPosition()));

        // but not up-left...
        Coordinates upLeft = redManOccupiedCoords.moveUpLeft(1);
        assertNotEquals(TileColor.SELECTED,
                game.getBackgroundSet().get(upLeft.getPosition()));

        // ... nor up-right
        Coordinates upRight = redManOccupiedCoords.moveUpRight(1);
        assertNotEquals(TileColor.SELECTED,
                game.getBackgroundSet().get(upRight.getPosition()));

        // pressing on blue tile should move piece and deselect that tile...
        coords = redManOccupiedCoords.moveDownLeft(1);
        game.resolveClick(coords);
        assertEquals(2, game.getDataSet().get(coords.getPosition()).intValue());
        assertNotEquals(TileColor.SELECTED,
                game.getBackgroundSet().get(coords.getPosition()));

        // ...and it should also deselect other tiles
        coords = redManOccupiedCoords.moveDownRight(1);
        assertNotEquals(TileColor.SELECTED,
                game.getBackgroundSet().get(coords.getPosition()));

        // TODO: capturing moves, canAnyCapture check
        // TODO: wrong team check, outside board check
    }

    @Test
    public void endTurn() {
    }

    @Test
    public void gameOver() {
    }
}