package com.example.pasha.timetableandroid.saved;

import com.example.pasha.timetableandroid.main.Route;

import java.util.ArrayList;
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
        for (Route route: listAll) {
            times.append(route.getStart());
            times.append("; ");
        }
        return times.toString();
    }

    public String getPath() {
        return path;
    }
}
