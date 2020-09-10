package com.example.weathersearchapp;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DetailedFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private Context context;
    protected String responseString = null;
    protected String responseCity = null;

    public DetailedFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0)
            return DetailedFragmentInfo.newInstance(position + 1,getResponseData(), getResponseCity() );
        else if ( position == 1 )
            return DetailedFragmentGraph.newInstance(position + 1, getResponseData(),getResponseCity());
        else
            return DetailedFragmentPhotos.newInstance(position + 1, getResponseData(),getResponseCity());
    }

    public void setResponseData(String response){
        responseString = response;
        System.out.println("method called");
    }

    public String getResponseData(){
        return responseString;
    }

    public void setResponseCity(String response){
        responseCity = response;
        System.out.println("method called");
    }

    public String getResponseCity(){
        return responseCity;
    }
}