package jak.draughts;

import java.util.*;

public class Board {

    private static final int BOARD_ROWS = 8;

    private List<Row> board;

    Board() {
        board = new ArrayList<>();
        for (int i = 0; i < BOARD_ROWS; i++) {
            board.add(new Row());
        }
    }

    public List<Row> getBoard() {
        return board;
    }

    public void setBoard(List<Row> board) {
        this.board = board;
    }
}
