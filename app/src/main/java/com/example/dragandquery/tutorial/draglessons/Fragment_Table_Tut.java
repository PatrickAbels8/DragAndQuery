package com.example.dragandquery.tutorial.draglessons;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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

import static com.example.dragandquery.block.BlockT.WHERE;

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

    private static final Map<String, String> map = new HashMap<String, String>(){{
        put("02_01", "select * from schüler");
        put("02_02", "select vorname, nachname from schüler ");
        put("02_03", "select vorname from schüler limit 10");
        put("02_04", "SELECT Vorname FROM Schüler WHERE Ort = \"Mainz\"");
        put("02_05", "SELECT Ort FROM Schüler WHERE Ort != \"Mainz\"");
        put("02_06", "SELECT Kürzel FROM Lehrkraft WHERE Fahrzeit < 30");
        put("02_07", "select kürzel FROM Lehrkraft WHERE Fahrzeit < 30 ORDER BY Fahrzeit ASC");
        put("02_08", "SELECT SchülerID FROM Schüler WHERE Ort = \"Budenheim\" OR Ort = \"Nackenheim\"");
        put("02_09", "SELECT Vorname FROM Lehrkraft WHERE Ort = \"Mainz\" AND Nachname = \"Meier\"");
        put("02_10", "SELECT SchülerID, Vorname, Nachname, Ort FROM Schüler WHERE NOT Ort = \"Mainz\"");
        put("02_11", "SELECT Raumnummer FROM Raum WHERE Beamer = 1");
        put("02_12", "SELECT Nachname,Ort FROM Lehrkraft WHERE Ort IN (\"Mommenheim\",\"Lörzweiler\", \"Harxheim\", \"Gau-Bischofsheim\")");
        put("02_13", "SELECT SchülerID FROM Schüler WHERE SchülerID LIKE \"_6%0\"");
        put("02_14", "SELECT DISTINCT Ort FROM Lehrkraft");
        put("02_15", "select Kürzel, Fahrzeit/60*50 FROM Lehrkraft");
        put("02_16", "SELECT Kürzel, Fahrzeit/60*50 AS Strecke FROM Lehrkraft ORDER BY Strecke ASC");
        put("03_02", "select count(kürzel) from lehrkraft");
        put("03_03", "select lehrkraftid, ifnull(fahrzeit, \"-1\") from lehrkraft WHERE Fahrzeit < 10");
        put("03_04", "select sum(fahrzeit*2) from lehrkraft");
        put("03_05", "select ort, count(schülerid) as Anzahl from schüler group by ort");
        put("03_06", "select avg(plätze) from raum");
        put("03_07", "select ort, min(fahrzeit), nachname from lehrkraft where ort in (\"Ingelheim\", \"Engelstadt\", \"Oppenheim\" group by ort");
        put("03_08", "select count(schülerid), klassenid from schüler group by klassenid having count(schülerid) > 28");
        put("04_02", "select nachname, name from klasse inner join schüler on schüler.klassenid = klasse.klassenid");
        put("04_03", "select name, plätze from raum join klasse on klasse.raumnummer = raum.raumnummer where (plätze>20) order by plätze asc");
        put("04_04", "select raum, raumnummer, klasse, name from raum left outer join klasse on raum.raumnummer = klasse.klassenraumnummer");
        put("04_05", "");
        put("04_06", "Select Klasse.Name, Klasse.KlassenID, Lehrkraft.Kürzel From Klasse FULL OUTER JOIN Lehrkraft ON Klasse.KlassenlehrerID=Lehrkraft.LehrkraftID");
        put("04_07", "select Vorname, Nachname from Klasse join Lehrkrafton Klasse.KlassenlehrerID = Lehrkraft.LehrkraftID where (KlassenID = 33)");
        put("04_08", "select Nachname, Name, RaumNummer from Schüler join Klasse on Schüler.KlassenID = Klasse.KlassenID join Raum on Klasse.Klassenraumnummer = Raum.Raumnummer");
        put("04_09", "select Fach.Fachname, Kürzel, Fachleiterkürzel, Stunden from Lehrkraft join Lehren on Lehrkraft.LehrkraftID = Lehren.LehrkraftID Join Fach on Lehren.FachID = Fach.FachID where (Stunden >= 10) order by Stunden asc");
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
        btn_back.setOnClickListener(view -> listener.onBack());
        btn_forth.setOnClickListener(view -> listener.onForth());


        return v;
    }

    public void goInvisible(){
        cl_table.startAnimation(tobottom);
        cl_table.setVisibility(View.INVISIBLE);
    }

    public void goVisible(String query, List<String[]> response, float runtime, boolean isCorrect){
        cl_table.setVisibility(View.VISIBLE);
        fillTable(response);
        cl_table.startAnimation(frombottom);
        //raw_query.setText(query.concat("\n Laufzeit: ").concat(Float.toString(runtime)).concat(" sec."));
        raw_query.setText("Laufzeit: ".concat(Float.toString(runtime)).concat(" sec."));
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
            entry.setPadding(dp_to_int(4), 0, dp_to_int(4), 0);
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
        return (int) (dp*scale+0.5f);
    }
}
