package com.example.dragandquery.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.dragandquery.R;
import com.example.dragandquery.Tutorial;
import com.example.dragandquery.tutorial.draglessons.DragLesson;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0100;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0101;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0102;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0103;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0104;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0105;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0106;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0107;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0108;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0109;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0110;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0111;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0301;
import com.example.dragandquery.tutorial.lections.Fragment_LectionContent_0401;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * TODO
 * -
 */

//for choice lessons only
public class TutorialCategoryLection
        extends AppCompatActivity
        implements
            Fragment_Feedback.Fragment_Feedback_Listener,
            Fragment_Input.Fragment_Input_Listener,
            Fragment_LectionContent_0108.Fragment_LectionContent_0100_Listener,
            Fragment_LectionContent_0101.Fragment_LectionContent_0101_Listener,
            Fragment_LectionContent_0102.Fragment_LectionContent_0102_Listener,
            Fragment_LectionContent_0103.Fragment_LectionContent_0103_Listener,
            Fragment_LectionContent_0104.Fragment_LectionContent_0104_Listener,
            Fragment_LectionContent_0105.Fragment_LectionContent_0105_Listener,
            Fragment_LectionContent_0106.Fragment_LectionContent_0106_Listener,
            Fragment_LectionContent_0107.Fragment_LectionContent_0107_Listener,
            Fragment_LectionContent_0108.Fragment_LectionContent_0108_Listener,
            Fragment_LectionContent_0108.Fragment_LectionContent_0109_Listener,
            Fragment_LectionContent_0108.Fragment_LectionContent_0110_Listener,
            Fragment_LectionContent_0108.Fragment_LectionContent_0111_Listener,
            Fragment_LectionContent_0301.Fragment_LectionContent_0301_Listener,
            Fragment_LectionContent_0401.Fragment_LectionContent_0401_Listener
{

    //fragments
    Fragment_Content curFrag;
    Fragment_Feedback fragFeedback;
    Fragment_Input fragInput;
    Fragment_Content[] frags;

    //vars
    private String lection_id; //of type "01_03_05" when its the 3rd out of 5 lections in cat 01
    private int numCat;
    private int numLec;
    private int numLecs;
    public static final String LEC_KEY = "com.example.dragandquery.tutorial.TutorialCategoryLection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_category_lection);

        //init coms and vars
        fragFeedback = new Fragment_Feedback();
        fragInput = new Fragment_Input();
        frags = new Fragment_Content[]{ //todo add lections here
                new Fragment_LectionContent_0100(),
                new Fragment_LectionContent_0101(),
                new Fragment_LectionContent_0102(),
                new Fragment_LectionContent_0103(),
                new Fragment_LectionContent_0104(),
                new Fragment_LectionContent_0105(),
                new Fragment_LectionContent_0106(),
                new Fragment_LectionContent_0107(),
                new Fragment_LectionContent_0108(),
                new Fragment_LectionContent_0109(),
                new Fragment_LectionContent_0110(),
                new Fragment_LectionContent_0111(),
                new Fragment_LectionContent_0301(),
                new Fragment_LectionContent_0401()
        };

        //intent stuff
        Intent i = getIntent();
        if(i.hasExtra(TutorialCategory.LECTION_ID)){
            lection_id = i.getStringExtra(TutorialCategory.LECTION_ID);
            String[] components = lection_id.split("_");
            numCat = Integer.parseInt(components[0]);
            numLec = Integer.parseInt(components[1]);
            numLecs = Integer.parseInt(components[2]);
        }

        //checkout current lection todo out of bounds
        curFrag = frags[getFragIndex(numCat, numLec)];

        //send right text to input frag
        Bundle lec = new Bundle();
        lec.putString(LEC_KEY, lection_id);
        fragInput.setArguments(lec);

        //open current lection
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_lectionFeedback, fragFeedback)
                .replace(R.id.container_lectionContent, curFrag)
                .replace(R.id.container_lectionInput, fragInput)
                .commit();
    }

    @Override
    public void onBack() {
        fragFeedback.goInvisible();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragInput.goClickable();
                fragInput.showBird();
            }
        }, Fragment_Feedback.TORIGHT_DURATION);

        curFrag.startExercise();
    }

    @Override
    public void onForth() {
        //go forth to next lection if there is one, if not back to cat overview
        if(hasNextLection()){
            String next_lec = getNextLectionID();
            if(!isDragLesson(next_lec)){
                Intent i = new Intent(getApplicationContext(), TutorialCategoryLection.class);
                i.putExtra(TutorialCategory.LECTION_ID, next_lec);
                startActivity(i);
            }else{
                Intent i = new Intent(getApplicationContext(), DragLesson.class);
                i.putExtra(TutorialCategory.LECTION_ID, next_lec);
                startActivity(i);
            }

        }else{
            Intent i2 = new Intent(getApplicationContext(), TutorialCategory.class);
            i2.putExtra(Tutorial.CAT_ID, numCat);
            i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i2);
        }
    }

    @Override
    public void onGo(boolean isCorrect) {
        fragFeedback.goVisible(isCorrect, lection_id);
        fragInput.goInclickable();
        fragInput.hideBird();

        if(isCorrect){
            setLectionDone();
        }

        curFrag.pauseExercise();
    }

    @Override
    public void onAccept() {
        curFrag.startExercise();
        fragInput.goInvisible();
    }

    @Override
    public void onBird(boolean wasOpen){
        if(wasOpen){
            curFrag.startExercise();
            fragInput.goInvisible();
        }else{
            curFrag.pauseExercise();
            fragInput.goVisible();
        }

    }

    public void setLectionDone(){
        String exp_key = "";
        String exp_unlocked_key = "";
        if (numCat == 1) {
            exp_key += getString(R.string.tutScore1_key);
            exp_unlocked_key += getString(R.string.tutScore1_unlocked_key);
        } else if (numCat == 2) {
            exp_key += getString(R.string.tutScore2_key);
            exp_unlocked_key += getString(R.string.tutScore2_unlocked_key);
        } else if (numCat == 3) {
            exp_key += getString(R.string.tutScore3_key);
            exp_unlocked_key += getString(R.string.tutScore3_unlocked_key);
        } else if (numCat == 4) {
            exp_key += getString(R.string.tutScore4_key);
            exp_unlocked_key += getString(R.string.tutScore4_unlocked_key);
        }
        saveData(exp_key, achievementsToExperience());
        saveData(exp_unlocked_key, unlockedachievementsToExperience());
    }

    //when 3 out of 5 lections are done, saved exp should be 61
    public int achievementsToExperience(){
        String[] counts = lection_id.split("_");
        int numDone = Integer.parseInt(counts[1]);
        int numTotal = Integer.parseInt(counts[2]);
        int exp = 100*numDone/numTotal +1;
        return exp;
    }

    //when 3 out of 5 lections are unlocked or done, saved unlocked exp should be 61
    public int unlockedachievementsToExperience(){
        String[] counts = lection_id.split("_");
        int numUnlocked = Integer.parseInt(counts[1])+1;
        int numTotal = Integer.parseInt(counts[2]);
        int exp = 100*numUnlocked/numTotal +1;
        return exp;
    }


    //key value store
    public void saveData(String key, int data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, data);
        editor.apply();
    }

    //key value store
    public int loadData(String key, int default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int data = sharedPref.getInt(key, default_value);
        return data;
    }

    //check whether you are the last lection of your category
    public boolean hasNextLection(){
        return !(numLec == numLecs);
    }

    // hard for every choice-->drag transition
    public boolean isDragLesson(String id){
        return !(id.substring(0, 2).equals("01") ||
                id.substring(0, 5).equals("03_01") ||
                id.substring(0, 5).equals("04_01"));
    }

    public String getNextLectionID(){
        String[] coms = lection_id.split("_");
        String nextLectionID = coms[0]+"_";
        int newLec = numLec +1;
        nextLectionID +=  newLec>9? Integer.toString(newLec): "0"+Integer.toString(newLec);
        nextLectionID += "_"+coms[2];
        return nextLectionID;
    }

    // change when lections changed
    public int getFragIndex(int chapter, int lection){
        int idx = 0;

        idx += chapter>1? 12: 0;
        idx += chapter>2? 16: 0;
        idx += chapter>3? 8: 0;
        idx += lection-1;

        return idx;
    }
}
