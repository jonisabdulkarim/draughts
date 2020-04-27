package jak.draughts;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jak.draughts.game.DraughtsGame;
import jak.draughts.game.Game;
import jak.draughts.game.gameobjects.Board;
import jak.draughts.game.gameobjects.DraughtBoard;

public class BoardActivity extends AppCompatActivity {

    // Debug variables
    char mode;
    String TAG;

    // Firebase variables
    FirebaseFirestore database;

    // RecycleView variables
    RecyclerView recyclerView;
    //RecyclerView.Adapter adapter;
    MyAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    // Game variables
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        mode = intent.getCharExtra(MainMenuActivity.EXTRA_CHAR, 'X');
        TAG = this.getClass().getName();

        createBoard();
        createView();
        updateView();

        initialiseFirebase(); // TODO: check
    }

    private void initialiseFirebase() {
        database = FirebaseFirestore.getInstance();

        Map<String, String> map = new HashMap<>();
        map.put("id", "0001");
        map.put("name", "tom");
        map.put("MMR", "9001");
        database.collection("test").document().set(map);

        User user = new User();
        user.setName("tom");
        user.setRank(1L);
        DocumentReference ref = database.collection("test").document();
        user.setId(ref.getId());
        ref.set(user);

        // TODO: create test data
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
        adapter = new MyAdapter(getApplicationContext(), game);
        recyclerView.setAdapter(adapter);
    }

    private void updateView() {
        // List<Integer> data = board.convertBoard();
        // adapter.setDataSet(board);
        // TODO
    }

    private void gameLoop() {
        // TODO
    }
}
