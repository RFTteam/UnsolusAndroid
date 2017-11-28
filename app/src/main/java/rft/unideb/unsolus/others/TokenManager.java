package rft.unideb.unsolus.others;

import android.content.SharedPreferences;
import android.media.session.MediaSession;

import rft.unideb.unsolus.entities.AccessToken;

/**
 * Created by Tibor on 2017. 11. 25..
 */

public class TokenManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences prefs){
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences prefs){
        if (INSTANCE == null){
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }

    public void saveToken(AccessToken token){
        editor.putString("TOKEN",token.getToken()).commit();
    }

    public void deleteToken(){
        editor.remove("TOKEN").commit();
    }

    public AccessToken getToken(){
        AccessToken token = new AccessToken();
        token.setToken(prefs.getString("TOKEN",null));
        return token;
    }
}
