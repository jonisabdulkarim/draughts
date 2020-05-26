package jak.draughts.room;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import jak.draughts.R;
import jak.draughts.Room;
import jak.draughts.User;

public class RoomActivity extends AppCompatActivity {

    String TAG;
    Room room;
    User host;
    User join;

    TextView hostTextView;
    TextView joinTextView;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        TAG = this.getClass().getName();
        hostTextView = findViewById(R.id.roomHostTextView);
        joinTextView = findViewById(R.id.roomJoinTextView);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String roomId = intent.getStringExtra("ROOM_ID");
        getRoom(roomId);
    }

    /**
     * Gets the room associated with the given id. On
     * success, sets the room variable and retrieves
     * the user(s).
     *
     * @param id String of room to get
     */
    private void getRoom(final String id) {
        db.collection("rooms").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    room = doc.toObject(Room.class);
                    writeRoom();
                } else {
                    Log.w(TAG, "Failed to find room with id: " + id);
                }
            }
        });
    }

    /**
     * Called by the getRoom(..) method, outputs log and
     * retrieves all users in this room.
     */
    private void writeRoom() {
        Log.d(TAG, "Room id: " + room.getRoomId());
        Log.d(TAG, "User name: " + room.getUserHostId());
        Log.d(TAG, "User host id: " + room.getUserHostId());
        Log.d(TAG, "Game mode: " + room.getGameMode());

        getUser('H', room.getUserHostId());

        if (room.getUserJoinId() != null) {
            getUser('J', room.getUserJoinId());
        }
    }

    /**
     * Gets the specified user according to its id in the
     * room object. On complete, assigns it to the relevant
     * variable given the <tt>userType</tt>.
     *
     * @param userType 'H' for host user, 'J' for join
     * @param userId String id of user to retrieve
     */
    private void getUser(final char userType, String userId) {
        db.collection("users").document(userId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    User user = doc.toObject(User.class);
                    getUserComplete(userType, user);
                }
            }
        });
    }


    /**
     * Called by the getUser(..) method to set the relevant
     * variables and call the relevant textView writing methods.
     *
     * @param userType 'H' for host, 'J' for join
     * @param user user object that was retrieved by getUser(..)
     */
    private void getUserComplete(final char userType, User user) {
        if (userType == 'H') {
            setHost(user);
            writeHost();
        } else if (userType == 'J') {
            setJoin(user);
            writeJoin();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setHost(User host) {
        this.host = host;
    }

    public void setJoin(User join) {
        this.join = join;
    }

    private void writeHost() {
        StringBuilder sb = new StringBuilder();
        sb.append("Host:").append(host.getName()).append("\n");
        sb.append("Id: ").append(host.getId());

        // hostTextView.setText(sb.toString());
    }

    private void writeJoin() {
        StringBuilder sb = new StringBuilder();
        sb.append("Join:").append(join.getName()).append("\n");
        sb.append("Id: ").append(join.getId());

        // joinTextView.setText(sb.toString());
    }
}
