package jak.draughts.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

import jak.draughts.LobbyAdapter;
import jak.draughts.LobbyDatabase;
import jak.draughts.R;
import jak.draughts.Room;
import jak.draughts.User;

public class LobbyActivity extends AppCompatActivity {

    LobbyDatabase database;
    List<Room> rooms;

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
        database.initialiseRooms();
        database.fetchUpdates(rooms);
    }

    private void createView() {
        recyclerView = findViewById(R.id.recyclerViewLobby);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

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
        for (Room room : rooms) {
            Log.d("LOBBY", room.getUserHost().getName());
        }

        lobbyAdapter.update();
        Log.d("LOBBY", "END");
    }


}
