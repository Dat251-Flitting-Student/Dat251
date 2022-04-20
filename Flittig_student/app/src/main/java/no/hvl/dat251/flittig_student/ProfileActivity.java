package no.hvl.dat251.flittig_student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import no.hvl.dat251.flittig_student.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    /*
    This class contains the behaviour for the points as well as displaying user info.
   The information needed to get the different fields are found in UserInfo.

   Total points: what the user has earned in total
   Current points: what the user has at this exact moment after spending etc.
     */

    private static final String TAG = "ProfileActivity";
    private int points = -1;

    /*
    public void setCurrentPoints(int value) {
        // Set points for the user.
        //TODO: instead of this method, maybe a calculating of total minus how much is spent.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("users").child(UserInfo.getUID()).child("points").child("current").setValue(value);

    }
     */

    private void setPoints(int value) {
        /* Set the total points for the user. */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child(UserInfo.getUID()).child("points").child("total").setValue(value);
    }

    public void incrementPoints() {
        /* Increment the points of the user. */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(UserInfo.getUID()).child("points").child("total");

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    int value = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                    // the incrementation
                    if (value > 0) {
                        setPoints(value + 1);
                    }
                }
            }
        });
    }



    public int getPoints() {
        /* Get the current points from the user (only once). */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(UserInfo.getUID()).child("points").child("total");

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    points = Integer.parseInt(String.valueOf(task.getResult().getValue()));

                }
            }
        });
        return points;
    }


    //TODO: implement getPoints to not retrieve the value asynch.

    /*
    public void getPoints() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(UserInfo.getUID()).child("points").child("total");

        myRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot task) {
                        String value = task.getValue().toString();
                        System.out.println("HALLO " + value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }
     */


    //TODO: implement a method that keeps track of all the points they have earned in total.
    public int pointsInTotal() {
        /* lagre som json objekt? */
        return 0;
    }

    ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* This is what happens when this activity is activated. */
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setSelectedItemId(R.id.ic_profile);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.ic_calendar:
                    return true;

                case R.id.ic_home:
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;

                case R.id.ic_prize:
                    return true;

                case R.id.ic_profile:
                    return true;

                case R.id.ic_scores:
                    return true;

            }
            return true;
        });
        // TODO: add profile picture

        // display the username
        TextView username = (TextView)findViewById(R.id.username);
        username.setText("Navn: " + UserInfo.getUsername());

        // display the school
        TextView school = (TextView)findViewById(R.id.school);
        school.setText("Skole: " + UserInfo.school());


        // Get the points from the database, updated automatically.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(UserInfo.getUID()).child("points").child("total");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
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
                    setPoints(0);
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
}
