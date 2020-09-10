package com.example.weathersearchapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplicationFragment extends Fragment {
    View view;
    static int position;
    String responseString;
    String responseCity;
    String city_name;
    String state_name;
    String country_name;
    public static ApplicationFragment newInstance(int val) {
        ApplicationFragment fragment = new ApplicationFragment();
        return fragment;
    }
    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_display, container, false);

        final String query = getArguments().getString("weather_string");
        int position = getArguments().getInt("position");

        final TextView card1_places = (TextView) view.findViewById(R.id.card1_places);
        final TextView card1_temperature = (TextView) view.findViewById(R.id.card1_temperature);
        final TextView card1_summary = (TextView) view.findViewById(R.id.card1_summary);

        final TextView text1_row1_card2 = (TextView) view.findViewById(R.id.text1_row1_card2);
        final TextView text2_row1_card2 = (TextView) view.findViewById(R.id.text2_row1_card2);
        final TextView text3_row1_card2 = (TextView) view.findViewById(R.id.text3_row1_card2);
        final TextView text4_row1_card2 = (TextView) view.findViewById(R.id.text4_row1_card2);

        final TextView text1_row2_card2 = (TextView) view.findViewById(R.id.text1_row2_card2);
        final TextView text2_row2_card2 = (TextView) view.findViewById(R.id.text2_row2_card2);
        final TextView text3_row2_card2 = (TextView) view.findViewById(R.id.text3_row2_card2);
        final TextView text4_row2_card2 = (TextView) view.findViewById(R.id.text4_row2_card2);


        if(query.length()!=0) {
            String[] split_query = query.split(",");
            if (split_query.length == 3) {
                city_name = split_query[0];
                setResponseCity(city_name);
                state_name = split_query[1];
                country_name = split_query[2];
            } else if (split_query.length==2) {
                city_name = split_query[0];
                setResponseCity(city_name);
                state_name = "";
                country_name = split_query[1];
            }

            card1_places.setText(query);
// Instantiate the RequestQueue.
            final RequestQueue queue = Volley.newRequestQueue(getActivity());
            String dark_sky_url_call = "http://weathersearch-android.us-east-1.elasticbeanstalk.com/weathersearchform?street=''&city=" + city_name + "&state=" + state_name + "&country=" + country_name;
            //card1_places.setText(city_name + ", " + state_name + ", " + country_name);
            StringRequest stringRequest_inner = new StringRequest(Request.Method.GET, dark_sky_url_call,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String dark_sky_response) {
                            try {
                                JSONObject dark_sky_response_json = new JSONObject(dark_sky_response);
                                JSONObject currently_object = dark_sky_response_json.getJSONObject("currently");
                                JSONObject weekly_data_object = dark_sky_response_json.getJSONObject("daily");

                                System.out.println(currently_object.get("temperature").getClass());
                                Double referenceDouble = new Double("0.0");
                                Integer referenceInteger = new Integer("0");
                                if (currently_object.get("temperature").getClass() == referenceDouble.getClass()) {

                                    DecimalFormat format_decimals = new DecimalFormat("#.##");
                                    double temp = (double) currently_object.get("temperature");
                                    card1_temperature.setText(Math.round(temp) + "\u00B0F");
                                } else {
                                    card1_temperature.setText(currently_object.get("temperature") + "\u00B0F");
                                }
                                card1_summary.setText(currently_object.get("summary").toString());

                                ImageView card_icon = (ImageView) view.findViewById(R.id.icon_card1);
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

                                        text1_row1_card2.setText(Math.round((double) currently_object.get("humidity") * 100) + "%");
                                    } else {
                                        text1_row1_card2.setText(Math.round((int) currently_object.get("humidity") * 100) + "%");
                                    }

                                } else
                                    text1_row1_card2.setText("0");

                                if (currently_object.get("windSpeed") != null) {
                                    if (currently_object.get("windSpeed").getClass() == referenceDouble.getClass()) {
                                        text2_row1_card2.setText(Math.round((double) currently_object.get("windSpeed") * 100.0) / 100.0 + " mph");
                                    } else
                                        text2_row1_card2.setText(Math.round((int) currently_object.get("windSpeed") * 100.0) / 100.0 + " mph");
                                } else
                                    text2_row1_card2.setText("0");

                                if (currently_object.get("visibility") != null) {
                                    if (currently_object.get("visibility").getClass() == referenceDouble.getClass()) {
                                        text3_row1_card2.setText(Math.round((double) currently_object.get("visibility") * 100.0) / 100.0 + " km");
                                    } else
                                        text3_row1_card2.setText(Math.round((int) currently_object.get("visibility") * 100.0) / 100.0 + " km");
                                } else
                                    text3_row1_card2.setText("0");

                                if (currently_object.get("pressure") != null) {
                                    if (currently_object.get("pressure").getClass() == referenceDouble.getClass()) {
                                        text4_row1_card2.setText(Math.round((double) currently_object.get("pressure") * 100.0) / 100.0 + " mb");
                                    } else
                                        text4_row1_card2.setText(Math.round((int) currently_object.get("pressure") * 100.0) / 100.0 + " mb");
                                } else
                                    text4_row1_card2.setText("0");

                                text1_row2_card2.setText("Humidity");
                                text2_row2_card2.setText("Wind Speed");
                                text3_row2_card2.setText("Visibility");
                                text4_row2_card2.setText("Pressure");

                                //System.out.println(weekly_data_object.get("data"));
                                JSONArray data_array = weekly_data_object.getJSONArray("data");

                                for (int i = 0; i < data_array.length(); i++) {
                                    JSONObject daily_data = data_array.getJSONObject(i);
                                    //System.out.println(daily_data);
                                    int counter = i + 1;
                                    String textView_obj_time = "table_row" + counter + "_col1_card3";
                                    //System.out.println((textView_obj_time));
                                    String textView_obj_icon = "table_row" + counter + "_col2_card3";
                                    String textView_obj_tempLow = "table_row" + counter + "_col3_card3";
                                    String textView_obj_tempHigh = "table_row" + counter + "_col4_card3";

                                    if (daily_data.get("time") != null) {
                                        int resourceID = getResources().getIdentifier(textView_obj_time, "id",
                                                getActivity().getPackageName());
                                        //System.out.println(resourceID);
                                        if (resourceID != 0) {
                                            TextView update_content = (TextView) view.findViewById(resourceID);
                                            if (update_content != null) {
                                                String daily_date = new SimpleDateFormat("MM/dd/yyyy").format(new Date((int) daily_data.get("time") * 1000L));
                                                update_content.setText(daily_date);
                                            }
                                        }
                                    }
                                    System.out.println("Icon" + daily_data.get("icon"));
                                    if (daily_data.get("icon") != null) {
                                        System.out.println("Icon" + daily_data.get("icon"));
                                        int resourceID = getResources().getIdentifier(textView_obj_icon, "id",
                                                getActivity().getPackageName());
                                        System.out.println("Resource" + resourceID);
                                        //System.out.println(resourceID);
                                        if (resourceID != 0) {
                                            ImageView update_content = (ImageView) view.findViewById(resourceID);
                                            if (update_content != null) {
                                                //update_content.setText(daily_data.get("icon").toString());
                                                String resultImage = iconImage(daily_data.get("icon").toString());
                                                System.out.println("Result" + resultImage);
                                                int imageResource = getResources().getIdentifier(resultImage, "drawable", getActivity().getPackageName());
                                                Drawable selected_image = getResources().getDrawable(imageResource);
                                                update_content.setImageDrawable(selected_image);
                                            }
                                        }
                                    }
                                    if (daily_data.get("temperatureLow") != null) {
                                        int resourceID = getResources().getIdentifier(textView_obj_tempLow, "id",
                                                getActivity().getPackageName());
                                        //System.out.println(resourceID);
                                        if (resourceID != 0) {
                                            TextView update_content = (TextView) view.findViewById(resourceID);
                                            if (update_content != null) {
                                                if (daily_data.get("temperatureLow").getClass() == referenceDouble.getClass())
                                                    update_content.setText(Long.toString(Math.round((double) daily_data.get("temperatureLow"))));
                                                else
                                                    update_content.setText(Long.toString(Math.round((int) daily_data.get("temperatureLow"))));
                                            }
                                        }
                                    }
                                    if (daily_data.get("temperatureHigh") != null) {
                                        int resourceID = getResources().getIdentifier(textView_obj_tempHigh, "id",
                                                getActivity().getPackageName());
                                        //System.out.println(resourceID);
                                        if (resourceID != 0) {
                                            TextView update_content = (TextView) view.findViewById(resourceID);
                                            if (update_content != null) {
                                                if (daily_data.get("temperatureHigh").getClass() == referenceDouble.getClass())
                                                    update_content.setText(Long.toString(Math.round((double) daily_data.get("temperatureHigh"))));
                                                else
                                                    update_content.setText(Long.toString(Math.round((int) daily_data.get("temperatureHigh"))));
                                            }
                                        }
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            setResponseString(dark_sky_response);
                            //System.out.println(dark_sky_response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(stringRequest_inner);

            ImageButton information_button = (ImageButton) view.findViewById(R.id.detailed_info_card1);
            information_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Hello World");
                    Intent intent = new Intent(getActivity(), DetailedActivity.class);
                    intent.putExtra("responseString", responseString);
                    intent.putExtra("city", responseCity);
                    startActivity(intent);

                }
            });
            FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
            if(city_name == "Los Angeles")
            {
                CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                p.setAnchorId(View.NO_ID);
                fab.setLayoutParams(p);
                fab.setVisibility(View.GONE);
            }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity() , query + " is removed from favourites", Toast.LENGTH_LONG).show();
                    SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("Query_map", Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                    mEditor.remove(city_name);
                    mEditor.apply();
                    System.out.println("Shared" + mSharedPreferences);
                }
            });
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

    protected void setResponseString(String response) {
        responseString = response;
    }

    protected String getResponseString() {
        return responseString;
    }

    protected void setResponseCity(String response) {
        responseCity = response;
    }

    protected String getResponseCity() {
        return responseCity;
    }
}