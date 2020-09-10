package com.example.weathersearchapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class DetailedFragmentInfo extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private static String responseString;

    public static DetailedFragmentInfo newInstance(int page, String response, String city) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        System.out.println("Response Detailed Fragment Info" + response);
        setResponseData(response);
        DetailedFragmentInfo fragment = new DetailedFragmentInfo();
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
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        TextView temperature = view.findViewById(R.id.temperature_label);
        TextView pressure = view.findViewById(R.id.pressure_label);
        TextView precipitation = view.findViewById(R.id.precipitation_label);
        TextView windspeed = view.findViewById(R.id.windSpeed_label);
        TextView summary = view.findViewById(R.id.summary_name);
        TextView ozone = view.findViewById(R.id.ozone_label);
        TextView humidity = view.findViewById(R.id.humidity_label);
        TextView visibility = view.findViewById(R.id.visibility_label);
        TextView cloudcover = view.findViewById(R.id.cloudCover_label);

        JSONObject dark_sky_response_json = null;
        try {
            dark_sky_response_json = new JSONObject(responseString);
            JSONObject currently_object = dark_sky_response_json.getJSONObject("currently");

            System.out.println(currently_object.get("temperature").getClass());
            Double referenceDouble = new Double("0.0");
            Integer referenceInteger = new Integer("0");
            if (currently_object.get("temperature").getClass() == referenceDouble.getClass()) {

                DecimalFormat format_decimals = new DecimalFormat("#.##");
                double temp = (double) currently_object.get("temperature");
                temperature.setText(Math.round(temp) + "\u00B0F");
            } else {
                temperature.setText(currently_object.get("temperature") + "\u00B0F");
            }
            summary.setText(currently_object.get("summary").toString());

            ImageView card_icon = (ImageView) view.findViewById(R.id.summaryimageView);
            if (card_icon != null) {
                //update_content.setText(daily_data.get("icon").toString());
                String resultImage = iconImage(currently_object.get("icon").toString());
                System.out.println("Result" + resultImage);
                int imageResource = getResources().getIdentifier(resultImage, "drawable", getActivity().getPackageName());
                Drawable selected_image = getResources().getDrawable(imageResource);
                card_icon.setImageDrawable(selected_image);
            }
            if (currently_object.get("humidity") != null) {
                //System.out.println(currently_object.get("humidity"));
                if (currently_object.get("humidity").getClass() == referenceDouble.getClass()) {

                    humidity.setText(Math.round((double) currently_object.get("humidity") * 100) + "%");
                } else {
                    humidity.setText(Math.round((int) currently_object.get("humidity") * 100) + "%");
                }

            } else
                humidity.setText("0");

            if (currently_object.get("windSpeed") != null) {
                if (currently_object.get("windSpeed").getClass() == referenceDouble.getClass()) {
                    windspeed.setText(Math.round((double) currently_object.get("windSpeed") * 100.0) / 100.0 + " mph");
                } else
                    windspeed.setText(Math.round((int) currently_object.get("windSpeed") * 100.0) / 100.0 + " mph");
            } else
                windspeed.setText("0");

            if (currently_object.get("visibility") != null) {
                if (currently_object.get("visibility").getClass() == referenceDouble.getClass()) {
                    visibility.setText(Math.round((double) currently_object.get("visibility") * 100.0) / 100.0 + " km");
                } else
                    visibility.setText(Math.round((int) currently_object.get("visibility") * 100.0) / 100.0 + " km");
            } else
                visibility.setText("0");

            if (currently_object.get("pressure") != null) {
                if (currently_object.get("pressure").getClass() == referenceDouble.getClass()) {
                    pressure.setText(Math.round((double) currently_object.get("pressure") * 100.0) / 100.0 + " mb");
                } else
                    pressure.setText(Math.round((int) currently_object.get("pressure") * 100.0) / 100.0 + " mb");
            } else
                pressure.setText("0");

            if (currently_object.get("precipIntensity") != null) {
                //System.out.println(currently_object.get("humidity"));
                if (currently_object.get("precipIntensity").getClass() == referenceDouble.getClass()) {
                    precipitation.setText(Math.round((double) currently_object.get("precipIntensity") * 100) + " mmph");
                } else {
                    precipitation.setText(Math.round((int) currently_object.get("precipIntensity") * 100) + " mmph");
                }

            } else
                precipitation.setText("0");

            if (currently_object.get("ozone") != null) {
                //System.out.println(currently_object.get("humidity"));
                if (currently_object.get("ozone").getClass() == referenceDouble.getClass()) {

                    ozone.setText(Math.round((double) currently_object.get("ozone") * 100)/ 100.0 + " DU");
                } else {
                    ozone.setText(Math.round((int) currently_object.get("ozone") * 100)/ 100.0 + " DU");
                }

            } else
                ozone.setText("0");

            if (currently_object.get("cloudCover") != null) {
                //System.out.println(currently_object.get("humidity"));
                if (currently_object.get("cloudCover").getClass() == referenceDouble.getClass()) {

                    cloudcover.setText(Math.round((double) currently_object.get("cloudCover") * 100) + "%");
                } else {
                    cloudcover.setText(Math.round((int) currently_object.get("cloudCover") * 100) + "%");
                }

            } else
                cloudcover.setText("0");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    protected String iconImage(String icon) {
        System.out.println("Icon got" + icon);
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

    public static void setResponseData(String response){
        responseString = response;
        System.out.println("method called");
    }

    public static String getResponseData(){
        return responseString;
    }
}