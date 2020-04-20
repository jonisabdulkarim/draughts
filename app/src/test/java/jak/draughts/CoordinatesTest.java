package jak.draughts;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoordinatesTest {

    Coordinates zero_zero; // top-left tile, white
    Coordinates zero_one; // one right of above, green
    Coordinates one_zero; // one below of top-left, green
    Coordinates one_one; // one right of above, white

    @Before
    public void setUp() {
        zero_zero = new Coordinates(0, 0);
        zero_one = new Coordinates(0, 1);
        one_zero = new Coordinates(1, 0);
        one_one = new Coordinates(1, 1);
    }

    @Test
    public void getPosition() {
        assertEquals(0, zero_zero.getPosition());
        assertEquals(1, zero_one.getPosition());
        assertEquals(8, one_zero.getPosition());
        assertEquals(9, one_one.getPosition());
    }

    @Test
    public void setPosition() {
        assertNotEquals(zero_zero.getPosition(), zero_one.getPosition());
        zero_one.setPosition(0);
        assertEquals(zero_zero.getPosition(), zero_one.getPosition());
        assertEquals(zero_zero, zero_one); // different coords with same position are equal
    }

    @Test
    public void getX() {
        assertEquals(0, zero_zero.getX());
        assertEquals(0, zero_one.getX());
        assertEquals(1, one_zero.getX());
        assertEquals(1, one_one.getX());
    }

    @Test
    public void setX() {
        Coordinates two_five = new Coordinates(2, 5);
        assertNotEquals(zero_zero.getX(), two_five.getX());
        zero_zero.setX(2);
        assertEquals(zero_zero.getX(), two_five.getX());
    }

    @Test
    public void getY() {
        assertEquals(0, zero_zero.getY());
        assertEquals(1, zero_one.getY());
        assertEquals(0, one_zero.getY());
        assertEquals(1, one_one.getY());
    }

    @Test
    public void setY() {
        Coordinates two_five = new Coordinates(2, 5);
        assertNotEquals(zero_zero.getY(), two_five.getY());
        zero_zero.setY(5);
        assertEquals(zero_zero.getY(), two_five.getY());
    }

    @Test
    public void isGreen() {
        assertFalse(zero_zero.isGreen());
        assertFalse(one_one.isGreen());
        assertTrue(zero_one.isGreen());
        assertTrue(one_zero.isGreen());
    }

    @Test
    public void staticIsGreen() {
        assertFalse(Coordinates.isGreen(0, 0));
        assertFalse(Coordinates.isGreen(1, 1));
        assertTrue(Coordinates.isGreen(0, 1));
        assertTrue(Coordinates.isGreen(1, 0));
    }

    @Test
    public void testEquals() {
        assertFalse(zero_zero.equals(zero_one));
        zero_one = null;
        assertFalse(zero_zero.equals(zero_one));
        zero_one = new Coordinates(0);
        assertTrue(zero_zero.equals(zero_one));
    }
}