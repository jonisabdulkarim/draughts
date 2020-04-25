package jak.draughts.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import jak.draughts.Coordinates;

import static org.junit.Assert.*;

public class DraughtsGameTest {

    private Game game;

    @Before
    public void setUp() {
        game = Game.chooseGameMode('D');
    }

    @Test
    public void chooseGameMode() {
        // throw exception for illegal game modes
        assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
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

        // select empty playable tile

    }

    @Test
    public void endTurn() {
    }

    @Test
    public void gameOver() {
    }
}