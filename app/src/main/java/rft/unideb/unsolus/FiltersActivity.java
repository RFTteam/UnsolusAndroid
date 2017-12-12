package rft.unideb.unsolus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rft.unideb.unsolus.fragments.PlayersFragment;

public class FiltersActivity extends AppCompatActivity{

    @BindView(R.id.isFortnite)
    CheckBox isFortnite;
    @BindView(R.id.isLol)
    CheckBox isLOL;
    @BindView(R.id.filter_rank_spinner)
    Spinner filterRank;
    @BindView(R.id.filter_role_spinner)
    Spinner filterRole;
    @BindView(R.id.filter_server_spinner)
    Spinner filterServer;
    @BindView(R.id.filter_style_spinner)
    Spinner filterStyle;
    @BindView(R.id.filter_motivation_spinner)
    Spinner filterMotivation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        ButterKnife.bind(this);

        isFortnite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillUpEmptySpinners();
            }
        });
        isLOL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillUpEmptySpinners();
            }
        });
    }

    void fillUpEmptySpinners(){
        List<String> regionSpinner = new ArrayList<String>();
        List<String> roleSpinner = new ArrayList<String>();
        List<String> rankSpinner = new ArrayList<String>();
        List<String> styleSpinner = new ArrayList<String>();

        if (isFortnite.isChecked()){
            regionSpinner.add("European");regionSpinner.add("American");

            roleSpinner.add("Soldier");roleSpinner.add("Constructor");roleSpinner.add("Ninja");roleSpinner.add("Outlander");

            rankSpinner.add("Not yet");

            styleSpinner.add("Offensive");styleSpinner.add("Defensive");
        }else if(isLOL.isChecked()){
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
        }else{
            regionSpinner.add("");
            rankSpinner.add("");
            roleSpinner.add("");
        }

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, regionSpinner);
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roleSpinner);
        ArrayAdapter<String> rankAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rankSpinner);
        ArrayAdapter<String> styleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, styleSpinner);

        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filterServer.setAdapter(regionAdapter);
        filterRole.setAdapter(roleAdapter);
        filterRank.setAdapter(rankAdapter);
        filterStyle.setAdapter(styleAdapter);
    }

    @OnClick(R.id.btn_goFilt)
    void sendFiltParams(){
        if (filterRank.getSelectedItem().toString().length() > 3) {
            Bundle bundle = new Bundle();
            bundle.putString("Rank", filterRank.getSelectedItem().toString());
            bundle.putString("Server", filterServer.getSelectedItem().toString());

            PlayersFragment test = new PlayersFragment();
            test.setArguments(bundle);
            finish();
        }
    }
}
