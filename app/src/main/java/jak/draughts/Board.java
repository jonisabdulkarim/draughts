package jak.draughts;

public class Board {

    private static final int BOARD_ROWS = 8;
    private static final int BOARD_COLUMNS = 8;

    private Tile[][] board;

    Board() {
        board = new Tile[BOARD_ROWS][BOARD_COLUMNS];
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLUMNS; j++) {
                board[i][j] = new Tile();
            }
        }
    }

    Board(char boardType) {
        board = new Tile[BOARD_ROWS][BOARD_COLUMNS];
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLUMNS; j++) {
                board[i][j] = new Tile();
            }
        }
    }

    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }
}
