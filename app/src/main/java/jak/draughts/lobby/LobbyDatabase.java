package jak.draughts.lobby;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import jak.draughts.Room;
import jak.draughts.User;

public class LobbyDatabase  {

    private FirebaseFirestore database;
    private LobbyActivity lobbyActivity;

    public LobbyDatabase(LobbyActivity lobbyActivity) {
        this.lobbyActivity = lobbyActivity;
        database = FirebaseFirestore.getInstance();
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

        long rank = 1L;
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
            room.setUserHost(user1);
            room.setUserJoin(user2);
            ref = database.collection("rooms").document();
            room.setRoomId(ref.getId());
            ref.set(room);
        }

        for (int i = 11; i < 12; i++) {
            // TODO: room test data
            User user1 = new User();
            user1.setName(arr[i]);
            DocumentReference ref = database.collection("users").document();
            user1.setId(ref.getId());
            ref.set(user1);

            Room room = new Room();
            room.setGameMode("D");
            room.setUserHost(user1);
            ref = database.collection("rooms").document();
            room.setRoomId(ref.getId());
            ref.set(room);
        }
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
        room.setUserHost(createUser());

        ref.set(room);
        return room.getRoomId();
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
