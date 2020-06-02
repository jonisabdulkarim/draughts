package jak.draughts.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import jak.draughts.R;
import jak.draughts.Room;
import jak.draughts.room.RoomActivity;

public class LobbyActivity extends AppCompatActivity {

    LobbyDatabase database;
    List<Room> rooms;
    Room selectedRoom;

    LobbyAdapter lobbyAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private static final int ROOM_VACANT = 0;
    private static final int ROOM_FULL = 1;
    private static final int ROOM_READY = 2;
    private static final int ROOM_PLAYING = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        initialiseDatabase();
        createView();
    }

    /**
     * Initialises the LobbyDatabase, which contains the relevant queries.
     * Once initialised, it will call fetchUpdates(...) automatically.
     */
    private void initialiseDatabase() {
        rooms = new ArrayList<>();
        database = new LobbyDatabase(this);
        // database.initialiseRooms(); todo: remove test data when done
        database.fetchUpdates(rooms);
    }

    /**
     * Links recyclerView object to XML file, sets layout manager and
     * vertical dividers, then links the recyclerview to its adapter.
     */
    private void createView() {
        recyclerView = findViewById(R.id.recyclerViewLobby);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(
                recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        lobbyAdapter = new LobbyAdapter(this, rooms);
        recyclerView.setAdapter(lobbyAdapter);
    }

    /**
     * Retrieves the latest data from the database. The view
     * will update once the query is complete. Called by the
     * "refresh" button
     */
    public void refresh() {
        database.fetchUpdates(rooms);
    }

    /**
     * Updates the lobby view. Called by the database when fetching
     * new data.
     */
    public void update() {
        lobbyAdapter.update();
    }

    /**
     * Sets the given room as the currently selected room.
     *
     * @param room to select, or null to deselect
     */
    public void setSelectedRoom(Room room) {
        this.selectedRoom = room;
    }

    public Room getSelectedRoom() {
        return selectedRoom;
    }

    /**
     * Starts the RoomActivity with the Intent to join
     * the selected room. Called by the "Join" button in
     * the Lobby screen.
     * <p>
     * The user will be temporarily named
     * "Guest" with the option of changing it in Room.
     * <p>
     * Joining while not selecting a room, or attempting
     * to join a room, will cause a pop-up to appear to
     * inform the user of the error.
     * <p>
     * The intent will contain both the String roomId and
     * the boolean isCreator, the latter set to false.
     */
    public void joinRoom(View view) {
        if (selectedRoom == null) {
            // TODO: popup: "must select room before joining"

        } else if (selectedRoom.getStatus() == ROOM_VACANT) {
            String roomId = selectedRoom.getRoomId();
            database.joinRoom(roomId);

            Intent intent = new Intent(this, RoomActivity.class);
            intent.putExtra("ROOM_ID", roomId);
            intent.putExtra("isCreator", false);

            startActivity(intent);
        } else {
            // TODO: popup: "cannot join non-vacant room"

        }
    }

    /**
     * Starts the RoomActivity with the Intent to create
     * a new room. Called by the "Create" button in
     * the Lobby screen.
     * <p>
     * The user will be temporarily named
     * "Guest" with the option of changing it.
     * <p>
     * The intent will contain both a String roomId and
     * a boolean isCreator, the latter set to true.
     */
    public void createRoom(View view) {
        String roomId = database.createRoom();

        Intent intent = new Intent(this, RoomActivity.class);
        intent.putExtra("ROOM_ID", roomId);
        intent.putExtra("isCreator", true);

        startActivity(intent);
    }
}
