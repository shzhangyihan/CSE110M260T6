package cse110m260t6.omnialarm;

/**
 * Created by dadongjing on 2/8/16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by dadongjing on 2/8/16.
 */
public class Database extends SQLiteOpenHelper{
    static Database database1;
    static SQLiteDatabase database2;

    public static final String DATABASE_NAME = "AlarmData.db";
    public static final String TEMP_TABLE = "Temp_Table";
    public static final String FINAL_TABLE = "Final_Table";
    public static final String COLUMN_0 = "ID";
    public static final String COLUMN_1 = "TIME";
    public static final String COLUMN_2 = "RINGTONE";
    public static final String COLUMN_3 = "ACTIVITY";



    public static final int DATABASE_VERSION = 1;


    //create a database
    Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void init(Context context){
        if(database1 == null){
            database1 = new Database(context);
        }
    }

    //call onCreate() when the database is created for the first time
    //create tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + FINAL_TABLE + "("
                + COLUMN_0 + " INTEGER primary key autoincrement, "
                + COLUMN_1 + " TEXT, "
                + COLUMN_2 + " TEXT, "
                + COLUMN_3 + " TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TEMP_TABLE + "("
                + COLUMN_0 + " INTEGER primary key autoincrement, "
                + COLUMN_1 + " TEXT, "
                + COLUMN_2 + " TEXT, "
                + COLUMN_3 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FINAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TEMP_TABLE);

        onCreate(db);
    }
    public static SQLiteDatabase getDataBase(){
        if(database2 == null){
            database2 = database1.getWritableDatabase();
        }
        return database2;
    }


    /* update method for temp alarm */
    public static long updateTemp(Alarm alarm){
        SQLiteDatabase db = getDataBase();
        Cursor alarmCurrsor = db.rawQuery("select * from " + TEMP_TABLE, null);
        ContentValues contentValues = new ContentValues();
        

        contentValues.put(COLUMN_1,alarm.getTimeString());
        contentValues.put(COLUMN_2," ");
        contentValues.put(COLUMN_3," ");

        String where = "id=1";

        if(alarmCurrsor == null){
            return db.insert(TEMP_TABLE,null,contentValues);
        }
        else{
            return db.update(TEMP_TABLE,contentValues,where,null);
        }

    }




    /*insert settings to final table  */
    public static long insertAlarm(Alarm alarm){
        ContentValues contentValues = new ContentValues();


        contentValues.put(COLUMN_1, alarm.getTimeString());
        contentValues.put(COLUMN_2, " ");
        contentValues.put(COLUMN_3, " ");

        return getDataBase().insert(FINAL_TABLE, null, contentValues);


    }

    /* getter for the actual valid alarm */
    public static Alarm getAlarm(){
        SQLiteDatabase db = getDataBase();
        Cursor alarmCursor = db.rawQuery("select * from " + FINAL_TABLE, null);
        Alarm returnAlarm = null;
        if(alarmCursor.moveToFirst()) {
            String Time = alarmCursor.getString(1);
            String ringtone = alarmCursor.getString(2);
            String wake_up_activity = alarmCursor.getString(3);
            returnAlarm = new Alarm(Time, ringtone, wake_up_activity);
        }
        return returnAlarm;
    }

    /* getter for the temp alarm */
    public static Alarm getTempAlarm(){
        SQLiteDatabase db = getDataBase();
        Alarm returnAlarm = null;
        Cursor alarmCursor = db.rawQuery("select * from " + TEMP_TABLE, null);
        if(alarmCursor.moveToFirst()) {
            String Time = alarmCursor.getString(1);
            String ringtone = alarmCursor.getString(2);
            String wake_up_activity = alarmCursor.getString(3);
            returnAlarm = new Alarm(Time, ringtone, wake_up_activity);
        }
        return returnAlarm;
    }

    public static Integer deleteAlarm(String id){
        SQLiteDatabase db = getDataBase();
        return db.delete(FINAL_TABLE, "ID = ?", new String[]{id});
    }

    public static boolean checkForExist(){
        SQLiteDatabase db = getDataBase();
        Cursor myCur = db.rawQuery("SELECT * FROM " + FINAL_TABLE, null);
        Boolean exist;
        if(myCur.moveToFirst()){
            exist = true;
        }
        else{
            exist = false;
        }
        return exist;
    }

    public static void deleteAll(){
        getDataBase().delete(FINAL_TABLE,null,null);
        getDataBase().delete(TEMP_TABLE,null,null);
    }

}
