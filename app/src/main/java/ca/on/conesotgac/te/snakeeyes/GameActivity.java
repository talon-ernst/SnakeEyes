package ca.on.conesotgac.te.snakeeyes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private int playerNumber;
    private int aiNumber;
    private SharedPreferences sharedPreferences;
    private boolean darkThemeChecked;
    private boolean saveState;
    private boolean createActivity;
    private String Winner;
    private String compareWinner;
    public static boolean BatteryStatus = false;

    Button buttonRoll;
    ImageView aiImage;
    ImageView playerImage;
    TextView WinLose;
    TextView Compare;
    TextView PlayerScore;
    TextView AiScore;

    FloatingActionButton ftb;

    String DesiredDiceSidesString;
    Integer DesiredDiceSidesInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Sets prefs
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Sets background
        SetBackgroundColor();
        createActivity = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Declare application for future use
        final SnakeEyesApplication application = ((SnakeEyesApplication) getApplication());

        //Get the last user roll
        final String lastRollText = getString(R.string.roll_notif_body)  + " " +  application.SEGetLastRoll("player_roll");

        //Constants for timer delay
        final int timerDelay = 5000;
        final int timerAfter = 30000;

        //timer declaration
        final Timer lastRollTimer = new Timer(true);

        //Sets UI
        buttonRoll = findViewById(R.id.btnPlayGame);
        aiImage = findViewById(R.id.imageViewAi);
        playerImage = findViewById(R.id.imageViewPlayer);
        WinLose = findViewById(R.id.textViewWinLose);
        Compare = findViewById(R.id.textViewCompare);
        PlayerScore = findViewById(R.id.textViewPlayerScore);
        AiScore = findViewById(R.id.textViewAiScore);
        ftb = findViewById(R.id.floatingBtn);

        //Gets the desired amount of dice sides
        DesiredDiceSidesString = sharedPreferences.getString("diceSides", "6");
        DesiredDiceSidesInt = Integer.parseInt(DesiredDiceSidesString);

        //On click listener for button
        buttonRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerNumber = GetDiceNumber(1, DesiredDiceSidesInt);
                aiNumber = GetDiceNumber(1, DesiredDiceSidesInt);
                PlayGame();
                ShowImages();
            }
        });

        //On click listener for floating action button
        ftb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        //timer for snack bar showing up
        lastRollTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Snackbar
                        .make(findViewById(R.id.gameLayout), lastRollText, Snackbar.LENGTH_LONG)
                        .setAction("Roll Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        }).show();
            }
        }, timerDelay, timerAfter);
    }

    //On restart function
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
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
            case R.id.menu_stats:
                startActivity(new Intent(getApplicationContext(), StatsActivity.class));
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

    public void ShowImages(){
        playerImage.setVisibility(View.VISIBLE);
        aiImage.setVisibility(View.VISIBLE);
    }

    private static int GetDiceNumber(int a, int b){
        return (b>=a
                ? a + (int)Math.floor(Math.random()*(b - a + 1))
                : GetDiceNumber(b, a));
    }

    private void PlayGame(){
      char gameResult;
      if(playerNumber>0)
      {
          if (playerNumber > aiNumber){
              Winner = getString(R.string.PlayerWins);
              compareWinner = "<";
              gameResult = 'W';
          }
          else if (aiNumber > playerNumber){
              Winner = getString(R.string.AiWins);
              compareWinner = ">";
              gameResult = 'L';
          }
          else {
              Winner = getString(R.string.tie);
              compareWinner = "-";
              gameResult = 'D';
          }
          if(BatteryStatus == true)
          {
              SetImagesWithoutAn(playerNumber, playerImage);
              SetImagesWithoutAn(aiNumber, aiImage);
          }
          else
          {
              SetImagesWithAn(playerNumber, playerImage);
              SetImagesWithAn(aiNumber, aiImage);
          }

          ((SnakeEyesApplication) getApplication())
                  .SEAddGameResult(DesiredDiceSidesInt, playerNumber, aiNumber, gameResult);
      }
    }

    private void SetImagesWithAn(int number, ImageView image){

        image.setAlpha(0f);
        switch (number){
            case 1:
                image.setImageResource(R.drawable.ic_dice_six_faces_one);
                break;
            case 2:
                image.setImageResource(R.drawable.ic_dice_six_faces_two);
                break;
            case 3:
                image.setImageResource(R.drawable.ic_dice_six_faces_three);
                break;
            case 4:
                image.setImageResource(R.drawable.ic_dice_six_faces_four);
                break;
            case 5:
                image.setImageResource(R.drawable.ic_dice_six_faces_five);
                break;
            case 6:
                image.setImageResource(R.drawable.ic_dice_six_faces_six);
                break;
        }

        image.animate().alpha(1f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                PlayerScore.setText("" + playerNumber);
                AiScore.setText("" + aiNumber);
                WinLose.setText("" + Winner);
                Compare.setText("" + compareWinner);
            }
        });

    }

    public void SetBackgroundColor(){
        darkThemeChecked = sharedPreferences.getBoolean("themeChoice", false);
        if (darkThemeChecked){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt("playerNumber",playerNumber);
        ed.putInt("aiNumber",aiNumber);
        ed.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        saveState = sharedPreferences.getBoolean("saveGame",false);

        if(saveState || !createActivity)
        {
            playerNumber = sharedPreferences.getInt("playerNumber", 0);
            aiNumber = sharedPreferences.getInt("aiNumber", 0);
        }
        PlayGame();
        createActivity =false;
        SetBackgroundColor();
    }
    private void SetImagesWithoutAn(int number, ImageView image){
        switch (number){
            case 1:
                image.setImageResource(R.drawable.ic_dice_six_faces_one);
                break;
            case 2:
                image.setImageResource(R.drawable.ic_dice_six_faces_two);
                break;
            case 3:
                image.setImageResource(R.drawable.ic_dice_six_faces_three);
                break;
            case 4:
                image.setImageResource(R.drawable.ic_dice_six_faces_four);
                break;
            case 5:
                image.setImageResource(R.drawable.ic_dice_six_faces_five);
                break;
            case 6:
                image.setImageResource(R.drawable.ic_dice_six_faces_six);
                break;
        }

        PlayerScore.setText("" + playerNumber);
        AiScore.setText("" + aiNumber);
        WinLose.setText("" + Winner);
        Compare.setText("" + compareWinner);
    }
}