package no.hvl.dat251.flittig_student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);
        bottomNavigationView.setSelectedItemId(R.id.ic_profile);
    }

    CalendarFragment calendarFragment = new CalendarFragment();
    HomeFragment homeFragment = new HomeFragment();
    PrizeFragment prizeFragment = new PrizeFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    ScoresFragment scoresFragment = new ScoresFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ic_calendar:
                getSupportFragmentManager().beginTransaction().replace(R.id.ic_calendar, calendarFragment).commit();
                return true;

            case R.id.ic_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.ic_home, homeFragment).commit();
                return true;

            case R.id.ic_prize:
                getSupportFragmentManager().beginTransaction().replace(R.id.ic_prize, prizeFragment).commit();
                return true;

            case R.id.ic_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.ic_profile, profileFragment).commit();
                return true;

            case R.id.ic_scores:
                getSupportFragmentManager().beginTransaction().replace(R.id.ic_scores, scoresFragment).commit();
                return true;

        }
        return false;
    }
}