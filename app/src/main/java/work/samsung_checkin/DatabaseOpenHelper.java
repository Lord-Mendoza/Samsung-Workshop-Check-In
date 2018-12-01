package work.samsung_checkin;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static work.samsung_checkin.MainActivity.NAME;

public class DatabaseOpenHelper extends SQLiteOpenHelper
{
    final static String TABLENAME = "checkInList";
    final private static String CREATE_CMD =
                "CREATE TABLE " + TABLENAME + " (" + MainActivity._ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL )";

    final private static Integer VERSION = 1;
    final private Context context;

    public DatabaseOpenHelper(Context context)
    {
        super(context, "checkIn_db", null, VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_CMD);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    void deleteDatabase()
    {
        context.deleteDatabase(TABLENAME);
    }
}
