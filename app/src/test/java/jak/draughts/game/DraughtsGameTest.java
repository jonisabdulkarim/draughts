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

    @Before
    public void setUp() {
        game = Game.chooseGameMode('D');
    }

    @Test
    public void chooseGameMode() {
        // throw exception for illegal game modes
        assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                Game.chooseGameMode('X');
            }
        });

        // create draught's game
        game = Game.chooseGameMode('D');
        assertTrue(game instanceof DraughtsGame);
    }

    @Test
    public void updateBoard() {
    }

    @Test
    public void resolveClick() {
        // select non-playable tiles
        Coordinates coords = new Coordinates(0, 0);
        game.resolveClick(coords);
        assertEquals(64, game.getDataSet().size());
        assertEquals(64, game.getBackgroundSet().size());

        // select movable piece
        game.resolveClick(redManOccupiedCoords);
        assertEquals(64, game.getDataSet().size());
        assertEquals(64, game.getBackgroundSet().size());

        Coordinates movedCoords = redManOccupiedCoords.moveDownLeft(1);
        assertEquals(TileColor.SELECTED,
                game.getBackgroundSet().get(movedCoords.getPosition()));

        movedCoords = redManOccupiedCoords.moveDownRight(1);
        assertEquals(TileColor.SELECTED,
                game.getBackgroundSet().get(movedCoords.getPosition()));

        movedCoords = redManOccupiedCoords.moveUpLeft(1);
        assertNotEquals(TileColor.SELECTED,
                game.getBackgroundSet().get(movedCoords.getPosition()));

        movedCoords = redManOccupiedCoords.moveUpRight(1);
        assertNotEquals(TileColor.SELECTED,
                game.getBackgroundSet().get(movedCoords.getPosition()));
    }

    @Test
    public void endTurn() {
    }

    @Test
    public void gameOver() {
    }
}