package ca.on.conesotgac.te.snakeeyes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StatsActivity extends AppCompatActivity {

    private Button btnResetGameStats;
    private Button btnResetRollStats;

    private TextView AvgPlayerRoll;
    private TextView AvgComputerRoll;
    private TextView AvgMaxGameRoll;
    private TextView WinRate;

    private TextView AvgRoll;
    private TextView AvgMaxRoll;
    private TextView LastRoll;

    private SharedPreferences sharedPreferences;

    private boolean darkThemeChecked;

    private SharedPreferences sharedPref;
    FloatingActionButton ftb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SetBackgroundColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ftb = findViewById(R.id.floatingBtn);
        btnResetGameStats = findViewById(R.id.btnResetGameStats);
        btnResetRollStats = findViewById(R.id.btnResetRollStats);

        btnResetGameStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SnakeEyesApplication) getApplication()).SEClearGameStats();
                SERefreshGameStats();
            }
        });

        ftb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        btnResetRollStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SnakeEyesApplication) getApplication()).SEClearRollStats();
                SERefreshRollStats();
            }
        });

        SERefreshGameStats();
        SERefreshRollStats();
    }

    private void SERefreshGameStats(){
        final SnakeEyesApplication application = ((SnakeEyesApplication) getApplication());;

        AvgPlayerRoll = findViewById(R.id.textViewAvgPlayerRoll);
        AvgComputerRoll = findViewById(R.id.textViewAvgComputerRoll);
        AvgMaxGameRoll = findViewById(R.id.textViewAvgMaxGameRoll);
        WinRate = findViewById(R.id.textViewWinRate);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        AvgPlayerRoll.setText("" + application.SEGetAverageGameRolls("player_roll", "tbl_game_stats"));
        AvgComputerRoll.setText("" + application.SEGetAverageGameRolls("computer_roll", "tbl_game_stats"));
        AvgMaxGameRoll.setText("" + application.SEGetAverageGameRolls("max_roll", "tbl_game_stats"));

        String winRateText = application.SEGetGameResults('W') + "-" +
                application.SEGetGameResults('L') + "-" +
                application.SEGetGameResults('D');
        WinRate.setText(winRateText);
    }
    private void SERefreshRollStats(){
        final SnakeEyesApplication application = ((SnakeEyesApplication) getApplication());;

        AvgRoll = findViewById(R.id.textViewAvgRoll);
        AvgMaxRoll = findViewById(R.id.textViewAvgMaxRoll);
        LastRoll = findViewById(R.id.textViewLastRoll);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        AvgRoll.setText("" + application.SEGetAverageGameRolls("player_roll", "tbl_roll_stats"));
        AvgMaxRoll.setText("" + application.SEGetAverageGameRolls("max_roll", "tbl_roll_stats"));

        String lastRollText = application.SEGetLastRoll("player_roll") + "/" +
                application.SEGetLastRoll("max_roll");
        LastRoll.setText(lastRollText);


    }

    //Creates the top right menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.snake_eyes_menu, menu);
        //Hides back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }
        return true;
    }

    //Starts the responding activity to what the user hits on the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean ret = true;

        switch (item.getItemId()) {
            case R.id.menu_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.menu_game:
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
                break;
            case R.id.menu_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            default:
                ret = super.onOptionsItemSelected(item);
                break;
        }
        return ret;
    }

    //Sets background color to light/dark based on players preference.
    public void SetBackgroundColor() {
        darkThemeChecked = sharedPreferences.getBoolean("themeChoice", false);
        if (darkThemeChecked) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

}