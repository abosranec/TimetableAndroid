package com.example.pasha.timetableandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.pasha.timetableandroid.main.Route;
import com.example.pasha.timetableandroid.saved.Saved;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class Updater extends AsyncTask<Void, Void, Void> {
    private Context context;
    private String resultStation = "init";
    private String resultSaved = "init";
    private static final String ERROR = "error";
    private static final String GOOD = "good";

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
            Toast.makeText(context.getApplicationContext(), "Ошибка обновления баз данных!", Toast.LENGTH_SHORT).show();
        }
        if (resultSaved.equals(Updater.GOOD)) {
            Toast.makeText(context.getApplicationContext(), "Сохранённые маршруты обновлены", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context.getApplicationContext(), "Ошибка обновления марштутов!", Toast.LENGTH_LONG).show();
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





        String[] strings = context.fileList();
        if (strings.length > 0) {
            for (String path: strings) {
                try {
                    String[] s = path.split("-");
                    String html = "http://www.minsktrans.by/mg/suburbt.php?find_runs=1&minsk=" + s[0] + "&other=" + s[1];
                    List<Route> listRoute = new ArrayList<>();
                    //download update route
                    try {
                        Document doc = Jsoup.connect(html).get();
                        Elements tableElements = doc.select("table[class=schedule_table]");
                        Elements tableRowElements = tableElements.select("tr");
                        for (int i = 2; i < tableRowElements.size(); i++) {
                            Element row = tableRowElements.get(i);
                            Elements rowItems = row.select("th");
                            if (rowItems.size() == 0)
                                rowItems = row.select("td");
                            //made routes
                            String days = "";
                            if (rowItems.get(3).text().equals("пн вт ср чт пт"))
                                days = "буд";
                            else
                                days = "вых";
                            listRoute.add(new Route(
                                    rowItems.get(0).text(),
                                    rowItems.get(3).text().toUpperCase(),
                                    rowItems.get(1).text(),
                                    rowItems.get(2).text(),
                                    days));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return Updater.ERROR;
                    }

                    //read and write route
                    if (listRoute.size() > 0){
                        try {
                            //read old route
                            BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput(path)));
                            String start = reader.readLine();
                            String end = reader.readLine();
                            reader.close();

                            //write new route
                            context.deleteFile(path);
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(path,Context.MODE_PRIVATE)));
                            writer.write(start + "\n");
                            writer.write(end + "\n");
                            writer.write(listRoute.size() + "\n");
                            for (Route route: listRoute) {
                                writer.write(route.getBusRoute() + "\n");
                                writer.write(route.getBusNumber() + "\n");
                                writer.write(route.getStart() + "\n");
                                writer.write(route.getEnd() + "\n");
                                writer.write(route.getDays() + "\n");
                            }
                            writer.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return Updater.ERROR;
                        }
                    }else{
                        return Updater.ERROR;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return Updater.ERROR;
                }
            }
        }else{
            return Updater.ERROR;
        }

        return Updater.GOOD;
    }
}
