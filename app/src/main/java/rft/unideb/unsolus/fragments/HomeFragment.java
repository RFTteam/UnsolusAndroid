package rft.unideb.unsolus.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import rft.unideb.unsolus.R;
import rft.unideb.unsolus.entities.User;
import rft.unideb.unsolus.network.ApiService;
import rft.unideb.unsolus.network.RetrofitBuilder;
import rft.unideb.unsolus.others.TokenManager;


public class HomeFragment extends Fragment {

    private ImageView games;
    private ImageView players;
    private ImageView teams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        games = (ImageView) view.findViewById(R.id.header_Games);
        players = (ImageView) view.findViewById(R.id.header_Players);
        teams = (ImageView) view.findViewById(R.id.header_Teams);

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Kattintottam");
            }
        });

        players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Kattintottam");
            }
        });

        teams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Kattintottam");
            }
        });

        return view;
    }

}
