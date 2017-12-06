package rft.unideb.unsolus.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rft.unideb.unsolus.R;

public class PlayersFragment extends Fragment {

    private TextView first;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_players, container, false);

        first = (TextView) view.findViewById(R.id.players_alap);

        return view;
    }

    public void setText(String param){
        first.setText(param);
    }

}
