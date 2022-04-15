package no.hvl.dat251.flittig_student;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserInfo {

    public static String getUsername() {
        FirebaseUser username = FirebaseAuth.getInstance().getCurrentUser();
        return username.getDisplayName();
    }

    public static String getUID() {
        String uid = FirebaseAuth.getInstance().getUid();
        return uid;
    }


    public static String email() {
        FirebaseUser email = FirebaseAuth.getInstance().getCurrentUser();
        return email.getEmail();
    }

    public static String school() {
        return "Høyskolen på Vestlandet";
    }

    public static Uri photo() {
        FirebaseUser userPhoto = FirebaseAuth.getInstance().getCurrentUser();
        return userPhoto.getPhotoUrl();
    }
    public static int points() {
        return 0;
    }
}
