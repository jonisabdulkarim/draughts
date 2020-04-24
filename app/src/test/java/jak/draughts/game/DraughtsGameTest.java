package jak.draughts.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

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
}