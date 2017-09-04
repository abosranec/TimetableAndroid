package com.example.pasha.timetableandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.pasha.timetableandroid.chosen.ChosenActivity;
import com.example.pasha.timetableandroid.saved.SavedAdapter;

public class SavedActivity extends Activity {

    private ListView savedView;
    private SavedAdapter savedAdapter;
    private ListView mDrawerListView;
    private DrawerLayout drawerLayout;

    public ListView getSavedView() {
        return savedView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_layout);

        //initial ListView
        savedView = findViewById(R.id.saved_result);
        savedAdapter = new SavedAdapter(this);
        savedView.setAdapter(savedAdapter);

        //menu drawer
        String[] menuTitles = getResources().getStringArray(R.array.heads_menu);
        drawerLayout = findViewById(R.id.drawer_layout_saved);
        mDrawerListView = findViewById(R.id.left_drawer_saved);
        mDrawerListView.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, menuTitles));
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerLayout.closeDrawers();
                switch(i) {
                    case 1:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
}
