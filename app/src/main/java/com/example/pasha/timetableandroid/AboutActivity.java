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

public class AboutActivity extends Activity {
    private ListView mDrawerListView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);

        //menu drawer
        String[] menuTitles = getResources().getStringArray(R.array.heads_menu);
        drawerLayout = findViewById(R.id.drawer_layout_about);
        mDrawerListView = findViewById(R.id.left_drawer_about);
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
}
