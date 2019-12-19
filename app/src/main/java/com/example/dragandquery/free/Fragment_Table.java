package com.example.dragandquery.free;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;

/***
 * TODO
 * - show code button
 * - code to tableview
 */

public class Fragment_Table extends Fragment {

    //coms
    RelativeLayout rl_table;
    ImageButton btn_retry;
    TextView msg;
    TextView raw_query;

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
        rl_table = v.findViewById(R.id.frag_table);
        btn_retry = v.findViewById(R.id.frag_retry);
        msg = v.findViewById(R.id.tv_table_msg);
        raw_query = v.findViewById(R.id.raw_query);

        //text msg
        msg.setVisibility(View.VISIBLE);

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
        rl_table.setVisibility(View.INVISIBLE);
    }

    public void goVisible(String query){
        rl_table.setVisibility(View.VISIBLE);
        raw_query.setText(query);

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
