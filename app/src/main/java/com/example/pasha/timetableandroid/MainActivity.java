package com.example.pasha.timetableandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.pasha.timetableandroid.routes.Route;
import com.example.pasha.timetableandroid.routes.RouteAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private TextView start;
    private TextView end;
    //private TextView result;
    private Button findRoute;
    private ListView resultView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        //initial button on activity
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        findRoute = findViewById(R.id.findRoute);

        //initial ListView
        resultView = findViewById(R.id.result);
        RouteAdapter routeAdapter = new RouteAdapter(this, initResult());
        resultView.setAdapter(routeAdapter);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rezultList);
//        result = findViewById(R.id.result);
//        findRoute.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                result.setText(result.getText().toString() + "\n" + start.getText() + "\t\t" + end.getText());
//            }
//        });
    }

    //for menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_refresh:
                Toast.makeText(getApplicationContext(),"blablabla", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //for ListView
    private List<Route> initResult(){
        List<Route> list = new ArrayList<>();
        list.add(new Route("АТОЛИНО - МИНСК АС-ЮгоЗападная","279","05:44", "06:02", "буд"));
        list.add(new Route("АТОЛИНО - МИНСК АС-Дружная","279","06:03", "06:19", "буд"));
        list.add(new Route("МАРИПОЛЬ - МИНСК АС-ЮгоЗападная","279","06:03", "06:19", "вых"));
        list.add(new Route("АТОЛИНО - МИНСК АС-ЮгоЗападная","279","06:03", "06:19", "буд"));
        list.add(new Route("МАРИПОЛЬ - МИНСК АС-ЮгоЗападная","279","06:03", "06:19", "вых"));
        list.add(new Route("АТОЛИНО - МИНСК АС-ЮгоЗападная","279","06:03", "06:19", "буд"));
        list.add(new Route("АТОЛИНО - МИНСК АС-ЮгоЗападная","279","06:03", "06:19", "вых"));
        list.add(new Route("ЛЕОНЦЕВИЧИ - МИНСК АС-ЮгоЗападная","279","06:03", "06:19", "буд"));
        list.add(new Route("АТОЛИНО - МИНСК АС-Дружная","279","06:03", "06:19", "вых"));
        return list;
    }

}
