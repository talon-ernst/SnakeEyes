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

public class GameActivity extends AppCompatActivity {


    private int playerNumber;
    private int aiNumber;
    private SharedPreferences sharedPreferences;
    private boolean darkThemeChecked;
    private boolean saveState;
    private boolean createActivity;
    private String Winner;
    private String compareWinner;

    Button buttonRoll;
    ImageView aiImage;
    ImageView playerImage;
    TextView WinLose;
    TextView Compare;
    TextView PlayerScore;
    TextView AiScore;

    String DesiredDiceSidesString;
    Integer DesiredDiceSidesInt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SetBackgroundColor();
        createActivity = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        buttonRoll = findViewById(R.id.btnPlayGame);
        aiImage = findViewById(R.id.imageViewAi);
        playerImage = findViewById(R.id.imageViewPlayer);
        WinLose = findViewById(R.id.textViewWinLose);
        Compare = findViewById(R.id.textViewCompare);
        PlayerScore = findViewById(R.id.textViewPlayerScore);
        AiScore = findViewById(R.id.textViewAiScore);

        DesiredDiceSidesString = sharedPreferences.getString("diceSides", "6");
        DesiredDiceSidesInt = Integer.parseInt(DesiredDiceSidesString);

        buttonRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerNumber = GetDiceNumber(1, DesiredDiceSidesInt);
                aiNumber = GetDiceNumber(1, DesiredDiceSidesInt);
                PlayGame();
                ShowImages();
            }
        });
    }

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
              Winner = "Player Wins";
              compareWinner = "<";
              gameResult = 'W';
          }
          else if (aiNumber > playerNumber){
              Winner = "AI Wins";
              compareWinner = ">";
              gameResult = 'L';
          }
          else {
              Winner = "Tie";
              compareWinner = "-";
              gameResult = 'D';
          }
          SetImages(playerNumber, playerImage);
          SetImages(aiNumber, aiImage);


          ((SnakeEyesApplication) getApplication())
                  .SEAddGameResult(DesiredDiceSidesInt, playerNumber, aiNumber, gameResult);
      }

    }

    private void SetImages(int number, ImageView image){

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
}