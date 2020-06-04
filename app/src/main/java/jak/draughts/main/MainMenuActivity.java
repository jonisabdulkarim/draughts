package jak.draughts.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import jak.draughts.R;
import jak.draughts.game.GameActivity;
import jak.draughts.lobby.LobbyActivity;

public class MainMenuActivity extends AppCompatActivity {
    public static final String EXTRA_CHAR = "gameMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

    }

    public void debugBoardActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_CHAR, 'D');
        startActivity(intent);
    }

    public void enterLobbyActivity(View view) {
        Intent intent = new Intent(this, LobbyActivity.class);
        // intent.putExtra(EXTRA_CHAR, 'D');
        startActivity(intent);
        finish();
    }
}
