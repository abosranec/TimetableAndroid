package com.example.pasha.timetableandroid.routes;

public class Route {
    private String busRoute;
    private String busNumber;
    private String start;
    private String end;
    private String days;

    public Route(String busRoute, String busNumber, String start, String end, String days) {
        this.busRoute = busRoute;
        this.busNumber = busNumber;
        this.start = start;
        this.end = end;
        this.days = days;
    }

    public String getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(String busRoute) {
        this.busRoute = busRoute;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
