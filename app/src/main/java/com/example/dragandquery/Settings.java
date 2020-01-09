package com.example.dragandquery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * TODO
 * -global reset
 */
public class Settings extends AppCompatActivity {

    //vars
    public static String UNAME = "com.example.dragandquery.Settings.UNAME";
    public static String UMAIL = "com.example.dragandquery.Settings.UMAIL";
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    //coms
    private TextView save;
    private EditText name;
    private EditText mail;
    private ProgressBar pb_tut;
    private ProgressBar pb_prac;
    private TextView reset_tut;
    private TextView reset_prac;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //intent stuff
        Intent intent = getIntent();

        //components
        name = (EditText) findViewById(R.id.settings_name);
        mail = (EditText) findViewById(R.id.settings_mail);
        image = (ImageView) findViewById(R.id.settings_image);
        save = (TextView) findViewById(R.id.save_settings);
        pb_prac = (ProgressBar) findViewById(R.id.settings_pb_practise);
        pb_tut = (ProgressBar) findViewById(R.id.settings_pb_tutorial);
        reset_prac = (TextView) findViewById(R.id.tv_reset_practise);
        reset_tut = (TextView) findViewById(R.id.tv_reset_tutorial);

        //change profile image
        if(loadDataBoolean(getString(R.string.userImageBool_key), false)){
            image.setImageURI(Uri.parse(loadDataString(getString(R.string.userImage_key), "")));
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }else{
                        pickImageFromGallery();
                    }
                }else{
                    pickImageFromGallery();
                }
            }
        });

        //lets go back to navigation
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                String userMail = mail.getText().toString();

                if(userMail.contains("@")){
                    Intent i = new Intent(getApplicationContext(), Navigation.class);
                    i.putExtra(UNAME, userName);
                    i.putExtra(UMAIL, userMail);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                } else{
                    Toast.makeText(getApplicationContext(), "Oops! Da hat wohl etwas noch nicht gestimmt!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //show bars
        pb_tut.setProgress(calcAvg(new int[]{
                loadDataInt(getString(R.string.tutScore1_key), 10),
                loadDataInt(getString(R.string.tutScore2_key), 20),
                loadDataInt(getString(R.string.tutScore3_key), 30),
                loadDataInt(getString(R.string.tutScore4_key), 40)
        }));
        pb_prac.setProgress(Integer.parseInt(loadDataString(getString(R.string.pracScore_key), Integer.toString(0))));

        //global tut/prac reset //todo popup reask
        reset_tut.setOnClickListener(new Settings.OnTutResetClickListener());
        reset_prac.setOnClickListener(new Settings.OnPracResetClickListener());
    }

    public void pickImageFromGallery(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        switch(requestCode){
            case PERMISSION_CODE:{
                Log.d("###################### granresult length:", Integer.toString(grantResult[0]));
                if(grantResult.length>0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }else{
                    Toast.makeText(this, getString(R.string.toast_ImagePermissionDenied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            Uri img_uri = data.getData();
            image.setImageURI(img_uri);
            saveData(getString(R.string.userImage_key), img_uri.toString());
            saveDataBoolean(getString(R.string.userImageBool_key), true);
        }
    }

    public boolean loadDataBoolean(String key, boolean default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        boolean data = sharedPref.getBoolean(key, default_value);
        return data;
    }

    public void saveDataBoolean(String key, boolean data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, data);
        editor.apply();
        //Toast.makeText(getApplicationContext(), "saved _"+data+"_ under _"+key, Toast.LENGTH_LONG).show();
    }

    public void saveData(String key, String data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, data);
        editor.apply();
        //Toast.makeText(getApplicationContext(), "saved _"+data+"_ under _"+key, Toast.LENGTH_LONG).show();
    }

    public String loadDataString(String key, String default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String data = sharedPref.getString(key, default_value);
        return data;
    }

    public int loadDataInt(String key, int default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int data = sharedPref.getInt(key, default_value);
        return data;
    }

    public int calcAvg(int[] partialExps){
        int num_cats = partialExps.length;
        int sum = 0;
        for (int partialExp : partialExps) {
            sum += partialExp;
        }
        return sum/num_cats;
    }

    public class OnTutResetClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //todo
            reset_tut.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.vibrate_short));
        }
    }

    public class OnPracResetClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //todo
            reset_prac.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.vibrate_short));
        }
    }
}
