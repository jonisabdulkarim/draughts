package jak.draughts.gameobjects;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DraughtBoardTest {

    // test values
    private static final Integer[] starterBoardArray = {
            0, 1, 0, 1, 0, 1, 0, 1,
            1, 0, 1, 0, 1, 0, 1, 0,
            0, 1, 0, 1, 0, 1, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            3, 0, 3, 0, 3, 0, 3, 0,
            0, 3, 0, 3, 0, 3, 0, 3,
            3, 0, 3, 0, 3, 0, 3, 0,
    };
    private static final List<Integer> starterBoard = Arrays.asList(starterBoardArray);

    private DraughtBoard board;

    @Before
    public void setUp() {
        board = new DraughtBoard();
    }

    @Test
    public void getDataSet() {
        assertEquals(board.getDataSet(), starterBoard);
    }

    @Test
    public void setDataSet() {

    }

    @Test
    public void createDataSet() {
    }

    @Test
    public void getBoard() {
    }

    @Test
    public void move() {
    }

    @Test
    public void put() {
    }

    @Test
    public void removePiece() {
    }

    @Test
    public void removeCoordinates() {
    }

    @Test
    public void isEmpty() {
    }
}