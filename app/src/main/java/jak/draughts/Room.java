package jak.draughts;

import java.util.List;

public class Room {

    String roomId;
    User userHost;
    User userJoin;
    String gameMode;
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

    public User getUserHost() {
        return userHost;
    }

    public void setUserHost(User userHost) {
        this.userHost = userHost;
    }

    public User getUserJoin() {
        return userJoin;
    }

    public void setUserJoin(User userJoin) {
        this.userJoin = userJoin;
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
}
