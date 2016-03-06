package com.example.oliver.customprogress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startButton = (Button) findViewById(R.id.btnStart);

        final TextView tvProgress = (TextView) findViewById(R.id.tvProgress);

        final CustomProgress progress = (CustomProgress) findViewById(R.id.customProgress);
        progress.setMaxProgress(5 * 1000);
        progress.setUpdateTimeMS(10);
        progress.setProgressChangeListener(new CustomProgress.OnProgressChangeListener() {
            @Override
            public void onProgressChange(CustomProgress customProgress, int value) {
                tvProgress.setText(String.valueOf(value / 1000) + " " +
                        String.valueOf(value % 1000));
            }
        });
        startButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        progress.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        progress.end();
                        return true;
                }
                return false;
            }
        });

        Button btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.clear();
            }
        });
    }

}
