package rft.unideb.unsolus.entities;

import com.squareup.moshi.Json;

public class AccessToken {

    @Json(name = "token")
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
