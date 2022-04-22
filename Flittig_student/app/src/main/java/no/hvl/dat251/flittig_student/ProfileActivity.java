package no.hvl.dat251.flittig_student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Bitmap;
import android.content.Intent;
import android.widget.Toast;
import android.app.ProgressDialog;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import no.hvl.dat251.flittig_student.databinding.ActivityCheckInBinding;
import no.hvl.dat251.flittig_student.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    /*
    This class contains the behaviour for the points as well as displaying user info.
   The information needed to get the different fields are found in UserInfo.

   Total points: what the user has earned in total
   Current points: what the user has at this exact moment after spending etc.
     */

    private static final String TAG = "ProfileActivity";
    private int points = -1;

    /*
    public void setCurrentPoints(int value) {
        // Set points for the user.
        //TODO: instead of this method, maybe a calculating of total minus how much is spent.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("users").child(UserInfo.getUID()).child("points").child("current").setValue(value);

    }
     */

    private void setPoints(int value) {
        /* Set the total points for the user. */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child(UserInfo.getUID()).child("points").child("total").setValue(value);
    }

    public void incrementPoints() {
        /* Increment the points of the user. */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(UserInfo.getUID()).child("points").child("total");

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    int value = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                    // the incrementation
                    if (value > 0) {
                        setPoints(value + 1);
                    }
                }
            }
        });
    }



    public int getPoints() {
        /* Get the current points from the user (only once). */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(UserInfo.getUID()).child("points").child("total");

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


    //TODO: implement getPoints to not retrieve the value asynch.

    /*
    public void getPoints() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(UserInfo.getUID()).child("points").child("total");

        myRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot task) {
                        String value = task.getValue().toString();
                        System.out.println("HALLO " + value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }
     */


    //TODO: implement a method that keeps track of all the points they have earned in total.
    public int pointsInTotal() {
        /* lagre som json objekt? */
        return 0;
    }

    //Profile pic
    Button btnUpload, btnSelect;
    ImageView imageView;
    //Indicates where the image will be picked from
    Uri filePath;
    final int PICK_IMAGE_REQUEST = 0;
    FirebaseStorage storage;
    StorageReference storageRef;
            

    ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* This is what happens when this activity is activated. */
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);


        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(
                Color.parseColor("#0F9D58"));
        actionBar.setBackgroundDrawable(colorDrawable);

        // initialise views
        btnSelect = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.profile_picture);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // on pressing btnSelect SelectImage() is called
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }

        });

        // on pressing btnUpload uploadImage() is called
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        binding.bottomNavigationView.setSelectedItemId(R.id.ic_profile);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.ic_calendar:
                    //replaceFragment(new CalendarFragment());
                    return true;

                case R.id.ic_home:
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;

                case R.id.ic_prize:
                    intent = new Intent(this, CheckInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;

                case R.id.ic_profile:

                    return true;

                case R.id.ic_scores:
                    //replaceFragment(new ScoresFragment());
                    return true;

            }
            return true;
        });
        // TODO: add profile picture
    }
        // Select Image method
        private void SelectImage()
        {
            // Defining Implicit Intent to mobile gallery
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(
                            intent,
                            "Velg bilde fra galleri..."),
                    PICK_IMAGE_REQUEST);
        }


        // Override onActivityResult method
        protected void onActivityResult(int requestCode,
                    int resultCode,
                    Intent data) {

            super.onActivityResult(requestCode,
                    resultCode,
                    data);


            // checking request code and result code
            // if request code is PICK_IMAGE_REQUEST and
            // resultCode is RESULT_OK
            // then set image in the image view
            if (requestCode == PICK_IMAGE_REQUEST
                    && resultCode == RESULT_OK
                    && data != null
                    && data.getData() != null) {

                // Get the Uri of data
                filePath = data.getData();
                try {

                    // Setting image on image view using Bitmap
                    Bitmap bitmap = MediaStore
                            .Images
                            .Media
                            .getBitmap(
                                    getContentResolver(),
                                    filePath);
                    imageView.setImageBitmap(bitmap);
                }

                catch (IOException e) {
                    // Log the exception
                    e.printStackTrace();
                }
            }
        }

        // UploadImage method
        private void uploadImage()
        {
            if (filePath != null) {

                // Code for showing progressDialog while uploading
                ProgressDialog progressDialog
                        = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                // Defining the child of storageReference
                StorageReference ref
                        = storageRef
                        .child(
                                "images/"
                                        + UUID.randomUUID().toString());

                // adding listeners on upload
                // or failure of image
                ref.putFile(filePath)
                        .addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(
                                            UploadTask.TaskSnapshot taskSnapshot)
                                    {

                                        // Image uploaded successfully
                                        // Dismiss dialog
                                        progressDialog.dismiss();
                                        Toast
                                                .makeText(ProfileActivity.this,
                                                        "Image Uploaded!!",
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {

                                // Error, Image not uploaded
                                progressDialog.dismiss();
                                Toast
                                        .makeText(ProfileActivity.this,
                                                "Failed " + e.getMessage(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .addOnProgressListener(
                                new OnProgressListener<UploadTask.TaskSnapshot>() {

                                    // Progress Listener for loading
                                    // percentage on the dialog box
                                    @Override
                                    public void onProgress(
                                            UploadTask.TaskSnapshot taskSnapshot)
                                    {
                                        double progress
                                                = (100.0
                                                * taskSnapshot.getBytesTransferred()
                                                / taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage(
                                                "Uploaded "
                                                        + (int)progress + "%");
                                    }
                                });
            }


        // display the username
        TextView username = (TextView)findViewById(R.id.username);
        username.setText("Navn: " + UserInfo.getUsername());

        // display the school
        TextView school = (TextView)findViewById(R.id.school);
        school.setText("Skole: " + UserInfo.school());


        // Get the points from the database, updated automatically.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(UserInfo.getUID()).child("points").child("total");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    String value = dataSnapshot.getValue().toString();
                    if (value != null) {
                        Log.d(TAG, "Value is: " + value);
                        TextView points = (TextView) findViewById(R.id.points);
                        points.setText("Poeng: " + value);
                    }
                }
                catch (NullPointerException ex){
                    //if the user does not have any points from before, set them to 0.
                    setPoints(0);
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
}
