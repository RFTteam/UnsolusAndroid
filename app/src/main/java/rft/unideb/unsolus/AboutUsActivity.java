package rft.unideb.unsolus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import rft.unideb.unsolus.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Support Contact", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"unsolus.support@gmail.com"});
                try{
                    startActivity(Intent.createChooser(i, "Send Mail.."));
                }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(getApplicationContext(), "No mail client sry :/", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
