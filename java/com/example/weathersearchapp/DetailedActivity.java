package com.example.weathersearchapp;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailedActivity extends AppCompatActivity {

    String response;
    String city;
    private int[] navIcons = {
            R.drawable.calendar_today,
            R.drawable.trending_up,
            R.drawable.google_photos
    };
    private int[] navLabels = {
            R.string.today,
            R.string.weekly,
            R.string.photos
    };
    // another resouces array for active state for the icon
    private int[] navIconsActive = {
            R.drawable.calendar_today_white,
            R.drawable.trending_up_white,
            R.drawable.google_photos_white
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Bundle extras = getIntent().getExtras();
        response = extras.getString("responseString");
        city = extras.getString("city");
        System.out.println(response);
        System.out.println("City found"+city);
        ActionBar actionBar = getSupportActionBar();
        // Set below attributes to add logo in ActionBar.
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        DetailedFragmentPagerAdapter fragmentPagerAdapter = new DetailedFragmentPagerAdapter(getSupportFragmentManager(),DetailedActivity.this);
        fragmentPagerAdapter.setResponseData(response);
        fragmentPagerAdapter.setResponseCity(city);
        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout navigation = (TabLayout) findViewById(R.id.sliding_tabs);
        navigation.setupWithViewPager(viewPager);

        for (int i = 0; i < navigation.getTabCount(); i++) {

            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.fragment_info, null);
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);

            tab_label.setText(getResources().getText(navLabels[i]));

            if(i == 0) {
                tab_label.setTextColor(Color.WHITE);
                tab_icon.setImageResource(navIconsActive[i]);
            } else {
                tab_icon.setImageResource(navIcons[i]);
            }
            navigation.getTabAt(i).setCustomView(tab);
        }

        navigation.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        View tabView = tab.getCustomView();
                        TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                        ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
                        tab_label.setTextColor(Color.WHITE);
                        tab_icon.setImageResource(navIconsActive[tab.getPosition()]);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        View tabView = tab.getCustomView();
                        TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                        ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
                        tab_label.setTextColor(Color.DKGRAY);
                        tab_icon.setImageResource(navIcons[tab.getPosition()]);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.twitter_share, menu);
        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.app_bar_twitter);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_twitter:
                try {
                    JSONObject dark_sky_json = new JSONObject(response);
                    Intent tweet = new Intent(Intent.ACTION_VIEW);
                    JSONObject currently_object = dark_sky_json.getJSONObject("currently");
                            String message = "Check Out "+city+"’s Weather! It is "+currently_object.get("temperature")+"°F! #CSCI571WeatherSearch";
                    tweet.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + Uri.encode(message)));//where message is your string message
                    startActivity(tweet);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("Twitter clicked");
        }
        return (super.onOptionsItemSelected(item));
    }
}