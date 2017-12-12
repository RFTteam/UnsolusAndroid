package rft.unideb.unsolus.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Spinner;
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
    private CheckBox isF;
    private CheckBox isL;
    private Spinner filterRole;
    private Spinner filterRank;
    private Spinner filterRegion;
    private Spinner filterStyle;
    private Spinner filterMotivation;

    private ApiService service;
    private TokenManager tokenManager;
    private Call<List<Player>> playersCall;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expandableListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private int counter;

    String[] paramsForFilt = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_players, container, false);

        setFilters = (Button) view.findViewById(R.id.btn_filter);
        removeFilters = (Button) view.findViewById(R.id.btn_rmvfilter);
        isF = (CheckBox) view.findViewById(R.id.isFortnite);
        isL = (CheckBox) view.findViewById(R.id.isLol);
        filterRole = (Spinner) view.findViewById(R.id.f_role_spinner);
        filterRank = (Spinner) view.findViewById(R.id.f_rank_spinner);
        filterRegion = (Spinner) view.findViewById(R.id.f_server_spinner);
        filterStyle = (Spinner) view.findViewById(R.id.f_style_spinner);
        filterMotivation = (Spinner) view.findViewById(R.id.f_motivation_spinner);

        isF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillSpinners();
            }
        });

        isL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillSpinners();
            }
        });

        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithToken(ApiService.class,tokenManager);

        expandableListView = (ExpandableListView) view.findViewById(R.id.playerNames_expandable);
/*
        setFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllPlayer();
            }
        });
*/
        getAllPlayer();
        listAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);
        listAdapter.notifyDataSetChanged();
        expandableListView.setAdapter(listAdapter);

        return view;
    }

    private void fillSpinners() {
        List<String> regionSpinner = new ArrayList<String>();
        List<String> roleSpinner = new ArrayList<String>();
        List<String> rankSpinner = new ArrayList<String>();
        List<String> styleSpinner = new ArrayList<String>();

        if (isF.isChecked()){
            regionSpinner.add("European");regionSpinner.add("American");regionSpinner.add("");

            roleSpinner.add("Soldier");roleSpinner.add("Constructor");roleSpinner.add("Ninja");roleSpinner.add("Outlander");roleSpinner.add("");

            rankSpinner.add("");

            styleSpinner.add("Offensive");styleSpinner.add("Defensive");styleSpinner.add("");
        }else if(isL.isChecked()){
            regionSpinner.add("EUNE"); regionSpinner.add("RU"); regionSpinner.add("NA"); regionSpinner.add("EUW"); regionSpinner.add("LAS"); regionSpinner.add("LAN"); regionSpinner.add("BR");
            regionSpinner.add("TR");regionSpinner.add("OCE");regionSpinner.add("JP");regionSpinner.add("SEA");regionSpinner.add("SG/MY");regionSpinner.add("PH");regionSpinner.add("ID");
            regionSpinner.add("TH");regionSpinner.add("TW");regionSpinner.add("VN");regionSpinner.add("KR");regionSpinner.add("PBE");regionSpinner.add("CN");regionSpinner.add("");

            roleSpinner.add("AD Carry"); roleSpinner.add("Support");roleSpinner.add("Top");roleSpinner.add("Mid");roleSpinner.add("Jungle");roleSpinner.add("");

            rankSpinner.add("Master");rankSpinner.add("Challenger");rankSpinner.add("");
            rankSpinner.add("Diamond I");rankSpinner.add("Diamond II");rankSpinner.add("Diamond III");rankSpinner.add("Diamond IV");rankSpinner.add("Diamond V");
            rankSpinner.add("Platinum I");rankSpinner.add("Platinum II");rankSpinner.add("Platinum III");rankSpinner.add("Platinum IV");rankSpinner.add("Platinum V");
            rankSpinner.add("Gold I");rankSpinner.add("Gold II");rankSpinner.add("Gold III");rankSpinner.add("Gold IV");rankSpinner.add("Gold V");
            rankSpinner.add("Silver I");rankSpinner.add("Silver II");rankSpinner.add("Silver III");rankSpinner.add("Silver IV");rankSpinner.add("Silver V");
            rankSpinner.add("Bronze I");rankSpinner.add("Bronze II");rankSpinner.add("Bronze III");rankSpinner.add("Bronze IV");rankSpinner.add("Bronze V");

            styleSpinner.add("Aggressive");styleSpinner.add("Passive");styleSpinner.add("Roamer");styleSpinner.add("Ganker");styleSpinner.add("Invader");styleSpinner.add("Farmer");styleSpinner.add("");
        }else{
            regionSpinner.add("");
            rankSpinner.add("");
            roleSpinner.add("");
            styleSpinner.add("");
        }

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, regionSpinner);
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, roleSpinner);
        ArrayAdapter<String> rankAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, rankSpinner);
        ArrayAdapter<String> styleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, styleSpinner);

        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filterRegion.setAdapter(regionAdapter);
        filterRole.setAdapter(roleAdapter);
        filterRank.setAdapter(rankAdapter);
        filterStyle.setAdapter(styleAdapter);
    }

    public void getAllPlayer(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add(" ");

        List<String> db = new ArrayList<String>();
        db.add(" ");
        listDataChild.put(listDataHeader.get(0), db);
        counter = 0;
            playersCall = service.getAllPlayer(tokenManager.getToken().getToken());
            playersCall.enqueue(new Callback<List<Player>>() {
                @Override
                public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                    Log.w(TAG, "onResponse: " + response );
                    if (response.isSuccessful()){
                        List<String> stuffsFromPlayers;
                        for (Player player : response.body()) {
                            stuffsFromPlayers = new ArrayList<String>();
                          /*  if (isF.isChecked()){
                                if (player.getGame().equals("Fortnite")){
                                    listDataHeader.add(player.getGamerName().toString());
                                    counter++;
                                    stuffsFromPlayers.add("\t Game - " + player.getGame() +
                                            "\n \t \t Rank - " + player.getRank() +
                                            "\n \t \t Style - " + player.getRegion() +
                                            "\n \t \t Role - " + player.getRole() +
                                            "\n \t \t Server - " + player.getServer() +
                                            "\n \t \t Motivation - " + player.getMotivation());
                                }
                            }else{
                                listDataHeader.add(player.getGamerName().toString());
                                counter++;
                                stuffsFromPlayers.add("\t Game - " + player.getGame() +
                                        "\n \t \t Rank - " + player.getRank() +
                                        "\n \t \t Style - " + player.getRegion() +
                                        "\n \t \t Role - " + player.getRole() +
                                        "\n \t \t Server - " + player.getServer() +
                                        "\n \t \t Motivation - " + player.getMotivation());
                            }*/
                            listDataHeader.add(player.getGamerName().toString());
                            counter++;
                            stuffsFromPlayers.add("\t Game - " + player.getGame() +
                                    "\n \t \t Rank - " + player.getRank() +
                                    "\n \t \t Style - " + player.getRegion() +
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

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
