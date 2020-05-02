package jak.draughts.game;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import jak.draughts.R;
import jak.draughts.Room;
import jak.draughts.User;
import jak.draughts.activities.MainMenuActivity;

public class GameActivity extends AppCompatActivity {

    // Debug variables
    char mode;
    String TAG;

    // Firebase variables
    FirebaseFirestore database;

    // RecycleView variables
    RecyclerView recyclerView;
    //RecyclerView.Adapter adapter;
    GameAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    // Game variables
    Game game;
    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        mode = intent.getCharExtra(MainMenuActivity.EXTRA_CHAR, 'X');
        TAG = this.getClass().getName();

        createBoard();
        createView();

        initialiseFirebase(); // TODO: check
        initialiseRoom();
        updateRoom();
    }

    private void initialiseFirebase() {
        database = FirebaseFirestore.getInstance();
    }

    private void initialiseRoom() {
        // TODO: room test data
        User user1 = new User();
        user1.setName("Adam");
        user1.setRank(1L);
        DocumentReference ref = database.collection("users").document();
        user1.setId(ref.getId());
        ref.set(user1);

        User user2 = new User();
        user2.setName("Bob");
        user2.setRank(2L);
        ref = database.collection("users").document();
        user2.setId(ref.getId());
        ref.set(user2);

        room = new Room();
        room.setGameMode("D");
        room.setUserHost(user1);
        room.setUserJoin(user2);
        ref = database.collection("rooms").document();
        room.setRoomId(ref.getId());
        ref.set(room);
    }

    private void createBoard() {
        game = Game.chooseGameMode('D');
    }

    private void createView() {
        // board data in 1d list
        List<Integer> data = game.getDataSet();

        // attach view to activity
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // create and attach grid layout manager to view
        layoutManager = new GridLayoutManager(getApplicationContext(), 8);
        recyclerView.setLayoutManager(layoutManager);

        // specify adapter
        adapter = new GameAdapter(getApplicationContext(), game);
        recyclerView.setAdapter(adapter);
    }

    private void updateRoom() {
        room.setDataSet(game.getDataSet());
        database.collection("rooms").document(room.getRoomId()).set(room);
    }

    private void gameLoop() {
        // TODO
    }
}
