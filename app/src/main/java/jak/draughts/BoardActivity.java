package jak.draughts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BoardActivity extends AppCompatActivity {

    // Debug variables
    char mode;
    String TAG;

    // Firebase variables
    FirebaseDatabase database;

    // RecycleView variables
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
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
        initialiseFirestore();
    }

    private void initialiseFirestore() {
        database = FirebaseDatabase.getInstance();

        /*DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");*/
    }

    private void createBoard() {
        board = new Board();
    }

    private void createView() {
        // temp data, to be switched to new
        String[] data = new String[64];

        for (int i = 0; i < data.length; i++) {
            data[i] = "" + i;
        }

        // attach view to activity
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // create and attach grid layout manager to view
        layoutManager = new GridLayoutManager(getApplicationContext(), 8);
        recyclerView.setLayoutManager(layoutManager);

        // specify adapter
        adapter = new MyAdapter(getApplicationContext(), data);
        recyclerView.setAdapter(adapter);
    }
}
