package rft.unideb.unsolus;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rft.unideb.unsolus.entities.User;
import rft.unideb.unsolus.network.ApiService;
import rft.unideb.unsolus.network.GMailSender;
import rft.unideb.unsolus.network.RetrofitBuilder;
import rft.unideb.unsolus.others.RandomString;
import rft.unideb.unsolus.others.TokenManager;

public class Forgot_password_activity extends AppCompatActivity {
    private static final String TAG = "Forgot_password_activit";

    @BindView(R.id.email_resetPW)
    TextView email;

    Button senderButton;
    GMailSender sender;
    ApiService service;
    TokenManager tokenManager;
    Call<User> call;

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_activity);

        ButterKnife.bind(this);

        senderButton = (Button) findViewById(R.id.sendEmail);
        sender = new GMailSender("unsolus.supp@gmail.com","UnsolusRFT");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithToken(ApiService.class,tokenManager);

        senderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewPassword();
            }
        });
    }

    void setNewPassword(){
        boolean validate = true;

        final String mail = email.getText().toString();
        final RandomString pw = new RandomString();

        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            validate = false;
            email.setError("Enter a valid email address.");
        }

        if (validate) {
            call = service.changePassword(mail, pw.getFinalString());
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.w(TAG, "onResponse: " + response);
                    if (response.isSuccessful()){
                        try {
                            sender.sendMail("Forgotten password", "Hello! \n\n Your new password: ", pw.getFinalString() , " \n\n Please change your password immediately after login. \n\n Unsolus Team.", "unsolus.supp@gmail.com", mail);
                        }
                        catch (Exception ex) {
                        }
                        Toast.makeText(getApplicationContext(), "Email send", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Email does not exist.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }

}
