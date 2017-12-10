package rft.unideb.unsolus.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

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

public class GamesFragment extends Fragment {

    private ApiService service;
    private TokenManager tokenManager;
    private Call<List<Game>> call;
    private Call<List<Player>> playersCall;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expandableListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private int counter = 0;
    private int variant = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_games, container, false);

        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithToken(ApiService.class,tokenManager);

        expandableListView = (ExpandableListView) view.findViewById(R.id.gameNames_expandable);
        getStuffs();
        listAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);

        expandableListView.setAdapter(listAdapter);

        return view;
    }

    void getStuffs(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("\t Few information.");

        final List<String> db = new ArrayList<String>();
        variant = 1;
        call = service.getGameNames(tokenManager.getToken().getToken());
        call.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                Log.w(TAG, "onResponse: " + response );
                if (response.isSuccessful()){
                    for (Game game : response.body()){
                        listDataHeader.add(game.getGameName());
                        counter++;

                        playersCall = service.getPlayersPerGame(game.getGameName(), tokenManager.getToken().getToken());
                        playersCall.enqueue(new Callback<List<Player>>() {
                            @Override
                            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                                Log.w(TAG, "onResponse: " + response );
                                if (response.isSuccessful()){
                                    List<String> list = new ArrayList<String>();
                                    for (Player player : response.body()) {
                                        list.add("Nickname - " + player.getGamerName() + "\n \t Rank - " + player.getRank());
                                    }
                                    listDataChild.put(listDataHeader.get(variant), list);
                                    variant++;
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Player>> call, Throwable t) {
                                Log.w(TAG, "onFailure: " + t.getMessage() );
                            }
                        });
                    }
                    db.add("\t \t Number of current available games: " + counter + "\n/ Under extension /");
                    db.add("\t \t You can't communicate with other players directly. / Yet... /");
                    db.add("\t \t These names are their ingame nicknames.");
                    listDataChild.put(listDataHeader.get(0), db);
                }
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }
}
