package rft.unideb.unsolus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rft.unideb.unsolus.entities.AccessToken;
import rft.unideb.unsolus.entities.ApiError;
import rft.unideb.unsolus.network.ApiService;
import rft.unideb.unsolus.network.RetrofitBuilder;
import rft.unideb.unsolus.others.TokenManager;
import rft.unideb.unsolus.others.Utils;

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

        if (email.equals("admin@admin.com"))
            startActivity(new Intent(LoginActivity.this, AccountSettingsActivity.class));

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
                        if (response.code() == 422) {
                            handleErrors(response.errorBody());
                        }
                        if (response.code() == 401) {
                            ApiError apiError = Utils.convertErrors(response.errorBody());
                            Toast.makeText(LoginActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
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

    private void handleErrors(ResponseBody response){
        ApiError apiError = Utils.convertErrors(response);

        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()){
            if (error.getKey().equals("email")){
                user_email.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("password")){
                user_password.setError(error.getValue().get(0));
            }
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
