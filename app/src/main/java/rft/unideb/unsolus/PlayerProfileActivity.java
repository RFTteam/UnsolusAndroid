package rft.unideb.unsolus;

import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rft.unideb.unsolus.entities.Player;
import rft.unideb.unsolus.fragments.GamesParamFragment;
import rft.unideb.unsolus.network.ApiService;
import rft.unideb.unsolus.network.RetrofitBuilder;
import rft.unideb.unsolus.others.ExpandableListAdapter;
import rft.unideb.unsolus.others.TokenManager;
//TODO: SPINNER INSTEAD OF TEXTFIELD
public class PlayerProfileActivity extends AppCompatActivity implements GamesParamFragment.GameInfoListener{

    private static final String TAG = "PlayerProfileActivity";

    ExpandableListAdapter listAdapter;
    ExpandableListView expandableListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    boolean alreadyClicked = false;

    ApiService service;
    TokenManager tokenManager;
    Call<Player> call;
    Call<List<Player>> call2;
    List<Player> myAccs;

    String actualGame;
    int actID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        ButterKnife.bind(this);

        expandableListView = (ExpandableListView) findViewById(R.id.setting_expendableLV);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expandableListView.setAdapter(listAdapter);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        service = RetrofitBuilder.createServiceWithToken(ApiService.class,tokenManager);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
             @Override
             public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                 actualGame = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                 if (alreadyClicked)
                     changeFragment("replace");
                 else
                     changeFragment("add");
                 alreadyClicked = true;
                 return false;
             }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.infoImageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(PlayerProfileActivity.this).create();
                alertDialog.setTitle("Information");
                alertDialog.setMessage("The fields with * are necessary. \n" +
                        "Only one player per game. \n" +
                        "You can update your player profile, but the empty fields will be empty, does not matter you filled up earlier. \n" +
                        "Please fill out the fields correctly.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Games");

        List<String> games = new ArrayList<String>();
        games.add("LeagueofLegends");
        games.add("Fortnite");

        listDataChild.put(listDataHeader.get(0), games);
    }

    @Override
    public void gamerInfo(String name, String role, String rank, String region, String server, String motivation) {

        Map<String, String> optionalParams = new HashMap<>();
        optionalParams.put("Role", role);
        optionalParams.put("Rank", rank);
        optionalParams.put("Region", region);
        optionalParams.put("Server", server);
        optionalParams.put("Motivation", motivation);

            call = service.uploadPlayer(actualGame, name, optionalParams, tokenManager.getToken().getToken());
            call.enqueue(new Callback<Player>() {
                @Override
                public void onResponse(Call<Player> call, Response<Player> response) {
                    Log.w(TAG, "onResponse: " + response );
                    if (response.isSuccessful())
                        Toast.makeText(getApplicationContext(), "Profile saved successfully", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "Can not create second profile or nickname required", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Player> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage() );
                }
            });
    }

    @Override
    public void deleteGamer() {

        getTheID();

        call = service.deletePlayer(actID, tokenManager.getToken().getToken());
        call.enqueue(new Callback<Player>() {
            @Override
            public void onResponse(Call<Player> call, Response<Player> response) {
                Log.w(TAG, "onResponse: " + response );
                if (response.isSuccessful()) Toast.makeText(getApplicationContext(), "Successfully deleted!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Player> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

    }

    @Override
    public void updateGamerInfo(String name, String role, String rank, String region, String server, String motivation) {
        getTheID();

        Map<String, String> optionalParams = new HashMap<>();
        optionalParams.put("Role", role);
        optionalParams.put("Rank", rank);
        optionalParams.put("Region", region);
        optionalParams.put("Server", server);
        optionalParams.put("Motivation", motivation);

        boolean validate = true;

        if (name.equals(null)){
            validate = false;
            Toast.makeText(getApplicationContext(), "Ingame name required!", Toast.LENGTH_LONG).show();
        }

        if (validate){
            call = service.updatePlayer(actID ,actualGame, name, optionalParams, tokenManager.getToken().getToken());
            call.enqueue(new Callback<Player>() {
                @Override
                public void onResponse(Call<Player> call, Response<Player> response) {
                    Log.w(TAG, "onResponse: " + response );
                    if (response.isSuccessful())
                        Toast.makeText(getApplicationContext(), "Profile changed successfully", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Player> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage() );
                }
            });
        }
    }
    //TODO: FIX 2 CLICK
    void getTheID(){
        call2 = service.getMyPlayers(tokenManager.getToken().getToken());
        call2.enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                Log.w(TAG, "onResponse: " + response );
                for (Player p : response.body()) {
                    if (p.getGame().equals(actualGame)) actID = p.getGamerId();
                }
            }
            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

    public void changeFragment(String state){
        if (findViewById(R.id.fragment_container) != null){
            Bundle bundle = new Bundle();

            if (actualGame.equals("Fortnite"))
                bundle.putString("game","Fortnite");
            else
                bundle.putString("game", "LoL");

            GamesParamFragment gameinfoFragment_1 = new GamesParamFragment();
            gameinfoFragment_1.setArguments(bundle);

            GamesParamFragment gameinfoFragment_2 = new GamesParamFragment();
            gameinfoFragment_2.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (state.equals("add"))
                transaction.add(R.id.fragment_container, gameinfoFragment_1);

            if (state.equals("replace")){
                transaction.replace(R.id.fragment_container, gameinfoFragment_2);
            }

            transaction.commit();

        }

    }

}
