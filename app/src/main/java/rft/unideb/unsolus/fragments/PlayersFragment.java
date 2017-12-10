package rft.unideb.unsolus.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rft.unideb.unsolus.AboutUsActivity;
import rft.unideb.unsolus.FiltersActivity;
import rft.unideb.unsolus.MainActivity;
import rft.unideb.unsolus.R;
import rft.unideb.unsolus.entities.Game;
import rft.unideb.unsolus.entities.Player;
import rft.unideb.unsolus.network.ApiService;
import rft.unideb.unsolus.network.RetrofitBuilder;
import rft.unideb.unsolus.others.ExpandableListAdapter;
import rft.unideb.unsolus.others.TokenManager;

import static android.content.ContentValues.TAG;

public class PlayersFragment extends Fragment {

    private Button setFilters;
    private Button removeFilters;

    private ApiService service;
    private TokenManager tokenManager;
    private Call<List<Player>> playersCall;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expandableListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private int counter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_players, container, false);

        /*if (){
            String testeset = getArguments().getString("criteria");
            Toast.makeText(getActivity(), testeset, Toast.LENGTH_LONG).show();
        }*/

        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithToken(ApiService.class,tokenManager);

        expandableListView = (ExpandableListView) view.findViewById(R.id.playerNames_expandable);
        getAllPlayer();
        listAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);

        expandableListView.setAdapter(listAdapter);

        setFilters = (Button) view.findViewById(R.id.btn_filter);
        setFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FiltersActivity.class));
            }
        });
        return view;
    }

    public void getAllPlayer(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add(" ");

        List<String> db = new ArrayList<String>();
        db.add(" ");
        listDataChild.put(listDataHeader.get(0), db);
        counter = 0;
            playersCall = service.getAllPlayers(tokenManager.getToken().getToken());
            playersCall.enqueue(new Callback<List<Player>>() {
                @Override
                public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                    Log.w(TAG, "onResponse: " + response );
                    if (response.isSuccessful()){
                        List<String> stuffsFromPlayers;
                        for (Player player : response.body()) {

                                listDataHeader.add(player.getGamerName().toString());
                                counter++;
                                stuffsFromPlayers = new ArrayList<String>();
                                stuffsFromPlayers.add("\t Game - " + player.getGame() +
                                        "\n \t \t Rank - " + player.getRank() +
                                        "\n \t \t Region - " + player.getRegion() +
                                        "\n \t \t Role - " + player.getRole() +
                                        "\n \t \t Style - " + player.getServer() +
                                        "\n \t \t Motivation - " + player.getMotivation());

                                listDataChild.put(listDataHeader.get(counter), stuffsFromPlayers);

                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Player>> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage() );
                }
        });
    }
}
