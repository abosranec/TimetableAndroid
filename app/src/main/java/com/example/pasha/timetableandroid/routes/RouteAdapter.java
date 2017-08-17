package com.example.pasha.timetableandroid.routes;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pasha.timetableandroid.MainActivity;
import com.example.pasha.timetableandroid.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class RouteAdapter extends BaseAdapter{

    private List<Route> list = new ArrayList<>();
    private Stations stations;
    private String routeStart = "";
    private String routeEnd = "";
    private LayoutInflater inflater;
    private Context context;

    public RouteAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        stations = new Stations();
        //reWrite();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewLocal = view;
        if (viewLocal == null) {
            viewLocal = inflater.inflate(R.layout.rezult_layout, viewGroup, false);
        }

        Route route = getRoute(i);

        TextView busRoute = viewLocal.findViewById(R.id.bus_route);
        TextView busNumber = viewLocal.findViewById(R.id.bus_number);
        TextView busStart = viewLocal.findViewById(R.id.bus_start);
        TextView busEnd = viewLocal.findViewById(R.id.bus_end);
        TextView busDays = viewLocal.findViewById(R.id.bus_days);
        busRoute.setText(route.getBusRoute());
        busNumber.setText(route.getBusNumber());
        busStart.setText(route.getStart());
        busEnd.setText(route.getEnd());
        busDays.setText(route.getDays());
        return viewLocal;
    }

    private Route getRoute(int position){
        return (Route)getItem(position);
    }

    //*********************************builds routes
    public void reWrite(String routeStart, String routeEnd){
        this.routeStart = routeStart;
        this.routeEnd = routeEnd;
        new ReWrite().execute();
    }

    class ReWrite extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            List<Route> listRoute = new ArrayList<>();
            String html = getHtmlForRoute();

            //Toast.makeText(context.getApplicationContext(),"Ошибка доступа в интернет!", Toast.LENGTH_SHORT).show();
            try {
                Document doc = Jsoup.connect(html).get();
                Elements tableElements = doc.select("table[class=schedule_table]");
                Elements tableRowElements = tableElements.select("tr");
                for (int i = 2; i < tableRowElements.size(); i++) {
                    Element row = tableRowElements.get(i);
                    Elements rowItems = row.select("th");
                    if (rowItems.size() == 0)
                        rowItems = row.select("td");

                    //made routes
                    String days = "";
                    if (rowItems.get(3).text().equals("пн вт ср чт пт"))
                        days = "буд";
                    else
                        days = "вых";
                    listRoute.add(new Route(
                            rowItems.get(0).text(),
                            "***",
                            rowItems.get(1).text(),
                            rowItems.get(2).text(),
                            days));
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            list.clear();
            list.addAll(listRoute);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    //make get-request route start - end
    private String getHtmlForRoute(){
        return "http://www.minsktrans.by/mg/suburbt.php?find_runs=1&minsk=" +
                stations.get(routeStart) +
                "&other=" +
                stations.get(routeEnd);
    }
}
