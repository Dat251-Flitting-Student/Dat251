package no.hvl.dat251.flittig_student;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {

    String name;
    Integer points_total;
    public User() {
    }

    public User(String name, Integer points_total) {
        this.name = name;
        this.points_total = points_total;
    }

    public String getName() {

        return this.name;
    }

    public Integer getPoints_total() {
        return this.points_total;
    }
}
