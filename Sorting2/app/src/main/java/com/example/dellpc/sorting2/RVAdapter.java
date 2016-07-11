package com.example.dellpc.sorting2;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by DELL PC on 10.07.2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.SheepViewHolder> {
    public static class SheepViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView sheepText;

        SheepViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            sheepText = (TextView) itemView.findViewById(R.id.textView);
        }
    }
    List<Sheep> sheep;

    RVAdapter(List<Sheep> sheep) {
        this.sheep = sheep;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
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
        Sheep sheep1=sheep.get(i);
       sheepViewHolder.sheepText.setText(sheep1.getText());

    }

    @Override
    public int getItemCount() {
        return sheep.size();
    }

}
