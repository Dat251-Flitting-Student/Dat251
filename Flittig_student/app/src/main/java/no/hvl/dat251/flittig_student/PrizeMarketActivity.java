package no.hvl.dat251.flittig_student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ListView;

import java.util.ArrayList;

import no.hvl.dat251.flittig_student.databinding.ActivityHomeBinding;
import no.hvl.dat251.flittig_student.databinding.ActivityPrizeMarketBinding;

public class PrizeMarketActivity extends AppCompatActivity {

    private ListView listView;
    private ActivityPrizeMarketBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrizeMarketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        menu();

        listView = findViewById(R.id.list_view);

        //Create data
        ArrayList<Prize> prizes = new ArrayList<>();

        prizes.add(new Prize("Gratis kaffe", 300, "Sammen", R.drawable.sammen));
        prizes.add(new Prize("10% avslag", 100, "Akademika", R.drawable.akademika));

        //UserInfo.getPrizesFromDatabse();

        System.out.println("SEE HERE!!!:::  "+prizes);

        //adapter
        PrizeAdapter prizeAdapter = new PrizeAdapter(this, R.layout.list_item, prizes);
        listView.setAdapter(prizeAdapter);

    }


    private void menu() {
        binding.bottomNavigationView.setSelectedItemId(R.id.ic_prize);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.ic_calendar:
                    //CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_home:
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    //CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_prize:
                    //CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_profile:
                    intent = new Intent(this, ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    //CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_scores:
                    //CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

            }
            return true;
        });
    }
}