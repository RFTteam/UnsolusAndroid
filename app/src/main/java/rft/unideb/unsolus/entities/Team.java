package rft.unideb.unsolus.entities;

import com.squareup.moshi.Json;

public class Team {

    @Json(name = "TeamID")
    int TeamID;
    @Json(name = "Teamname")
    String Teamname;
    @Json(name = "Teamgoal")
    String Teamgoal;
    @Json(name = "Server")
    String Server;
    @Json(name = "country")
    String country;
    @Json(name = "language")
    String language;
    @Json(name = "game")
    String game;

    public int getTeamID() {
        return TeamID;
    }

    public void setTeamID(int teamID) {
        TeamID = teamID;
    }

    public String getTeamname() {
        return Teamname;
    }

    public void setTeamname(String teamname) {
        Teamname = teamname;
    }

    public String getTeamgoal() {
        return Teamgoal;
    }

    public void setTeamgoal(String teamgoal) {
        Teamgoal = teamgoal;
    }

    public String getServer() {
        return Server;
    }

    public void setServer(String server) {
        Server = server;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        language = language;
    }

    public String getGamename() {
        return game;
    }

    public void setGamename(String gamename) {
        game = gamename;
    }
}
