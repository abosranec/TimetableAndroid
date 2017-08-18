package com.example.pasha.timetableandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.pasha.timetableandroid.routes.Route;
import com.example.pasha.timetableandroid.routes.RouteAdapter;
import com.example.pasha.timetableandroid.routes.Stations;


public class MainActivity extends Activity {
    private AutoCompleteTextView start;
    private AutoCompleteTextView end;
    //private TextView result;
    private Button findRoute;
    private ListView resultView;
    private RouteAdapter routeAdapter;
    private ArrayAdapter arrayAdapterStart;
    private ArrayAdapter arrayAdapterEnd;


    private String[] textProb = {"asdfg","asdfghj","sdf","yrytu","m,bjhk","ljjih"};
//    private AutoCompleteTextView autoCompleteTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        //initial button on activity
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
            //button find
        findRoute = findViewById(R.id.findRoute);

            //initial ListView
        resultView = findViewById(R.id.result);
        routeAdapter = new RouteAdapter(this, resultView, start, arrayAdapterStart, end, arrayAdapterEnd);
        resultView.setAdapter(routeAdapter);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rezultList);
//        result = findViewById(R.id.result);
//        findRoute.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                result.setText(result.getText().toString() + "\n" + start.getText() + "\t\t" + end.getText());
//            }
//        });
//        autoCompleteTextView = findViewById(R.id.probn);
//        ImageView imageView = findViewById(R.id.startImage);
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,textProb);
//        autoCompleteTextView.setThreshold(2);
//        autoCompleteTextView.setAdapter(arrayAdapter);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                autoCompleteTextView.showDropDown();
//            }
//        });


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
//                stations.reWrite();
                resultView.setAdapter(routeAdapter);
                routeAdapter.reWrite();
                //Toast.makeText(getApplicationContext(),"blablabla ", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //*********************for ListView

    //*********************for find route
    public void findRoute(View view){
        //Toast.makeText(getApplicationContext(),"blablabla ", Toast.LENGTH_SHORT).show();
        routeAdapter.buildRoute(
                start.getText().toString().toLowerCase(),
                end.getText().toString().toLowerCase());
        //resultView.setAdapter(routeAdapter);
    }

    public void showWarning(){
        Toast.makeText(getApplicationContext(),"Нет соединения с интернетом!", Toast.LENGTH_SHORT).show();
    }
}
