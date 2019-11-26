package com.example.dragandquery;

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

/***
 * TODO
 * -text visibile
 */

public class Fragment_Table extends Fragment {

    //coms
    RelativeLayout rl_table;
    ImageButton btn_retry;
    TextView msg;

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

    public void goVisible(){
        Toast.makeText(getContext(), "lets go visible", Toast.LENGTH_LONG).show();
        rl_table.setVisibility(View.VISIBLE);
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
