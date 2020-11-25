package ca.on.conesotgac.te.snakeeyes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

public class AboutActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private boolean darkThemeChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SetBackgroundColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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