package jak.draughts;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import jak.draughts.activities.LobbyActivity;

public class LobbyDatabase  {

    FirebaseFirestore database;
    LobbyActivity lobbyActivity;

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

    /**
     * Updates the database by adding the newly created room.
     *
     * @param room the created room
     */
    public void create(Room room) {
        // TODO
    }

    /**
     * Sets up a listener to automatically fetch updates when
     * changes have been made in the database.
     *
     * @return
     */
    public boolean listenForChanges() {
        // TODO
        return false;
    }
}
