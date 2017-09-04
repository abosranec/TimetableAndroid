package com.example.pasha.timetableandroid.saved;

import com.example.pasha.timetableandroid.main.Route;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Saved {
    private List<Route> listAll = new ArrayList<>();
    private String path = "";
    private String routeStart = "";
    private String routeEnd = "";
    private HashMap<String, Integer> dayOfWeek = new HashMap<>();

    public Saved(List<Route> listAll, String routeStart, String routeEnd, String path) {
        this.listAll.addAll(listAll);
        this.routeStart = routeStart;
        this.routeEnd = routeEnd;
        this.path = path;
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

    public String getRoute(){
        return routeStart + " -> " + routeEnd;
    }
    public String getTimes(){
        StringBuilder times = new StringBuilder();
        int i = 0;
        for (Route route: listAll) {
            //define day
            Calendar c = Calendar.getInstance();
            int currentDay = c.get(Calendar.DAY_OF_WEEK);
            boolean goodDay = false;
            String[] regular = route.getBusNumber().split(" ");
            for (String str: regular){
                int neededDay;
                try {
                    neededDay = dayOfWeek.get(str);
                }catch(Exception e){
                    str = "ежедн";
                    neededDay = dayOfWeek.get(str);
                }
                if (currentDay == neededDay || neededDay == 0){
                    goodDay = true;
                    break;
                }
            }
            if (!goodDay){
                continue;
            }

            //define time
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
            int hourThis = c.get(Calendar.HOUR_OF_DAY);
            int minuteThis = c.get(Calendar.MINUTE);
            if ((hour > hourThis) || (hour == hourThis && minute >= minuteThis)) {
                times.append(startTime);
                if (i++ > 8) {
                    break;
                }
                times.append("; ");
            }
        }
        return times.toString();
    }

    public String getPath() {
        return path;
    }
}
