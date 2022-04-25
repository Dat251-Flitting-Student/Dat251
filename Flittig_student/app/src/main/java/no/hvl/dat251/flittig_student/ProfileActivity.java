package no.hvl.dat251.flittig_student;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import no.hvl.dat251.flittig_student.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    /*
    This class shows the profile of a logged in User. All the information about a that user
    is found in the **UserInfo** class.
     */

    private static final String TAG = "ProfileActivity";
    private int points = -1;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private ActivityProfileBinding binding;

    //Profile picture
    private Button btnUpload, btnSelect;
    private ImageView imageView;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Uri fileURI;

    //TODO: implement getPoints to not retrieve the value asynch.


    //TODO: implement a method that keeps track of all the points they have earned in total.
    public int pointsInTotal() {
        /* lagre som json objekt? */
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* This is what happens when this activity is activated. */
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        menu();

        // initialise views
        btnSelect = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.profile_picture);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // display the username
        TextView username = findViewById(R.id.username);
        username.setText("Navn: " + UserInfo.getUsername());

        // display the school
        TextView school = findViewById(R.id.school);
        school.setText("Skole: " + UserInfo.school());

        displayPoints();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            //what happens when activtyResultLauncher is launched
            if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                fileURI = result.getData().getData();
                imageView.setImageURI(fileURI);
            }
        });

        // on pressing btnSelect SelectImage() is called
        btnSelect.setOnClickListener(view -> selectImage());

        // on pressing btnUpload uploadImage() is called
        btnUpload.setOnClickListener(view -> uploadPicture());

    }

    private void displayPoints() {
        // Get the points from the database, updated automatically.
        myRef = database.getReference().child("users").child(UserInfo.getUID()).child("points").child("total");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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


    //-------PROFILE PICTURE---------

    // Select Image method
    private void selectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        activityResultLauncher.launch(intent);
    }

    private void uploadPicture() {
        // Code for showing progressDialog while uploading
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        // Defining the child of storageReference
        StorageReference imageRef = storageRef.child("images/"
                                + UserInfo.getUID());

        UploadTask uploadTask = imageRef.putFile(fileURI);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Failed to upload image:(", Toast.LENGTH_LONG).show();
        }).addOnSuccessListener(taskSnapshot -> {
            progressDialog.dismiss();
            Snackbar.make(binding.getRoot(), "Image Uploaded!", Snackbar.LENGTH_LONG).show();

        }).addOnProgressListener(snapshot -> {
            //TODO: fix progressbar
            //double p = (100.0 * snapshot.getTask().getResult().getBytesTransferred()/snapshot.getTotalByteCount());
            progressDialog.setMessage("Uploaded " + 80+ "%");
        });

    }

    //----------MENU-----------

    private void menu() {
        binding.bottomNavigationView.setSelectedItemId(R.id.ic_profile);

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
                    return true;

                case R.id.ic_scores:
                    return true;

            }
            return true;
        });
    }
}