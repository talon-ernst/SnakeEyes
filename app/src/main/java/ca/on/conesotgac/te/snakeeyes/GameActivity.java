package ca.on.conesotgac.te.snakeeyes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    Button buttonRoll;
    ImageView aiImage;
    ImageView playerImage;
    TextView WinLose;
    TextView Compare;
    TextView PlayerScore;
    TextView AiScore;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SetBackgroundColor();
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

        buttonRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayGame();
                ShowImages();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        SetBackgroundColor();
    }
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
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
      playerNumber = GetDiceNumber(1, 6);
      aiNumber = GetDiceNumber(1, 6);

      if (playerNumber > aiNumber){
          WinLose.setText(R.string.PlayerWins);
          Compare.setText("<");
      }
      else if (aiNumber > playerNumber){
          WinLose.setText(R.string.AiWins);
          Compare.setText(">");
      }
      else {
          WinLose.setText(R.string.tie);
          Compare.setText("-");
      }
        SetImages(playerNumber, playerImage);
        SetImages(aiNumber, aiImage);
        PlayerScore.setText("" + playerNumber);
        AiScore.setText("" + aiNumber);
    }

    private void SetImages(int number, ImageView image){
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
}