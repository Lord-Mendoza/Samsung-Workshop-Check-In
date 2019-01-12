package samsung.samsung_checkin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static samsung.samsung_checkin.MainActivity.NAME;
import static samsung.samsung_checkin.MainActivity._ID;

public class Main3Activity extends AppCompatActivity
{
    final static String[] all_columns = {_ID, NAME};
    private SQLiteDatabase db = null;
    private DatabaseOpenHelper dbHelper = null;
    SimpleCursorAdapter myAdapter;
    ListView mlist;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mlist = findViewById(R.id.clientList);
        dbHelper = new DatabaseOpenHelper(this);
    }

    public void onResume()
    {
        super.onResume();

        db = dbHelper.getWritableDatabase();
        mCursor = db.query(dbHelper.TABLENAME, all_columns, null, null, null, null, null);

        myAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1, mCursor, new String[] {"name"},
                    new int[] { android.R.id.text1 });

        mlist.setAdapter(myAdapter);

        mlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                String emailCopied = (String) ((TextView) view).getText();
                emailCopied = emailCopied.substring(emailCopied.lastIndexOf("Email: ") + 6);

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Client Email", emailCopied);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getApplicationContext(), "Email copied to clipboard", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    public void onPause()
    {
        super.onPause();
        if (db != null)
            db.close();
        mCursor.close();
    }

    public void goBack(View view)
    {
        setResult(RESULT_OK);
        finish();
    }

    public void onBackPressed(View view)
    {
        setResult(RESULT_OK);
        finish();
    }

    public void clearDatabase(View view)
    {
        int len = myAdapter.getCount();
        Cursor c = myAdapter.getCursor();
        c.moveToFirst();

        for (int i=len-1;i >= 0;i--)
        {
            String task = c.getString(1);
            //Removes the entries from the database
            db = dbHelper.getWritableDatabase();
            db.execSQL("delete from "+ dbHelper.TABLENAME);
        }

        //Allows for changes to be reflected
        mCursor = db.query(dbHelper.TABLENAME, all_columns, null, null,
                null, null, null);
        myAdapter.swapCursor(mCursor);
    }
}