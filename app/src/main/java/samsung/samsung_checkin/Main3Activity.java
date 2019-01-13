/*
Lord Mendoza

The following class is in charge of handling SEC interactions on the client list, including copying
email addresses, and clearing the client list.
 */

package samsung.samsung_checkin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
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
    //Database-related variables
    final static String[] all_columns = {_ID, NAME};
    private SQLiteDatabase db = null;
    private DatabaseOpenHelper dbHelper = null;

    //Listview-related variables for displaying the results from the database
    SimpleCursorAdapter myAdapter;
    ListView mlist;
    Cursor mCursor;

    //For displaying the confirmation message to user
    private AlertDialog actions;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //Initializing the database, and setting the listview to the mList variable
        mlist = findViewById(R.id.clientList);
        dbHelper = new DatabaseOpenHelper(this);
    }

    /*
    For querying all the information from the database and displaying it to the user
     */
    public void onResume()
    {
        super.onResume();

        //Queries the database
        db = dbHelper.getWritableDatabase();
        mCursor = db.query(dbHelper.TABLENAME, all_columns, null, null, null, null, null);

        //And shows results to SEC
        myAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1, mCursor, new String[] {"name"},
                    new int[] { android.R.id.text1 });
        mlist.setAdapter(myAdapter);

        //For handling single clicks to a client information; it will copy the name to the clip board
        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String nameCopied = (String) ((TextView) view).getText();
                nameCopied = nameCopied.substring(nameCopied.indexOf(": ") + 1, nameCopied.indexOf("Em"));
                nameCopied = nameCopied.replaceFirst("\\s++$", "");

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Client Name", nameCopied);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getApplicationContext(), "Name copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        //For handling long-clicks to a client information; it will copy the email to the clip board
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

        //For displaying alert message to confirm if user really wants to clear the client list
        DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case 0:
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
                        Toast.makeText(getApplicationContext(), "Client list cleared", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };

        //Presents the user with the confirmation message to delete the workout that they long-pressed
        AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
        builder.setTitle("Are you sure you want to clear the list?");
        String[] options = {"Confirm"};
        builder.setItems(options, actionListener);
        builder.setNegativeButton("Cancel", null);
        actions = builder.create();
    }

    public void onPause()
    {
        super.onPause();
        if (db != null)
            db.close();
        mCursor.close();
    }

    public void onBackPressed(View view)
    {
        setResult(RESULT_OK);
        finish();
    }

    public void clearDatabase(View view)
    {
        actions.show();
    }
}
