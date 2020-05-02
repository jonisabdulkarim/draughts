package jak.draughts;

public enum TileColor {
    GREEN(0),
    WHITE(1),
    SELECTED(2),
    CAPTURE_SELECT(3);

    private final int value; // TODO: getter?

    TileColor(int value) {
        this.value = value;
    }
}
