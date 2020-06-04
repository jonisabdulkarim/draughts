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
import jak.draughts.main.MainMenuActivity;

public class GameActivity extends AppCompatActivity {

    // Debug variables
    String TAG;

    // Firebase variables
    FirebaseFirestore database;

    // RecycleView variables
    RecyclerView recyclerView;
    GameAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    // Game variables
    Game game;
    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        TAG = this.getClass().getName();

        Intent intent = getIntent();
        String roomId = intent.getStringExtra("ROOM_ID");
        String mode = intent.getStringExtra("GAME_MODE");
        int turn = intent.getIntExtra("TURN", 0);

        createBoard(mode, roomId, turn);
        createView();

        initialiseFirebase(); // TODO: check
        // updateRoom();
    }

    private void initialiseFirebase() {
        database = FirebaseFirestore.getInstance();
    }

    private void createBoard(String mode, String roomId, int turn) {
        game = Game.chooseGameMode(mode, roomId, turn);
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
