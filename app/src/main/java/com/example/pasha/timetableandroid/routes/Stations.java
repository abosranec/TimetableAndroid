package com.example.pasha.timetableandroid.routes;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.TreeMap;

public class Stations extends TreeMap<String, String> {
    //private Context context;
    public Stations() {
        super();
        reWrite();
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
            clear();
            putAll(nb);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
