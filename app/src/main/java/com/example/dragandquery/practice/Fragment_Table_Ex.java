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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    //vars
    private Fragment_Table_Ex_Listener listener;

    public Map<String, String> map;

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
        table = v.findViewById(R.id.tl_table);

        map = new HashMap<String, String>(){{
            put("100", "SELECT Firma, Kontaktperson, Telefon FROM Lieferant");
            put("101", "SELECT Artikelname FROM Artikel WHERE KategorieNr = 5 LIMIT 15");
            put("102", "SELECT count(ArtikelName)FROM Artikel WHERE KategorieNr = 3 OR KategorieNr = 4 OR KategorieNr = 5");
            put("103","SELECT Vorname, Nachname FROM Personal WHERE Ort = \"Wiesbaden\" ");
            put("104", "SELECT avg(Anzahl) FROM Bestelldetails");
            put("105", "SELECT count(KontoID) FROM Kundenkonto");
            put("106", "SELECT KategorieName, Beschreibung FROM Kategorie WHERE KategorieNr NOT IN (3, 4, 5)");
            put("107", "SELECT ArtikelNr, Lagerbestand, LieferantenNr, Liefereinheit FROM Artikel WHERE ArtikelNr = 117 OR ArtikelNr = 119");
            put("108", "SELECT ArtikelNr, ArtikelName, Lagerbestand FROM Artikel WHERE LieferantenNr = 1");
            put("109", "SELECT Vorname, Nachname, Geburtstag FROM Personal");
            put("200", "SELECT count(BestellNr) FROM Bestelldetails WHERE Anzahl * Einzelpreis > 4");
            put("201", "SELECT Nachname, Vorname FROM Personal WHERE Ort = \"Mainz\" ORDER BY Nachname desc");
            put("202", "SELECT BestellNr, max(Anzahl * Einzelpreis) FROM Bestelldetails");
            put("203", "SELECT count(BestellNr) FROM Bestelldetails WHERE ArtikelNr IN (62, 88)GROUP BY ArtikelNr");
            put("204", "SELECT KategorieName, count(ArtikelNr) FROM Kategorie INNER JOIN Artikel ON Kategorie.KategorieNr = Artikel.KategorieNr GROUP BY Artikel.KategorieNr");
            put("205", "SELECT KategorieNr FROM Artikel GROUP BY Kategorienr HAVING avg(Einzelpreis) < 1");
            put("206", "SELECT sum(Lagerbestand), Artikel.LieferantenNr FROM Artikel GROUP BY LieferantenNr HAVING sum(lagerbestand) > 1500");
            put("207", "SELECT count(DISTINCT PersonalNr) FROM Bestellung");
            put("208", "SELECT avg(Anzahl) FROM Bestelldetails INNER JOIN Artikel ON ArtikelNr WHERE Artikel.LieferantenNr = 2");
            put("300", "SELECT Bestelldatum, count(BestellNr) FROM Bestellung WHERE Bestelldatum LIKE \"__.02.2018\" GROUP BY Bestelldatum");
            put("301", "SELECT PLZ, count(PLZ) FROM Personal GROUP BY PLZ");
            put("302", "SELECT sum(Einzelpreis * Anzahl) FROM Bestelldetails INNER JOIN Bestellung ON Bestelldetails.BestellNr = Bestellung.BestellNr INNER JOIN Kundenkonto ON Kundenkonto.KontoID = Bestellung.KontoID WHERE Kundenkonto.KontoID = 17");
            put("303", "SELECT count(Bestelldetails.BestellNr) FROM BestelldetailsINNER JOIN Artikel ON Artikel.ArtikelNr = Bestelldetails.ArtikelNr INNER JOIN Bestellung ON Bestelldetails.BestellNr = Bestellung.BestellNr WHERE Artikel.KategorieNr = 3 AND Bestellung.PersonalNr = 5");
            put("304", "SELECT count(distinct Bestelldetails.BestellNr) FROM Bestelldetails INNER JOIN Artikel ON Artikel.ArtikelNr = Bestelldetails.ArtikelNr WHERE Artikel.KategorieNr != 2");
            put("305", "SELECT Kundenkonto.KontoID, count(BestellNr) FROM Kundenkonto LEFT OUTER JOIN Bestellung ON Kundenkonto.KontoID = Bestellung.KontoID GROUP BY Kundenkonto.KontoID");
            put("306", "SELECT count(BestellNr) FROM Bestelldetails INNER JOIN Artikel ON Bestelldetails.ArtikelNr = Artikel.ArtikelNr WHERE Artikel.KategorieNr IN (1, 2) AND (Bestelldetails.Anzahl) > 1");
            put("307", "SELECT count(Bestellnr), PersonalNr FROM Bestellung WHERE Bestelldatum LIKE \"__.02.2018\" GROUP BY PersonalNr HAVING count(Bestellnr) > 5");


        }};

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

    public void goVisible(String query, List<String[]> response, float runtime, boolean isCorrect){
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
            entry.setPadding(dp_to_int(2), dp_to_int(2), dp_to_int(2), 0);
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

    public boolean isCorrect(List<String[]> response, int ex_id){
        String correctQuery = map.get(Integer.toString(ex_id));

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context, DatabaseAccess.DB_CAFETARIA);
        databaseAccess.open();

        List<String[]> correctResponse = databaseAccess.query(correctQuery);
        databaseAccess.close();

        boolean num_rows = correctResponse.size() == response.size();
        boolean num_cols = correctResponse.get(0).length == response.get(0).length;

        if (num_cols && num_rows){
            for(int i=0; i<response.get(0).length; i++){
                if(response.size()<=1)
                    return true;
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
