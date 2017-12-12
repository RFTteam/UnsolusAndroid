package rft.unideb.unsolus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rft.unideb.unsolus.entities.User;
import rft.unideb.unsolus.fragments.GamesFragment;
import rft.unideb.unsolus.fragments.HomeFragment;
import rft.unideb.unsolus.fragments.PlayersFragment;
import rft.unideb.unsolus.fragments.TeamsFragment;
import rft.unideb.unsolus.network.ApiService;
import rft.unideb.unsolus.network.RetrofitBuilder;
import rft.unideb.unsolus.others.FragmentChangeListener;
import rft.unideb.unsolus.others.TokenManager;

public class MainActivity extends AppCompatActivity implements FragmentChangeListener{

    private static final String TAG = "MainActivity";

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View hView;
    private TextView txtName, txtMail;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    public static int navItemIndex = 0;

    private static final String TAG_Home = "home";
    private static final String TAG_Games = "games";
    private static final String TAG_Players = "players";
    private static final String TAG_Teams = "teams";
    public static String CURRENT_TAG = TAG_Home;

    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    ApiService service;
    TokenManager tokenManager;
    Call<User> call;
    Call<List<User>> callforallUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mHandler = new Handler();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Support Contact", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"unsolus.supp@gmail.com"});
                try{
                    startActivity(Intent.createChooser(i, "Send Mail.."));
                }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(getApplicationContext(), "No mail client sry :/", Toast.LENGTH_LONG).show();
                }
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        hView = navigationView.getHeaderView(0);
        txtName = (TextView) hView.findViewById(R.id.userName);
        txtMail = (TextView) hView.findViewById(R.id.userEmail);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithToken(ApiService.class,tokenManager);
        getUserCredentials();
        getAllUser();

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        setUpNavigationView();

        if (savedInstanceState == null){
            navItemIndex = 0;
            CURRENT_TAG = TAG_Home;
            loadHomeFragment();
        }

    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();

                switch (id){
                    case R.id.nav_home:
                        navItemIndex=0;
                        CURRENT_TAG = TAG_Home;
                        break;
                    case R.id.nav_games:
                        navItemIndex=1;
                        CURRENT_TAG=TAG_Games;
                        break;
                    case R.id.nav_players:
                        navItemIndex=2;
                        CURRENT_TAG=TAG_Players;
                        break;
                    case R.id.nav_teams:
                        navItemIndex=3;
                        CURRENT_TAG=TAG_Teams;
                        break;
                    case R.id.nav_about_us:
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_account_settings:
                        startActivity(new Intent(MainActivity.this, AccountSettingsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
                        logout();
                    default:
                        navItemIndex=0;
                }

                if (item.isChecked()){
                    item.setChecked(false);
                }else{
                    item.setChecked(true);
                }
                item.setChecked(true);

                loadHomeFragment();

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void loadHomeFragment() {
        selectNavMenu();
        setToolbarTitle();
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null){
            drawer.closeDrawers();

            toggleFab();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                replaceFragment(getHomeFragment(),CURRENT_TAG);
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        toggleFab();

        drawer.closeDrawers();

        invalidateOptionsMenu();

    }

    private Fragment getHomeFragment() {
        switch (navItemIndex){
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                GamesFragment gameFragment = new GamesFragment();
                return gameFragment;
            case 2:
                PlayersFragment playersFragment = new PlayersFragment();
                return playersFragment;
            case 3:
                TeamsFragment teamsFragment = new TeamsFragment();
                return teamsFragment;
            default:
                return new HomeFragment();
        }
    }

    void getUserCredentials(){
        call = service.getUser(tokenManager.getToken().getToken());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.w(TAG, "onResponse: " + response );
                txtName.setText(response.body().getUsername());
                txtMail.setText(response.body().getEmail());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

    void getAllUser(){
        callforallUser = service.getUsers(tokenManager.getToken().getToken());
        callforallUser.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.w(TAG, "onResponse: " + response );
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

    }

    private void toggleFab() {
        if (navItemIndex == 0 || navItemIndex == 2 || navItemIndex == 3)
            fab.show();
        else
            fab.hide();
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (shouldLoadHomeFragOnBackPress){
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_Home;
                loadHomeFragment();
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (navItemIndex == 0){
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout){
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void logout(){
        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
       // startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;
        }
        logout();
    }

    @Override
    public void replaceFragment(Fragment fragment, String current_tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, current_tag);
        fragmentTransaction.commit();
    }
}