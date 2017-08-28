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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RouteAdapter extends BaseAdapter{

    private List<Route> list = new ArrayList<>();
    private List<Route> listAll = new ArrayList<>();
    private Stations stations;
    private String routeStart = "";
    private String routeEnd = "";
    private LayoutInflater inflater;
    private ListView resultView;
    private ToggleButton ButtonWeekdays;
    private ToggleButton ButtonHoliday;
    private TextView textCurrentRoute;

    public RouteAdapter(MainActivity context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        stations = new Stations(context);
        this.resultView = context.getResultView();
        this.ButtonWeekdays = context.getButtonWeekdays();
        this.ButtonHoliday = context.getButtonHoliday();
        this.textCurrentRoute = context.getTextCurrentRoute();
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
        new BuildRoute().execute();
    }

    class BuildRoute extends AsyncTask<Void, Void, Void> {

        private boolean resultBuild;
        @Override
        protected Void doInBackground(Void... params) {
            List<Route> listRoute = new ArrayList<>();
            String html = getHtmlForRoute();

            //Toast.makeText(context.getApplicationContext(),"Ошибка доступа в интернет!", Toast.LENGTH_SHORT).show();
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
                            "***",
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
            resultBuild = true;
            choiceDays();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
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
        if(c.get(Calendar.DAY_OF_WEEK) == 1 || c.get(Calendar.DAY_OF_WEEK) == 7 ){
            for (Route route: listAll) {
                if (route.getDays().equals("вых")){
                    list.add(route);
                }
            }
        }
        else{
            for (Route route: listAll) {
                if (route.getDays().equals("буд")){
                    list.add(route);
                }
            }
        }
    }
    public void setHolidays(){
        list.clear();
        for (Route route: listAll) {
            if (route.getDays().equals("вых")){
                    list.add(route);
            }
        }
        resultView.setAdapter(this);
    }
    public void setWeekdays(){
        list.clear();
        for (Route route: listAll) {
            if (route.getDays().equals("буд")){
                list.add(route);
            }
        }
        resultView.setAdapter(this);
    }
}
