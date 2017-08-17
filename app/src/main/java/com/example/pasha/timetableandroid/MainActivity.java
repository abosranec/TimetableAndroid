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
    private EditText start;
    private EditText end;
    //private TextView result;
    private Button findRoute;
    private ListView resultView;
    private RouteAdapter routeAdapter;


    String[] textProb = {"asdfg","asdfghj","sdf","yrytu","m,bjhk","ljjih"};
    AutoCompleteTextView autoCompleteTextView;


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
        routeAdapter = new RouteAdapter(this, resultView);
        resultView.setAdapter(routeAdapter);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rezultList);
//        result = findViewById(R.id.result);
//        findRoute.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                result.setText(result.getText().toString() + "\n" + start.getText() + "\t\t" + end.getText());
//            }
//        });



        autoCompleteTextView = findViewById(R.id.probn);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.select_dialog_item,textProb);
        autoCompleteTextView.setThreshold(3);
        autoCompleteTextView.setAdapter(arrayAdapter);


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
