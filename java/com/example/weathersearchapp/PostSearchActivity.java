package com.example.weathersearchapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;

public class PostSearchActivity extends AppCompatActivity {

    String response;
    String query;
    String city_name="",state_name="",country_name="";
    String responseString;
    String responseCity;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_search);
        final TextView card1_places = (TextView) findViewById(R.id.card1_places);
        Bundle extras = getIntent().getExtras();
        response = extras.getString("responseString");
        setResponseString(response);
        query = extras.getString("query");
        System.out.println(response);
        ActionBar actionBar = getSupportActionBar();
        // Set below attributes to add logo in ActionBar.
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

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

// Instantiate the RequestQueue.
            final RequestQueue queue = Volley.newRequestQueue(this);
            String dark_sky_url_call = "http://weathersearch-android.us-east-1.elasticbeanstalk.com/weathersearchform?street=''&city=" + city_name + "&state=" + state_name + "&country=" + country_name;
            //card1_places.setText(city_name + ", " + state_name + ", " + country_name);
            StringRequest stringRequest_inner = new StringRequest(Request.Method.GET, dark_sky_url_call,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String dark_sky_response) {
                            assignData(dark_sky_response);
                            setResponseString(dark_sky_response);
                            //System.out.println(dark_sky_response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(stringRequest_inner);
            ImageButton information_button = (ImageButton) findViewById(R.id.detailed_info_card1);
            information_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Hello World");
                    Intent intent = new Intent(PostSearchActivity.this, DetailedActivity.class);
                    intent.putExtra("responseString", responseString);
                    intent.putExtra("city", responseCity);
                    startActivity(intent);

                }
            });
            FloatingActionButton fab = findViewById(R.id.floatingActionButton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext() , query + " is added to favourites", Toast.LENGTH_LONG).show();
                    SharedPreferences mSharedPreferences = getSharedPreferences("Query_map", Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                    mEditor.putString(city_name,query);
                    mEditor.apply();
                    System.out.println("Shared" + mSharedPreferences);
                }
            });
        }
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

    protected void assignData(String dark_sky_response) {

        final TextView card1_temperature = (TextView) findViewById(R.id.card1_temperature);
        final TextView card1_summary = (TextView) findViewById(R.id.card1_summary);

        final TextView text1_row1_card2 = (TextView) findViewById(R.id.text1_row1_card2);
        final TextView text2_row1_card2 = (TextView) findViewById(R.id.text2_row1_card2);
        final TextView text3_row1_card2 = (TextView) findViewById(R.id.text3_row1_card2);
        final TextView text4_row1_card2 = (TextView) findViewById(R.id.text4_row1_card2);

        final TextView text1_row2_card2 = (TextView) findViewById(R.id.text1_row2_card2);
        final TextView text2_row2_card2 = (TextView) findViewById(R.id.text2_row2_card2);
        final TextView text3_row2_card2 = (TextView) findViewById(R.id.text3_row2_card2);
        final TextView text4_row2_card2 = (TextView) findViewById(R.id.text4_row2_card2);

        JSONObject dark_sky_response_json = null;
        try {
            dark_sky_response_json = new JSONObject(dark_sky_response);
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

            ImageView card_icon = (ImageView) findViewById(R.id.icon_card1);
            if (card_icon != null) {
                //update_content.setText(daily_data.get("icon").toString());
                String resultImage = iconImage(currently_object.get("icon").toString());
                System.out.println("Result" + resultImage);
                int imageResource = getResources().getIdentifier(resultImage, "drawable", getPackageName());
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
                            getPackageName());
                    //System.out.println(resourceID);
                    if (resourceID != 0) {
                        TextView update_content = (TextView) findViewById(resourceID);
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
                            getPackageName());
                    System.out.println("Resource" + resourceID);
                    //System.out.println(resourceID);
                    if (resourceID != 0) {
                        ImageView update_content = (ImageView) findViewById(resourceID);
                        if (update_content != null) {
                            //update_content.setText(daily_data.get("icon").toString());
                            String resultImage = iconImage(daily_data.get("icon").toString());
                            System.out.println("Result" + resultImage);
                            int imageResource = getResources().getIdentifier(resultImage, "drawable", getPackageName());
                            Drawable selected_image = getResources().getDrawable(imageResource);
                            update_content.setImageDrawable(selected_image);
                        }
                    }
                }
                if (daily_data.get("temperatureLow") != null) {
                    int resourceID = getResources().getIdentifier(textView_obj_tempLow, "id",
                            getPackageName());
                    //System.out.println(resourceID);
                    if (resourceID != 0) {
                        TextView update_content = (TextView) findViewById(resourceID);
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
                            getPackageName());
                    //System.out.println(resourceID);
                    if (resourceID != 0) {
                        TextView update_content = (TextView) findViewById(resourceID);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_search_example_menu, menu);
        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.app_bar_menu_search);

        // Get SearchView object.
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);

        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.BLACK);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);

        // Create a new ArrayAdapter and add data to search auto complete object.

        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setAdapter(autoSuggestAdapter);
        searchAutoComplete.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        System.out.println("TAKE"+autoSuggestAdapter.getObject(position));
                        searchAutoComplete.setText(autoSuggestAdapter.getObject(position));
                    }
                });
        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                        makeApiCall(searchAutoComplete.getText().toString());
                    }
                }
                return false;
            }
        });
        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AlertDialog alertDialog = new AlertDialog.Builder(PostSearchActivity.this).create();
                alertDialog.setMessage("Search keyword is " + query);
                alertDialog.show();
                System.out.println("Hello World");
                Intent intent = new Intent(PostSearchActivity.this, PostSearchActivity.class);
                intent.putExtra("responseString", responseString);
                intent.putExtra("query", query);
                startActivity(intent);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Get the share menu item.
        MenuItem shareMenuItem = menu.findItem(R.id.app_bar_menu_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareActionProvider.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_menu_search:
                //count=(TextView)findViewById(R.id.textView);
                //count.setText("Add is clicked");
                System.out.println("Add clicked");
        }
        return (super.onOptionsItemSelected(item));
    }

    private void makeApiCall(String text) {
        ApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray array = responseObject.getJSONArray("predictions");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("description"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(stringList);
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

    }

}
