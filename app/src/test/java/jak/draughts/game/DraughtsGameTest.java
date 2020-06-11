package jak.draughts.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import jak.draughts.Coordinates;
import jak.draughts.Room;
import jak.draughts.TileColor;
import jak.draughts.game.gameobjects.DraughtBoard;
import jak.draughts.game.gameobjects.DraughtPiece;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DraughtsGameTest {

    @Mock private GameDatabase database;
    @Mock private GameAdapter adapter;

    @Spy private DraughtBoard board;
    @Mock private DraughtPiece selectedPiece = null;
    @Mock private Coordinates lastMovedPieceCoords;

    @InjectMocks private DraughtsGame game = new DraughtsGame("", 0);

    private Coordinates redManOccupiedCoords = new Coordinates(2, 1);

    @Before
    public void setUp() {

    }

    @Test
    public void chooseGameMode() {

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

    @Test
    public void initialiseDatabase() {
    }

    @Test
    public void getRoom() {
    }

    @Test
    public void setRoom() {
    }

    @Test
    public void getAdapter() {
    }

    @Test
    public void setAdapter() {
    }

    @Test
    public void stopGame() {
    }

    @Test
    public void setFirstTurn() {
    }

    @Test
    public void endMove() {
    }

    @Test
    public void getDataSet() {
    }

    @Test
    public void getBackgroundSet() {
    }
}