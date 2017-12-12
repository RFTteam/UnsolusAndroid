package rft.unideb.unsolus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rft.unideb.unsolus.entities.AccessToken;
import rft.unideb.unsolus.entities.Team;
import rft.unideb.unsolus.network.ApiService;
import rft.unideb.unsolus.network.RetrofitBuilder;
import rft.unideb.unsolus.others.TokenManager;

public class TeamManagementActivity extends AppCompatActivity {

    private static final String TAG = "TeamManagementActivity";

    @BindView(R.id.isF)
    RadioButton fortniteClicked;
    @BindView(R.id.isL)
    RadioButton lolClicked;
    @BindView(R.id.team_server_spinner)
    Spinner serverSpinner;
    @BindView(R.id.team_goal_spinner)
    Spinner goalSpinner;
    @BindView(R.id.team_country_spinner)
    Spinner countrySpinner;
    @BindView(R.id.team_language_spinner)
    Spinner languageSpinner;
    @BindView(R.id.btn_crtTeam)
    Button createTeam;
    @BindView(R.id.btn_deleteTeam)
    Button deleteTeam;
    @BindView(R.id.team_i_name)
    EditText teamName;

    ApiService service;
    TokenManager tokenManager;
    Call<Team> call;

    boolean validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_management);

        ButterKnife.bind(this);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createService(ApiService.class);

        fillUpEmptySpinner();
        fortniteClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillUpEmptySpinner();
            }
        });

        lolClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillUpEmptySpinner();
            }
        });
    }

    private void fillUpEmptySpinner() {
        List<String> regionSpinner = new ArrayList<String>();

       if (fortniteClicked.isChecked()){

            regionSpinner.add("European");regionSpinner.add("American");

        }if(lolClicked.isChecked()){

            regionSpinner.add("EUNE"); regionSpinner.add("RU"); regionSpinner.add("NA"); regionSpinner.add("EUW"); regionSpinner.add("LAS"); regionSpinner.add("LAN"); regionSpinner.add("BR");
            regionSpinner.add("TR");regionSpinner.add("OCE");regionSpinner.add("JP");regionSpinner.add("SEA");regionSpinner.add("SG/MY");regionSpinner.add("PH");regionSpinner.add("ID");
            regionSpinner.add("TH");regionSpinner.add("TW");regionSpinner.add("VN");regionSpinner.add("KR");regionSpinner.add("PBE");regionSpinner.add("CN");
        }else{
            regionSpinner.add("");
        }

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, regionSpinner);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serverSpinner.setAdapter(regionAdapter);
    }

    @OnClick(R.id.btn_crtTeam)
    void createTeam(){
        validate = true;
        String teamname = teamName.getText().toString();
        String teamgoal = goalSpinner.getSelectedItem().toString();
        String server = serverSpinner.getSelectedItem().toString();
        String country = countrySpinner.getSelectedItem().toString();
        String language = languageSpinner.getSelectedItem().toString();
        String game;
        if (fortniteClicked.isChecked())
            game = fortniteClicked.getText().toString();
        else
            game = lolClicked.getText().toString();

        if (teamname.isEmpty() || teamgoal.isEmpty() || server.isEmpty() || country.isEmpty() || language.isEmpty() || game.isEmpty()){
            Toast.makeText(getApplicationContext(), "Must fill all fields.", Toast.LENGTH_LONG).show();
            validate = false;
        }

        if (validate){
            call = service.createTeam(teamname, teamgoal, server, country, language, game, tokenManager.getToken().getToken());
            call.enqueue(new Callback<Team>() {
                @Override
                public void onResponse(Call<Team> call, Response<Team> response) {
                    Log.w(TAG, "onResponse: " + response );
                    if (response.isSuccessful())
                        Toast.makeText(getApplicationContext(), "Team created successfully", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "Can not create team.", Toast.LENGTH_LONG).show();
                    if (response.code() == 401){
                        Toast.makeText(getApplicationContext(), "You dont have account for the game.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Team> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage() );
                }
            });
        }
    }

    @OnClick(R.id.btn_deleteTeam)
    void deleteTeam(){
        try {
            String teamname = teamName.getText().toString();

            call = service.deleteTeam(Integer.parseInt(teamname), tokenManager.getToken().getToken());
            call.enqueue(new Callback<Team>() {
                @Override
                public void onResponse(Call<Team> call, Response<Team> response) {
                    Log.w(TAG, "onResponse: " + response );
                    if (response.isSuccessful())
                        Toast.makeText(getApplicationContext(), "Team deleted successfully", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Team> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage() );
                }
            });

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Enter the TeamID for delete.", Toast.LENGTH_LONG).show();
        }

    }
}
