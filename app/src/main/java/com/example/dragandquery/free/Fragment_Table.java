package com.example.dragandquery.free;

import android.content.ClipData;
import android.content.Context;
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
 * TODO
 * - show code button
 * - code to tableview
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

    public void goVisible(String query, String response){
        cl_table.setVisibility(View.VISIBLE);
        fillTable(response);
        cl_table.startAnimation(frombottom);
        raw_query.setText(response);

    }

    public void fillTable(String response){
        /*removeRows();
        addHeadRow();
        List<String[]> rows = response;
        for(int i=0; i<rows.size(); i++)
            addRow(rows.get(i));*/
    }

    public void removeRows(){
        table.removeAllViews();
    }

    public void addHeadRow(){

    }

    public void addRow(String[] row){
        TableRow newRow = new TableRow(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        newRow.setLayoutParams(lp);

        for(int i=0; i<row.length; i++){
            TextView entry = new TextView(context);
            entry.setText(row[i]);
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
}
