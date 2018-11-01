package work.samsung_checkin;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

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