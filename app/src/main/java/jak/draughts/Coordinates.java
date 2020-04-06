package jak.draughts;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int position) {
        setX(position / 8);
        setY(position % 8);
    }

    public Coordinates(int x, int y) {
        setX(x);
        setY(y);
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

    public boolean isGreen() {
        return getX() % 2 == 0 ^ getY() % 2 == 0;
    }

    public static boolean isGreen(int row, int col) {
        return row % 2 == 0 ^ col % 2 == 0;
    }
}
