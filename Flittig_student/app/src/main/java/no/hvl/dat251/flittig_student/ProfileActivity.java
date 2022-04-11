package no.hvl.dat251.flittig_student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    //public int id;
    public String username;
    public String email;
    public String photoURL;
    public int points;

    public ProfileActivity(){

    }

    public ProfileActivity(String username, String email, String photoURL, int points) {
        this.username = username;
        this.email = email;
        this.photoURL = photoURL;
        this.points = points;
    }

    public void writeNewUser(String userId, String username, String email, String photoURL, int points) {
        ProfileActivity user = new ProfileActivity(username, email, photoURL, points);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("user").child(userId).child("username").setValue(username);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

    }
}