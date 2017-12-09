package rft.unideb.unsolus.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rft.unideb.unsolus.R;

public class GamesParamFragment extends Fragment {

    private EditText playerName;
    private EditText playerRole;
    private EditText playerRank;
    private EditText playerRegion;
    private EditText playerServer;
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

        playerName = (EditText) view.findViewById(R.id.input_playerName);
        playerRole = (EditText) view.findViewById(R.id.input_role);
        playerRank = (EditText) view.findViewById(R.id.input_rank);
        playerRegion = (EditText) view.findViewById(R.id.input_region);
        playerServer = (EditText) view.findViewById(R.id.input_server);
        playerMotivation = (Spinner) view.findViewById(R.id.input_motivation_spinner);

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
                                    playerRole.getText().toString(),
                                    playerRank.getText().toString(),
                                    playerRegion.getText().toString(),
                                    playerServer.getText().toString(),
                                    playerMotivation.getSelectedItem().toString());
    }

    private void deletePlayer(){
        activityCommander.deleteGamer();
    }

    private void updatePlayer(){
        activityCommander.updateGamerInfo(  playerName.getText().toString(),
                                            playerRole.getText().toString(),
                                            playerRank.getText().toString(),
                                            playerRegion.getText().toString(),
                                            playerServer.getText().toString(),
                                            playerMotivation.getSelectedItem().toString());
    }
}
