package rft.unideb.unsolus.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rft.unideb.unsolus.R;
import rft.unideb.unsolus.entities.Game;
import rft.unideb.unsolus.entities.Player;
import rft.unideb.unsolus.network.ApiService;
import rft.unideb.unsolus.network.RetrofitBuilder;
import rft.unideb.unsolus.others.ExpandableListAdapter;
import rft.unideb.unsolus.others.TokenManager;

import static android.content.ContentValues.TAG;

public class PlayersFragment extends Fragment {

    ApiService service;
    TokenManager tokenManager;
    Call<List<Player>> playersCall;

    ExpandableListAdapter listAdapter;
    ExpandableListView expandableListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    int counter = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_players, container, false);

        String[] gameNames = new String[]{"Fortnite", "LeagueofLegends"};

        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithToken(ApiService.class,tokenManager);

        expandableListView = (ExpandableListView) view.findViewById(R.id.playerNames_expandable);
        getAllPlayer(gameNames);
        listAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);

        expandableListView.setAdapter(listAdapter);

        return view;
    }

    public void getAllPlayer(String[] games){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add(" ");

        List<String> db = new ArrayList<String>();
        db.add(" ");
        listDataChild.put(listDataHeader.get(0), db);

        for (String x : games){
            playersCall = service.getPlayersPerGame(x, tokenManager.getToken().getToken());
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
                                    "\n \t \t Server - " + player.getServer() +
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
}
