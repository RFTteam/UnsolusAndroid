package rft.unideb.unsolus.entities;

import com.squareup.moshi.Json;

/**
 * Created by Tibor on 2017. 11. 21..
 */

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
