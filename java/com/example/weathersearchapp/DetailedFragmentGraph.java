package com.example.weathersearchapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;


public class DetailedFragmentGraph extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static String responseString;
    private int mPage;

    public static DetailedFragmentGraph newInstance(int page, String response, String city) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        System.out.println("Response Detailed Fragment Graph" + response);
        setResponseData(response);
        DetailedFragmentGraph fragment = new DetailedFragmentGraph();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        LineChart chart = view.findViewById(R.id.chart);
        ImageView weekly_icon = view.findViewById(R.id.weekly_icon);
        TextView weekly_description = view.findViewById(R.id.weekly_description);
        String response = getResponseData();
        JSONObject dark_sky_response_json = null;
        try {
            dark_sky_response_json = new JSONObject(response);
            Double d = new Double("1.0");
            JSONObject weekly_data_object = dark_sky_response_json.getJSONObject("daily");

            String resultImage = iconImage(weekly_data_object.get("icon").toString());
            System.out.println("Result"+ resultImage);
            int imageResource = getResources().getIdentifier(resultImage, "drawable", getActivity().getPackageName());
            Drawable selected_image = getResources().getDrawable(imageResource);
            weekly_icon.setImageDrawable(selected_image);

            weekly_description.setText(weekly_data_object.get("summary").toString());

            JSONArray data_array = weekly_data_object.getJSONArray("data");
            List<Entry> entries = new ArrayList<Entry>();
                for(int i=0;i<data_array.length();i++) {
                    JSONObject daily_data = data_array.getJSONObject(i);
                    if (daily_data.get("temperatureLow") != null) {
                        if (daily_data.get("temperatureLow").getClass() == d.getClass()) {
                            int tempLow = (int)Math.round((double)daily_data.get("temperatureLow"));
                            entries.add(new Entry(i, tempLow));

                        } else {
                            int tempLow =(int)daily_data.get("temperatureLow");
                            entries.add(new Entry(i, tempLow));
                        }
                    }
                }
            List<Entry> entries2 = new ArrayList<Entry>();
            for(int i=0;i<data_array.length();i++) {
                JSONObject daily_data = data_array.getJSONObject(i);
                if (daily_data.get("temperatureHigh") != null) {
                    if (daily_data.get("temperatureHigh").getClass() == d.getClass()) {
                        int tempHigh = (int)Math.round((double)daily_data.get("temperatureHigh"));
                        entries2.add(new Entry(i, tempHigh));
                    } else {
                        int tempHigh =(int)daily_data.get("temperatureHigh");
                        entries2.add(new Entry(i, tempHigh));
                    }
                }
            }
            chart.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
            chart.getXAxis().setTextColor(Color.WHITE);
            chart.getLegend().setTextColor(Color.WHITE);
            LineDataSet dataSet = new LineDataSet(entries, "Minimum Temperature"); // add entries to dataset
            LineDataSet dataSet2 = new LineDataSet(entries2, "Maximum Temperature"); // add entries to dataset
            dataSet.setColor(Color.rgb(172, 108, 199));
            dataSet2.setColor(Color.rgb(250,196,19));
            LineData chartData = new LineData();
            chartData.addDataSet(dataSet);
            chartData.addDataSet(dataSet2);
            YAxis yleft = chart.getAxisLeft();
            yleft.setDrawLabels(false); // no axis labels
            yleft.setDrawAxisLine(true); // no axis line
            yleft.setDrawGridLines(false);

            YAxis yright = chart.getAxisLeft();
            yright.setDrawLabels(true); // no axis labels
            yright.setDrawAxisLine(true); // no axis line
            yright.setDrawGridLines(false);

            XAxis xleft = chart.getXAxis();
            xleft.setDrawLabels(true); // no axis labels
            xleft.setDrawAxisLine(true);
            xleft.setDrawGridLines(false);
            chart.setData(chartData);
            chart.invalidate();

            }catch(JSONException e)
            {
                e.printStackTrace();
            }
        return view;
    }

    public static void setResponseData(String response){
        responseString = response;
        System.out.println("method called");
    }

    public static String getResponseData(){
        return responseString;
    }

    protected String iconImage(String icon) {
        System.out.println("Icon got"+icon);
        String imageName = null;
        if (icon.equals("clear-day") || icon.equals("clear")) {
            imageName = "weather_sunny";
        } else if (icon.equals("clear-night")) {
            imageName = "weather_night";
        } else if (icon.equals("rain")) {
            imageName = "weather_rainy";
        } else if (icon.equals("sleet")) {
            imageName = "weather_snowy_rainy";
        } else if (icon.equals("snow")) {
            imageName = "weather_snowy";
        } else if (icon.equals("wind")) {
            imageName = "weather_windy_variant";
        } else if (icon.equals("fog")) {
            imageName = "weather_fog";
        } else if (icon.equals("cloudy")) {
            imageName = "weather_cloudy";
        } else if (icon.equals("partly-cloudy-night")) {
            imageName = "weather_night_partly_cloudy";
        } else if (icon.equals("partly-cloudy-day")) {
            imageName = "weather_partly_cloudy";
        } else
            imageName = "weather_sunny";
        return imageName;
    }
}