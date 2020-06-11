package jak.draughts.game;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import jak.draughts.Room;

public class GameDatabase {

    private Game game;
    private String TAG;
    private FirebaseFirestore database;
    private ListenerRegistration roomListener;

    GameDatabase(Game game) {
        TAG = GameDatabase.class.getName();
        this.game = game;
        database = FirebaseFirestore.getInstance();
    }

    /**
     * Gets the room with the given roomId from the database.
     *
     * @param roomId document id of room object
     */
    public void getRoom(String roomId) {
        database.collection("rooms").document(roomId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    assert doc != null && doc.exists();
                    Room room = doc.toObject(Room.class);

                    game.setRoom(room);
                    game.setFirstTurn();
                }
            }
        });
    }

    /**
     * Sets up a listener with the given roomId to return
     * a new room anytime changes are detected in the database.
     *
     * @param roomId document id of room object
     */
    public void listenForChanges(String roomId) {
        final DocumentReference docRef = database.collection("rooms")
                .document(roomId);
        roomListener = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed for listenForChanges()", e);
                    return;
                }
                String source = documentSnapshot != null
                        && documentSnapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.d(TAG, "Source: " + source + ", Current data: " + documentSnapshot.getData());

                    Room room = documentSnapshot.toObject(Room.class);

                    game.setRoom(room);
                    game.updateBoard();
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    /**
     * Updates the given room.
     *
     * @param room the room object to update
     */
    public void setRoom(Room room) {
        database.collection("rooms").document(room.getRoomId()).set(room);
    }

    /**
     * Detaches the room listeners.
     */
    public void detachListeners() {
        roomListener.remove();
    }
}
