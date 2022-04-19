package no.hvl.dat251.flittig_student;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserInfo {
    /*
    All the user information for a logged in user.
     */

    public static String getUsername() {
        // Get the username
        FirebaseUser username = FirebaseAuth.getInstance().getCurrentUser();
        return username.getDisplayName();
    }

    public static String getUID() {
        // Get the users UID (unique identification number)
        String uid = FirebaseAuth.getInstance().getUid();
        return uid;
    }

    public static String email() {
        // Get the users email, (not sure if this is needed).
        FirebaseUser email = FirebaseAuth.getInstance().getCurrentUser();
        return email.getEmail();
    }

    public static String school() {
        return "Høyskolen på Vestlandet";
    }
    // Get the school the user goes to (so far only HVL)

    public static Uri photo() {
        // Get the profile picture of the user.
        //TODO: This should be changed so that the user can change it. Now it is the photo from Google.
        FirebaseUser userPhoto = FirebaseAuth.getInstance().getCurrentUser();
        return userPhoto.getPhotoUrl();
    }
}
