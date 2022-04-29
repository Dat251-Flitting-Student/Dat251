package no.hvl.dat251.flittig_student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import java.io.*;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;


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
    MyAdapter myAdapter;
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
        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        query.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 list.clear();
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     User user = snapshot.getValue(User.class);
                     list.add(user);

                 }
                 Collections.reverse(list);
                 myAdapter.notifyDataSetChanged();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        menu();

    }


    private void menu() {
        binding.bottomNavigationView.setSelectedItemId(R.id.ic_scores);

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
                    Intent intent1 = new Intent(this, ProfileActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent1);
                    return true;

                case R.id.ic_scores:
                    return true;

            }
            return true;
        });
    }
}
