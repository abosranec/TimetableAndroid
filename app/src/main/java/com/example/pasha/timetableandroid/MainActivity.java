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
    private ToggleButton ButtonWeekdays;
    private ToggleButton ButtonHoliday;
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
//                        resultView.setAdapter(routeAdapter);
//                        routeAdapter.reWrite();
                        new Updater(getApplicationContext()).execute();
                        return;
                }
            }
        });
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
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    //**********************for menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_items, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.nav_refresh:
//                resultView.setAdapter(routeAdapter);
//                routeAdapter.reWrite();
//                //Toast.makeText(getApplicationContext(),"blablabla ", Toast.LENGTH_SHORT).show();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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

    //for save route
    public void saveRoute(View view) {
        routeAdapter.saveRoute(this);
    }
}
