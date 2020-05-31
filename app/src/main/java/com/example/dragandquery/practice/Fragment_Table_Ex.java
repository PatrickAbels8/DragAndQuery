package com.example.dragandquery.practice;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;
import com.example.dragandquery.db.DatabaseAccess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/***
 *
 * - btn to exs
 */

public class Fragment_Table_Ex extends Fragment {

    //coms
    private TableLayout table;
    private ConstraintLayout cl_table;
    private Button btn_retry;
    private TextView raw_query;
    private Animation frombottom;
    private Animation tobottom;
    private Context context;

    //vars
    private Fragment_Table_Ex_Listener listener;

    public Map<String, String> map;

    //interface
    public interface Fragment_Table_Ex_Listener{
        void onRetry();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_table_ex, container, false);

        //init coms
        context = getContext();
        cl_table = v.findViewById(R.id.frag_table);
        btn_retry = v.findViewById(R.id.frag_retry);
        raw_query = v.findViewById(R.id.raw_query);
        frombottom = AnimationUtils.loadAnimation(context, R.anim.frombottom);
        tobottom = AnimationUtils.loadAnimation(context, R.anim.tobottom);
        table = v.findViewById(R.id.tl_table);

        map = new HashMap<String, String>(){{ //todo
            put("100", getString(R.string.ex_easy_1_sol));
            put("101", getString(R.string.ex_easy_2_sol));
            put("102", getString(R.string.ex_easy_3_sol));
            put("103", getString(R.string.ex_easy_4_sol));
            put("104", getString(R.string.ex_easy_5_sol));
            put("105", getString(R.string.ex_easy_6_sol));
            put("106", getString(R.string.ex_easy_7_sol));
            put("107", getString(R.string.ex_easy_8_sol));
            put("108", getString(R.string.ex_easy_9_sol));
            put("109", getString(R.string.ex_easy_10_sol));
            put("200", getString(R.string.ex_medium_1_sol));
            put("201", getString(R.string.ex_medium_2_sol));
            put("202", getString(R.string.ex_medium_3_sol));
            put("203", getString(R.string.ex_medium_4_sol));
            put("204", getString(R.string.ex_medium_5_sol));
            put("205", getString(R.string.ex_medium_6_sol));
            put("206", getString(R.string.ex_medium_7_sol));
            put("207", getString(R.string.ex_medium_8_sol));
            put("208", getString(R.string.ex_medium_9_sol));
            put("300", getString(R.string.ex_hard_1_sol));
            put("301", getString(R.string.ex_hard_2_sol));
            put("302", getString(R.string.ex_hard_3_sol));
            put("303", getString(R.string.ex_hard_4_sol));
            put("304", getString(R.string.ex_hard_5_sol));
            put("305", getString(R.string.ex_hard_6_sol));
            put("306", getString(R.string.ex_hard_7_sol));
            put("307", getString(R.string.ex_hard_8_sol));


        }};

        //get back to edit or forth to next lec
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRetry();
            }
        });

        return v;
    }

    public void goInvisible(){
        cl_table.startAnimation(tobottom);
        cl_table.setVisibility(View.GONE);
    }

    // todo good or bad ffedback
    public void goVisible(String query, List<String[]> response, float runtime, boolean isCorrect){
        cl_table.setVisibility(View.VISIBLE);
        fillTable(response);
        cl_table.startAnimation(frombottom);
        raw_query.setText(query.concat("\n Laufzeit: ").concat(Float.toString(runtime)).concat(" sec."));
    }

    public void fillTable(List<String[]> response){
        removeRows();
        addHeadRow(response.get(0));
        int rows = response.size()-1;
        if(rows>0)
            for(int i=0; i<rows; i++)
                addRow(response.get(i+1));
    }

    public void removeRows(){
        table.removeAllViews();
    }

    public void addHeadRow(String[] col_names){
        TableRow newRow = new TableRow(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        newRow.setLayoutParams(lp);
        newRow.setBackground(getResources().getDrawable(R.drawable.border_white_half));

        for(int i=0; i<col_names.length; i++){
            TextView entry = new TextView(context);
            entry.setText(col_names[i]);
            entry.setPadding(dp_to_int(2), 0, dp_to_int(2), 0);
            entry.setTextColor(getResources().getColor(R.color.textcolor_white));
            //entry.setTypeface(Typeface.createFromFile("font/comfortaa.ttf"));
            newRow.addView(entry);
        }

        table.addView(newRow);

    }

    public void addRow(String[] row){
        TableRow newRow = new TableRow(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        newRow.setLayoutParams(lp);

        for(int i=0; i<row.length; i++){
            TextView entry = new TextView(context);
            entry.setText(row[i]);
            entry.setPadding(dp_to_int(2), 0, dp_to_int(2), 0);
            entry.setTextColor(getResources().getColor(R.color.textcolor_white));
            //entry.setTypeface(Typeface.createFromFile("font/comfortaa.ttf"));
            newRow.addView(entry);
        }

        table.addView(newRow);
    }

    public boolean isCorrect(List<String[]> response, int ex_id){
        String correctQuery = map.get(Integer.toString(ex_id));

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context, DatabaseAccess.DB_CAFETARIA);
        databaseAccess.open();
        List<String[]> correctResponse = databaseAccess.query(correctQuery);
        databaseAccess.close();

        boolean num_rows = correctResponse.size() == response.size();
        boolean num_cols = correctResponse.get(0).length == response.get(0).length;

        if (num_cols && num_rows){
            for(int i=0; i<response.get(0).length; i++){
                int ran_row = (new Random().nextInt(response.size()-1))+1;
                String ran_cell = response.get(ran_row)[i];
                boolean is_in = false;
                for(int j=1; j<response.size(); j++){
                    if(Arrays.asList(correctResponse.get(j)).contains(ran_cell)) {
                        is_in = true;
                        break;
                    }
                }
                if(!is_in)
                    return false;
            }
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Fragment_Table_Ex_Listener){
            listener = (Fragment_Table_Ex_Listener) context;
        } else{
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //helper
    public int dp_to_int(int dp){
        float scale = getResources().getDisplayMetrics().density;
        int pix = (int) (dp*scale+0.5f);
        return pix;
    }
}
