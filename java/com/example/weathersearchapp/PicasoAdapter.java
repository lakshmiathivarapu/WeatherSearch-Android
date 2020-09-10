package com.example.weathersearchapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PicasoAdapter extends RecyclerView.Adapter<PicasoAdapter.ViewHolder> {
    private List<PhotoListItem> listItems;
    private Context context;

    public PicasoAdapter(List<PhotoListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PhotoListItem listItem = listItems.get(position);

        Picasso.with(context)
                .load(listItem.getImageUrl())
                .into(holder.imageRecyclerView);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.individual_item, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageRecyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageRecyclerView = (ImageView)itemView.findViewById(R.id.imageRecyclerView);

        }
    }
}