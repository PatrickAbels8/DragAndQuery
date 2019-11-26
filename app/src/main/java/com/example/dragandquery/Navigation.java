package com.example.dragandquery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/***
 * TODO:
 * -
 */

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //vars
    public static final String SHARED_PREFS = "sharedPrefs";
    private String user_name;
    private String user_mail;
    private int tutorial_exp;
    private int practise_exp;

    //coms
    TextView name;
    TextView mail;
    ProgressBar tscore;
    ProgressBar pscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //intent stuff
        Intent i = getIntent();

        //components
        name = (TextView) findViewById(R.id.tv_name);
        mail = (TextView) findViewById(R.id.tv_mail);
        tscore = (ProgressBar) findViewById(R.id.pb_tutorial);
        pscore = (ProgressBar) findViewById(R.id.pb_practise);

        //key value store
        if(i.hasExtra(LoginActivity.UNAME)){
            saveData(getString(R.string.userName_key), i.getStringExtra(LoginActivity.UNAME));
        }
        if(i.hasExtra(LoginActivity.UMAIL)) {
            saveData(getString(R.string.userMail_key), i.getStringExtra(LoginActivity.UMAIL));
        }
        saveData(getString(R.string.tutScore_key), Integer.toString(30)); //just for now
        saveData(getString(R.string.pracScore_key), Integer.toString(70)); //just for now

        user_name = loadData(getString(R.string.userName_key), "Name");
        user_mail = loadData(getString(R.string.userMail_key), "Mail");
        tutorial_exp = Integer.parseInt(loadData(getString(R.string.tutScore_key), Integer.toString(0)));
        practise_exp = Integer.parseInt(loadData(getString(R.string.pracScore_key), Integer.toString(0)));

        //show user details
        name.setText(user_name);
        mail.setText(user_mail);
        tscore.setProgress(tutorial_exp);
        pscore.setProgress(practise_exp);

        //navigation stuff TODO first time show right swipe
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*TextView title_big = (TextView) findViewById(R.id.nav_title_big);
        title_big.setText(user_name);
        TextView title_small = (TextView) findViewById(R.id.nav_title_small);
        title_small.setText(user_mail);*/

        //handle navigation menu
        navigationView.setNavigationItemSelectedListener(this);
    }

    //key value store
    public void saveData(String key, String data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, data);
        editor.apply();
        Toast.makeText(getApplicationContext(), "saved _"+data+"_ under _"+key, Toast.LENGTH_LONG).show();
    }

    public String loadData(String key, String default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String data = sharedPref.getString(key, default_value);
        return data;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    //not used yet maybe later for action bar items
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getApplicationContext(), "onOptionItemSelected", Toast.LENGTH_LONG).show();
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //TODO back stack + add all the navi stuff to the other main activities after creating the main activities each
    public boolean onNavigationItemSelected(MenuItem item) {
        Toast.makeText(getApplicationContext(), "onNavigationItemSelected", Toast.LENGTH_LONG).show();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(getApplicationContext(), Navigation.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        } else if (id == R.id.nav_free) {
            Intent i = new Intent(getApplicationContext(), Free.class);
            startActivity(i);

        } else if (id == R.id.nav_tutorial) {
            //TODO int var for current level

        } else if (id == R.id.nav_practice) {
            //TODO int var for number of solved

        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_impressum) {
            //TODO basic scrollpage with text and links to mail etc
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
