package rft.unideb.unsolus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rft.unideb.unsolus.others.ExpandableListAdapter;

public class AccountSettings extends AppCompatActivity {

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
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Change Password");
        listDataHeader.add("Games settings");

        List<String> pwChange = new ArrayList<String>();
        pwChange.add("Old password: ");
        pwChange.add("New password: ");
        pwChange.add("Confirm new password: ");

        List<String> games = new ArrayList<String>();
        games.add("Steam account: ");
        games.add("Battle.net account: ");
        games.add("Uplay account: ");
        games.add("Origins account:");

        listDataChild.put(listDataHeader.get(0), pwChange);
        listDataChild.put(listDataHeader.get(1), games);
    }
}
