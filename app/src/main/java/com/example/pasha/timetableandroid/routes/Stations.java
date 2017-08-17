package com.example.pasha.timetableandroid.routes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static android.content.Context.MODE_PRIVATE;

public class Stations extends TreeMap<String, String> {

    private Context context;

    public Stations(Context context) {
        super();
        this.context = context;
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
                clear();
                putAll(nb);
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
