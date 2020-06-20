package com.example.dragandquery;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.example.dragandquery.practice.Complexity;
import com.example.dragandquery.practice.Exercise;
import com.example.dragandquery.practice.Practices;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/***
 */

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //vars
    public static final String SHARED_PREFS = "sharedPrefs";
    private String user_name;
    private String user_mail;
    private int tutorial_exp_avg;
    private int[] tutorial_exps;

    //coms
    private ImageView image;
    private TextView name;
    private TextView mail;
    private TextView tv_tutorial;
    private TextView tv_practise;
    private ProgressBar pb_tutorial;
    private ProgressBar pb_practise;
    private TextView title_big;
    private TextView title_small;
    private DrawerLayout drawer;
    private ImageView swipeHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //intent stuff
        Intent i = getIntent();

        //components
        image = (ImageView) findViewById(R.id.profile_image);
        name = (TextView) findViewById(R.id.profile_name);
        mail = (TextView) findViewById(R.id.profile_mail);
        tv_tutorial = (TextView) findViewById(R.id.tv_tutorial);
        tv_practise = (TextView) findViewById(R.id.tv_practise);
        pb_tutorial = (ProgressBar) findViewById(R.id.pb_tutorial);
        pb_practise = (ProgressBar) findViewById(R.id.pb_practise);
        drawer = findViewById(R.id.drawer_layout);
        swipeHint = (ImageView) findViewById(R.id.swipehint);

        //show nd gesture hint
        swipeHint.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fromleft));

        //key value store
        if(i.hasExtra(Settings.UNAME)){
            saveData(getString(R.string.userName_key), i.getStringExtra(Settings.UNAME));
        }
        if(i.hasExtra(Settings.UMAIL)) {
            saveData(getString(R.string.userMail_key), i.getStringExtra(Settings.UMAIL));
        }
        user_name = loadDataString(getString(R.string.userName_key), "Name");
        user_mail = loadDataString(getString(R.string.userMail_key), "Mail");
        tutorial_exps = new int[]{
                loadDataInt(getString(R.string.tutScore1_key), 10),
                loadDataInt(getString(R.string.tutScore2_key), 20),
                loadDataInt(getString(R.string.tutScore3_key), 30),
                loadDataInt(getString(R.string.tutScore4_key), 40)
        };
        tutorial_exp_avg = calcAvg(tutorial_exps);


        //show user details in profile
        name.setText(user_name);
        mail.setText(user_mail);
        pb_tutorial.setProgress(tutorial_exp_avg);
        pb_practise.setProgress(calcExAvg());

        //open settings via tv's
        name.setOnClickListener(new Navigation.OnSettingsClickListener());
        mail.setOnClickListener(new Navigation.OnSettingsClickListener());

        //change profile image
        if(loadDataBoolean(getString(R.string.userImageBool_key), false)){ //todo stays true when reinstalling
            image.setImageURI(Uri.parse(loadDataString(getString(R.string.userImage_key), "")));
        } else{
            image.setImageResource(R.drawable.profile_image);
        }

        //open tutorial (and practise) via pb/tv
        tv_tutorial.setOnClickListener(new Navigation.OnTutorialClickListener());
        pb_tutorial.setOnClickListener(new Navigation.OnTutorialClickListener());

        tv_practise.setOnClickListener(new Navigation.OnPracticeClickListener());
        pb_practise.setOnClickListener(new Navigation.OnPracticeClickListener());

        //navigation stuff
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //handle navigation menu
        navigationView.setNavigationItemSelectedListener(this);
    }

    //key value store
    public void saveData(String key, String data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, data);
        editor.apply();
        //Toast.makeText(getApplicationContext(), "saved _"+data+"_ under _"+key, Toast.LENGTH_LONG).show();
    }

    //key value store
    public void saveDataBoolean(String key, boolean data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, data);
        editor.apply();
        //Toast.makeText(getApplicationContext(), "saved _"+data+"_ under _"+key, Toast.LENGTH_LONG).show();
    }

    //key value store
    public String loadDataString(String key, String default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String data = sharedPref.getString(key, default_value);
        return data;
    }

    //key value store
    public int loadDataInt(String key, int default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int data = sharedPref.getInt(key, default_value);
        return data;
    }

    public boolean loadDataBoolean(String key, boolean default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        boolean data = sharedPref.getBoolean(key, default_value);
        return data;
    }

    public int calcExAvg(){
        String e1 = loadDataString(getString(R.string.prac_easy_key), Practices.DEFAULT_EASY);
        String e2 = loadDataString(getString(R.string.prac_medium_key), Practices.DEFAULT_MEDIUM);
        String e3 = loadDataString(getString(R.string.prac_hard_key), Practices.DEFAULT_HARD);
        int num_exs = Practices.DEFAULT_EASY.length()+Practices.DEFAULT_MEDIUM.length()+Practices.DEFAULT_HARD.length();
        int num_dones = getNonZeros(e1)+getNonZeros(e2)+getNonZeros(e3);
        return num_dones==0? 1: 100*num_dones/num_exs;
    }

    public int calcAvg(int[] partialExps){
        int num_cats = partialExps.length;
        int sum = 0;
        for (int partialExp : partialExps) {
            sum += partialExp;
        }
        return sum/num_cats;
    }

    public int getNonZeros(String s){
        int num_NonZeros = 0;
        for(int i=0; i<s.length(); i++)
            if(s.charAt(i)!='0')
                num_NonZeros++;
        return num_NonZeros;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //popup
        if (requestCode == PopUp.REQUEST_CODE && resultCode == RESULT_OK){
            super.onBackPressed();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            swipeHint.setVisibility(View.VISIBLE);
        } else {
            Intent i = new Intent(getApplicationContext(), PopUp.class);
            i.putExtra(PopUp.KEY, PopUp.CLOSEAPP);
            startActivityForResult(i, PopUp.REQUEST_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        swipeHint.setVisibility(View.GONE);
        return true;
    }

    @Override
    //not used yet maybe later for action bar items
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toast.makeText(getApplicationContext(), "onOptionItemSelected", Toast.LENGTH_SHORT).show();
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
    //TODO add all the navi stuff to the other main activities after creating the main activities each
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(getApplicationContext(), Navigation.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        } else if (id == R.id.nav_free) {
            Intent i = new Intent(getApplicationContext(), Free.class);
            startActivity(i);

        } else if (id == R.id.nav_tutorial) {
            Intent i = new Intent(getApplicationContext(), Tutorial.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        } else if (id == R.id.nav_practice) {
            Intent i = new Intent(getApplicationContext(), Complexity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(getApplicationContext(), Settings.class);
            startActivity(i);

        } else if (id == R.id.nav_impressum) {
            Intent i = new Intent(getApplicationContext(), Impressum.class);
            startActivity(i);

        } else if (id == R.id.nav_dbView) {
            Intent i = new Intent(getApplicationContext(), DbView.class);
            startActivity(i);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class OnTutorialClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), Tutorial.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    public class OnPracticeClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), Complexity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    public class OnSettingsClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), Settings.class);
            startActivity(i);
        }
    }

    //key value store
    public int loadData(String key, int default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int data = sharedPref.getInt(key, default_value);
        return data;
    }
}
