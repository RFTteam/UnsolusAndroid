package rft.unideb.unsolus.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rft.unideb.unsolus.entities.AccessToken;

public interface ApiService {

    @POST("api/register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("Username") String name, @Field("Email") String email, @Field("Password") String password);

    @POST("api/signin")
    @FormUrlEncoded
    Call<AccessToken> signin(@Field("Email") String email, @Field("Password") String password);


}
