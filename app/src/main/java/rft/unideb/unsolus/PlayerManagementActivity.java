package rft.unideb.unsolus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rft.unideb.unsolus.entities.Team;
import rft.unideb.unsolus.entities.Teammember;
import rft.unideb.unsolus.network.ApiService;
import rft.unideb.unsolus.network.RetrofitBuilder;
import rft.unideb.unsolus.others.TokenManager;

public class PlayerManagementActivity extends AppCompatActivity {
    private static final String TAG = "PlayerManagementActivit";

    @BindView(R.id.player_management)
    EditText teamID;

    ApiService service;
    TokenManager tokenManager;
    Call<Teammember> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_management);

        ButterKnife.bind(this);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createService(ApiService.class);
    }

    @OnClick(R.id.btn_joinTeam)
    void joinTeam(){
        String id = teamID.getText().toString();

        if (!id.isEmpty()){
            call = service.joinTeam(Integer.parseInt(id), tokenManager.getToken().getToken());
            call.enqueue(new Callback<Teammember>() {
                @Override
                public void onResponse(Call<Teammember> call, Response<Teammember> response) {
                    Log.w(TAG, "onResponse: " + response );
                    if (response.isSuccessful())
                        Toast.makeText(getApplicationContext(), "Joined to the team successfully", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Teammember> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage() );
                }
            });
        }
    }

    @OnClick(R.id.btn_leaveTeam)
    void leaveTeam(){
        String id = teamID.getText().toString();

        if (!id.isEmpty()){
            call = service.leaveTeam(Integer.parseInt(id), tokenManager.getToken().getToken());
            call.enqueue(new Callback<Teammember>() {
                @Override
                public void onResponse(Call<Teammember> call, Response<Teammember> response) {
                    Log.w(TAG, "onResponse: " + response );
                    if (response.isSuccessful())
                        Toast.makeText(getApplicationContext(), "Leave team successfully", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Teammember> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage() );
                }
            });
        }
    }

}
