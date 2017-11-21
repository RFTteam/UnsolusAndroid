package rft.unideb.unsolus.entities;

import com.squareup.moshi.Json;

/**
 * Created by Tibor on 2017. 11. 21..
 */

public class AccessToken {

    @Json(name = "token_type")
    String tokenType;
    @Json(name = "expires_in")
    int expiresIn;
    @Json(name = "access_token")
    String accessToken;
    @Json(name = "refresh_token")
    String refreshToken;

}
