package com.example.dragandquery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.dragandquery.practice.Complexity;
import com.example.dragandquery.tutorial.TutorialCategory;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * TODO:
 * - add nd (copy navigation class, include nav layout but this layout, in nav layout include this layout)
 * -
 */

public class Tutorial extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    //coms
    ProgressBar[] cats;
    TextView[] titles;

    //vars
    private final static int num_cats = 4;
    private int[] cats_exp;
    public final static String CAT_ID = "com.example.dragandquery.Tutorial.CAT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        ViewStub stub = findViewById(R.id.stub);
        stub.setLayoutResource(R.layout.activity_tutorial);
        stub.inflate();

        // nav drawer
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //init coms
        cats_exp = new int[]{
                loadData(getString(R.string.tutScore1_key), 10),
                loadData(getString(R.string.tutScore2_key), 20),
                loadData(getString(R.string.tutScore3_key), 30),
                loadData(getString(R.string.tutScore4_key), 40)
        };

        cats = new ProgressBar[]{
                findViewById(R.id.pb_cat1), //Rel DB
                findViewById(R.id.pb_cat2), //Anfragen
                findViewById(R.id.pb_cat3), //Aggreg. fkt.
                findViewById(R.id.pb_cat4)}; //Join
        titles = new TextView[]{
                findViewById(R.id.tv_title_cat1),
                findViewById(R.id.tv_title_cat2),
                findViewById(R.id.tv_title_cat3),
                findViewById(R.id.tv_title_cat4)};

        for(int i=0; i<num_cats; i++){
            cats[i].setProgress(cats_exp[i]);
        }

        for(int i=0; i<num_cats; i++){
            cats[i].setOnClickListener(new Tutorial.OnClickListener());
            titles[i].setOnClickListener(new Tutorial.OnClickListener());
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    //key value store
    public int loadData(String key, int default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPref.getInt(key, default_value);
    }

    //key value store
    public void saveData(String key, int data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, data);
        editor.apply();
        //Toast.makeText(getApplicationContext(), "saved _"+data+"_ under _"+key, Toast.LENGTH_SHORT).show();
    }

    public class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent1 = new Intent(view.getContext(), TutorialCategory.class);
            String cat_name = view.getResources().getResourceName(view.getId());
            int cat_id = Integer.parseInt(Character.toString(cat_name.charAt(cat_name.length()-1))); //1-4
            intent1.putExtra(CAT_ID, cat_id);
            startActivity(intent1);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            //swipeHint.setVisibility(View.VISIBLE);
        } else {
            Intent i = new Intent(getApplicationContext(), PopUp.class);
            i.putExtra(PopUp.KEY, PopUp.CLOSEAPP);
            startActivityForResult(i, PopUp.REQUEST_CODE);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        //swipeHint.setVisibility(View.GONE);
        return true;
    }*/

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
}
