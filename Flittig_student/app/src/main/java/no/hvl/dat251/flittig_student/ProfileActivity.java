package no.hvl.dat251.flittig_student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";


    public void setPoints(int value) {
        // change this so that the users get the correct amount of points.
        // this have to be updated ? idk, i am tired
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(UserInfo.getUID());

        myRef.setValue(value);
    }

    public void incrementPoints(){
        //TODO: implement this method.

        // get current points from database (not eventlistener, but read value once
        // Check that the received value is valid.
        // Increment the received value after validation.
        // Submit (overwrite?) the current value in the database
    }

    public void getPoints() {
        // change this so that it reads value at given time, not event listener.
        // cannot change if the user gets more points.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(UserInfo.getUID());

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //setPoints(101);
        //getPoints();

        TextView username = (TextView)findViewById(R.id.username);
        username.setText("Navn: " + UserInfo.getUsername());

        TextView school = (TextView)findViewById(R.id.school);
        school.setText("Skole: " + UserInfo.school());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(UserInfo.getUID());

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
