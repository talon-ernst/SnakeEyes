package ca.on.conesotgac.te.snakeeyes;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SnakeEyesApplication extends Application{
    private static final String DB_NAME = "db_dice_stats";
    private static final int DB_VERSION = 1;

    private SQLiteOpenHelper helper;

    @Override
    public void onCreate() {
        helper = new SQLiteOpenHelper(this, DB_NAME, null, DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                //Table for game page stats
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS tbl_game_stats(" +
                        "game_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, max_roll NUMBER, " +
                        "player_roll NUMBER, computer_roll NUMBER, game_result CHAR)");

                //Table for roll page stats
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS tbl_roll_stats(" +
                        "roll_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, max_roll NUMBER, " +
                        "player_roll NUMBER)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                //No-op
            }
        };
        super.onCreate();
    }


    //Function to add a result into the database upon game completion
    public void SEAddGameResult(int maxRoll, int playerRoll, int computerRoll, char result){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO tbl_game_stats(max_roll, player_roll, computer_roll, game_result)" +
                "VALUES(" + maxRoll + ", " + playerRoll + ", " + computerRoll + ", '" + result + "')");
    }

    //Function to add a result into the database after a dice roll
    public void SEAddDiceResult(int maxRoll, int playerRoll, int computerRoll, char result){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO tbl_roll_stats(max_roll, player_roll)" +
                "VALUES(" + maxRoll + ", " + playerRoll + ")");
    }


    //Function to get the total game results based on W, L or D
    public int SEGetGameResults(char searchTerm){
        int returnNumber;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(game_id) FROM tbl_game_stats " +
                "GROUP BY game_result HAVING game_result= '" + searchTerm + "'", null);

        if (cursor.getCount() <= 0) {
            returnNumber = 0;
        }
        else {
            cursor.moveToFirst();
            returnNumber = cursor.getInt(0);
            cursor.close();
        }
        return(returnNumber);
    }

    //Function to get the average of the rolls for any table
    public int SEGetAverageGameRolls(String searchTerm, String tableName){
        int returnNumber;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(" + searchTerm + ") FROM " + tableName, null);

        if (cursor.getCount() <= 0) {
            returnNumber = 0;
        }
        else {
            cursor.moveToFirst();
            returnNumber = cursor.getInt(0);
            cursor.close();
        }
        return(returnNumber);
    }

    //function to clear existing database of its game statistics
    public void SEClearGameStats(){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM tbl_game_stats");
    }

    //function to clear existing database of its roll statistics
    public void SEClearRollStats(){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM tbl_roll_stats");
    }
}
