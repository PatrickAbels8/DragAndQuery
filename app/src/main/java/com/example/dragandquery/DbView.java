package com.example.dragandquery;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.TextView;

import com.example.dragandquery.practice.Complexity;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.navigation.NavigationView;


/***
 */

public class DbView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    private PhotoView db_view;
    private TextView db_title_school;
    private TextView db_title_cafeteria;
    private TextView db_title_legend;

    public static final int DB_SCHOOL_ER = R.drawable.er_school_colourful_background;
    public static final int DB_CAFETERIA_ER = R.drawable.er_cafeteria_colourfull_background;
    public static final int DB_LEGEND = R.drawable.legende_er_bunt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        ViewStub stub = findViewById(R.id.stub);
        stub.setLayoutResource(R.layout.activity_db_view);
        stub.inflate();

        db_view = findViewById(R.id.db_view);
        db_title_school = findViewById(R.id.db_title_school);
        db_title_cafeteria = findViewById(R.id.db_title_cafetaria);
        db_title_legend = findViewById(R.id.db_title_legend);


        db_title_school.setOnClickListener(view -> {
            db_title_school.setBackground(getResources().getDrawable(R.drawable.border_white));
            db_title_cafeteria.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_title_legend.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_view.setImageResource(DB_SCHOOL_ER);
        });

        db_title_cafeteria.setOnClickListener(view -> {
            db_title_school.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_title_cafeteria.setBackground(getResources().getDrawable(R.drawable.border_white));
            db_title_legend.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_view.setImageResource(DbView.DB_CAFETERIA_ER);
        });

        db_title_legend.setOnClickListener(view -> {
            db_title_school.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_title_cafeteria.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_title_legend.setBackground(getResources().getDrawable(R.drawable.border_white));
            db_view.setImageResource(DbView.DB_LEGEND);
        });

        // nav drawer
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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
            //swipeHint.setVisibility(View.VISIBLE);
        } else {
            Intent i = new Intent(getApplicationContext(), PopUp.class);
            i.putExtra(PopUp.KEY, PopUp.CLOSEAPP);
            startActivityForResult(i, PopUp.REQUEST_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        //swipeHint.setVisibility(View.GONE);
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
