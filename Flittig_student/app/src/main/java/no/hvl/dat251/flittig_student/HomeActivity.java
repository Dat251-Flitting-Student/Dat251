package no.hvl.dat251.flittig_student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import no.hvl.dat251.flittig_student.databinding.ActivityHomeBinding;



public class HomeActivity extends AppCompatActivity {

    public static boolean atSchool;
    //use it to request location updates and get the latest location
    private FusedLocationProviderClient fusedLocClient;

    private final int REQUEST_LOCATION = 1;
    private final String TAG = "MapActivity";

    protected static Button checkInBtn;
    private TextView points;

    private ActivityHomeBinding binding;
    private Snackbar errorPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getStatus();

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getPoints();
        menu();
        setupLocClient();

        //This is the error message if you are not at school:)
        createErrorPop();

        checkInBtn = findViewById(R.id.checkInBtn);
        points = findViewById(R.id.pointsNow);

        checkInBtn.setOnClickListener(view -> {

            getCurrentLocation();
        });
    }

    private void checkInIntent() {
        Intent intent2 = new Intent(this, CheckedInActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent2);
    }

    private void getStatus() {
        // Get the status from the database, updated automatically.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(UserInfo.getUID()).child("checked in");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    boolean value = (boolean) dataSnapshot.getValue();
                    if (value) {
                        Log.d(TAG, "Value is: " + value);
                        checkInIntent();
                    }
                }
                catch (NullPointerException ex){
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

    private void getPoints() {
        // Get the points from the database, updated automatically.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(UserInfo.getUID()).child("points_total");


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    String value = dataSnapshot.getValue().toString();
                    Log.d(TAG, "Value is: " + value);
                    points.setText(value);
                }
                catch (NullPointerException ex){
                    //if the user does not have any points from before, set them to 0.
                    UserInfo.setPoints(0);
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


    //-------------LOCATION----------------
    private void setupLocClient() {
        fusedLocClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void requestLocPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{(Manifest.permission.ACCESS_FINE_LOCATION)}, REQUEST_LOCATION);
    }

    private void getCurrentLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestLocPermissions();
        } else {
            fusedLocClient.getLastLocation().addOnSuccessListener(this, location -> {

                if(location != null) {

                    if(isInGrid(location)) {
                        System.out.println("Du er på riktig sted!!");
                        atSchool = true;
                        UserInfo.setStatus(true);

                        Intent intent = new Intent(this, CheckedInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                    else {
                        atSchool = false;
                        System.out.println("Du er uten for området!!");
                        errorPop.show();
                    }

                }else {
                    Log.e(TAG, "No location found");
                }

            });


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Log.e(TAG, "Location permission has been denied");
            }
        }
    }

    private boolean isInGrid(Location location){
        System.out.println(location);
        System.out.println(distanceFromHVL(location));

        //meter
        return distanceFromHVL(location) < 100;

    }

    private float distanceFromHVL(Location userLoc) {
        LatLng latLngHVL = new LatLng(60.36891982478414, 5.351046742392926);

        float[] results = new float[1];

        Location.distanceBetween(latLngHVL.latitude, latLngHVL.longitude, userLoc.getLatitude(), userLoc.getLongitude(), results);

        return results[0];
    }

    //-------------MENU----------------
    private void menu() {
        binding.bottomNavigationView.setSelectedItemId(R.id.ic_home);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.ic_calendar:
                    return true;

                case R.id.ic_home:
                    return true;

                case R.id.ic_prize:
                    intent = new Intent(this, PrizeMarketActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;

                case R.id.ic_profile:
                    intent = new Intent(this, ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;

                case R.id.ic_scores:
                    Intent intent1 = new Intent(this, ScoreboardActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent1);
                    return true;

            }
            return true;
        });
    }

    private void createErrorPop() {
        errorPop = Snackbar.make(binding.getRoot(), R.string.error_mesg, Snackbar.LENGTH_SHORT);
        View view2 = errorPop.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view2.getLayoutParams();
        params.gravity = Gravity.TOP;
        view2.setLayoutParams(params);
        errorPop.setBackgroundTint(getResources().getColor(R.color.green2))
                .setTextColor(getResources().getColor(R.color.white));
    }
}