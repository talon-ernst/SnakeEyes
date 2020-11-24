package ca.on.conesotgac.te.snakeeyes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    private Button buttonRoll;
    private ImageView DiceSide;

    private int DiceNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonRoll = findViewById(R.id.btnRollDice);
        DiceSide = findViewById(R.id.imageViewDice);


        buttonRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              DiceNumber = GetDiceNumber(1, 6);
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
                      DiceSide.setImageResource(R.drawable.ic_cauliflower);
                      break;
              }
            }
        });
    }

    private static int GetDiceNumber(int a, int b){
        return (b>=a
                ? a + (int)Math.floor(Math.random()*(b - a + 1))
                : GetDiceNumber(b, a));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.snake_eyes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean ret = true;

        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.menu_stats:
                startActivity(new Intent(getApplicationContext(), StatsActivity.class));
                break;
            default:
                ret = super.onOptionsItemSelected(item);
                break;
        }
        return ret;
    }
}