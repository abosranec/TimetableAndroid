package com.example.pasha.timetableandroid.saved;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.pasha.timetableandroid.R;
import com.example.pasha.timetableandroid.main.Route;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SavedAdapter extends BaseAdapter {
    private List<Saved> list = new ArrayList<>();
    private LayoutInflater inflater;

    public SavedAdapter(Context context, List<Saved> list) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initList(context);
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
            viewLocal = inflater.inflate(R.layout.saved_result_layout, viewGroup, false);
        }

        Saved saved = getSaved(i);

        TextView savedRoute = viewLocal.findViewById(R.id.textSevedRoute);
        TextView savedTimes = viewLocal.findViewById(R.id.headNumber);
        ImageView delete = viewLocal.findViewById(R.id.imageDeleteEnd);
        savedRoute.setText(saved.getRoute());
        savedTimes.setText(saved.getTimes());
        //busStart.setText(route.getStart());
        //busDays.setText(route.getDays());

        if (i % 2 == 0) {
            savedRoute.setBackgroundColor(Color.argb(130, 220, 220, 220));
            savedTimes.setBackgroundColor(Color.argb(130, 220, 220, 220));
            delete.setBackgroundColor(Color.argb(130, 220, 220, 220));
//            busEnd.setBackgroundColor(Color.argb(130, 220, 220, 220));
        } else{
            savedRoute.setBackgroundColor(Color.argb(0, 255, 255, 255));
            savedTimes.setBackgroundColor(Color.argb(0, 255, 255, 255));
            delete.setBackgroundColor(Color.argb(0, 255, 255, 255));
//            busEnd.setBackgroundColor(Color.argb(0, 255, 255, 255));
        }
        return viewLocal;
    }
    private Saved getSaved(int position){
        return (Saved)getItem(position);
    }

    //create list from memory
    private void initList(Context context) {
        String[] strings = context.fileList();
        if (strings.length > 0) {
            for (String path: strings) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput(path)));
                    String start = reader.readLine();
                    String end = reader.readLine();
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
                    Saved saved = new Saved(listRoute, start, end);
                    list.add(saved);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
