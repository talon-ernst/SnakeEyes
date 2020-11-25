package ca.on.conesotgac.te.snakeeyes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btnResetGameStats = findViewById(R.id.btnResetGameStats);
        btnResetRollStats = findViewById(R.id.btnResetRollStats);

        btnResetGameStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SnakeEyesApplication) getApplication()).SEClearGameStats();
                SERefreshGameStats();
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


        /* SQL ERROR IN SnakeEyesApplication.java
        String lastRollText = application.SEGetLastRoll("player_roll") + "/" +
                application.SEGetLastRoll("max_roll");
        LastRoll.setText(lastRollText);

         */
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean ret = true;

        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;
            default:
                ret =  super.onOptionsItemSelected(item);
                break;
        }
        return ret;
    }

}