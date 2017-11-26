package rft.unideb.unsolus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;

    boolean validate;
    ApiService service;
    Call<AccessToken> call;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        service = RetrofitBuilder.createService(ApiService.class);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken().getToken() != null){
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        }
    }

    @OnClick(R.id.btn_signup)
    void register() {
        validate = true;

        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (name.isEmpty()){
            inputName.setError("Enter your name");
            validate = false;
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Enter a valid email address");
            validate = false;
        }

        if (password.isEmpty()){
            inputPassword.setError("Enter your password");
            validate = false;
        }

        if (validate){
            call = service.register(name, email, password);
            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()){
                        Log.w(TAG, "onResponse: " + response.body());
                        tokenManager.saveToken(response.body());
                        startActivity(new Intent(RegisterActivity.this, AccountSettingsActivity.class));
                        finish();
                    }else{
                        handleErrors(response.errorBody());
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
            if (error.getKey().equals("name")){
                inputName.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("email")){
                inputEmail.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("password")){
                inputPassword.setError(error.getValue().get(0));
            }
        }
    }

    public void setupRules(){


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null) {
            call.cancel();
            call = null;
        }
    }

    @OnClick(R.id.link_login)
    void backToLogin(){
        finish();
    }
}