package com.example.pasha.timetableandroid.main;

public class Route  implements Comparable<Route>{
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

    //for sorting
    @Override
    public int compareTo(Route route) {
        String[] time = route.getStart().split(":");
        int hour = 0;
        int minute = 0;
        if (time.length > 1){
            hour = Integer.parseInt(time[0]);
            minute = Integer.parseInt(time[1]);
        }
        String[] timeThis = this.getStart().split(":");
        int hourThis = 0;
        int minuteThis = 0;
        if (timeThis.length > 1){
            hourThis = Integer.parseInt(timeThis[0]);
            minuteThis = Integer.parseInt(timeThis[1]);
        }

        if (hour == hourThis && minute == minuteThis) {
            return 0;
        }
        if (hour > hourThis) {
            return -1;
        } else if (hour < hourThis){
            return 1;
        } else if ( minute > minuteThis){
            return -1;
        } else {
            return 1;
        }
    }
}
