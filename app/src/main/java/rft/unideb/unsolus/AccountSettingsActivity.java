package rft.unideb.unsolus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.squareup.haha.perflib.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.OnClick;
import rft.unideb.unsolus.R;
import rft.unideb.unsolus.others.ExpandableListAdapter;

public class AccountSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        final Button saveSettings = findViewById(R.id.btn_saveSettings);
        saveSettings.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AccountSettingsActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Press save settings!", Toast.LENGTH_LONG).show();
    }

}
