package jak.draughts.room;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import jak.draughts.R;
import jak.draughts.Room;

public class RoomActivity extends AppCompatActivity {

    String TAG;
    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        String roomId = intent.getStringExtra("ROOM_ID");
        TAG = this.getClass().getName();

        getRoom(roomId);
    }

    /**
     * Temp method, gets room from db based on id
     *
     * @param id String of room to get
     */
    private void getRoom(final String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("rooms").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    room = doc.toObject(Room.class);
                    readRoom();
                } else {
                    Log.w(TAG, "Failed to find room with id: " + id);
                }
            }
        });
    }

    /**
     * Temp method: logs room stats, called onComplete()
     */
    private void readRoom() {
        Log.d(TAG, "Room id: " + room.getRoomId());
        Log.d(TAG, "User name: " + room.getUserHostId());
        Log.d(TAG, "User host id: " + room.getUserHostId());
        Log.d(TAG, "Game mode: " + room.getGameMode());
    }
}
