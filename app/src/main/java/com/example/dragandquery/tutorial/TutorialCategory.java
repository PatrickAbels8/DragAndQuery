package com.example.dragandquery.tutorial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.dragandquery.R;
import com.example.dragandquery.Tutorial;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * TODO:
 * - cats 2-5
 */
public class TutorialCategory extends AppCompatActivity {

    //coms
    LinearLayout lections;
    List<Button> cat_lections;
    ImageButton reset;

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
        setContentView(R.layout.activity_tutorial_category);

        //intent stuff
        Intent intent = getIntent();

        //init coms&vars
        lections = (LinearLayout) findViewById(R.id.ll_tutorial_lections);
        cat_lections = new ArrayList<>();
        lections_achievement = new ArrayList<>();
        context = lections.getContext();
        reset = (ImageButton) findViewById(R.id.reset);

        //show which category is currently open and open corresponding lections
        if(intent.hasExtra(Tutorial.CAT_ID)){
            cat_id = intent.getIntExtra(Tutorial.CAT_ID, 1); //1-5
            loadLections(cat_id);
        }

        //what if someone advanced their experience on some lection
        for(Button b: cat_lections){
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int lection_id = cat_lections.indexOf(view); //0-9
                    int level_of_achievement = lections_achievement.get(lection_id);
                    if(level_of_achievement == UNLOCKED){
                        startLection(lection_id);
                    }else if(level_of_achievement == DONE){
                        showLectionDone();
                    }else if(level_of_achievement == LOCKED){
                        showLectionLocked();
                    }
                }
            });
        }

        //reset exps
        reset.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                resetLocks();
                return true;
            }
        });

        //toolbar stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //start lection
    public void startLection(int lection_id){
        Intent i = new Intent(context, TutorialCategoryLection.class);
        i.putExtra(LECTION_ID, getLectionID(lection_id));
        startActivity(i);
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
    }

    //set drawable and stuff
    public void viewLectionLocked(int lection_id){
        cat_lections.get(lection_id).setAlpha(0.2f);
    }

    //set drawable and stuff
    public void viewLectionUnlocked(int lection_id){
        cat_lections.get(lection_id).setAlpha(0.6f);
    }

    //key value store
    public void saveData(String key, int data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, data);
        editor.apply();
        Toast.makeText(getApplicationContext(), "saved _"+data+"_ under _"+key, Toast.LENGTH_SHORT).show();
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
                //todo make via menu/list
                addLection(getString(R.string.cat1_lec1));
                addLection(getString(R.string.cat1_lec2));
                addLection(getString(R.string.cat1_lec3));
                addLection("another");
                addLection("one more");
                addLection("take that");
                addLection("not enough");
                addLection("3 left");
                addLection("almost done");
                addLection("last one");

                exp_key = getString(R.string.tutScore1_key);
                exp_unlocked_key = getString(R.string.tutScore1_unlocked_key);
                cat_exp = loadData(exp_key, 1); //default 0 lections done yet
                cat_exp_unlocked = loadData(exp_unlocked_key, 100/lections_achievement.size()+1); //default 1 lection unlocked yet

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
                break;
            case 2:
                cat_exp = loadData(getString(R.string.tutScore2_key), 10);
                break;
            case 3:
                cat_exp = loadData(getString(R.string.tutScore3_key), 10);
                break;
            case 4:
                cat_exp = loadData(getString(R.string.tutScore4_key), 10);
                break;
            case 5:
                cat_exp = loadData(getString(R.string.tutScore5_key), 10);
                break;
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
        Button l = new Button(context);
        l.setText(name);
        cat_lections.add(l);
        lections_achievement.add(LOCKED);
        lections.addView(l);
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
        int exp = 100*numDone/numTotal +1;
        return exp;
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
        int exp = 100*numDone/numTotal +1;
        return exp;
    }

    //when exp is 61 , 3 out of 5 lections should be done
    public int experienceToAchievements(){
        int numTotal = lections_achievement.size();
        int ach = numTotal*cat_exp/100;
        return ach;
    }

    //when unlocked exp is 61 , 3 out of 5 lections should be unlocked
    public int unlockedexperienceToAchievements(){
        int numTotal = lections_achievement.size();
        int ach = numTotal*cat_exp_unlocked/100;
        return ach;
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

    //reset everything
    public void resetLocks(){
        setLectionUnlocked(0);
        for(int i=1; i<lections_achievement.size(); i++){
            setLectionLocked(i);
        }
    }
}
