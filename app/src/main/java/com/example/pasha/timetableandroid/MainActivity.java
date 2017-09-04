package com.example.pasha.timetableandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.pasha.timetableandroid.main.RouteAdapter;

import java.util.Calendar;


public class MainActivity extends Activity {
    private AutoCompleteTextView start;
    private AutoCompleteTextView end;
    private Button findRoute;
    private ListView resultView;
    private RouteAdapter routeAdapter;
    private ArrayAdapter arrayAdapterStart;
    private ArrayAdapter arrayAdapterEnd;
    private ToggleButton ButtonMonday;
    private ToggleButton ButtonTuesday;
    private ToggleButton ButtonWednesday;
    private ToggleButton ButtonThursday;
    private ToggleButton ButtonFriday;
    private ToggleButton ButtonSaturday;
    private ToggleButton ButtonSunday;
    private TextView textCurrentRoute;
    private ListView mDrawerListView;
    private DrawerLayout drawerLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        //initial button on activity
            //progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
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

        //menu drawer
        String[] menuTitles = getResources().getStringArray(R.array.heads_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        mDrawerListView = findViewById(R.id.left_drawer);
        mDrawerListView.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, menuTitles));
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerLayout.closeDrawers();
                switch(i) {
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), SavedActivity.class);
                        startActivity(intent);
                        return;
                    case 2:
                        //Toast.makeText(getApplicationContext(),"Запущено обновление данных", Toast.LENGTH_SHORT).show();
                        new Updater(getApplicationContext()).execute();
                        return;
                }
            }
        });
    }

    //getters
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
    public TextView getTextCurrentRoute() {
        return textCurrentRoute;
    }
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    //for find route
    public void findRoute(View view){
        //clear current route
        textCurrentRoute.setBackgroundColor(Color.rgb(48,63,159));
        textCurrentRoute.setText("");

        //build route
        routeAdapter.buildRoute(
                start.getText().toString().toLowerCase(),
                end.getText().toString().toLowerCase());

        //choice switcher for days
        currentDay();
    }

    //for switch days
    private void initSwitcher(){
        ButtonMonday = findViewById(R.id.toggleMonday);
        ButtonTuesday = findViewById(R.id.toggleTuesday);
        ButtonWednesday = findViewById(R.id.toggleWednesday);
        ButtonThursday = findViewById(R.id.toggleThursday);
        ButtonFriday = findViewById(R.id.toggleFriday);
        ButtonSaturday = findViewById(R.id.toggleSaturday);
        ButtonSunday = findViewById(R.id.toggleSunday);
        CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    resetSwitchAll();
                    compoundButton.setBackgroundColor(Color.argb(120,0,0, 255));
                    compoundButton.setTextColor(Color.rgb(255,255, 255));
                    routeAdapter.setDay(compoundButton.getText().toString());
                }
            }
        };
        ButtonMonday.setOnCheckedChangeListener(changeListener);
        ButtonTuesday.setOnCheckedChangeListener(changeListener);
        ButtonWednesday.setOnCheckedChangeListener(changeListener);
        ButtonThursday.setOnCheckedChangeListener(changeListener);
        ButtonFriday.setOnCheckedChangeListener(changeListener);
        ButtonSaturday.setOnCheckedChangeListener(changeListener);
        ButtonSunday.setOnCheckedChangeListener(changeListener);
        resetSwitchAll();
    }
    private void resetSwitchAll(){
        resetSwitch(ButtonMonday);
        resetSwitch(ButtonTuesday);
        resetSwitch(ButtonWednesday);
        resetSwitch(ButtonThursday);
        resetSwitch(ButtonFriday);
        resetSwitch(ButtonSaturday);
        resetSwitch(ButtonSunday);
    }
    private void resetSwitch(ToggleButton toggleButton){
        toggleButton.setChecked(false);
        toggleButton.setTextColor(Color.rgb(0,0, 0));
        toggleButton.setBackgroundColor(Color.rgb(200,200,200));
    }
    private void setSwitch(ToggleButton toggleButton){
        toggleButton.setChecked(true);
        toggleButton.setTextColor(Color.rgb(255,255, 255));
        toggleButton.setBackgroundColor(Color.argb(120,0,0, 255));
    }
    private void currentDay(){
        resetSwitchAll();
        Calendar c = Calendar.getInstance();
        if(c.get(Calendar.DAY_OF_WEEK) == 2)
            setSwitch(ButtonMonday);
        if(c.get(Calendar.DAY_OF_WEEK) == 3)
            setSwitch(ButtonTuesday);
        if(c.get(Calendar.DAY_OF_WEEK) == 4)
            setSwitch(ButtonWednesday);
        if(c.get(Calendar.DAY_OF_WEEK) == 5)
            setSwitch(ButtonThursday);
        if(c.get(Calendar.DAY_OF_WEEK) == 6)
            setSwitch(ButtonFriday);
        if(c.get(Calendar.DAY_OF_WEEK) == 7)
            setSwitch(ButtonSaturday);
        if(c.get(Calendar.DAY_OF_WEEK) == 1)
            setSwitch(ButtonSunday);
    }

    //for save route
    public void saveRoute(View view) {
        routeAdapter.saveRoute(this);
    }
}
