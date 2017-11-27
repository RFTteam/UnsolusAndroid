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

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        expListView = (ExpandableListView) findViewById(R.id.setting_expendableLV);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        //child item click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(),
                                listDataHeader.get(groupPosition)
                                   + " : " +
                                        listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),
                                            Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        final Button saveSettings = findViewById(R.id.btn_saveSettings);
        saveSettings.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AccountSettingsActivity.this, MainActivity.class));
            }
        });

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Personal Information");
        listDataHeader.add("Change Password");
        listDataHeader.add("Games settings");

        List<String> personalInfo = new ArrayList<String>();
        personalInfo.add("Year of birth: ");
        personalInfo.add("Country: ");
        personalInfo.add("Language: ");

        List<String> pwChange = new ArrayList<String>();
        pwChange.add("Old password: ");
        pwChange.add("New password: ");
        pwChange.add("Confirm new password: ");

        List<String> games = new ArrayList<String>();
        games.add("Steam account: ");
        games.add("Battle.net account: ");

        listDataChild.put(listDataHeader.get(0), personalInfo);
        listDataChild.put(listDataHeader.get(1), pwChange);
        listDataChild.put(listDataHeader.get(2), games);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Press save settings!", Toast.LENGTH_LONG).show();
    }

}
