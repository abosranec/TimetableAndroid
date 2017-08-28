package com.example.pasha.timetableandroid.main;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.example.pasha.timetableandroid.MainActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static android.content.Context.MODE_PRIVATE;

public class Stations extends TreeMap<String, String> {

    private MainActivity context;
    private AutoCompleteTextView start;
    private ArrayAdapter arrayAdapterStart;
    private AutoCompleteTextView end;
    private ArrayAdapter arrayAdapterEnd;

    public Stations(MainActivity context) {
        super();
        this.context = context;
        this.start = context.getStart();
        this.arrayAdapterStart = context.getArrayAdapterStart();
        this.end = context.getEnd();
        this.arrayAdapterEnd = context.getArrayAdapterEnd();
        loadStations();
    }

    private void loadStations(){
        TreeMap<String, String> nb = new TreeMap<>();
        try {
            SharedPreferences sPref = context.getSharedPreferences("stations", MODE_PRIVATE);
            nb.putAll((Map<String,String>) sPref.getAll());
            if (nb.size() > 0){
                clear();
                putAll(nb);
                arrayAdapterStart.clear();
                arrayAdapterStart.addAll(nb.keySet().toArray());
                start.setAdapter(arrayAdapterStart);
                arrayAdapterEnd.clear();
                arrayAdapterEnd.addAll(nb.keySet().toArray());
                end.setAdapter(arrayAdapterEnd);
            }
            else {
                reWrite();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reWrite(){
        new ReWrite().execute();
    }

    class ReWrite extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            TreeMap<String, String> nb = new TreeMap<>();
            String html = "http://www.minsktrans.by/mg/suburb.php";
            try {
                Document doc = Jsoup.connect(html).get();
                Elements tableElements = doc.select("select[id=minsk0] option");
                for (int i = 0; i < tableElements.size() - 2; i++) {
                    nb.put(tableElements.get(i).text().toLowerCase(), tableElements.get(i).attr("value"));
                }
                nb.put(tableElements.get(tableElements.size() - 1).text().toLowerCase(),
                        tableElements.get(tableElements.size() - 1).attr("value"));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            if(nb.size() > 0){
                //write in application
                clear();
                putAll(nb);
                //write for choice stations
                arrayAdapterStart.clear();
                arrayAdapterStart.addAll(nb.keySet().toArray());
                start.setAdapter(arrayAdapterStart);
                arrayAdapterEnd.clear();
                arrayAdapterEnd.addAll(nb.keySet().toArray());
                end.setAdapter(arrayAdapterEnd);
                //write in memory
                SharedPreferences sPref = context.getSharedPreferences("stations", MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.clear();
                //ed.commit();
                for (String key: nb.keySet()) {
                    ed.putString(key, nb.get(key));
                }
                ed.apply();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
