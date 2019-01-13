/*
Lord Mendoza

The following class is in charge of the main screen, which retrieves the user's information
and inserts it to the database.
 */
package samsung.samsung_checkin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    //Database-related variables
    final static String _ID = "_id";
    final static String NAME = "name";
    private SQLiteDatabase db = null;
    private DatabaseOpenHelper dbHelper = null;

    //For counting the number of clicks when accessing the client list
    static int i = 0;

    //For when the activity resumes back to the main activity
    public final int ACTIVITY_RESULT = 0;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating instance of database
        dbHelper = new DatabaseOpenHelper(this);
    }

    /*
    For when the SECs click on the secret icon that brings them to the client list
     */
    public void secretPage(View view)
    {
        //Adds to click counter variable for every click
        i++;

        //If the click is counted between 3 or 4, the SEC is notified to click 5-i more times
        if (i >= 3 && i < 5)
        {
            final Toast toast = Toast.makeText(getApplicationContext(), "Click " + (5 - i) + " more times",
                    Toast.LENGTH_SHORT);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 300);
        }
        //Else if five clicks has been done, take them to the client list activity
        else if(i == 5)
        {
            i = 0;
            Intent intent = new Intent(this, Main3Activity.class);
            startActivityForResult(intent, ACTIVITY_RESULT);
        }
    }

    /*
    For when the client clicks submit, it takes their information and submits it to the database; also redirects
    the client to the confirmation page activity
     */
    public void submitInfo(View view)
    {
        EditText temp1 = findViewById(R.id.eTxt_Name);
        EditText temp2 = findViewById(R.id.eTxt_Email);

        //Retrieving the input
        String name = temp1.getText().toString();
        String email = temp2.getText().toString();

        //Checks that the user has filled in all textboxes before proceeding
        if (name.length() == 0 || email.length() == 0)
        {
            Toast.makeText(getApplicationContext(), "Please complete all the information",
                    Toast.LENGTH_SHORT).show();
        }

        //If all is good, info is inserted to database & client is redirected
        else
        {
            String info = "Name: " + name + "\nEmail: " + email;

            ContentValues values = new ContentValues();
            values.put(NAME, info);

            db = dbHelper.getWritableDatabase();
            db.insert(dbHelper.TABLENAME, null, values);

            Intent intent = new Intent(this, Main2Activity.class);
            startActivityForResult(intent, ACTIVITY_RESULT);
        }

    }

    /*
    Clearing out the text boxes when the application comes back to main screen
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        EditText temp1 = findViewById(R.id.eTxt_Name);
        temp1.setText("");
        EditText temp2 = findViewById(R.id.eTxt_Email);
        temp2.setText("");
    }
}
