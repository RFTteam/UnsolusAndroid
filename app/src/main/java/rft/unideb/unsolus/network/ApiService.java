package rft.unideb.unsolus.network;

import android.support.annotation.RequiresPermission;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rft.unideb.unsolus.entities.AccessToken;
import rft.unideb.unsolus.entities.Player;
import rft.unideb.unsolus.entities.User;

public interface ApiService {

    @POST("api/register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("Username") String name, @Field("Email") String email, @Field("Password") String password);

    @PUT("api/user?token=")
    @FormUrlEncoded
    Call<User> register_part2(@Field("DateOfBirth") String dateofBirth, @Field("Country") String country, @Field("Language") String language, @Query("token") String token);

    @POST("api/signin")
    @FormUrlEncoded
    Call<AccessToken> signin(@Field("Email") String email, @Field("Password") String password);

    @GET("api/user?token=")
    Call<User> getUser(@Query("token") String token);

    @GET("api/users?token=")
    Call<User> getUsers(@Query("token") String token);

    @PUT("api/user/password")
    @FormUrlEncoded
    Call<User> changePassword(@Field("Email") String email, @Field("Password") String password);

    @POST("api/player?token=")
    @FormUrlEncoded
    Call<Player> uploadPlayer(@Field("Gamename") String gamename, @Field("Gamername") String gamername, @FieldMap Map<String, String> optionalFields, @Query("token") String token);
}
