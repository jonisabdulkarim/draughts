package jak.draughts;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import jak.draughts.gameobjects.Board;

public class BoardActivity extends AppCompatActivity {

    // Debug variables
    char mode;
    String TAG;

    // Firebase variables
    FirebaseDatabase database;

    // RecycleView variables
    RecyclerView recyclerView;
    //RecyclerView.Adapter adapter;
    MyAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    // Game variables
    Board board;

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
    }

    private void initialiseFirebase() {
        database = FirebaseDatabase.getInstance();

        // TODO: remove test data
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue(board);
    }

    private void createBoard() {
        board = new Board();
    }

    private void createView() {
        // board data in 1d list
        List<Integer> data = board.convertBoard();

        // attach view to activity
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // create and attach grid layout manager to view
        layoutManager = new GridLayoutManager(getApplicationContext(), 8);
        recyclerView.setLayoutManager(layoutManager);

        // specify adapter
        adapter = new MyAdapter(getApplicationContext(), board);
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
