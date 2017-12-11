package rft.unideb.unsolus.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rft.unideb.unsolus.R;

public class GamesParamFragment extends Fragment {

    private EditText playerName;
    private Spinner playerRole;
    private Spinner playerRank;
    private Spinner playerRegion;
    private Spinner playerStyle;
    private Spinner playerMotivation;

    GameInfoListener activityCommander;

    public interface GameInfoListener{
        public void gamerInfo(String name, String role, String rank, String region, String server, String motivation);
        public void deleteGamer();
        public void updateGamerInfo(String name, String role, String rank, String region, String server, String motivation);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity) {
            activity = (Activity) context;
            try {
                activityCommander = (GameInfoListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games_param, container, false);

        String actGame = getArguments().getString("game");

        List<String> regionSpinner = new ArrayList<String>();
        List<String> roleSpinner = new ArrayList<String>();
        List<String> rankSpinner = new ArrayList<String>();

        if (actGame.equals("LoL")){

            regionSpinner.add("EUNE"); regionSpinner.add("RU"); regionSpinner.add("NA"); regionSpinner.add("EUW"); regionSpinner.add("LAS"); regionSpinner.add("LAN"); regionSpinner.add("BR");
            regionSpinner.add("TR");regionSpinner.add("OCE");regionSpinner.add("JP");regionSpinner.add("SEA");regionSpinner.add("SG/MY");regionSpinner.add("PH");regionSpinner.add("ID");
            regionSpinner.add("TH");regionSpinner.add("TW");regionSpinner.add("VN");regionSpinner.add("KR");regionSpinner.add("PBE");regionSpinner.add("CN");

            roleSpinner.add("AD Carry"); roleSpinner.add("Support");roleSpinner.add("Top");roleSpinner.add("Mid");roleSpinner.add("Jungle");

            rankSpinner.add("Master");rankSpinner.add("Challenger");
            rankSpinner.add("Diamond I");rankSpinner.add("Diamond II");rankSpinner.add("Diamond III");rankSpinner.add("Diamond IV");rankSpinner.add("Diamond V");
            rankSpinner.add("Platinum I");rankSpinner.add("Platinum II");rankSpinner.add("Platinum III");rankSpinner.add("Platinum IV");rankSpinner.add("Platinum V");
            rankSpinner.add("Gold I");rankSpinner.add("Gold II");rankSpinner.add("Gold III");rankSpinner.add("Gold IV");rankSpinner.add("Gold V");
            rankSpinner.add("Silver I");rankSpinner.add("Silver II");rankSpinner.add("Silver III");rankSpinner.add("Silver IV");rankSpinner.add("Silver V");
            rankSpinner.add("Bronze I");rankSpinner.add("Bronze II");rankSpinner.add("Bronze III");rankSpinner.add("Bronze IV");rankSpinner.add("Bronze V");

        }else if(actGame.equals("Fortnite")){

            regionSpinner.add("European");regionSpinner.add("American");

            roleSpinner.add("There is no point here");

            rankSpinner.add("There is no point here");
        }

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, regionSpinner);
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, roleSpinner);
        ArrayAdapter<String> rankAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, rankSpinner);

        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        playerName = (EditText) view.findViewById(R.id.input_playerName);
        playerRole = (Spinner) view.findViewById(R.id.input_role_spinner);
        playerRank = (Spinner) view.findViewById(R.id.input_rank_spinner);
        playerRegion = (Spinner) view.findViewById(R.id.input_server_spinner);
        playerStyle = (Spinner) view.findViewById(R.id.input_style_spinner);
        playerMotivation = (Spinner) view.findViewById(R.id.input_motivation_spinner);

        playerRegion.setAdapter(regionAdapter);
        playerRole.setAdapter(roleAdapter);
        playerRank.setAdapter(rankAdapter);

        final Button saveButton = (Button) view.findViewById(R.id.btn_saveGame);
        final Button updateButton = (Button) view.findViewById(R.id.btn_updateProfil);
        final Button deleteButton = (Button) view.findViewById(R.id.btn_deleteProfil);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInfo();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePlayer();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlayer();
            }
        });

        return view;
    }

    private void sendInfo() {
        activityCommander.gamerInfo(playerName.getText().toString(),
                                    playerRole.getSelectedItem().toString(),
                                    playerRank.getSelectedItem().toString(),
                                    playerRegion.getSelectedItem().toString(),
                                    playerStyle.getSelectedItem().toString(),
                                    playerMotivation.getSelectedItem().toString());
    }

    private void deletePlayer(){
        activityCommander.deleteGamer();
    }

    private void updatePlayer(){
        activityCommander.updateGamerInfo(  playerName.getText().toString(),
                                            playerRole.getSelectedItem().toString(),
                                            playerRank.getSelectedItem().toString(),
                                            playerRegion.getSelectedItem().toString(),
                                            playerStyle.getSelectedItem().toString(),
                                            playerMotivation.getSelectedItem().toString());
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
