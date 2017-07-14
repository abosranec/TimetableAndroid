package com.example.pasha.timetableandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        findRoute = findViewById(R.id.findRoute);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

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
