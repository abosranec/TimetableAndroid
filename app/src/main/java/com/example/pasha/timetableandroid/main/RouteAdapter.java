package com.example.pasha.timetableandroid.main;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.pasha.timetableandroid.MainActivity;
import com.example.pasha.timetableandroid.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

public class RouteAdapter extends BaseAdapter{

    private List<Route> list = new ArrayList<>();
    private List<Route> listAll = new ArrayList<>();
    private Stations stations;
    private String routeStart = "";
    private String routeEnd = "";
    private LayoutInflater inflater;
    private ListView resultView;
    private TextView textCurrentRoute;
    private MainActivity context;
    private ProgressBar progressBar;
    private HashMap<String, Integer> dayOfWeek = new HashMap<>();

    public RouteAdapter(MainActivity context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        stations = new Stations(context);
        this.resultView = context.getResultView();
        this.textCurrentRoute = context.getTextCurrentRoute();
        this.context = context;
        this.progressBar = context.getProgressBar();

        //init dayOfWeek
        dayOfWeek.put("ЕЖЕДН", 0);
        dayOfWeek.put("ВС", 1);
        dayOfWeek.put("ПН", 2);
        dayOfWeek.put("ВТ", 3);
        dayOfWeek.put("СР", 4);
        dayOfWeek.put("ЧТ", 5);
        dayOfWeek.put("ПТ", 6);
        dayOfWeek.put("СБ", 7);
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int i) {
        return list.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewLocal = view;
        if (viewLocal == null) {
            viewLocal = inflater.inflate(R.layout.rezult_layout, viewGroup, false);
        }

        Route route = getRoute(i);

        TextView busRoute = viewLocal.findViewById(R.id.bus_route);
        TextView busNumber = viewLocal.findViewById(R.id.bus_number);
        TextView busStart = viewLocal.findViewById(R.id.bus_start);
        TextView busEnd = viewLocal.findViewById(R.id.bus_end);
        //TextView busDays = viewLocal.findViewById(R.id.bus_days);
        busRoute.setText(route.getBusRoute());
        busNumber.setText(route.getBusNumber());
        busStart.setText(route.getStart());
        busEnd.setText(route.getEnd());
        //busDays.setText(route.getDays());

        if (i % 2 == 0) {
            busRoute.setBackgroundColor(Color.argb(130, 220, 220, 220));
            busNumber.setBackgroundColor(Color.argb(130, 220, 220, 220));
            busStart.setBackgroundColor(Color.argb(130, 220, 220, 220));
            busEnd.setBackgroundColor(Color.argb(130, 220, 220, 220));
        } else{
            busRoute.setBackgroundColor(Color.argb(0, 255, 255, 255));
            busNumber.setBackgroundColor(Color.argb(0, 255, 255, 255));
            busStart.setBackgroundColor(Color.argb(0, 255, 255, 255));
            busEnd.setBackgroundColor(Color.argb(0, 255, 255, 255));
        }
        return viewLocal;
    }
    private Route getRoute(int position){
        return (Route)getItem(position);
    }

    //*********************************builds routes
    public void buildRoute(String routeStart, String routeEnd){
        this.routeStart = routeStart;
        this.routeEnd = routeEnd;
        listAll.clear();
        list.clear();
        resultView.setAdapter(RouteAdapter.this);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        new BuildRoute().execute();
    }

    class BuildRoute extends AsyncTask<Void, Void, Void> {

        private boolean resultBuild;
        @Override
        protected Void doInBackground(Void... params) {
            List<Route> listRoute = new ArrayList<>();
            String html = getHtmlForRoute();

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
                    String days;
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
            } catch (IOException e) {
                e.printStackTrace();
                list.clear();
                resultBuild = false;
                return null;
            }
            listAll.clear();
            listAll.addAll(listRoute);
            Collections.sort(listAll);
            resultBuild = true;
            choiceDays();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            resultView.setAdapter(RouteAdapter.this);
            buildCurrentRoute();
        }

        //build current route
        private void buildCurrentRoute(){
            if (resultBuild) {
                if (listAll.size() > 0) {
                    textCurrentRoute.setText(routeStart + " - " + routeEnd);
                    textCurrentRoute.setBackgroundColor(Color.rgb(48,63,159));
                }
                else{
                    textCurrentRoute.setText("Выбранный маршрут не существует!");
                    textCurrentRoute.setBackgroundColor(Color.rgb(0,180,0));
                }
            }
            else {
                textCurrentRoute.setText("Ошибка доступа в интернет!");
                textCurrentRoute.setBackgroundColor(Color.rgb(255,0,0));
            }
        }
    }

    //make get-request route start - end
    private String getHtmlForRoute(){
        return "http://www.minsktrans.by/mg/suburbt.php?find_runs=1&minsk=" +
                stations.get(routeStart) +
                "&other=" +
                stations.get(routeEnd);
    }

    //reWrite all data
    public void reWrite(){
        stations.reWrite();
    }




    //rebuild list with days of week
    private void choiceDays(){
        Calendar c = Calendar.getInstance();
        list.clear();
        for (Route route: listAll) {
            String[] regular = route.getBusNumber().split(" ");
            for (String str: regular){
                int currentDay = c.get(Calendar.DAY_OF_WEEK);
                int neededDay;
                try {
                    neededDay = dayOfWeek.get(str);
                }catch(Exception e){
                    str = "ежедн";
                    neededDay = dayOfWeek.get(str);
                }
                if (currentDay == neededDay || neededDay == 0){
                    list.add(route);
                    break;
                }
            }
        }
    }
    public void setDay(String day){
        list.clear();
        for (Route route: listAll) {
            String[] regular = route.getBusNumber().split(" ");
            for (String str: regular){
                int currentDay = dayOfWeek.get(day);
                int neededDay;
                try {
                    neededDay = dayOfWeek.get(str);
                }catch(Exception e){
                    str = "ежедн";
                    neededDay = dayOfWeek.get(str);
                }
                if (currentDay == neededDay || neededDay == 0){
                    list.add(route);
                    break;
                }
            }
        }
        resultView.setAdapter(this);
    }




    //for save route
    public void saveRoute(Context context) {
        if (listAll.size() > 0){
            String path = stations.get(routeStart) + "-" + stations.get(routeEnd);
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(path,Context.MODE_PRIVATE)));
                writer.write(routeStart + "\n");
                writer.write(routeEnd + "\n");
                writer.write(listAll.size() + "\n");
                for (Route route: listAll) {
                    writer.write(route.getBusRoute() + "\n");
                    writer.write(route.getBusNumber() + "\n");
                    writer.write(route.getStart() + "\n");
                    writer.write(route.getEnd() + "\n");
                    writer.write(route.getDays() + "\n");
                }
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context.getApplicationContext(),"Ошибка сохранения маршрута!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(context.getApplicationContext(),"Маршрут сохранен", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context.getApplicationContext(),"Неправильный маршрут!", Toast.LENGTH_SHORT).show();
        }
    }
}
