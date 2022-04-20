package no.hvl.dat251.flittig_student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import no.hvl.dat251.flittig_student.databinding.ActivityCheckedInBinding;

public class CheckedInActivity extends AppCompatActivity {

    ActivityCheckedInBinding binding;
    private static final String TAG = "CheckedInActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCheckedInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        System.out.println("RUN ON CREATE IN CHECKEDINACTIVITY" + binding.getRoot());

        binding.bottomNavigationView.setSelectedItemId(R.id.ic_home);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.ic_calendar:
                    return true;

                case R.id.ic_home:
                    return true;

                case R.id.ic_prize:
                    return true;

                case R.id.ic_profile:
                    intent = new Intent(this, ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;

                case R.id.ic_scores:
                    return true;

            }
            return true;
        });

        Random r = new Random();
        int int_random = r.nextInt(10);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Quotes").child(""+int_random);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    String quote = dataSnapshot.getValue().toString();
                    System.out.println(quote);
                    if (quote != null) {
                        Log.d(TAG, "The chosen quote is: " + quote);
                        TextView quoteView = (TextView) findViewById(R.id.quote);
                        quoteView.setText(quote);
                    }
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


}