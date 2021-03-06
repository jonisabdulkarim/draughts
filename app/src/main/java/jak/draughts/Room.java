package jak.draughts;

import java.util.List;

public class Room {

    // status: int
    public static final int VACANT = 0;
    public static final int FULL = 1;
    public static final int READY = 2;
    public static final int PLAYING = 3;
    public static final int RESULT = 4;

    // turn: int
    public static final int STOP = -1; // i.e. game over in draughts
    public static final int HOST = 0;
    public static final int JOIN = 1;

    String roomId;
    String userHostId;
    String userJoinId;
    int status; // {0: vacant, 1: full, 2: ready, 3: playing, 4: result}
    String gameMode;
    int turn; // {0: host, 1: join}
    List<Integer> dataSet;
    Coordinates movedPiece;

    public Room() {
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserHostId() {
        return userHostId;
    }

    public void setUserHostId(String userHostId) {
        this.userHostId = userHostId;
    }

    public String getUserJoinId() {
        return userJoinId;
    }

    public void setUserJoinId(String userJoinId) {
        this.userJoinId = userJoinId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public List<Integer> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<Integer> dataSet) {
        this.dataSet = dataSet;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Coordinates getMovedPiece() {
        return movedPiece;
    }

    public void setMovedPiece(Coordinates movedPiece) {
        this.movedPiece = movedPiece;
    }
}
