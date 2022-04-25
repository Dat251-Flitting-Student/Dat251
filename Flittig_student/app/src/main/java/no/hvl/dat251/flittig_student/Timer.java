package no.hvl.dat251.flittig_student;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class Timer extends AppCompatActivity {

    private Chronometer chronometer;
    private long PauseOffset = 0;
    private boolean isPlaying = false;
    private boolean checkinAccept = false;
    private ToggleButton toggleButton;
    private Button reset_btn;
    private boolean atSchool;
    private boolean tempBool = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checked_in);
        chronometer = findViewById(R.id.chronometer);
//        toggleButton = findViewById(R.id.toggleButton);
        if (HomeActivity.atSchool) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            isPlaying = true;
        }
        else {
            chronometer.stop();
            isPlaying = false;
        }
    }
}
