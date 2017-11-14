package rft.unideb.unsolus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class Forgot_password_activity extends AppCompatActivity {

    private AutoCompleteTextView mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_activity);

        mEmail = (AutoCompleteTextView) findViewById(R.id.email_resetPW);
        checkEmail();

        Button sendemail = (Button) findViewById(R.id.sendEmail);
        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmail()){
                    //send email with new password
                    finish();
                }
            }
        });
    }

    private boolean checkEmail() {
        //search in database
        String EMAIL = mEmail.getText().toString();

            if (EMAIL.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()) {
                mEmail.setError("enter a valid email address");
                return false;
            } else {
                mEmail.setError(null);
                return true;
            }

    }
}
