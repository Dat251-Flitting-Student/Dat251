package no.hvl.dat251.flittig_student;

import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UserInfo {
    /*
    All the user information for a logged in user.
     */

    public int points = -1;

    public static String getUsername() {
        // Get the username
        FirebaseUser username = FirebaseAuth.getInstance().getCurrentUser();

        return username.getDisplayName();
    }

    public static String getUID() {
        // Get the users UID (unique identification number)
        return FirebaseAuth.getInstance().getUid();
    }

    public static String email() {
        // Get the users email, (not sure if this is needed).
        FirebaseUser email = FirebaseAuth.getInstance().getCurrentUser();
        return email.getEmail();
    }

    public static String school() {
        return "Høyskolen på Vestlandet";
    }
    // Get the school the user goes to (so far only HVL)

    public static Uri photo() {
        // Get the profile picture of the user.
        //TODO: This should be changed so that the user can change it. Now it is the photo from Google.
        FirebaseUser userPhoto = FirebaseAuth.getInstance().getCurrentUser();
        return userPhoto.getPhotoUrl();
    }

    public static void setPoints(int value) {
        // Set the total points of the user.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child(getUID()).child("points_total").setValue(value);

    }

    public static void setStatus(Boolean checkedIn) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child(UserInfo.getUID()).child("checked in").setValue(checkedIn);
    }

    public int getPoints() {
        /* Get the current points from the user. */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(UserInfo.getUID()).child("points_total");

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

    public static void incrementPoints() {
        /* Increment the points of the user. */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(UserInfo.getUID()).child("points_total");

        myRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                Log.d("firebase", String.valueOf(task.getResult().getValue()));
                int value = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                // the incrementation
                if (value >= 0) {
                    UserInfo.setPoints(value + 1);
                }
            }
        });
    }
    /*
    //TODO: instead of this method, maybe a calculating of total minus how much is spent.
    public void setCurrentPoints(int value) {
        // Set points for the user.
        myRef.child("users").child(UserInfo.getUID()).child("points").child("current").setValue(value);
    }
     */

    //TODO: implement getPoints to not retrieve the value asynch.


    //TODO: implement a method that keeps track of all the points they have earned in total.
    public int pointsInTotal() {
        /* lagre som json objekt? */
        return 0;
    }

    public static ArrayList<Prize> getPrizesFromDatabse() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Prizes").child("1");

        ArrayList<Prize> prizes = new ArrayList<>();
        // Attach a listener to read the data at our posts reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> list = dataSnapshot.getChildren();

                for (DataSnapshot p : list) {
                    Prize prize = p.getValue(Prize.class);
                    prizes.add(prize);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return prizes;

    }

    /*
    * private void getQuotes() {
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
    * */
}