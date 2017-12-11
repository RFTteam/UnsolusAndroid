package rft.unideb.unsolus.entities;

import com.squareup.moshi.Json;

public class Game {
    @Json(name = "GameID")
    int GameID;

    @Json(name = "GameName")
    String GameName;

    @Json(name = "Shortname")
    String Shortname;

    @Json(name = "created_at")
    String created_at;

    @Json(name = "updated_at")
    String updated_at;

    public int getGameID() {
        return GameID;
    }

    public void setGameID(int gameID) {
        GameID = gameID;
    }

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String gameName) {
        GameName = gameName;
    }

    public String getShortname() {
        return Shortname;
    }

    public void setShortname(String shortname) {
        Shortname = shortname;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
