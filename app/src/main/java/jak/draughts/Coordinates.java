package jak.draughts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * The primary purpose of this class is to store the coordinates of a tile that may have been
 * clicked or to quickly determine the location of a team's pieces. For this purpose, the class acts
 * as a data structure holding the two indices of a 2D Tile array. The secondary purpose is to
 * quickly convert between the coordinates and the relevant index in a 1D array.
 * <p>
 * Coordinates are always stored in Piece objects for quick access to all of
 * a team's pieces on a board. This class assumes there are 8 columns, 8 rows and 64 tiles
 * in a board.
 */
public class Coordinates {
    private int x;
    private int y;

    public Coordinates() {} // used in db

    /**
     * Creates a Coordinates object according to the index of a tile in a 1D array.
     *
     * @param position the index from a 1D array
     */
    public Coordinates(int position) {
        setPosition(position);
    }

    /**
     * Creates a Coordinates object according to the row and column of a tile
     *
     * @param x the row of a tile
     * @param y the column of a tile
     */
    public Coordinates(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Calculates the index of a 1D array according to this object's x and y values
     *
     * @return the index in a 1D array
     */
    public int getPosition() {
        return getX() * 8 + getY();
    }

    /**
     * Sets the coordinates of this object according to the index of a tile in a 1D array.
     *
     * @param position the index from a 1D array
     */
    public void setPosition(int position) {
        setX(position / 8);
        setY(position % 8);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Used to determine whether a tile in this position should be painted green,
     * and thus whether it is a playable tile i.e in Draughts.
     *
     * @return true if tile is green, false otherwise
     */
    public boolean isGreen() {
        return getX() % 2 == 0 ^ getY() % 2 == 0;
    }

    /**
     * Used to quickly determine whether a tile in this position should be painted green,
     * without the need to create a Coordinates object.
     *
     * @param row the row containing this tile
     * @param col the column containing this tile
     * @return true if tile is green, false otherwise
     */
    public static boolean isGreen(int row, int col) {
        return row % 2 == 0 ^ col % 2 == 0;
    }

    /**
     * Determines whether two objects are both Coordinates with the same x and y values.
     *
     * @param obj another object to compare this coordinate to
     * @return true if the other object is also an instance of a Coordinates class
     * AND the x and y values of both objects are equal, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Coordinates) {
            Coordinates other = (Coordinates) obj;
            return getX() == other.getX() && getY() == other.getY();
        } else {
            return false;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    /**
     * Creates a new Coordinate object based on the number of spaces
     * from this object's coordinates in the up-left diagonal direction.
     *
     * @param spaces number of tiles to destination in given direction
     * @return coordinate at that destination
     */
    public Coordinates moveUpLeft(int spaces) {
        return new Coordinates(getX() - spaces, getY() - spaces);
    }

    /**
     * Creates a new Coordinate object based on the number of spaces
     * from this object's coordinates in the up-right diagonal direction.
     *
     * @param spaces number of tiles to destination in given direction
     * @return coordinate at that destination
     */
    public Coordinates moveUpRight(int spaces) {
        return new Coordinates(getX() - spaces, getY() + spaces);
    }

    /**
     * Creates a new Coordinate object based on the number of spaces
     * from this object's coordinates in the down-left diagonal direction.
     *
     * @param spaces number of tiles to destination in given direction
     * @return coordinate at that destination
     */
    public Coordinates moveDownLeft(int spaces) {
        return new Coordinates(getX() + spaces, getY() - spaces);
    }

    /**
     * Creates a new Coordinate object based on the number of spaces
     * from this object's coordinates in the down-right diagonal direction.
     *
     * @param spaces number of tiles to destination in given direction
     * @return coordinate at that destination
     */
    public Coordinates moveDownRight(int spaces) {
        return new Coordinates(getX() + spaces, getY() + spaces);
    }

    /**
     * Returns the coordinate at the mid-point of this and the
     * given coordinate.
     *
     * @param other the other coordinate
     * @return middle of two coordinates
     */
    public Coordinates middle(Coordinates other) {
        int x = getX() + other.getX();
        int y = getY() + other.getY();
        return new Coordinates(x/2, y/2);
    }
}
