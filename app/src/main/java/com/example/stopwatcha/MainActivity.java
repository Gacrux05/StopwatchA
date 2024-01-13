package com.example.stopwatcha;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView elapsedTimeTextView;
    private Button startPauseButton;
    private Button lapButton;
    private Button stopButton;
    private LinearLayout lapLayout;
    private List<String> lapTimes;

    private boolean isRunning = false;
    private long startTime;
    private long elapsedTime;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        elapsedTimeTextView = findViewById(R.id.elapsedTimeTextView);
        startPauseButton = findViewById(R.id.startPauseButton);
        lapButton = findViewById(R.id.lapButton);
        stopButton = findViewById(R.id.stopButton);
        lapLayout = findViewById(R.id.lapLayout);

        lapTimes = new ArrayList<>();
        handler = new Handler();

        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleStartPause();
            }
        });

        lapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLapTime();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });
    }

    private void toggleStartPause() {
        if (isRunning) {
            pauseTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        if (!isRunning) {
            startTime = SystemClock.elapsedRealtime() - elapsedTime;
            handler.postDelayed(updateTimer, 10);
            startPauseButton.setText("Pause");
            isRunning = true;
        }
    }

    private void pauseTimer() {
        if (isRunning) {
            handler.removeCallbacks(updateTimer);
            startPauseButton.setText("Resume");
            isRunning = false;
        }
    }

    private void stopTimer() {
        handler.removeCallbacks(updateTimer);
        isRunning = false;
        elapsedTime = 0;
        updateElapsedTime();
        startPauseButton.setText("Start");
        lapTimes.clear();
        updateLapTextView();
    }

    private void addLapTime() {
        if (isRunning) {
            lapTimes.add(getFormattedTime(elapsedTime));
            updateLapTextView();
        }
    }

    private void updateLapTextView() {
        lapLayout.removeAllViews();

        for (int i = 0; i < lapTimes.size(); i++) {
            TextView lapTimeTextView = new TextView(this);
            lapTimeTextView.setText("Lap " + (i + 1) + ": " + lapTimes.get(i));
            lapTimeTextView.setTextSize(18);
            lapTimeTextView.setTextColor(getResources().getColor(R.color.black));
            lapLayout.addView(lapTimeTextView);
        }
    }

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            elapsedTime = SystemClock.elapsedRealtime() - startTime;
            updateElapsedTime();
            handler.postDelayed(this, 10);
        }
    };

    private void updateElapsedTime() {
        elapsedTimeTextView.setText(getFormattedTime(elapsedTime));
    }

    private String getFormattedTime(long timeInMillis) {
        int hours = (int) (timeInMillis / 3600000);
        int minutes = (int) ((timeInMillis % 3600000) / 60000);
        int seconds = (int) ((timeInMillis % 60000) / 1000);
        int milliseconds = (int) (timeInMillis % 1000);

        return String.format(Locale.getDefault(), "%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
    }
}
