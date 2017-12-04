package rft.unideb.unsolus;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
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

    String actualGame;

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
               //  Toast.makeText(getApplicationContext(), listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                 actualGame = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                 if (alreadyClicked)
                     changeFragment("replace");
                 else
                     changeFragment("add");
                 alreadyClicked = true;
                 return false;
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
                    Toast.makeText(getApplicationContext(), "Can not create second profile for the same game", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Player> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

    public void changeFragment(String state){
        if (findViewById(R.id.fragment_container) != null){

            GamesParamFragment gameinfoFragment_1 = new GamesParamFragment();
            gameinfoFragment_1.setArguments(getIntent().getExtras());

            GamesParamFragment gameinfoFragment_2 = new GamesParamFragment();
            gameinfoFragment_2.setArguments(getIntent().getExtras());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (state.equals("add"))
                transaction.add(R.id.fragment_container, gameinfoFragment_1);

            if (state.equals("replace")){
                transaction.replace(R.id.fragment_container, gameinfoFragment_2);
                transaction.addToBackStack(null);
            }

            transaction.commit();

        }

    }

}
