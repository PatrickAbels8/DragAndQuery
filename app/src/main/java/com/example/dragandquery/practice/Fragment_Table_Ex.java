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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;

    //vars
    private Fragment_Table_Ex_Listener listener;

    public static final Map<String, String> map = new HashMap<String, String>(){{ //todo
        put("100", "SELECT ...");
        put("101", "");
        put("102", "");
        put("103", "");
        put("104", "");
        put("105", "");
        put("200", "SELECT ...");
        put("201", "");
        put("202", "");
        put("203", "");
        put("204", "");
        put("205", "");
        put("206", "");
        put("207", "");
        put("300", "SELECT ...");
        put("301", "");
        put("302", "");
        put("303", "");
        put("304", "");
        put("305", "");
        put("306", "");
        put("307", "");
        put("308", "");
        put("309", "");
        put("310", "");
        put("311", "");
    }};

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
        star1 = (ImageView) v.findViewById(R.id.star1);
        star2 = (ImageView) v.findViewById(R.id.star2);
        star3 = (ImageView) v.findViewById(R.id.star3);
        table = v.findViewById(R.id.tl_table);

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

    public void goVisible(String query, List<String[]> response, float runtime, int isCorrect){
        cl_table.setVisibility(View.VISIBLE);
        fillTable(response);
        cl_table.startAnimation(frombottom);
        raw_query.setText(query.concat("\n Laufzeit: ").concat(Float.toString(runtime)).concat(" sec."));

        if(isCorrect>0)
            star1.setImageResource(R.drawable.star_full);
        else
            star1.setImageResource(R.drawable.star_empty);
        if(isCorrect>1)
            star2.setImageResource(R.drawable.star_full);
        else
            star2.setImageResource(R.drawable.star_empty);
        if(isCorrect>2)
            star3.setImageResource(R.drawable.star_full);
        else
            star3.setImageResource(R.drawable.star_empty);
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

    public int isCorrect(List<String[]> response, int ex_id){
        String correctQuery = map.get(Integer.toString(ex_id));

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context, DatabaseAccess.DB_CAFETARIA);
        databaseAccess.open();
        List<String[]> correctResponse = databaseAccess.query(correctQuery);
        databaseAccess.close();

        boolean num_rows = correctResponse.size() == response.size();
        boolean num_cols = correctResponse.get(0).length == response.get(0).length;

        int stars = (num_cols && num_rows)? 3: 0; //todo 0-3 stars
        return stars;
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
