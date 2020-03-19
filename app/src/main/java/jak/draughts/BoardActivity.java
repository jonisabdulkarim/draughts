package jak.draughts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class BoardActivity extends AppCompatActivity {

    // Debug variables
    char mode;
    String TAG;

    // Firestore variables
    FirebaseFirestore db;

    // Board/RecycleView variables
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        mode = intent.getCharExtra(MainMenuActivity.EXTRA_CHAR, 'X');
        TAG = this.getClass().getName();

        createView();
        initialiseFirestore();

        Board board = new Board();

        db.collection("rooms").document("room1")
                .set(board)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });


    }

    private void initialiseFirestore() {
        db = FirebaseFirestore.getInstance();

    }

    private void createView() {
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
