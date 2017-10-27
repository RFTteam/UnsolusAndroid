package rft.unideb.unsolus;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button createAccount = (Button) findViewById(R.id.btn_signup);
        TextView backToLogin = (TextView) findViewById(R.id.link_login);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    private void signUp() {
        Log.d(TAG, "SignUp");
        if (!validate()){
            onSignupFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this, R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

        EditText _name = (EditText) findViewById(R.id.input_name);
        EditText _email = (EditText) findViewById(R.id.input_email);
        EditText _password = (EditText) findViewById(R.id.input_password);
        EditText _age = (EditText) findViewById(R.id.input_birthday);
        EditText _country = (EditText) findViewById(R.id.input_country);
        EditText _language = (EditText) findViewById(R.id.input_language);

        String name = _name.getText().toString();
        String email = _email.getText().toString();
        String password = _password.getText().toString();
        String age = _age.getText().toString();
        String country = _country.getText().toString();
        String language = _language.getText().toString();

        //waiting for backend
        //TODO: implement logic

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    private void onSignupSuccess() {
        setResult(RESULT_OK, null);
        finish();
    }

    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_LONG).show();
    }

    private boolean validate() {
        boolean valid = true;
        EditText _name = (EditText) findViewById(R.id.input_name);
        EditText _email = (EditText) findViewById(R.id.input_email);
        EditText _password = (EditText) findViewById(R.id.input_password);
        EditText _age = (EditText) findViewById(R.id.input_birthday);
        EditText _country = (EditText) findViewById(R.id.input_country);
        EditText _language = (EditText) findViewById(R.id.input_language);

        String name = _name.getText().toString();
        String email = _email.getText().toString();
        String password = _password.getText().toString();
        String age = _age.getText().toString();
        String country = _country.getText().toString();
        String language = _language.getText().toString();

        if (name.isEmpty() || name.length() < 3 || name.length() > 15){
            _name.setError("between 3 & 15 character");
            valid = false;
        }else{
            _name.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _email.setError("enter a valid email address");
            valid = false;
        }else{
            _email.setError(null);
        }

        if (password.isEmpty() || password.length() < 6){
            _password.setError("at least 6 character");
            valid = false;
        }else{
            _password.setError(null);
        }

        if (age.isEmpty() || age.length() != 2){
            _age.setError((age.length() != 2 && age.length()>0) ? "nah.." : "enter your age");
            valid = false;
        }else{
            _age.setError(null);
        }

        if (country.isEmpty()){
            _country.setError("enter your country");
            valid = false;
        }else{
            _country.setError(null);
        }

        if (language.isEmpty()){
            _language.setError("enter your language");
            valid = false;
        }else{
            _language.setError(null);
        }

        return valid;
    }
}
