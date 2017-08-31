package com.example.pasha.timetableandroid.chosen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.*;
import com.example.pasha.timetableandroid.MainActivity;
import com.example.pasha.timetableandroid.R;
import com.example.pasha.timetableandroid.SavedActivity;
import com.example.pasha.timetableandroid.Updater;
import com.example.pasha.timetableandroid.saved.SavedAdapter;

import java.util.Calendar;

public class ChosenActivity extends Activity {

    private ListView chosenView;
    private ChosenAdapter chosenAdapter;
    private ListView mDrawerListView;
    private DrawerLayout drawerLayout;
    private ToggleButton buttonWeekdays;
    private ToggleButton buttonHoliday;
    private TextView route;

    public ListView getChosenView() {
        return chosenView;
    }
    public TextView getRoute() {
        return route;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosen_layout);

        //textView route
        route = findViewById(R.id.route_chosen);

        //initial ListView
        chosenView = findViewById(R.id.result_chosen);
        chosenAdapter = new ChosenAdapter(this);
        chosenView.setAdapter(chosenAdapter);

        //switch days
        initSwitcher();

        //menu drawer
        String[] menuTitles = getResources().getStringArray(R.array.heads_menu);
        drawerLayout = findViewById(R.id.drawer_layout_chosen);
        mDrawerListView = findViewById(R.id.left_drawer_chosen);
        mDrawerListView.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, menuTitles));
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerLayout.closeDrawers();
                switch(i) {
                    case 0:
                        Intent intent1 = new Intent(getApplicationContext(), SavedActivity.class);
                        startActivity(intent1);
                        return;
                    case 1:
                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent2);
                        return;
                    case 2:
                        new Updater(getApplicationContext()).execute();
                        return;
                }
            }
        });
    }


    //switch days
    private void initSwitcher(){
        buttonWeekdays = findViewById(R.id.toggleWeekdaysChosen);
        //buttonWeekdays.setBackgroundColor(Color.rgb(200,200,200));
        buttonHoliday = findViewById(R.id.toggleHolidaysChosen);
        //buttonHoliday.setBackgroundColor(Color.rgb(200,200,200));
        buttonWeekdays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    compoundButton.setBackgroundColor(Color.argb(120,0,0, 255));
                    compoundButton.setTextColor(Color.rgb(255,255, 255));
                    chosenAdapter.setWeekdays();
                    buttonHoliday.setChecked(false);
                    buttonHoliday.setTextColor(Color.rgb(0,0, 0));
                    buttonHoliday.setBackgroundColor(Color.rgb(200,200,200));
                }
            }
        });
        buttonHoliday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    compoundButton.setBackgroundColor(Color.argb(120,0,0, 255));
                    compoundButton.setTextColor(Color.rgb(255,255, 255));
                    chosenAdapter.setHolidays();
                    buttonWeekdays.setChecked(false);
                    buttonWeekdays.setTextColor(Color.rgb(0,0, 0));
                    buttonWeekdays.setBackgroundColor(Color.rgb(200,200,200));
                }
            }
        });
        //choice switcher for days
        Calendar c = Calendar.getInstance();
        if(c.get(Calendar.DAY_OF_WEEK) == 1 || c.get(Calendar.DAY_OF_WEEK) == 7 ){
            buttonHoliday.setChecked(true);
            buttonHoliday.setTextColor(Color.rgb(255,255, 255));
            buttonHoliday.setBackgroundColor(Color.argb(120,0,0, 255));
            buttonWeekdays.setChecked(false);
            buttonWeekdays.setTextColor(Color.rgb(0,0, 0));
            buttonWeekdays.setBackgroundColor(Color.rgb(200,200,200));
        }
        else{
            buttonHoliday.setChecked(false);
            buttonHoliday.setTextColor(Color.rgb(0,0, 0));
            buttonHoliday.setBackgroundColor(Color.rgb(200,200,200));
            buttonWeekdays.setChecked(true);
            buttonWeekdays.setTextColor(Color.rgb(255,255, 255));
            buttonWeekdays.setBackgroundColor(Color.argb(120,0,0, 255));
        }
    }
}
