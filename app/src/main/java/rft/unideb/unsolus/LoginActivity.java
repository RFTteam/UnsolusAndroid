package rft.unideb.unsolus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rft.unideb.unsolus.entities.AccessToken;
import rft.unideb.unsolus.network.ApiService;
import rft.unideb.unsolus.network.RetrofitBuilder;
import rft.unideb.unsolus.others.TokenManager;

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";

    @BindView(R.id.email)
    EditText user_email;
    @BindView(R.id.password)
    EditText user_password;

    boolean validator;
    ApiService service;
    TokenManager tokenManager;
    Call<AccessToken> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        service = RetrofitBuilder.createService(ApiService.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken().getToken() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @OnClick(R.id.email_sign_in_button)
    void signin(){

        validator = true;

        String email = user_email.getText().toString();
        String password = user_password.getText().toString();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            validator = false;
            user_email.setError("Incorrect email or password.");
        }

        if (password.isEmpty()){
            validator = false;
            user_email.setError("Incorrect email or password.");
        }

        if (validator) {

            call = service.signin(email, password);
            call.enqueue(new Callback<AccessToken>() {

                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        tokenManager.saveToken(response.body());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        if (response.code() == 401) {
                            user_email.setError(response.message());
                        }
                    }
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;
        }
    }

    @OnClick(R.id.register_button)
    void toRegisterActivity(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @OnClick(R.id.forgot_PW)
    void toForgotPWActivity(){
        startActivity(new Intent(LoginActivity.this, Forgot_password_activity.class));
    }
}
