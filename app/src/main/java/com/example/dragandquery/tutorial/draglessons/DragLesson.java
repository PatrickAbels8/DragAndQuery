package com.example.dragandquery.tutorial.draglessons;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.dragandquery.R;
import com.example.dragandquery.Tutorial;
import com.example.dragandquery.block.BlockT;
import com.example.dragandquery.tutorial.TutorialCategory;
import com.example.dragandquery.tutorial.TutorialCategoryLection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * TODO:
 * - cat 2-4 (keep hard coded stuff in mind)
 */

public class DragLesson extends AppCompatActivity implements Fragment_Table_Tut.Fragment_Table_Tut_Listener, Fragment_Query_Tut.Fragment_Query_Tut_Listener, Fragment_Blocks_Tut.Fragment_Blocks_Tut_Listener{

    //vars
    String lec_id;
    public static final String ARGS_KEY = "com.example.dragandquery.tutorial.draglessons.DragLesson.ARGS_KEY";
    public static final String ID_KEY = "com.example.dragandquery.tutorial.draglessons.DragLesson.ID_KEY";
    public static final Map<String, String[]> map = new HashMap<String, String[]>(){{
        put("01_09", new String[]{BlockT.STAR.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName()});
        put("01_10", new String[]{BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName()});
        put("01_11", new String[]{BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.LIMIT.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName()});
        put("02_01", new String[]{BlockT.GREATER.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("02_02", new String[]{BlockT.GREATER.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("02_03", new String[]{BlockT.GREATER.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("02_04", new String[]{BlockT.GREATER.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.ORDERBY.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("02_05", new String[]{BlockT.ATTRIBUTE.getName(), BlockT.BETWEEN.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("02_06", new String[]{BlockT.GREATER.getName(), BlockT.AND.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("02_07", new String[]{BlockT.GREATER.getName(), BlockT.AND.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("02_08", new String[]{BlockT.GREATER.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.NOT.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("02_09", new String[]{BlockT.GREATER.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("02_10", new String[]{BlockT.GREATER.getName(), BlockT.AND.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("02_11", new String[]{BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.IN.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("02_12", new String[]{BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.LIKE.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("02_13", new String[]{BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECTDISTINCT.getName(), BlockT.TABLE.getName()});
        put("02_14", new String[]{BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName()});
        put("02_15", new String[]{BlockT.AS.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.ORDERBY.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName()});
        put("03_02", new String[]{BlockT.ATTRIBUTE.getName(), BlockT.COUNT.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName()});
        put("03_03", new String[]{BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.ISNULL.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName()});
        put("03_04", new String[]{BlockT.ATTRIBUTE.getName(), BlockT.COUNT.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName()});
        put("03_05", new String[]{BlockT.AS.getName(), BlockT.ATTRIBUTE.getName(), BlockT.COUNT.getName(), BlockT.FROM.getName(), BlockT.GROUPBY.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName()});
        put("03_06", new String[]{BlockT.ATTRIBUTE.getName(), BlockT.COUNT.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName()});
        put("03_07", new String[]{BlockT.ATTRIBUTE.getName(), BlockT.COUNT.getName(), BlockT.FROM.getName(), BlockT.IN.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("03_08", new String[]{BlockT.GREATER.getName(), BlockT.ATTRIBUTE.getName(), BlockT.COUNT.getName(), BlockT.FROM.getName(), BlockT.GROUPBY.getName(), BlockT.HAVING.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName()});
        put("04_02", new String[]{BlockT.GREATER.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("04_03", new String[]{BlockT.GREATER.getName(), BlockT.AND.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.ORDERBY.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("04_04", new String[]{BlockT.GREATER.getName(), BlockT.AND.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("04_05", new String[]{BlockT.GREATER.getName(), BlockT.AND.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("04_06", new String[]{BlockT.GREATER.getName(), BlockT.AND.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.ORDERBY.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
        put("04_07", new String[]{BlockT.GREATER.getName(), BlockT.AND.getName(), BlockT.ATTRIBUTE.getName(), BlockT.FROM.getName(), BlockT.SELECT.getName(), BlockT.TABLE.getName(), BlockT.WHERE.getName()});
    }};

    //fragments
    Fragment_Query_Tut fragQuery;
    Fragment_Blocks_Tut fragBlocks;
    Fragment_Table_Tut fragTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_lesson);

        //choose content match ex id 0-(n-1)+100/200/300
        Intent intent = getIntent();
        if(intent.hasExtra(TutorialCategory.LECTION_ID)){
            lec_id = intent.getStringExtra(TutorialCategory.LECTION_ID);
        }

        fragQuery = new Fragment_Query_Tut();
        fragBlocks = new Fragment_Blocks_Tut();
        fragTable = new Fragment_Table_Tut();

        //notify blocks wich one to show
        Bundle blocks_args = new Bundle();
        blocks_args.putStringArrayList(ARGS_KEY, getBlockStrings());
        fragBlocks.setArguments(blocks_args);

        Bundle query_args = new Bundle();
        query_args.putString(ID_KEY, lec_id);
        fragQuery.setArguments(query_args);
        fragTable.setArguments(query_args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_query, fragQuery)
                .replace(R.id.container_blocks, fragBlocks)
                .replace(R.id.container_table, fragTable)
                .commit();


    }

    @Override
    public void onBlockDragged(View view, float x, float y) {
        fragQuery.createView(view, x, y);
    }

    @Override
    public void onGo(String query, String response, boolean isCorrect) {
        fragQuery.goInclickable();
        fragBlocks.goInvisible();
        if(isCorrect){
            setLectionDone();
        }
        fragTable.goVisible(query, response, isCorrect);
    }

    @Override
    public void onBack() {
        fragQuery.goClickable();
        fragBlocks.goVisible();
        fragTable.goInvisible();
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
            i2.putExtra(Tutorial.CAT_ID, Integer.parseInt(lec_id.substring(0, 2)));
            i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i2);
        }
    }

    // global hash map with 0 and 1, check row for lec id and add ones to list
    public ArrayList<String> getBlockStrings(){
        String[] blocks = map.get(lec_id.substring(0, 5));
        return new ArrayList(Arrays.asList(blocks));
    }

    public void setLectionDone(){
        String exp_key = "";
        String exp_unlocked_key = "";
        int numCat = Integer.parseInt(lec_id.substring(0, 2));
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

    //key value store
    public void saveData(String key, int data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, data);
        editor.apply();
    }

    //when 3 out of 5 lections are done, saved exp should be 61
    public int achievementsToExperience(){
        String[] counts = lec_id.split("_");
        int numDone = Integer.parseInt(counts[1]);
        int numTotal = Integer.parseInt(counts[2]);
        int exp = 100*numDone/numTotal +1;
        return exp;
    }

    //when 3 out of 5 lections are unlocked or done, saved unlocked exp should be 61
    public int unlockedachievementsToExperience(){
        String[] counts = lec_id.split("_");
        int numUnlocked = Integer.parseInt(counts[1])+1;
        int numTotal = Integer.parseInt(counts[2]);
        int exp = 100*numUnlocked/numTotal +1;
        return exp;
    }

    //check whether you are the last lection of your category
    public boolean hasNextLection(){
        return !(Integer.parseInt(lec_id.substring(3, 5)) == Integer.parseInt(lec_id.substring(6)));
    }

    public String getNextLectionID(){
        String[] coms = lec_id.split("_");
        String nextLectionID = coms[0]+"_";
        int newLec = Integer.parseInt(coms[1]) +1;
        nextLectionID +=  newLec>9? Integer.toString(newLec): "0"+Integer.toString(newLec);
        nextLectionID += "_"+coms[2];
        return nextLectionID;
    }

    //todo hard for every choice-->drag transition
    public boolean isDragLesson(String id){
        return id.substring(0, 5).equals("01_09") ||
                id.substring(0, 5).equals("01_10") ||
                id.substring(0, 5).equals("01_11") ||
                id.substring(0, 5).equals("02_01") ||
                id.substring(0, 5).equals("02_02") ||
                id.substring(0, 5).equals("02_03") ||
                id.substring(0, 5).equals("02_04") ||
                id.substring(0, 5).equals("02_05") ||
                id.substring(0, 5).equals("02_06") ||
                id.substring(0, 5).equals("02_07") ||
                id.substring(0, 5).equals("02_08") ||
                id.substring(0, 5).equals("02_09") ||
                id.substring(0, 5).equals("02_10") ||
                id.substring(0, 5).equals("02_11") ||
                id.substring(0, 5).equals("02_12") ||
                id.substring(0, 5).equals("02_13") ||
                id.substring(0, 5).equals("02_15") ||
                id.substring(0, 5).equals("03_02") ||
                id.substring(0, 5).equals("03_03") ||
                id.substring(0, 5).equals("03_04") ||
                id.substring(0, 5).equals("03_05") ||
                id.substring(0, 5).equals("03_06") ||
                id.substring(0, 5).equals("03_07") ||
                id.substring(0, 5).equals("03_08") ||
                id.substring(0, 5).equals("04_01") ||
                id.substring(0, 5).equals("04_02") ||
                id.substring(0, 5).equals("04_03") ||
                id.substring(0, 5).equals("04_04") ||
                id.substring(0, 5).equals("04_05") ||
                id.substring(0, 5).equals("04_06") ||
                id.substring(0, 5).equals("04_07") ||
                id.substring(0, 5).equals("04_08");
    }
}
