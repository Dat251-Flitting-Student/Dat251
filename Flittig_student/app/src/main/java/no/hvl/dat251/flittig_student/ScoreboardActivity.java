package no.hvl.dat251.flittig_student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import java.util.Collections;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import no.hvl.dat251.flittig_student.databinding.ActivityScoreboardBinding;

public class ScoreboardActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    DatabaseReference database;
    ScoreAdapter scoreAdapter;
    ArrayList<User> list;
    private ActivityScoreboardBinding binding;
    private final String TAG = "ScoreboardActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.userList);
        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("points_total").limitToLast(10);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        scoreAdapter = new ScoreAdapter(this, list);
        recyclerView.setAdapter(scoreAdapter);

        query.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 list.clear();
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     User user = snapshot.getValue(User.class);
                     list.add(user);

                 }
                 Collections.reverse(list);
                 scoreAdapter.notifyDataSetChanged();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        menu();

        if (CheckedInActivity.running) {
            CheckedInActivity.chronometer.setBase(SystemClock.elapsedRealtime() + CheckedInActivity.pauseValue);
            CheckedInActivity.chronometer.start();
            CheckedInActivity.chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                public void onChronometerTick(Chronometer chronometer) {
                    if ((CheckedInActivity.mTicks % 60 * 30) == 0) {
                        // comment for test in 10 sec
//                    if ((CheckedInActivity.mTicks % 10) == 0) {
                        UserInfo.incrementPoints();
                    }
                    CheckedInActivity.mTicks++;
                }
            });
        }
    }


    private void menu() {
        binding.bottomNavigationView.setSelectedItemId(R.id.ic_scores);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.ic_calendar:
                    if(CheckedInActivity.chronometer != null)
                        CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_home:
                    intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    if(CheckedInActivity.chronometer != null)
                        CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_prize:
                    intent = new Intent(this, PrizeMarketActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    if(CheckedInActivity.chronometer != null)
                        CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_profile:
                    intent = new Intent(this, ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    if(CheckedInActivity.chronometer != null)
                        CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_scores:
                    if(CheckedInActivity.chronometer != null)
                        CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

            }
            return true;
        });
    }
}
