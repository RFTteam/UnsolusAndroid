package rft.unideb.unsolus.entities;

import com.squareup.moshi.Json;

public class Teammember {

    @Json(name = "TeammemberID")
    int TeammemberID;
    @Json(name = "team")
    String team;
    @Json(name = "gamerinfo")
    String gamerinfo;

    public int getTeammemberID() {
        return TeammemberID;
    }

    public void setTeammemberID(int teammemberID) {
        TeammemberID = teammemberID;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getGamerinfo() {
        return gamerinfo;
    }

    public void setGamerinfo(String gamerinfo) {
        this.gamerinfo = gamerinfo;
    }
}
