package jak.draughts.room;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import jak.draughts.R;
import jak.draughts.Room;
import jak.draughts.User;

public class RoomActivity extends AppCompatActivity {

    boolean isCreator;

    String TAG;
    Room room;
    User host;
    User join;

    TextView hostTextView; // "Host:" textView
    TextView joinTextView; // "Join:" textView
    EditText hostEditText; // host text box
    EditText joinEditText; // join text box

    ChipGroup chipGroup;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        TAG = this.getClass().getName();

        hostTextView = findViewById(R.id.roomHostTextView);
        joinTextView = findViewById(R.id.roomJoinTextView);
        hostEditText = findViewById(R.id.roomHostEditText);
        joinEditText = findViewById(R.id.roomJoinEditText);
        // chipGroup = findViewById(R.id.chipGroup);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String roomId = intent.getStringExtra("ROOM_ID");
        isCreator = intent.getBooleanExtra("isCreator", true);
        getRoom(roomId);

    }

    /**
     * Short-hand method that will call <tt>setUpEditTextListener(...)</tt>
     * for the relevant user type i.e. host XOR join user.
     * <p>
     * The other user's data is collected using a listener on the database.
     *
     * @see RoomActivity#setUpEditTextListener(EditText, User)
     */
    private void initialiseEditTextListener() {
        if (isCreator) {
            setUpEditTextListener(hostEditText, host);
            joinEditText.setFocusable(false);
        } else {
            setUpEditTextListener(joinEditText, join);
            hostEditText.setFocusable(false);
        }
    }

    /**
     * Sets up the this editText to listen for username changes.
     * It will inform the database when the change is confirmed.
     *
     * @param editText listener will be attached to
     * @param user which user object is updated
     */
    private void setUpEditTextListener(EditText editText, final User user) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;

                if (i == EditorInfo.IME_ACTION_DONE) {
                    user.setName(textView.getText().toString());
                    handled = true;
                    updateUserName(user);
                    Log.d(TAG, "Name changed to: " + user.getName());
                }

                return handled;
            }
        });
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
                    DocumentSnapshot document = task.getResult();
                    assert document != null;

                    Room room = document.toObject(Room.class);
                    assert room != null;

                    setRoom(room);
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
    private void setRoom(Room room) {
        Log.d(TAG, "Room id: " + room.getRoomId());
        Log.d(TAG, "User name: " + room.getUserHostId());
        Log.d(TAG, "User host id: " + room.getUserHostId());
        Log.d(TAG, "Game mode: " + room.getGameMode());

        this.room = room;

        getUser('H', room.getUserHostId());

        if (!isCreator) {
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
                    assert doc != null;
                    User user = doc.toObject(User.class);
                    setUser(userType, user);
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
    private void setUser(final char userType, User user) {
        if (userType == 'H') {
            setHost(user);
        } else if (userType == 'J') {
            setJoin(user);
        } else {
            throw new IllegalArgumentException();
        }

        // initialise editText when: create room -> host is ready, join room -> both users ready
        if (host != null && (join != null || isCreator)) {
            initialiseEditTextListener();
        }
    }

    /**
     * Updates this user's fields in the database.
     *
     * @param user which will be updated
     */
    public void updateUserName(final User user) {
        db.collection("users").document(user.getId()).set(user);
    }

    public void setHost(User host) {
        this.host = host;
    }

    public void setJoin(User join) {
        this.join = join;
    }

    @Deprecated
    private void writeHost() {
        StringBuilder sb = new StringBuilder();
        sb.append("Host:").append(host.getName()).append("\n");
        sb.append("Id: ").append(host.getId());

        hostTextView.setText(sb.toString());
    }

    @Deprecated
    private void writeJoin() {
        StringBuilder sb = new StringBuilder();
        sb.append("Join:").append(join.getName()).append("\n");
        sb.append("Id: ").append(join.getId());

        joinTextView.setText(sb.toString());
    }

}
