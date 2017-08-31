package com.example.pasha.timetableandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class Updater extends AsyncTask<Void, Void, Void> {
    private Context context;
    private String resultStation = "init";
    private String resultSaved = "init";
    public static final String ERROR = "error";
    public static final String GOOD = "good";

    public Updater(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        resultStation = updateStations();
        resultSaved = updateSaved();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (resultStation.equals(Updater.GOOD)) {
            Toast.makeText(context.getApplicationContext(), "Базы данных обновлены", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context.getApplicationContext(), "Ошибка обновления!", Toast.LENGTH_SHORT).show();
        }
    }

    //update stations
    private String updateStations(){
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
        } catch (Exception e) {
            e.printStackTrace();
            return Updater.ERROR;
        }
        if(nb.size() > 0){
            //write in memory
            SharedPreferences sPref = context.getSharedPreferences("stations", MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.clear();
            //ed.commit();
            for (String key: nb.keySet()) {
                ed.putString(key, nb.get(key));
            }
            ed.apply();
        }else {
            return Updater.ERROR;
        }
        return Updater.GOOD;
    }

    //update saved
    private String updateSaved() {
        return "";
    }
}
