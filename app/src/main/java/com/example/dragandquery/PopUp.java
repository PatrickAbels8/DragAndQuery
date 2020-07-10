package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;

/***
 */
public class PopUp extends AppCompatActivity {

    //vars
    public final static int GLOBALRESET = 0;
    public final static int CLOSEAPP = 1;

    public final static String KEY = "com.example.dragandquery.PopUp.KEY";

    public final static int REQUEST_CODE = 100;

    private static final double width_factor = 0.7;
    private static final double height_factor = 0.6;

    //coms
    private TextView msg;
    private Button accepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        //set window size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*width_factor), (int) (height*height_factor));

        //init stuff
        msg = findViewById(R.id.popup_msg);
        accepted = findViewById(R.id.popup_accepted);

        //set text to show up
        Intent i = getIntent();
        int key = i.getIntExtra(KEY, -1);
        switch(key){
            case -1:
                msg.setText(getString(R.string.popup_default_msg));
                break;
            case GLOBALRESET:
                msg.setText(getString(R.string.popup_globalreset_msg));
                break;
            case CLOSEAPP:
                msg.setText(getString(R.string.popup_closeapp_msg));
                break;
        }

        //button logic
        accepted.setOnClickListener(view -> {
            setResult(RESULT_OK);
            onBackPressed();
        });


    }
}
