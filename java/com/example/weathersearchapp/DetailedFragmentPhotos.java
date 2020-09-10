package com.example.weathersearchapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DetailedFragmentPhotos extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    protected  static List<PhotoListItem> listItems;
    protected static String responseString;
    protected static String responseCity;

    private int mPage;

    public static DetailedFragmentPhotos newInstance(int page, String response, String city) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        System.out.println("Response Detailed Fragment Photos" + response);
        setResponseData(response);
        System.out.println(city);
        setResponseCity(city);
        DetailedFragmentPhotos fragment = new DetailedFragmentPhotos();
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
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        listItems = new ArrayList<>();
        String fetch_photos_url = null;
        try {
            fetch_photos_url = "http://weathersearch-android.us-east-1.elasticbeanstalk.com/displaystatelogo?city="+ URLEncoder.encode(getResponseCity(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.imageRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        listItems = new ArrayList<>();
        System.out.println(fetch_photos_url);
        listItems = new ArrayList<>();
        final RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, fetch_photos_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray all_links = new JSONArray(response);
                            System.out.println(all_links);
                            for(int i=0;i<all_links.length();i++) {
                                JSONObject each_link_data = all_links.getJSONObject(i);
                                PhotoListItem item = new PhotoListItem(
                                        each_link_data.get("title").toString(),
                                        each_link_data.get("link").toString()
                                );
                                System.out.println("photolistitem"+item);
                                listItems.add(item);
                                System.out.println("LIST" + getListItems());
                                adapter = new PicasoAdapter(listItems, getActivity().getApplicationContext());
                                recyclerView.setAdapter(adapter);
                            }
                            setListItems(listItems);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(response);
                        System.out.println("Response for photos");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error couldn't fetch");
            }
        });
        queue.add(stringRequest);
        return view;
    }

    public static void setResponseData(String response){
        responseString = response;
        System.out.println("method called");
    }

    public static String getResponseData(){
        return responseString;
    }

    public static void setResponseCity(String response){
        responseCity = response;
        System.out.println("method called");
    }

    public static String getResponseCity(){
        return responseCity;
    }

    public static void setListItems(List<PhotoListItem> listItemsPhoto){
        listItems = listItemsPhoto;
        System.out.println("method called");
    }

    public static List<PhotoListItem> getListItems(){
        return listItems;
    }

    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}