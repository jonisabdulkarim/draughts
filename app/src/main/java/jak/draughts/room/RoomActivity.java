package jak.draughts.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import jak.draughts.game.GameActivity;

public class RoomActivity extends AppCompatActivity {

    boolean isCreator;
    boolean isInitialised;

    String TAG;
    Room room;
    User host;
    User join;

    TextView statusTextView;
    TextView hostTextView; // "Host:" textView
    TextView joinTextView; // "Join:" textView
    EditText hostEditText; // host text box
    EditText joinEditText; // join text box

    ChipGroup chipGroup;
    Chip gaypChip;
    Chip moveChip;
    Switch turnSwitch;
    Button startButton;
    Button inviteButton;

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
        initialiseStartButton();

        getRoom(roomId);
    }

    public void startButtonOnClick(View view) {
        if (isCreator) { // START button
            if (room.getStatus() == Room.READY) {
                // TODO: start the game! check details first though
                if (verifyGameReady()) {
                    hostStartGame();
                } else {
                    Log.d(TAG, "Can't start game. One of the details are invalid. Room: " + room);
                }
            } else if (room.getStatus() == Room.FULL) {
                // TODO: popup: "Opponent must be ready"
            } else {
                // TODO: popup: "An opponent must join the room first"
            }
        } else { // READY button
            if (room.getStatus() == Room.READY) { // unready, if already ready
                room.setStatus(Room.FULL);
            } else { // else ready up
                room.setStatus(Room.READY);
            }

            updateServerRoom();
        }
    }

    private void hostStartGame() {
        Log.d(TAG, "Starting game...");

        room.setStatus(Room.PLAYING);
        updateServerRoom(); // update room -> listener detect change -> startGame()
    }

    private void startGame() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("ROOM_ID", room.getRoomId());
        startActivity(intent);
    }


    /**
     * Ensures this room has valid fields before starting game.
     *
     * @return true if all fields are valid, else false
     */
    private boolean verifyGameReady() {
        return isValidString(room.getGameMode())
                && isValidString(room.getUserHostId())
                && isValidString(room.getUserJoinId())
                && isValidString(room.getRoomId())
                && (room.getTurn() == Room.HOST || room.getTurn() == Room.JOIN);
    }

    /**
     * Checks if given string exists and is non-empty.
     *
     * @param string to be checked
     * @return true if not null AND non-empty, else false
     */
    private boolean isValidString(String string) {
        return string != null && string.length() != 0;
    }

    // TODO: long-term goal
    private void initialiseInviteButton() {
        inviteButton = findViewById(R.id.roomButtonInvite);
    }

    /**
     * Initialises 'START' button, and if joining, sets its
     * text to 'READY' instead.
     */
    private void initialiseStartButton() {
        startButton = findViewById(R.id.roomButtonStart);

        if (!isCreator) {
            startButton.setText(R.string.ready);
        }
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
        statusTextView = findViewById(R.id.roomOpponentStatusLabel);
    }

    private void initialiseChipGroup() {
        chipGroup = findViewById(R.id.roomChipGroupGameMode);
        gaypChip = findViewById(R.id.roomChipGameModeGAYP);
        moveChip = findViewById(R.id.roomChipGameMode3MOVE);
        if (isCreator) {
            setChipGroupListener();
        } else {
            gaypChip.setClickable(false);
            moveChip.setClickable(false);
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
                    gaypChip.setChecked(true);
                    room.setGameMode("GAYP");
                } else if (checkedId == gaypChip.getId()) {
                    room.setGameMode("GAYP");
                } else if (checkedId == moveChip.getId()) {
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
     * @param user     which user object is updated
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

    private void logRoomData() {
        Log.d(TAG, "Room id: " + room.getRoomId());
        Log.d(TAG, "Game mode: " + room.getGameMode());
        Log.d(TAG, "Status: " + room.getStatus());
        Log.d(TAG, "Turn: " + room.getTurn());
        Log.d(TAG, "Host id: " + room.getUserHostId());
        Log.d(TAG, "Join id: " + room.getUserJoinId());
        if (host != null)
            Log.d(TAG, "Host name: " + host.getName());
        if (join != null)
            Log.d(TAG, "Join name: " + join.getName());
    }

    /**
     * Called by the getRoom(..) method, outputs log and
     * retrieves all users in this room.
     */
    private void setRoom(Room room) {
        this.room = room;
        getUser('H', room.getUserHostId());

        if (isCreator) {
            room.setGameMode("GAYP"); // default: GAYP mode
            room.setTurn(Room.HOST); // default: host first turn
        } else {
            getUser('J', room.getUserJoinId());
            checkChip();
            checkTurn();
        }

        updateServerRoom(); // update room in database
        updateLocalRoom(); // set up room listener for future changes

        logRoomData();
    }

    /**
     * Checks the chip according to this room's game mode.
     * The join user's listener will use call this method
     * when entering the room and when the host user makes
     * changes online.
     */
    private void checkChip() {
        if (room.getGameMode().equals("GAYP")) {
            gaypChip.setChecked(true);
        } else if (room.getGameMode().equals("3MOVE")) {
            moveChip.setChecked(true);
        } else {
            throw new IllegalStateException();
        }
    }

    private void checkTurn() {
        if (room.getTurn() == Room.HOST) {
            turnSwitch.setChecked(true);
        } else if (room.getTurn() == Room.JOIN) {
            turnSwitch.setChecked(false);
        } else {
            throw new IllegalStateException();
        }
    }

    private void checkStatus() {
        if (room.getStatus() == Room.VACANT) {
            statusTextView.setText("Vacant");
        } else if (room.getStatus() == Room.FULL) {
            statusTextView.setText("Not Ready");
        } else if (room.getStatus() == Room.READY) {
            statusTextView.setText("Ready");
        } else if (room.getStatus() == Room.PLAYING) {
            startGame();
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
     * @param userId   String id of user to retrieve
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
     * @param user     user object that was retrieved by getUser(..)
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
                    checkStatus();

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
