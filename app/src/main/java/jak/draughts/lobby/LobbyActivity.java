package jak.draughts.lobby;

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
import jak.draughts.User;

public class LobbyActivity extends AppCompatActivity {

    LobbyDatabase database;
    List<Room> rooms;
    Room selectedRoom;

    LobbyAdapter lobbyAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

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
        // TODO: remove test data
        for (Room room : rooms) {
            Log.d("LOBBY", room.getUserHost().getName());
        }

        lobbyAdapter.update();
        Log.d("LOBBY", "END");
    }

    /**
     * Selects a room by highlighting it or, if already selected,
     * deselects the room.
     */
    public void selectRoom() {

    }

    /**
     * Starts the RoomActivity with the Intent to join
     * the selected room. Called by the "Join" button in
     * the Lobby screen. The user will be temporarily named
     * "Guest" with the option of changing it.
     */
    public void joinRoom(View view) {
        if (selectedRoom == null) return;

        // TODO: create lobby method
    }

    /**
     * Starts the RoomActivity with the Intent to create
     * a new room. Called by the "Create" button in
     * the Lobby screen. The user will be temporarily named
     * "Guest" with the option of changing it.
     */
    public void createRoom(View view) {
        // TODO: create lobby method
    }
}
