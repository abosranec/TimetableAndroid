package com.example.pasha.timetableandroid.chosen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.pasha.timetableandroid.R;
import com.example.pasha.timetableandroid.SavedActivity;
import com.example.pasha.timetableandroid.main.Route;
import com.example.pasha.timetableandroid.saved.Saved;
import com.example.pasha.timetableandroid.saved.SavedAdapter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ChosenAdapter extends BaseAdapter {
    private List<Route> list = new ArrayList<>();
    private List<Route> listAll = new ArrayList<>();
    private LayoutInflater inflater;
    private ChosenActivity context;
    private ListView chosenView;
    private String routeStart = "";
    private String routeEnd = "";
    private TextView route;
    private HashMap<String, Integer> dayOfWeek = new HashMap<>();

    public ChosenAdapter(ChosenActivity context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        initList(context, context.getIntent().getStringExtra("path"));
        this.chosenView = context.getChosenView();
        this.route = context.getRoute();
        route.setText(routeStart + " -> " + routeEnd);
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
        busRoute.setText(route.getBusRoute());
        busNumber.setText(route.getBusNumber());
        busStart.setText(route.getStart());
        busEnd.setText(route.getEnd());

        if ((i % 2) == 0) {
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

    //create list from memory
    private void initList(Context context, String path) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput(path)));
            routeStart = reader.readLine();
            routeEnd = reader.readLine();
            int size = Integer.parseInt(reader.readLine());
            List<Route> listRoute = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                String busRoute = reader.readLine();
                String busNumber = reader.readLine();
                String startTime = reader.readLine();
                String endTime = reader.readLine();
                String days = reader.readLine();
                listRoute.add(new Route(busRoute,busNumber,startTime,endTime,days));
            }
            reader.close();
            listAll.clear();
            listAll.addAll(listRoute);
            choiceDays();
            chosenView.setAdapter(ChosenAdapter.this);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        chosenView.setAdapter(this);
    }
}
