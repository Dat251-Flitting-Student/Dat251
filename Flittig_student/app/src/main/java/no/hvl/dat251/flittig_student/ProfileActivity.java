package no.hvl.dat251.flittig_student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import no.hvl.dat251.flittig_student.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    /*
    This class shows the profile of a logged in User. All the information about a that user
    is found in the **UserInfo** class.
     */

    private static final String TAG = "ProfileActivity";
    private int points = -1;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* This is what happens when this activity is activated. */
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        menu();

        // TODO: add profile picture

        // display the username
        TextView username = findViewById(R.id.username);
        username.setText("Navn: " + UserInfo.getUsername());

        // display the school
        TextView school = findViewById(R.id.school);
        school.setText("Skole: " + UserInfo.school());

        displayPoints();

        if (CheckedInActivity.running) {
            CheckedInActivity.chronometer.setBase(SystemClock.elapsedRealtime() + CheckedInActivity.pauseValue);
            CheckedInActivity.chronometer.start();
            CheckedInActivity.chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                public void onChronometerTick(Chronometer chronometer) {
                    if ((CheckedInActivity.mTicks % 60 * 30) == 0) {
//                        if ((CheckedInActivity.mTicks % 10) == 0) {
//                            ProfileActivity.incrementPoints();
                        Log.d(TAG, "Point added");
                        Log.d(TAG, "Points: " + CheckedInActivity.points);
                    }
                    CheckedInActivity.mTicks++;
                    CheckedInActivity.points++;
                }
            });
        }

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
                        Log.d(TAG, "Value is: " + value);
                        TextView points = (TextView) findViewById(R.id.points);
                        points.setText("Poeng: " + value);
                    }
                }
                catch (NullPointerException ex){
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
        binding.bottomNavigationView.setSelectedItemId(R.id.ic_profile);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.ic_calendar:
                    CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_home:
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_prize:
                    CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_profile:
                    CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_scores:
                    CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

            }
            return true;
        });
    }
}