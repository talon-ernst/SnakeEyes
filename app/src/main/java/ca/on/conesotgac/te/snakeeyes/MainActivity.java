package ca.on.conesotgac.te.snakeeyes;

import androidx.annotation.NonNull;
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

public class MainActivity extends AppCompatActivity {

    //Sets a bunch of things used throughout the program
    private Button buttonRoll;
    private ImageView DiceSide;
    private TextView DiceRolled;
    private SharedPreferences sharedPreferences;

    private int DiceNumber;
    private int DesiredDiceSidesInt = 6;

    private boolean darkThemeChecked;
    private boolean saveState;
    private boolean creatingActivity;
    private boolean multipleGames = false;

    private String DesiredDiceSidesString;
    //On Create Function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SetBackgroundColor();
        setContentView(R.layout.activity_main);
        creatingActivity = true;
        super.onCreate(savedInstanceState);

        //Sets UI
        buttonRoll = findViewById(R.id.btnRollDice);
        DiceSide = findViewById(R.id.imageViewDice);
        DiceRolled = findViewById(R.id.textViewDiceRolled);
        DiceSide.setImageResource(R.drawable.ic_dice_target);
        DesiredDiceSidesString = sharedPreferences.getString("diceSides", "6");
        DesiredDiceSidesInt = Integer.parseInt(DesiredDiceSidesString);

        buttonRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets random number and changes picture to what it is
                if (multipleGames == false){
                    multipleGames = true;
                    DiceNumber = GetDiceNumber(1, DesiredDiceSidesInt);
                    Roll();
                }
            }
        });
    }

    //Gets a random number for dice roll
    private static int GetDiceNumber(int a, int b){
        return (b>=a
                ? a + (int)Math.floor(Math.random()*(b - a + 1))
                : GetDiceNumber(b, a));
    }

    //Creates the top right menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.snake_eyes_menu, menu);
        return true;
    }

    //Starts the responding activity to what the user hits on the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean ret = true;

        switch (item.getItemId()) {
            case R.id.menu_game:
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
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

    @Override
    protected void onPause() {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt("DiceInt", DiceNumber);
        ed.commit();
        super.onPause();
    }

    //On Resume function
    @Override
    protected void onResume() {
        super.onResume();
        saveState = sharedPreferences.getBoolean("saveGame",false);

        if(saveState || !creatingActivity)
        {
            DiceNumber= sharedPreferences.getInt("DiceInt",0);
        }

        Roll();
        SetBackgroundColor();
        creatingActivity = false;
    }

    //On Restart function
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    //Sets background color to light/dark based on players preference.
    public void SetBackgroundColor(){
        darkThemeChecked = sharedPreferences.getBoolean("themeChoice", false);
        if (darkThemeChecked){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
    }

    public void Roll()
    {
        DiceSide.setAlpha(0f);
        switch (DiceNumber)
        {
            case 1:
                DiceSide.setImageResource(R.drawable.ic_dice_six_faces_one);
                break;
            case 2:
                DiceSide.setImageResource(R.drawable.ic_dice_six_faces_two);
                break;
            case 3:
                DiceSide.setImageResource(R.drawable.ic_dice_six_faces_three);
                break;
            case 4:
                DiceSide.setImageResource(R.drawable.ic_dice_six_faces_four);
                break;
            case 5:
                DiceSide.setImageResource(R.drawable.ic_dice_six_faces_five);
                break;
            case 6:
                DiceSide.setImageResource(R.drawable.ic_dice_six_faces_six);
                break;
            default:
                DiceSide.setImageResource(R.drawable.ic_dice_target_light);
                break;
        }
        DiceSide.animate().alpha(1f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                multipleGames = false;
                super.onAnimationEnd(animation);
            }
        });
        if(DiceNumber>0)
        {
            DiceRolled.setText("" + DiceNumber);
            ((SnakeEyesApplication) getApplication())
                    .SEAddDiceResult(DesiredDiceSidesInt, DiceNumber);
        }
    }

    @Override
    protected void onStop() {
        //start notification service
        startService(new Intent(getApplicationContext(), RollNotificationService.class));
        super.onStop();
    }
}