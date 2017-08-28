package com.example.pasha.timetableandroid.main;

import java.io.IOException;
import java.util.Calendar;

public class MainClass {
    public static void main(String[] args) throws IOException {
//        String url = "http://www.minsktrans.by/mg/suburb.php";
//        URL obj = new URL(url);
//        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
//        connection.setRequestMethod("GET");
//        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//        //make out.html file
//        PrintWriter outFileHTML = new PrintWriter(new BufferedWriter(new FileWriter("out2.html")));
//        String inputLine;
//        while ((inputLine = in.readLine()) != null) {
//            outFileHTML.println(inputLine);
//        }
//        outFileHTML.close();
//        in.close();

//        ///////////////////////////////////////////////////////////////////////////////////////////////////

//        Stations nb = new Stations();
//        System.out.println(nb.toString());
        ///////////////////////////////////////////////////////////

//        String html = "http://www.minsktrans.by/mg/suburbt.php?find_runs=1&minsk=501113&other=501106";
//        try {
//            Document doc = Jsoup.connect(html).get();
//            Elements tableElements = doc.select("table[class=schedule_table]");
//            Elements tableRowElements = tableElements.select("tr");
//            for (int i = 2; i < tableRowElements.size(); i++) {
//                Element row = tableRowElements.get(i);
//                Elements rowItems = row.select("th");
//                if (rowItems.size() == 0)
//                    rowItems = row.select("td");
//                //for (int j = 0; j < rowItems.size() - 1; j++) {
//                    System.out.println(rowItems.get(0).text());
//                    System.out.println(rowItems.get(1).text());
//                    System.out.println(rowItems.get(2).text());
//                    System.out.println(rowItems.get(3).text());
//                //}
//                System.out.println();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ////////////////////////////////////////
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.HOUR);
        System.out.println(c.get(Calendar.HOUR_OF_DAY));
        System.out.println(c.get(Calendar.MINUTE));
        String string = "14:56";
        for (String str: string.split(":")) {
            System.out.println(str);
        }
    }
}
