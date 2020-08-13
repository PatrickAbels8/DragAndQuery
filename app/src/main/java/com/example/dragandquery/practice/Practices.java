package com.example.dragandquery.practice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;

import com.example.dragandquery.DbView;
import com.example.dragandquery.Free;
import com.example.dragandquery.Impressum;
import com.example.dragandquery.Navigation;
import com.example.dragandquery.PopUp;
import com.example.dragandquery.R;
import com.example.dragandquery.Settings;
import com.example.dragandquery.Tutorial;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * - show ex depending on tut exp
 */

public class Practices extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    //coms
    Context context;
    LinearLayout layout;

    //vars
    private int complexity; //1 easy:10, 2 medium:9, 3 hard:8
    public static final String EX_ID = "com.example.dragandquery.practice.Practices.EX_ID";
    private List<Button> exercises;
    private String dones; //"010111001010"
    public static final String DEFAULT_EASY = "0000000000";
    public static final String DEFAULT_MEDIUM = "000000000";
    public static final String DEFAULT_HARD = "00000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        ViewStub stub = findViewById(R.id.stub);
        stub.setLayoutResource(R.layout.activity_practices);
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

        //init stuff
        context = getApplicationContext();
        layout = (LinearLayout) findViewById(R.id.ll_practices_lections);
        exercises = new ArrayList<>();

        //intent stuff
        Intent intent = getIntent();
        if(intent.hasExtra(Complexity.COMPLEXITY)){
            complexity = intent.getIntExtra(Complexity.COMPLEXITY, 1);
        }

        //choose exercises where matching lessons are already done + color match complexitiy + check already done exercises
        loadExercises();
        for(int i=0; i<exercises.size(); i++){
            exercises.get(i).setOnClickListener(new MyOnExerciseClickListener());
        }

    }

    public void loadExercises(){
        switch(complexity){
            case Complexity.EASY:
                setTitle(getString(R.string.title_prac_easy));
                dones = loadData(getString(R.string.prac_easy_key), DEFAULT_EASY);
                addExercise(0, getString(R.string.ex_easy_1));
                addExercise(1, getString(R.string.ex_easy_2));
                addExercise(2, getString(R.string.ex_easy_3));
                addExercise(3, getString(R.string.ex_easy_4));
                addExercise(4, getString(R.string.ex_easy_5));
                addExercise(5, getString(R.string.ex_easy_6));
                addExercise(6, getString(R.string.ex_easy_7));
                addExercise(7, getString(R.string.ex_easy_8));
                addExercise(8, getString(R.string.ex_easy_9));
                addExercise(9, getString(R.string.ex_easy_10));
                break;

            case Complexity.MEDIUM:
                setTitle(getString(R.string.title_prac_medium));
                dones = loadData(getString(R.string.prac_medium_key), DEFAULT_MEDIUM);
                addExercise(0, getString(R.string.ex_medium_1));
                addExercise(1, getString(R.string.ex_medium_2));
                addExercise(2, getString(R.string.ex_medium_3));
                addExercise(3, getString(R.string.ex_medium_4));
                addExercise(4, getString(R.string.ex_medium_5));
                addExercise(5, getString(R.string.ex_medium_6));
                addExercise(6, getString(R.string.ex_medium_7));
                addExercise(7, getString(R.string.ex_medium_8));
                addExercise(8, getString(R.string.ex_medium_9));
                break;

            case Complexity.HARD:
                setTitle(getString(R.string.title_prac_hard));
                dones = loadData(getString(R.string.prac_hard_key), DEFAULT_HARD);
                addExercise(0, getString(R.string.ex_hard_1));
                addExercise(1, getString(R.string.ex_hard_2));
                addExercise(2, getString(R.string.ex_hard_3));
                addExercise(3, getString(R.string.ex_hard_4));
                addExercise(4, getString(R.string.ex_hard_5));
                addExercise(5, getString(R.string.ex_hard_6));
                addExercise(6, getString(R.string.ex_hard_7));
                addExercise(7, getString(R.string.ex_hard_8));
                break;
        }
    }

    public int getColor(){
        switch(complexity){
            case Complexity.EASY:
                return R.color.easy;
            case Complexity.MEDIUM:
                return R.color.medium;
        }
        return R.color.hard;
    }

    //add a new exercise to every list and layout
    public void addExercise(int id, String name){
        int numStars = Character.getNumericValue(dones.charAt(id));

        //vertical ll
        LinearLayout ll_v = new LinearLayout(context);
        ll_v.setBackground(getDrawable(R.drawable.btn_lection));
        ll_v.setOrientation(LinearLayout.VERTICAL);

        //horizontal ll for stars
        LinearLayout ll_h = new LinearLayout(context);
        ll_h.setOrientation(LinearLayout.HORIZONTAL);
        ll_h.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
        ll_v.addView(ll_h, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        //3 stars
        ImageView star1 = new ImageView(context);
        if(numStars>0)
            star1.setImageResource(R.drawable.star_full);
        else
            star1.setImageResource(R.drawable.star_empty);

        ll_h.addView(star1);

        //button
        Button l = new Button(context);
        l.setText(name);
        l.setTextColor(getResources().getColor(getColor()));
        l.setTextSize(20);
        l.setBackgroundColor(Color.parseColor("#00000000"));
        l.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //Typeface face = Typeface.createFromAsset(getAssets(), "font/comfortaa.ttf");
        Typeface face = ResourcesCompat.getFont(context, R.font.comfortaa);
        l.setTypeface(face);

        ll_v.addView(l, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        exercises.add(l);

        //add btn and space to ll
        layout.addView(ll_v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(new Space(context), 300, 15);

    }

    //key value store
    public void saveData(String key, String data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, data);
        editor.apply();
    }

    //key value store
    public String loadData(String key,  String default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String data = sharedPref.getString(key, default_value);
        return data;
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

    /***
     * listeenrs
     */

    public class MyOnExerciseClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), Exercise.class);
            i.putExtra(EX_ID, complexity*100+exercises.indexOf(view)); //0-(n-1) + 100/200/300
            startActivity(i);
        }
    }
}
