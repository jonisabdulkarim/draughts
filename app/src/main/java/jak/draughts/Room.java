package jak.draughts;

import com.google.firebase.firestore.FirebaseFirestore;

import jak.draughts.game.Game;

public class Room {

    FirebaseFirestore db;
    User hostingUser;
    User joiningUser;
    Game game;
    String gameMode;
    String roomId;

    public Room() {}

    public FirebaseFirestore getDb() {
        return db;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    public User getHostingUser() {
        return hostingUser;
    }

    public void setHostingUser(User hostingUser) {
        this.hostingUser = hostingUser;
    }

    public User getJoiningUser() {
        return joiningUser;
    }

    public void setJoiningUser(User joiningUser) {
        this.joiningUser = joiningUser;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
