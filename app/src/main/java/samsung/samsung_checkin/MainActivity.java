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
    final static String _ID = "_id";
    final static String NAME = "name";
    static int i = 0;

    private SQLiteDatabase db = null;
    private DatabaseOpenHelper dbHelper = null;

    public final int ACTIVITY_RESULT = 0;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseOpenHelper(this);
    }

    public void secretPage(View view)
    {
        i++;
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
        else if(i == 5)
        {
            i = 0;
            Intent intent = new Intent(this, Main3Activity.class);
            startActivityForResult(intent, ACTIVITY_RESULT);
        }
    }

    public void submitInfo(View view)
    {
        EditText temp1 = findViewById(R.id.eTxt_Name);
        EditText temp2 = findViewById(R.id.eTxt_Email);

        String name = temp1.getText().toString();
        String email = temp2.getText().toString();

        if (name.length() == 0 || email.length() == 0)
        {
            Toast.makeText(getApplicationContext(), "Please complete all the information",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            String info = "Name: " + name + "          Email: " + email;

            ContentValues values = new ContentValues();
            values.put(NAME, info);

            db = dbHelper.getWritableDatabase();
            db.insert(dbHelper.TABLENAME, null, values);

            Intent intent = new Intent(this, Main2Activity.class);
            startActivityForResult(intent, ACTIVITY_RESULT);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        EditText temp1 = findViewById(R.id.eTxt_Name);
        temp1.setText("");
        EditText temp2 = findViewById(R.id.eTxt_Email);
        temp2.setText("");
    }
}
