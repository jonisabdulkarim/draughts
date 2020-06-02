package jak.draughts.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import jak.draughts.R;
import jak.draughts.Room;
import jak.draughts.User;

public class RoomActivity extends AppCompatActivity {

    boolean isCreator;
    boolean isInitialised;

    String TAG;
    Room room;
    User host;
    User join;

    TextView hostTextView; // "Host:" textView
    TextView joinTextView; // "Join:" textView
    EditText hostEditText; // host text box
    EditText joinEditText; // join text box
    TextView opponentStatus;

    ChipGroup chipGroup;
    Chip chipGAYP;
    Chip chip3MOVE;
    Switch turnSwitch;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        TAG = this.getClass().getName();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String roomId = intent.getStringExtra("ROOM_ID");
        isCreator = intent.getBooleanExtra("isCreator", true);
        isInitialised = false; // for editText, prevent re-initialising editText

        initialiseTextViews();
        initialiseChipGroup();
        initialiseTurnSwitch();

        getRoom(roomId);
    }

    private void initialiseTurnSwitch() {
        turnSwitch = findViewById(R.id.roomSwitchTurn);
        if (isCreator) {
            setUpTurnSwitchListener();
        } else {
            turnSwitch.setClickable(false);
        }
    }

    private void setUpTurnSwitchListener() {
        turnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    room.setTurn(0);
                } else {
                    room.setTurn(1);
                }
                updateServerRoom();
            }
        });
    }

    private void initialiseTextViews() {
        hostTextView = findViewById(R.id.roomHostTextView);
        joinTextView = findViewById(R.id.roomJoinTextView);
        hostEditText = findViewById(R.id.roomHostEditText);
        joinEditText = findViewById(R.id.roomJoinEditText);
    }

    private void initialiseChipGroup() {
        chipGroup = findViewById(R.id.roomChipGroupGameMode);
        chipGAYP = findViewById(R.id.roomChipGameModeGAYP);
        chip3MOVE = findViewById(R.id.roomChipGameMode3MOVE);
        if (isCreator) {
            setChipGroupListener();
        } else {
            chipGAYP.setClickable(false);
            chip3MOVE.setClickable(false);
        }
    }

    /**
     * Sets checkedChangeListener for the room's chip group.
     * It will default to "GAYP" when deselecting chips.
     * Otherwise, it will update the room's game mode to the
     * newly selected chip.
     */
    private void setChipGroupListener() {
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (checkedId == -1) { // deselect -> revert to default
                    chipGAYP.setChecked(true);
                    room.setGameMode("GAYP");
                } else if (checkedId == chipGAYP.getId()) {
                    room.setGameMode("GAYP");
                } else if (checkedId == chip3MOVE.getId()) {
                    room.setGameMode("3MOVE");
                } else {
                    throw new IllegalStateException();
                }
                updateServerRoom();
            }
        });
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
        isInitialised = true;
    }

    /**
     * Sets up the this editText to listen for username changes.
     * It will call updateServerUser(User) to update the user
     * profile on the database.
     *
     * @param editText listener will be attached to
     * @param user which user object is updated
     * @see RoomActivity#updateServerUser(User)
     */
    private void setUpEditTextListener(EditText editText, final User user) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;

                if (i == EditorInfo.IME_ACTION_DONE) {
                    user.setName(textView.getText().toString());
                    handled = true;
                    updateServerUser(user);
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

        if (isCreator) {
            room.setGameMode("GAYP"); // default: GAYP mode
            room.setTurn(0); // default: host first turn
        } else {
            getUser('J', room.getUserJoinId());
            checkChip();
            checkTurn();
        }

        updateServerRoom(); // update room in database
        updateLocalRoom(); // set up room listener for future changes
    }

    /**
     * Checks the chip according to this room's game mode.
     * The join user's listener will use call this method
     * when entering the room and when the host user makes
     * changes online.
     */
    private void checkChip() {
        if (room.getGameMode().equals("GAYP")) {
            chipGAYP.setChecked(true);
        } else if (room.getGameMode().equals("3MOVE")) {
            chip3MOVE.setChecked(true);
        } else {
            throw new IllegalStateException();
        }
    }

    private void checkTurn() {
        final int HOST = 0;
        final int JOIN = 1;
        if (room.getTurn() == HOST) {
            turnSwitch.setChecked(true);
        } else if (room.getTurn() == JOIN) {
            turnSwitch.setChecked(false);
        } else {
            throw new IllegalStateException();
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

        updateLocalUser(userType, user); // set up real-time listener on this user

        // initialise editText when: create room -> host is ready, join room -> both users ready
        if (host != null && (join != null || isCreator) && !isInitialised) {
            initialiseEditTextListener();
        }
    }

    /**
     * Updates this user's fields in the database.
     *
     * @param user which will be updated
     */
    public void updateServerUser(final User user) {
        db.collection("users").document(user.getId()).set(user);
    }

    public void updateLocalUser(final char userType, final User user) {
        final DocumentReference docRef = db.collection("users")
                .document(user.getId());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed for updateLocalUser()", e);
                } else if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.d(TAG, "Current data: " + documentSnapshot.getData());
                    User tempUser = documentSnapshot.toObject(User.class);
                    assert tempUser != null;
                    user.setName(tempUser.getName());
                    updateEditText(userType);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void updateEditText(char userType) {
        if (userType == 'H') {
            hostEditText.setText(host.getName());
        } else if (userType == 'J') {
            joinEditText.setText(join.getName());
        } else {
            throw new IllegalStateException();
        }
    }

    public void updateServerRoom() {
        db.collection("rooms").document(room.getRoomId()).set(room);
    }

    /**
     * Sets up a real-time listener to this room in the database.
     * If changes are found, the new room replaces the old one.
     * It will always call checkChip() and checkTurn() to update
     * room/game information.
     * <p>
     * It will also call the joinUser() if the joinUser object is
     * not already initialised. This happens on the host's side,
     * which always creates a room with a null joinUser object.
     */
    public void updateLocalRoom() {
        final DocumentReference docRef = db.collection("rooms")
                .document(room.getRoomId());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed for updateLocalRoom()", e);
                } else if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.d(TAG, "Current data: " + documentSnapshot.getData());

                    room = documentSnapshot.toObject(Room.class);
                    checkChip();
                    checkTurn();

                    if (join == null && room.getUserJoinId() != null) {
                        getUser('J', room.getUserJoinId());
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
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
