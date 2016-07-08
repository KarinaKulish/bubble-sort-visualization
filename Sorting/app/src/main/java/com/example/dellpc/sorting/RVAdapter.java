package com.example.dellpc.sorting;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Created by DELL PC on 06.07.2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.SheepViewHolder>{
    public static class SheepViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView sheepImage;

        SheepViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            sheepImage = (ImageView)itemView.findViewById(R.id.sheep_image);
        }

    }

    List<Sheep> sheep;
    RVAdapter(List<Sheep> sheep){
        this.sheep=sheep;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SheepViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        SheepViewHolder svh = new SheepViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(SheepViewHolder sheepViewHolder, int i) {
        sheepViewHolder.sheepImage.setImageResource(sheep.get(i).id);
    }

    @Override
    public int getItemCount() {
        return sheep.size();
    }


}
