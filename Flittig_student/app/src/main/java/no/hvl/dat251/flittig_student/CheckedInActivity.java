package no.hvl.dat251.flittig_student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import no.hvl.dat251.flittig_student.databinding.ActivityCheckedInBinding;

public class CheckedInActivity extends AppCompatActivity {

    private ActivityCheckedInBinding binding;
    private static final String TAG = "CheckedInActivity";
    private Button btn_checkout;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public static Chronometer chronometer;
    public static boolean running;
    public static int pauseValue = 0;
    public static long mTicks = 0;
//    private int points = -1;
    private TextView points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        binding = ActivityCheckedInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btn_checkout = findViewById(R.id.btn_check_out);

        Log.d(TAG, "Points: " + points);

        // Stoppeklokke
        Log.d(TAG, "Running test 11111: " + running);
        if (!running) {
            pauseValue = 0;
            chronometer = findViewById(R.id.stoppeklokke);
            if (HomeActivity.atSchool) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                running = true;
                // Can uncomment when the incrementPoints are ready
                UserInfo.incrementPoints();
                Log.d(TAG, "Point added");
                Log.d(TAG, "Points: " + points);
                chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    public void onChronometerTick(Chronometer chronometer) {
//                        if ((mTicks % 60 * 30) == 0) {
                        if ((mTicks % 10) == 0) {
                            if (HomeActivity.atSchool) {
                                UserInfo.incrementPoints();
                            }
                            Log.d(TAG, "Point added");
                            Log.d(TAG, "Points: " + points);
                        }
                        mTicks++;
                    }
                });
                Log.d(TAG, "Running test 2222222: " + running);
            }
        }
        else {
            chronometer = findViewById(R.id.stoppeklokke);
            chronometer.setBase(SystemClock.elapsedRealtime() + pauseValue);
            chronometer.start();
        }

        btn_checkout.setOnClickListener(view -> {
            UserInfo.setStatus(false);
            //TODO: timer and set points
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.stop();
            running = false;

        });

        menu();

        getQuotes();

        displayPoints();

    }

    private void getQuotes() {
        Random r = new Random();
        int int_random = r.nextInt(9);
        myRef = database.getReference().child("Quotes").child(""+int_random);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    String quote = dataSnapshot.getValue().toString();
                    System.out.println(quote);
                    Log.d(TAG, "The chosen quote is: " + quote);
                    TextView quoteView = findViewById(R.id.quote);
                    quoteView.setText(quote);
                }
                catch (NullPointerException ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read quote.", error.toException());
            }
        });
    }

    private void displayPoints() {
        // Get the points from the database, updated automatically.
        myRef = database.getReference().child("users").child(UserInfo.getUID()).child("points").child("total");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String value = dataSnapshot.getValue().toString();
                    if (value != null) {
                        Log.d(TAG, "Value issssssssss: " + value);
                        TextView points = (TextView) findViewById(R.id.points2);
                        points.setText("Poeng: " + value);
                    }
                } catch (NullPointerException ex) {
                    //if the user does not have any points from before, set them to 0.
                    UserInfo.setPoints(0);
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void menu() {
        binding.bottomNavigationView.setSelectedItemId(R.id.ic_home);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.ic_calendar:
                    pauseValue = (int) (chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_home:
                    pauseValue = (int) (chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_prize:
                    pauseValue = (int) (chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_profile:
                    intent = new Intent(this, ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    pauseValue = (int) (chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_scores:
                    pauseValue = (int) (chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

            }
            return true;
        });
    }
}