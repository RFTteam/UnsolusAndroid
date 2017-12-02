package rft.unideb.unsolus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rft.unideb.unsolus.entities.User;
import rft.unideb.unsolus.network.ApiService;
import rft.unideb.unsolus.network.RetrofitBuilder;
import rft.unideb.unsolus.others.TokenManager;

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String TAG = "AccountSettingsActivity";

    @BindView(R.id.input_birthday)
    TextView inputBirthday;
    @BindView(R.id.input_country)
    Spinner inputCountry;
    @BindView(R.id.input_language)
    Spinner inputLanguage;
    @BindView(R.id.input_Newpassword)
    EditText inputNewPW;
    @BindView(R.id.input_ConfirmNewpassword)
    EditText inputConfPW;

    ApiService service;
    TokenManager tokenManager;
    Call<User> call;

    boolean birthdayIsNULL;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        ButterKnife.bind(this);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        service = RetrofitBuilder.createServiceWithToken(ApiService.class,tokenManager);

        getUserCredentials();

        Button personalInfoSave = (Button) findViewById(R.id.btn_saveSettings);
                personalInfoSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (birthdayIsNULL)
                                savePersonalInfo();

                        if (!inputNewPW.getText().toString().isEmpty())
                            setNewPassword();

                        if (!birthdayIsNULL && inputNewPW.getText().toString().isEmpty())
                            Toast.makeText(getApplicationContext(), "Nothing to be saved.", Toast.LENGTH_LONG).show();
                    }
                });

    }

    void getUserCredentials(){
        call = service.getUser(tokenManager.getToken().getToken());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.w(TAG, "onResponse: " + response );
                userEmail = response.body().getEmail();
                if (response.body().getDateOfBirth() == null)
                    birthdayIsNULL = true;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

    void savePersonalInfo(){
        boolean matches = true;

        String bd = inputBirthday.getText().toString();
        String country = inputCountry.getSelectedItem().toString();
        String language = inputLanguage.getSelectedItem().toString();

        if (birthdayIsNULL) {
            String patternString = "....-..-..";
            Pattern pattern = Pattern.compile(patternString);

            Matcher matcher = pattern.matcher(bd);
            matches = matcher.matches();

            if (!matches)
                Toast.makeText(getApplicationContext(), "Please enter your birthday with this format: 'YYYY-MM-DD'", Toast.LENGTH_LONG).show();
        }

        if (matches){
            call = service.register_part2(bd,country,language,tokenManager.getToken().getToken());

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.w(TAG, "onResponse: " + response );

                    if (response.isSuccessful()){
                        Log.w(TAG, "onResponse: " + response.body());
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AccountSettingsActivity.this, MainActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage() );
                }
            });
        }
    }

    void setNewPassword(){
        boolean validate = true;

        String newPW = inputNewPW.getText().toString();
        String conf = inputConfPW.getText().toString();

        if (newPW.isEmpty() || conf.isEmpty() || !newPW.equals(conf)) {
            validate = false;
            inputNewPW.setError("The 2 password doesn't match!");
        }

        if (newPW.length() < 2 || conf.length() < 2 ){
            validate = false;
            inputNewPW.setError("2 char at least");
        }


        if (validate){
            call = service.changePassword(userEmail, newPW);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.w(TAG, "onResponse: " + response );
                    if (response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Password successfully changed!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AccountSettingsActivity.this, MainActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }

    @OnClick(R.id.btn_editPlayerProfile)
    void changeToPlayerProfile(){
        startActivity(new Intent(AccountSettingsActivity.this, PlayerProfileActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;
        }
    }
}
