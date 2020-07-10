package com.example.dragandquery.free;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;

import java.util.List;

/***
 * -
 */

public class Fragment_Table extends Fragment {

    //coms
    private TableLayout table;
    private ConstraintLayout cl_table;
    private Button btn_retry;
    private TextView raw_query;
    private Animation frombottom;
    private Animation tobottom;
    private Context context;

    //vars
    private Fragment_Table_Listener listener;

    //interface
    public interface Fragment_Table_Listener{
        void onRetry();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_table, container, false);

        //init coms
        context = getContext();
        cl_table = v.findViewById(R.id.frag_table);
        btn_retry = v.findViewById(R.id.frag_retry);
        raw_query = v.findViewById(R.id.raw_query);
        frombottom = AnimationUtils.loadAnimation(context, R.anim.frombottom);
        tobottom = AnimationUtils.loadAnimation(context, R.anim.tobottom);
        table = v.findViewById(R.id.tl_table);

        //get back to edit
        btn_retry.setOnClickListener(view -> listener.onRetry());

        return v;
    }

    public void goInvisible(){
        cl_table.startAnimation(tobottom);
        cl_table.setVisibility(View.GONE);
    }

    public void goVisible(String query, List<String[]> response, float runtime){
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
            entry.setPadding(dp_to_int(4), 0, dp_to_int(4), 0);
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
            entry.setBackground(getResources().getDrawable(R.drawable.border_notdotted));
            entry.setTextColor(getResources().getColor(R.color.textcolor_white));
            //entry.setTypeface(Typeface.createFromFile("font/comfortaa.ttf"));
            newRow.addView(entry);
        }

        table.addView(newRow);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Fragment_Table_Listener){
            listener = (Fragment_Table_Listener) context;
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
        return (int) (dp*scale+0.5f);
    }
}
