package jak.draughts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {
    public static final String EXTRA_CHAR = "gameMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

    }

    public void debugBoardActivity(View view) {
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra(EXTRA_CHAR, 'D');
        startActivity(intent);
    }
}
