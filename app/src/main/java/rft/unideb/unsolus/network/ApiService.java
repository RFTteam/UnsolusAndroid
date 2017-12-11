package rft.unideb.unsolus.network;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rft.unideb.unsolus.entities.AccessToken;
import rft.unideb.unsolus.entities.Game;
import rft.unideb.unsolus.entities.Player;
import rft.unideb.unsolus.entities.Team;
import rft.unideb.unsolus.entities.Teammember;
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
    Call<List<User>> getUsers(@Query("token") String token);

    @PUT("api/user/password")
    @FormUrlEncoded
    Call<User> changePassword(@Field("Email") String email, @Field("Password") String password);

    @POST("api/player?token=")
    @FormUrlEncoded
    Call<Player> uploadPlayer(@Field("Gamename") String gamename, @Field("Gamername") String gamername, @FieldMap Map<String, String> optionalFields, @Query("token") String token);

    @PUT("api/player/{id}?token=")
    @FormUrlEncoded
    Call<Player> updatePlayer(@Path("id") int id,@Field("Gamename") String gamename, @Field("Gamername") String gamername, @FieldMap Map<String, String> optionalFields, @Query("token") String token);

    @DELETE("api/player/{id}?token=")
    Call<Player> deletePlayer(@Path("id") int id, @Query("token") String token);

    @GET("api/myaccounts?token=")
    Call<List<Player>> getMyPlayers(@Query("token") String token);

    @GET("api/games?token=")
    Call<List<Game>> getGameNames(@Query("token") String token);

    @GET("api/players/{game}?token=")
    Call<List<Player>> getPlayersPerGame(@Path("game") String game, @Query("token") String token);

    @GET("api/players?token=")
    Call<List<Player>> getAllPlayer(@Query("token") String token);

    @POST("api/team?token=")
    @FormUrlEncoded
    Call<Team> createTeam(@Field("Teamname") String teamName, @Field("Teamgoal") String teamGoal, @Field("Server") String server, @Field("Country") String country, @Field("Language") String language, @Field("Gamename") String gameName, @Query("token") String token);

    @DELETE("api/team/{id}?token=")
    Call<Team>  deleteTeam(@Path("id") int id, @Query("token") String token);

    @GET("api/team/{id}?token=")
    Call<Team>  getTeambyID(@Path("id") int id, @Query("token") String token);

    @GET("api/teams?token=")
    Call<List<Team>> getAllTeam(@Query("token") String token);

    @GET("api/teams/{game}?token=")
    Call<List<Team>> getTeamsPerGame(@Path("game") String game, @Query("token") String token);

    @POST("api/teammember/{id}?token=")
    Call<Teammember> joinTeam(@Path("id") int id, @Query("token") String token);

    @DELETE("api/teammember/{id}?token=")
    Call<Teammember>  leaveTeam(@Path("id") int id, @Query("token") String token);

    @GET("api/teammembers/{id}?token=")
    Call<List<Player>> getPlayersPerTeam(@Path("id") int id, @Query("token") String token);
}
