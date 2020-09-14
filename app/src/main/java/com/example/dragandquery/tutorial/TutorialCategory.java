package com.example.dragandquery.tutorial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.fonts.Font;
import android.os.Bundle;

import com.example.dragandquery.DbView;
import com.example.dragandquery.Free;
import com.example.dragandquery.Impressum;
import com.example.dragandquery.Navigation;
import com.example.dragandquery.PopUp;
import com.example.dragandquery.R;
import com.example.dragandquery.Settings;
import com.example.dragandquery.Tutorial;
import com.example.dragandquery.practice.Complexity;
import com.example.dragandquery.tutorial.draglessons.DragLesson;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * - do done lections againg without reset
 */
public class TutorialCategory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    //coms
    private LinearLayout lections;
    private List<Button> cat_lections;
    private List<ImageView> checksOrLocks;
    private Animation vibrate;

    //vars
    public static final String LECTION_ID = "com.example.dragandquery.tutorial.TutorialCategory.LECTION_ID";
    private int cat_id;
    private List<Integer> lections_achievement;
    private int cat_exp;
    private int cat_exp_unlocked;
    private Context context;
    private final static int LOCKED = -1;
    private final static int UNLOCKED = 0;
    private final static int DONE = 1;
    private String exp_key;
    private String exp_unlocked_key; //caution: done lections are also unlocked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        ViewStub stub = findViewById(R.id.stub);
        stub.setLayoutResource(R.layout.activity_tutorial_category);
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

        //intent stuff
        Intent intent = getIntent();

        //init coms&vars
        lections = findViewById(R.id.ll_tutorial_lections);
        cat_lections = new ArrayList<>();
        checksOrLocks = new ArrayList<>();
        lections_achievement = new ArrayList<>();
        context = lections.getContext();
        vibrate = AnimationUtils.loadAnimation(context, R.anim.vibrate);

        //show which category is currently open and open corresponding lections
        if(intent.hasExtra(Tutorial.CAT_ID)){
            cat_id = intent.getIntExtra(Tutorial.CAT_ID, 1); //1-4
            loadLections(cat_id);
        }

        //what if someone advanced their experience on some lection
        for(Button b: cat_lections){
            b.setOnClickListener(view -> {
                int lection_id = cat_lections.indexOf(view); //0-9
                int level_of_achievement = lections_achievement.get(lection_id);
                if(level_of_achievement == UNLOCKED){
                    startLection(lection_id);
                }else if(level_of_achievement == DONE){
                    showLectionDone(); //startLection(lection_id); contodo
                }else if(level_of_achievement == LOCKED){
                    showLectionLocked();//startLection(lection_id); contodo
                }
            });
        }

    }

    //start lection
    public void startLection(int lection_id){
        String lec = getLectionID(lection_id);
        if(lec.substring(0, 5).equals("01_01") ||
                lec.substring(0, 5).equals("01_02") ||
                lec.substring(0, 5).equals("01_03") ||
                lec.substring(0, 5).equals("01_04") ||
                lec.substring(0, 5).equals("01_05") ||
                lec.substring(0, 5).equals("01_06") ||
                lec.substring(0, 5).equals("01_07") ||
                lec.substring(0, 5).equals("01_08") ||
                lec.substring(0, 5).equals("01_09") ||
                lec.substring(0, 5).equals("01_10") ||
                lec.substring(0, 5).equals("01_11") ||
                lec.substring(0, 5).equals("01_12") ||
                lec.substring(0, 5).equals("03_01") ||
                lec.substring(0, 5).equals("04_01")){
            Intent i = new Intent(context, TutorialCategoryLection.class);
            i.putExtra(LECTION_ID, lec);
            startActivity(i);
        }else{
            Intent drag_i = new Intent(context, DragLesson.class);
            drag_i.putExtra(LECTION_ID, lec);
            startActivity(drag_i);
        }
    }

    //save new achievement in layout and in sharedPrefs
    public void setLectionUnlocked(int lection_id){
        lections_achievement.set(lection_id, UNLOCKED);
        saveData(exp_key, achievementsToExperience());
        saveData(exp_unlocked_key, unlockedachievementsToExperience());
        viewLectionUnlocked(lection_id);
    }

    //show user that lection has to be unlocked yet
    public void showLectionLocked(){
        Toast.makeText(context, getString(R.string.toast_notUnlockedYet), Toast.LENGTH_SHORT).show();
    }

    //show user that lection is already finished
    public void showLectionDone(){
        Toast.makeText(context, getString(R.string.toast_alreadyDone), Toast.LENGTH_SHORT).show();
    }

    //set locked agein
    public void setLectionLocked(int lection_id){
        lections_achievement.set(lection_id, LOCKED);
        saveData(exp_key, achievementsToExperience());
        saveData(exp_unlocked_key, unlockedachievementsToExperience());
        viewLectionLocked(lection_id);
    }

    //set drawable and stuff
    public void viewLectionDone(int lection_id){
        cat_lections.get(lection_id).setAlpha(1f);
        checksOrLocks.get(lection_id).setImageDrawable(getDrawable(R.drawable.check));
        cat_lections.get(lection_id).clearAnimation();
    }

    //set drawable and stuff
    public void viewLectionLocked(int lection_id){
        cat_lections.get(lection_id).setAlpha(0.5f);
        checksOrLocks.get(lection_id).setImageDrawable(getDrawable(R.drawable.lock));
    }

    //set drawable and stuff
    public void viewLectionUnlocked(int lection_id){
        cat_lections.get(lection_id).setAlpha(1f);
        checksOrLocks.get(lection_id).setImageDrawable(null);
        cat_lections.get(lection_id).startAnimation(vibrate);
    }

    //key value store
    public void saveData(String key, int data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, data);
        editor.apply();
        //Toast.makeText(getApplicationContext(), "saved _"+data+"_ under _"+key, Toast.LENGTH_SHORT).show();
    }

    //key value store
    public int loadData(String key, int default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int data = sharedPref.getInt(key, default_value);
        return data;
    }

    //open the right lections and stuff
    public void loadLections(int cat_id){
        switch(cat_id){
            case 1:
                setTitle(getString(R.string.tutorial_category1));
                addLection(getString(R.string.cat1_lec1));
                addLection(getString(R.string.cat1_lec2));
                addLection(getString(R.string.cat1_lec3));
                addLection(getString(R.string.cat1_lec4));
                addLection(getString(R.string.cat1_lec5));
                addLection(getString(R.string.cat1_lec6));
                addLection(getString(R.string.cat1_lec7));
                addLection(getString(R.string.cat1_lec8));
                addLection(getString(R.string.cat1_lec9));
                addLection(getString(R.string.cat1_lec10));
                addLection(getString(R.string.cat1_lec11));
                addLection(getString(R.string.cat1_lec12));

                exp_key = getString(R.string.tutScore1_key);
                exp_unlocked_key = getString(R.string.tutScore1_unlocked_key);
                break;
            case 2:
                setTitle(getString(R.string.tutorial_category2));
                addLection(getString(R.string.cat2_lec1));
                addLection(getString(R.string.cat2_lec2));
                addLection(getString(R.string.cat2_lec3));
                addLection(getString(R.string.cat2_lec4));
                addLection(getString(R.string.cat2_lec5));
                addLection(getString(R.string.cat2_lec6));
                addLection(getString(R.string.cat2_lec7));
                addLection(getString(R.string.cat2_lec8));
                addLection(getString(R.string.cat2_lec9));
                addLection(getString(R.string.cat2_lec10));
                addLection(getString(R.string.cat2_lec11));
                addLection(getString(R.string.cat2_lec12));
                addLection(getString(R.string.cat2_lec13));
                addLection(getString(R.string.cat2_lec14));
                addLection(getString(R.string.cat2_lec15));
                addLection(getString(R.string.cat2_lec16));

                exp_key = getString(R.string.tutScore2_key);
                exp_unlocked_key = getString(R.string.tutScore2_unlocked_key);
                break;
            case 3:
                setTitle(getString(R.string.tutorial_category3));
                addLection(getString(R.string.cat3_lec1));
                addLection(getString(R.string.cat3_lec2));
                addLection(getString(R.string.cat3_lec3));
                addLection(getString(R.string.cat3_lec4));
                addLection(getString(R.string.cat3_lec5));
                addLection(getString(R.string.cat3_lec6));
                addLection(getString(R.string.cat3_lec7));
                addLection(getString(R.string.cat3_lec8));

                exp_key = getString(R.string.tutScore3_key);
                exp_unlocked_key = getString(R.string.tutScore3_unlocked_key);
                break;
            case 4:
                setTitle(getString(R.string.tutorial_category4));
                addLection(getString(R.string.cat4_lec1));
                addLection(getString(R.string.cat4_lec2));
                addLection(getString(R.string.cat4_lec3));
                addLection(getString(R.string.cat4_lec4));
                addLection(getString(R.string.cat4_lec5));
                addLection(getString(R.string.cat4_lec6));
                addLection(getString(R.string.cat4_lec7));
                addLection(getString(R.string.cat4_lec8));
                addLection(getString(R.string.cat4_lec9));

                exp_key = getString(R.string.tutScore4_key);
                exp_unlocked_key = getString(R.string.tutScore4_unlocked_key);
                break;
        }

        cat_exp = loadData(exp_key, 1); //default 0 lections done yet
        cat_exp_unlocked = loadData(exp_unlocked_key, 100/lections_achievement.size()+1); //default 1 lection unlocked yet

        //every lection finished?
        if(cat_exp>=100){
            for(int i=0; i<lections_achievement.size(); i++){
                lections_achievement.set(i, DONE);
                viewLectionDone(i);
            }
            return;
        }

        //otherwise
        setAchsAndViews();

    }

    //set achievements and views
    public void setAchsAndViews(){
        for(int i=0; i<lections_achievement.size(); i++){
            lections_achievement.set(i, LOCKED);
            viewLectionLocked(i);
        }

        for(int i=0; i<unlockedexperienceToAchievements(); i++){
            lections_achievement.set(i, UNLOCKED);
            viewLectionUnlocked(i);
        }

        for(int i=0; i<experienceToAchievements(); i++){
            lections_achievement.set(i, DONE);
            viewLectionDone(i);
        }
    }

    //print achievements
    public String toStringAch(){
        String a = "";
        for (int i = 0; i < lections_achievement.size(); i++) {
            a += Integer.toString(lections_achievement.get(i));
        }
        return a;
    }

    //add a new lection to every list and layout
    public void addLection(String name){
        //create lection btn
        Button l = new Button(context);
        l.setText(name);
        l.setTextColor(getResources().getColor(R.color.berry));
        l.setBackground(getDrawable(R.drawable.btn_lection));
        l.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cat_lections.add(l);
        lections_achievement.add(LOCKED);

        //create hor ll
        LinearLayout l_layout = new LinearLayout(context);
        l_layout.setOrientation(LinearLayout.HORIZONTAL);
        l_layout.setGravity(Gravity.CENTER);

        //create check/lock img
        ImageView checkOrLock = new ImageView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMarginEnd(32);
        checksOrLocks.add(checkOrLock);

        //add img and btn to hor ll
        l_layout.addView(checkOrLock, lp);
        l_layout.addView(l, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //add hor ll to lections
        lections.addView(l_layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //add space to lections
        lections.addView(new Space(context), 300, 15);

    }

    //when 3 out of 5 lections are done, saved exp should be 61
    public int achievementsToExperience(){
        int numDone = 0;
        for(int a: lections_achievement){
            if(a == DONE){
                numDone++;
            }
        }
        int numTotal = lections_achievement.size();
        return 100*numDone/numTotal +1;
    }

    //when 3 out of 5 lections are unlocked or done, saved unlocked exp should be 61
    public int unlockedachievementsToExperience(){
        int numDone = 0;
        for(int a: lections_achievement){
            if(a == UNLOCKED || a == DONE){
                numDone++;
            }
        }
        int numTotal = lections_achievement.size();
        return 100*numDone/numTotal +1;
    }

    //when exp is 61 , 3 out of 5 lections should be done
    public int experienceToAchievements(){
        int numTotal = lections_achievement.size();
        return numTotal*cat_exp/100;
    }

    //when unlocked exp is 61 , 3 out of 5 lections should be unlocked
    public int unlockedexperienceToAchievements(){
        int numTotal = lections_achievement.size();
        return numTotal*cat_exp_unlocked/100;
    }

    //when in cat1 return lec 4 (index = 3) out of 5 as "01_04_05"
    public String getLectionID(int lection_id){
        int size = lections_achievement.size();

        String ID = "";
        if(cat_id>9){
            ID += Integer.toString(cat_id);
        }else{
            ID += "0"+Integer.toString(cat_id);
        }
        ID += "_";
        if(lection_id+1>9){
            ID += Integer.toString(lection_id+1);
        }else{
            ID += "0"+Integer.toString(lection_id+1);
        }
        ID += "_";
        if(size>9){
            ID += Integer.toString(size);
        }else{
            ID += "0"+Integer.toString(size);
        }

        return ID;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
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
