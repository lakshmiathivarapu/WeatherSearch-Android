package com.example.weathersearchapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String responseString;
    String responseCity;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private String append_current_location_query ="";
    String city_name;
    String state_name;
    String country_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        // Set below attributes to add logo in ActionBar.
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.frameLayout);
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        String current_location_url = "http://ip-api.com/json";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, current_location_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string
                        //System.out.println(response);
                        double latitude;
                        double longitude;
                        String dark_sky_url_call = "http://lakshmiath-weather-search.us-east-1.elasticbeanstalk.com/currentlocation";
                        try {
                            JSONObject response_json = new JSONObject(response);
                            city_name = response_json.get("city").toString();
                            state_name = response_json.get("region").toString();
                            country_name = response_json.get("countryCode").toString();
                            append_current_location_query=(response_json.get("city") + ", " + response_json.get("region") + ", " + response_json.get("countryCode"));
                            System.out.println("current_location"+append_current_location_query);
                            ApplicationFragmentAdapter fragmentPagerAdapter = new ApplicationFragmentAdapter(getSupportFragmentManager(),MainActivity.this);

                            SharedPreferences mSharedPreferences = getSharedPreferences("Query_map", Context.MODE_PRIVATE);
                            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                            mEditor.putString(city_name,append_current_location_query);
                            System.out.println("current_location"+append_current_location_query);
                            fragmentPagerAdapter.addFavourites(append_current_location_query);
                            //favourites.add(append_current_location_query);

                            Map<String, ?> keys = mSharedPreferences.getAll();
                            for (Map.Entry<String, ?> entry: keys.entrySet()) {
                                fragmentPagerAdapter.addFavourites(entry.getValue().toString());
                                //favourites.add(entry.getValue().toString());
                            }
                            //numTabs = fragmentPagerAdapter.getCount();
                            viewPager.setAdapter(fragmentPagerAdapter);
                            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                            tabLayout.setupWithViewPager(viewPager, true);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error couldn't fetch");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

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
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage("Search keyword is " + query);
                alertDialog.show();
                System.out.println("Hello World");
                Intent intent = new Intent(MainActivity.this, PostSearchActivity.class);
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
