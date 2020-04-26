package com.example.dragandquery.tutorial.draglessons;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;
import com.example.dragandquery.db.DatabaseAccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/***
 * -
 */

public class Fragment_Table_Tut extends Fragment {

    //coms
    private TableLayout table;
    private ConstraintLayout cl_table;
    private Button btn_back;
    private Button btn_forth;
    private TextView raw_query;
    private Animation frombottom;
    private Animation tobottom;
    private Context context;

    //vars
    private Fragment_Table_Tut_Listener listener;
    private String lec_id;

    public static final Map<String, String> map = new HashMap<String, String>(){{ //todo
        put("01_09", "SELECT ...");
        put("01_10", "");
        put("01_11", "");
        put("02_01", "");
        put("02_02", "");
        put("02_03", "");
        put("02_04", "");
        put("02_05", "");
        put("02_06", "");
        put("02_07", "");
        put("02_08", "");
        put("02_09", "");
        put("02_10", "");
        put("02_11", "");
        put("02_12", "");
        put("02_13", "");
        put("02_14", "");
        put("02_15", "");
        put("03_02", "");
        put("03_03", "");
        put("03_04", "");
        put("03_05", "");
        put("03_06", "");
        put("03_07", "");
        put("03_08", "");
        put("04_02", "");
        put("04_03", "");
        put("04_04", "");
        put("04_05", "");
        put("04_06", "");
        put("04_07", "");
    }};

    //interface
    public interface Fragment_Table_Tut_Listener{
        void onBack();
        void onForth();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_table_tut, container, false);

        //init coms
        context = getContext();
        cl_table = v.findViewById(R.id.frag_table);
        btn_back = v.findViewById(R.id.btn_feedback_back);
        btn_forth = v.findViewById(R.id.btn_feedback_forth);
        raw_query = v.findViewById(R.id.raw_query);
        frombottom = AnimationUtils.loadAnimation(context, R.anim.frombottom);
        tobottom = AnimationUtils.loadAnimation(context, R.anim.tobottom);
        table = v.findViewById(R.id.tl_table);

        //load lection id
        lec_id = this.getArguments().getString(DragLesson.ID_KEY);

        //get back to edit or forth to next lec
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onBack();
            }
        });
        btn_forth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onForth();
            }
        });


        return v;
    }

    public void goInvisible(){
        cl_table.startAnimation(tobottom);
        cl_table.setVisibility(View.INVISIBLE);
    }

    //todo good or bad feedback
    public void goVisible(String query, List<String[]> response, float runtime, boolean isCorrect){
        cl_table.setVisibility(View.VISIBLE);
        fillTable(response);
        cl_table.startAnimation(frombottom);
        raw_query.setText(query.concat("\n Laufzeit: ").concat(Float.toString(runtime)).concat(" sec."));
        if(isCorrect)
            btn_forth.setVisibility(View.VISIBLE);
        else
            btn_forth.setVisibility(View.INVISIBLE);

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

    public boolean isCorrect(List<String[]> response){
        String correctQuery = map.get(lec_id.substring(0, 5));

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context, DatabaseAccess.DB_SCHOOL);
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
        if(context instanceof Fragment_Table_Tut_Listener){
            listener = (Fragment_Table_Tut_Listener) context;
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
