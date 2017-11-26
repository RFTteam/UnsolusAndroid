package rft.unideb.unsolus.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rft.unideb.unsolus.entities.AccessToken;

/**
 * Created by Tibor on 2017. 11. 21..
 */

public interface ApiService {

    @POST("api/register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @POST("api/signin")
    @FormUrlEncoded
    Call<AccessToken> signin(@Field("email") String email, @Field("password") String password);
}
