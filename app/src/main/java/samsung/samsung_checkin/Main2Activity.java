/*
Lord Mendoza

The following class is in charge of displaying a confirmation message, and showing a countdown until
the program refreshes again and the application is redirected to the main screen.
 */
package samsung.samsung_checkin;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class Main2Activity extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //For setting the 10 second countdown and showing it
        new CountDownTimer(10000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                TextView timer = findViewById(R.id.txt_timer);
                timer.setText("This page will refresh in " + (millisUntilFinished / 1000) + " seconds");
            }

            public void onFinish()
            {
                TextView timer = findViewById(R.id.txt_timer);
                timer.setText("This page will refresh in 0 seconds");

                setResult(RESULT_OK);
                finish();
            }
        }.start();
    }
}