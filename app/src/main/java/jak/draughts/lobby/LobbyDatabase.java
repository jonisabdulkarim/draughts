package jak.draughts.lobby;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jak.draughts.Room;
import jak.draughts.User;

public class LobbyDatabase  {

    private FirebaseFirestore database;
    private LobbyActivity lobbyActivity;

    private static final int ROOM_VACANT = 0;
    private static final int ROOM_FULL = 1;
    private static final int ROOM_PLAYING = 2;

    public LobbyDatabase(LobbyActivity lobbyActivity) {
        this.lobbyActivity = lobbyActivity;
        this.database = FirebaseFirestore.getInstance();
        // initialiseRooms(); // TODO: testing only
    }

    /**
     * Sends a query to the database to retrieve all rooms. Called by
     * the initialiseDatabase() and refresh() method in LobbyActivity.
     * Once the query is complete, it will call its update() method.
     *
     * @param rooms
     */
    public void fetchUpdates(final List<Room> rooms) {
        database.collection("rooms")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            rooms.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Room room = document.toObject(Room.class);
                                rooms.add(room);
                            }
                        } else {
                            Log.w("FAIL", "Error getting documents.", task.getException());
                        }

                        lobbyActivity.update();
                    }
                });
    }

    public void initialiseRooms() {
        String[] arr = {
                "Adam", "Bob", "Charlie", "Darlene", "Emma", "Felix",
                "Guy", "Hector", "India", "Julia", "Kevin", "Liam"
        };

        for (int i = 0; i < 10; i+=2) {
            // TODO: room test data
            User user1 = new User();
            user1.setName(arr[i]);
            DocumentReference ref = database.collection("users").document();
            user1.setId(ref.getId());
            ref.set(user1);

            User user2 = new User();
            user2.setName(arr[i+1]);
            ref = database.collection("users").document();
            user2.setId(ref.getId());
            ref.set(user2);

            Room room = new Room();
            room.setGameMode("D");
            room.setUserHostId(user1.getId());
            room.setUserJoinId(user2.getId());
            room.setStatus(ROOM_FULL);
            ref = database.collection("rooms").document();
            room.setRoomId(ref.getId());
            ref.set(room);
        }

        int i = 11;
        // TODO: room test data
        User user1 = new User();
        user1.setName(arr[i]);
        DocumentReference ref = database.collection("users").document();
        user1.setId(ref.getId());
        ref.set(user1);

        Room room = new Room();
        room.setGameMode("D");
        room.setUserHostId(user1.getId());
        room.setStatus(ROOM_VACANT);
        ref = database.collection("rooms").document();
        room.setRoomId(ref.getId());
        ref.set(room);
    }

    /**
     * Creates a new room which is sent to the database.
     * The room id is returned once the request is sent.
     * The created room will contain a Guest user with
     * a unique id.
     *
     * @return String of created room id
     */
    public String createRoom() {
        DocumentReference ref = database.collection("rooms").document();

        Room room = new Room();
        room.setRoomId(ref.getId());
        room.setUserHostId(createUser().getId());
        room.setStatus(ROOM_VACANT);

        ref.set(room);
        return room.getRoomId();
    }

    public void joinRoom(String roomId) {
        Map<String, Object> roomFields = new HashMap<>();
        roomFields.put("userJoinId", createUser().getId());
        roomFields.put("status", ROOM_FULL);

        database.collection("rooms").document(roomId).update(roomFields);
    }

    /**
     * Helper method which creates a "guest" user and
     * uploads it to the database before returning the
     * created user object.
     *
     * @return the created user object
     */
    private User createUser() {
        DocumentReference ref = database.collection("users").document();

        User user = new User();
        user.setId(ref.getId());
        user.setName("Guest");

        ref.set(user);
        return user;
    }

    /**
     * Sets up a listener to automatically fetch updates when
     * changes have been made in the database.
     *
     * @return TODO
     */
    public boolean listenForChanges() {
        // TODO
        return false;
    }
}
