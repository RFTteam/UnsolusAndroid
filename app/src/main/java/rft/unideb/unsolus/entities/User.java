package rft.unideb.unsolus.entities;

import com.squareup.moshi.Json;

import java.util.Date;

public class User {

    int UserID;
    String Username;
    String Email;

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
