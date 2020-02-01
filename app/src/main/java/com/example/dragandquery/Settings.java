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

import com.example.dragandquery.practice.Practices;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * TODO
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
        reset_tut = (TextView) findViewById(R.id.tv_reset_tutorial);

        //text hints should be the currentty saved data
        String cur_name = loadDataString(getString(R.string.userName_key), getString(R.string.default_name));
        String cur_mail = loadDataString(getString(R.string.userMail_key), getString(R.string.default_mail));
        name.setText(cur_name);
        mail.setText(cur_mail);

        //change profile image
        if(loadDataBoolean(getString(R.string.userImageBool_key), false)){
            image.setImageURI(Uri.parse(loadDataString(getString(R.string.userImage_key), "")));
        } else{
            image.setImageResource(R.drawable.profile_image);
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
        pb_prac.setProgress(calcExAvg());

        //global tut/prac reset //todo popup reask
        reset_tut.setOnClickListener(new Settings.OnTutResetClickListener());
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
            saveDataString(getString(R.string.userImage_key), img_uri.toString());
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
    }

    public void saveDataString(String key, String data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, data);
        editor.apply();
    }

    public void saveDataInt(String key, int data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, data);
        editor.apply();
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

    public int calcExAvg(){
        String e1 = loadDataString(getString(R.string.prac_easy_key), Practices.DEFAULT_EASY);
        String e2 = loadDataString(getString(R.string.prac_medium_key), Practices.DEFAULT_MEDIUM);
        String e3 = loadDataString(getString(R.string.prac_hard_key), Practices.DEFAULT_HARD);
        int num_exs = Practices.DEFAULT_EASY.length()+Practices.DEFAULT_MEDIUM.length()+Practices.DEFAULT_HARD.length();
        int num_dones = getNonZeros(e1)+getNonZeros(e2)+getNonZeros(e3);
        return num_dones==0? 1: 100*num_dones/num_exs;
    }

    public int getNonZeros(String s){
        int num_NonZeros = 0;
        for(int i=0; i<s.length(); i++)
            if(s.charAt(i)!='0')
                num_NonZeros++;
        return num_NonZeros;
    }


    //listeners
    public class OnTutResetClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            reset_tut.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.vibrate_short));
            //reset done lections
            saveDataInt(getString(R.string.tutScore1_key), 1);
            saveDataInt(getString(R.string.tutScore2_key), 1);
            saveDataInt(getString(R.string.tutScore3_key), 1);
            saveDataInt(getString(R.string.tutScore4_key), 1);
            //reset unlocked lections todo hard
            saveDataInt(getString(R.string.tutScore1_unlocked_key), (int)(100f/11f+1));
            saveDataInt(getString(R.string.tutScore2_unlocked_key), (int)(100f/15f+1));
            saveDataInt(getString(R.string.tutScore3_unlocked_key), (int)(100f/8f+1));
            saveDataInt(getString(R.string.tutScore4_unlocked_key), (int)(100f/7f+1));
            //reset done exercises
            saveDataString(getString(R.string.prac_easy_key), Practices.DEFAULT_EASY);
            saveDataString(getString(R.string.prac_medium_key), Practices.DEFAULT_MEDIUM);
            saveDataString(getString(R.string.prac_hard_key), Practices.DEFAULT_HARD);
        }
    }
}
