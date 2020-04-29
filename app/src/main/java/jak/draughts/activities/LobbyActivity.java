package jak.draughts.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import jak.draughts.LobbyDatabase;
import jak.draughts.R;
import jak.draughts.Room;

public class LobbyActivity extends AppCompatActivity {

    LobbyDatabase database;
    List<Room> rooms;

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
    }

    /**
     * Initialises the LobbyDatabase, which contains the relevant queries.
     * Once initialised, it will call fetchUpdates(...) automatically.
     */
    private void initialiseDatabase() {
        rooms = new ArrayList<>();
        database = new LobbyDatabase(this);
        database.fetchUpdates(rooms);
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

        Log.d("LOBBY", "END");
    }
}
