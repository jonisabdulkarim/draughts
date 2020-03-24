package jak.draughts;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int position) {
        this.x = position / 8;
        this.y = position % 8;
    }

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
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
}
