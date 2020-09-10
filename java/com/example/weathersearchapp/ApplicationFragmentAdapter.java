package com.example.weathersearchapp;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ApplicationFragmentAdapter extends FragmentPagerAdapter {
    private Context context;
    protected String responseString = null;
    protected String responseCity = null;
    private final List<ApplicationFragment> FragmentList = new ArrayList<>();
    private List<String> favourites = new ArrayList<>();

    public ApplicationFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }
    @Override
    public int getCount() {
        return favourites.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    public void addFavourites(String weatherString) {
        favourites.add(weatherString);
        FragmentList.add(new ApplicationFragment());
    }


    @Override
    public Fragment getItem(int position) {
        ApplicationFragment fragment = FragmentList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("weather_string", favourites.get(position));
        bundle.putInt("position", position);
        Log.d(ApplicationFragmentAdapter.class.getSimpleName(), "Position: " + position + " weather_string_val = "  + favourites.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

}