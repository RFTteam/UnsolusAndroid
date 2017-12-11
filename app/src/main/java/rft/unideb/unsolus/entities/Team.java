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
    @Json(name = "Country")
    String Country;
    @Json(name = "Language")
    String Language;
    @Json(name = "Gamename")
    String Gamename;

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
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getGamename() {
        return Gamename;
    }

    public void setGamename(String gamename) {
        Gamename = gamename;
    }
}
