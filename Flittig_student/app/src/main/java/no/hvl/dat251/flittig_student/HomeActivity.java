package no.hvl.dat251.flittig_student;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;


public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.ic_home);


    }

    CalendarFragment calendarFragment = new CalendarFragment();
    HomeFragment homeFragment = new HomeFragment();
    PrizeFragment prizeFragment = new PrizeFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    ScoresFragment scoresFragment = new ScoresFragment();

    @SuppressLint("NonConstantResourceId") //usikker på hva gjor men den ville ha den
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ic_calendar:
                getSupportFragmentManager().beginTransaction().replace(R.id.ic_calendar, calendarFragment).commit();
                return true;

            case R.id.ic_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.ic_home, homeFragment).commit();
                //setFragment();
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

    protected void setFragment() {
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.flFragment, new HomeFragment());
        // or ft.add(R.id.your_placeholder, new ABCFragment());
        // Complete the changes added above
        ft.commit();
    }
}