package com.example.pasha.timetableandroid.saved;

import com.example.pasha.timetableandroid.main.Route;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Saved {
    private List<Route> listAll = new ArrayList<>();
    private String path = "";
    private String routeStart = "";
    private String routeEnd = "";

    public Saved(List<Route> listAll, String routeStart, String routeEnd, String path) {
        this.listAll.addAll(listAll);
        this.routeStart = routeStart;
        this.routeEnd = routeEnd;
        this.path = path;
    }

    public String getRoute(){
        return routeStart + " -> " + routeEnd;
    }
    public String getTimes(){
        StringBuilder times = new StringBuilder();
        int i = 0;
        for (Route route: listAll) {
            String startTime = route.getStart();
            String[] time = startTime.split(":");
            int hour = 0;
            int minute = 0;
            if (time.length > 1){
                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
            }
            else{
                continue;
            }
            Calendar c = Calendar.getInstance();
            String day = "";
            if(c.get(Calendar.DAY_OF_WEEK) == 1 || c.get(Calendar.DAY_OF_WEEK) == 7 )
                day = "вых";
            else
                day = "буд";

            if (day.equals(route.getDays())) {
                if (hour >= c.get(Calendar.HOUR_OF_DAY)) {
                    times.append(startTime);
                    if (i++ > 3) {
                        break;
                    }
                    times.append("; ");
                }
            }
        }
        return times.toString();
    }

    public String getPath() {
        return path;
    }
}
