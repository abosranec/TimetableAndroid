package com.example.pasha.timetableandroid;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.pasha.timetableandroid.routes.Route;
import com.example.pasha.timetableandroid.routes.RouteAdapter;
import com.example.pasha.timetableandroid.routes.Stations;

import java.util.Calendar;


public class MainActivity extends Activity {
    private AutoCompleteTextView start;
    private AutoCompleteTextView end;
    private Button findRoute;
    private ListView resultView;
    private RouteAdapter routeAdapter;
    private ArrayAdapter arrayAdapterStart;
    private ArrayAdapter arrayAdapterEnd;
    private ToggleButton ButtonWeekdays;
    private ToggleButton ButtonHoliday;
    private TextView textCurrentRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        //initial button on activity
            //current route
        textCurrentRoute = findViewById(R.id.currentRoute);
            //start
        start = findViewById(R.id.start);
        ImageView imageStart = findViewById(R.id.imageStart);
        arrayAdapterStart = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line);
        //arrayAdapterStart.addAll(textProb);
        start.setAdapter(arrayAdapterStart);
        start.setThreshold(2);
        imageStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.showDropDown();
            }
        });
        ImageView imageDeleteStart = findViewById(R.id.imageDeleteStart);
        imageDeleteStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setText("");
            }
        });
            //stop
        end = findViewById(R.id.end);
        ImageView imageEnd = findViewById(R.id.imageEnd);
        arrayAdapterEnd = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line);
        end.setAdapter(arrayAdapterEnd);
        end.setThreshold(2);
        imageEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end.showDropDown();
            }
        });
        ImageView imageDeleteEnd = findViewById(R.id.imageDeleteEnd);
        imageDeleteEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end.setText("");
            }
        });
            //button find
        findRoute = findViewById(R.id.findRoute);

            //initial ListView
        resultView = findViewById(R.id.result);
        routeAdapter = new RouteAdapter(this);
        resultView.setAdapter(routeAdapter);

            //switch days
        initSwitcher();

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rezultList);
//        result = findViewById(R.id.result);
//        findRoute.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                result.setText(result.getText().toString() + "\n" + start.getText() + "\t\t" + end.getText());
//            }
//        });
    }

    public AutoCompleteTextView getStart() {
        return start;
    }
    public AutoCompleteTextView getEnd() {
        return end;
    }
    public ListView getResultView() {
        return resultView;
    }
    public ArrayAdapter getArrayAdapterStart() {
        return arrayAdapterStart;
    }
    public ArrayAdapter getArrayAdapterEnd() {
        return arrayAdapterEnd;
    }
    public ToggleButton getButtonWeekdays() {
        return ButtonWeekdays;
    }
    public ToggleButton getButtonHoliday() {
        return ButtonHoliday;
    }
    public TextView getTextCurrentRoute() {
        return textCurrentRoute;
    }

    //**********************for menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_refresh:
                resultView.setAdapter(routeAdapter);
                routeAdapter.reWrite();
                //Toast.makeText(getApplicationContext(),"blablabla ", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //*********************for find route
    public void findRoute(View view){
        //build route
        routeAdapter.buildRoute(
                start.getText().toString().toLowerCase(),
                end.getText().toString().toLowerCase());

        //choice switcher for days
        Calendar c = Calendar.getInstance();
        if(c.get(Calendar.DAY_OF_WEEK) == 1 || c.get(Calendar.DAY_OF_WEEK) == 7 ){
            ButtonHoliday.setChecked(true);
            ButtonHoliday.setTextColor(Color.rgb(255,255, 255));
            ButtonHoliday.setBackgroundColor(Color.argb(120,0,0, 255));
            ButtonWeekdays.setChecked(false);
            ButtonWeekdays.setTextColor(Color.rgb(0,0, 0));
            ButtonWeekdays.setBackgroundColor(Color.rgb(200,200,200));
        }
        else{
            ButtonHoliday.setChecked(false);
            ButtonHoliday.setTextColor(Color.rgb(0,0, 0));
            ButtonHoliday.setBackgroundColor(Color.rgb(200,200,200));
            ButtonWeekdays.setChecked(true);
            ButtonWeekdays.setTextColor(Color.rgb(255,255, 255));
            ButtonWeekdays.setBackgroundColor(Color.argb(120,0,0, 255));
        }
    }

    //switch days
    private void initSwitcher(){
        ButtonWeekdays = findViewById(R.id.toggleWeekdays);
        ButtonWeekdays.setBackgroundColor(Color.rgb(200,200,200));
        ButtonHoliday = findViewById(R.id.toggleHolidays);
        ButtonHoliday.setBackgroundColor(Color.rgb(200,200,200));
        ButtonWeekdays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    compoundButton.setBackgroundColor(Color.argb(120,0,0, 255));
                    compoundButton.setTextColor(Color.rgb(255,255, 255));
                    routeAdapter.setWeekdays();
                    ButtonHoliday.setChecked(false);
                    ButtonHoliday.setTextColor(Color.rgb(0,0, 0));
                    ButtonHoliday.setBackgroundColor(Color.rgb(200,200,200));
                }
            }
        });
        ButtonHoliday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    compoundButton.setBackgroundColor(Color.argb(120,0,0, 255));
                    compoundButton.setTextColor(Color.rgb(255,255, 255));
                    routeAdapter.setHolidays();
                    ButtonWeekdays.setChecked(false);
                    ButtonWeekdays.setTextColor(Color.rgb(0,0, 0));
                    ButtonWeekdays.setBackgroundColor(Color.rgb(200,200,200));
                }
            }
        });
    }
}
