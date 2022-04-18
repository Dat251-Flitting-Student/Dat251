package no.hvl.dat251.flittig_student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class CheckInActivity extends AppCompatActivity {



    //use it to request location updates and get the latest location
    private FusedLocationProviderClient fusedLocClient;

    private final int REQUEST_LOCATION = 1;
    private final String TAG = "MapActivity";

    protected Button checkInBtn;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);


        //binding = ActivityMapsBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());


        setupLocClient();

        getCurrentLocation();

        checkInBtn = findViewById(R.id.checkInBtn);
        time = findViewById(R.id.time);

        //LocationRequest locationRequest = createLocationRequest();



        checkInBtn.setOnClickListener(view -> {

            //fusedLocClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            time.setText("Hei");

        });
    }

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

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("test");


                if(location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    ref.setValue(location);

                    if(isInGrid(location))
                        System.out.println("Du er på riktig sted!!");
                    else
                        System.out.println("Du er uten for området!!");

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
}