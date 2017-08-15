package com.example.pasha.timetableandroid.routes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.TreeMap;

public class Stations extends TreeMap<String, String> {
    public Stations() {
        super();
        reWrite();
    }

    public void reWrite(){
        TreeMap<String, String> nb = new TreeMap<>();
        String html = "http://www.minsktrans.by/mg/suburb.php";
        try {
            Document doc = Jsoup.connect(html).get();
            Elements tableElements = doc.select("select[id=minsk0] option");
            for (int i = 0; i < tableElements.size() - 2; i++) {
                nb.put(tableElements.get(i).text(), tableElements.get(i).attr("value"));
            }
            nb.put(tableElements.get(tableElements.size() - 1).text(),
                    tableElements.get(tableElements.size() - 1).attr("value"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        clear();
        putAll(nb);
    }
}
