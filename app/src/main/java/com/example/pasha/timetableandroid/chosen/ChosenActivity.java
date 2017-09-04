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
    private ToggleButton ButtonMonday;
    private ToggleButton ButtonTuesday;
    private ToggleButton ButtonWednesday;
    private ToggleButton ButtonThursday;
    private ToggleButton ButtonFriday;
    private ToggleButton ButtonSaturday;
    private ToggleButton ButtonSunday;
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
                        Toast.makeText(getApplicationContext(),"Запущено обновление данных", Toast.LENGTH_SHORT).show();
                        new Updater(getApplicationContext()).execute();
                        return;
                }
            }
        });
    }


    //switch days
    private void initSwitcher(){
        ButtonMonday = findViewById(R.id.toggleMondayChosen);
        ButtonTuesday = findViewById(R.id.toggleTuesdayChosen);
        ButtonWednesday = findViewById(R.id.toggleWednesdayChosen);
        ButtonThursday = findViewById(R.id.toggleThursdayChosen);
        ButtonFriday = findViewById(R.id.toggleFridayChosen);
        ButtonSaturday = findViewById(R.id.toggleSaturdayChosen);
        ButtonSunday = findViewById(R.id.toggleSundayChosen);
        CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    resetSwitchAll();
                    compoundButton.setBackgroundColor(Color.argb(120,0,0, 255));
                    compoundButton.setTextColor(Color.rgb(255,255, 255));
                    chosenAdapter.setDay(compoundButton.getText().toString());
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
        currentDay();
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
}
