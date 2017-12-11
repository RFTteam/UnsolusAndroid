package rft.unideb.unsolus.entities;


import com.squareup.moshi.Json;

public class Player {

    @Json(name = "GamerName")
    String GamerName;
    @Json(name = "Rank")
    String Rank;
    @Json(name = "Role")
    String Role;
    @Json(name = "Style")
    String Region;
    @Json(name = "Server")
    String Server;
    @Json(name = "Motivation")
    String Motivation;
    @Json(name = "GamerId")
    int GamerId;
    @Json(name = "game")
    String game;

    public String getGamerName() {
        return GamerName;
    }

    public void setGamerName(String gamerName) {
        GamerName = gamerName;
    }

    public String getRank() {
        return Rank;
    }

    public void setRank(String rank) {
        Rank = rank;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getServer() {
        return Server;
    }

    public void setServer(String server) {
        Server = server;
    }

    public String getMotivation() {
        return Motivation;
    }

    public void setMotivation(String motivation) {
        Motivation = motivation;
    }

    public int getGamerId() {
        return GamerId;
    }

    public void setGamerId(int gamerId) {
        GamerId = gamerId;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }
}
