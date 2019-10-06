package com.example.android.samplethesis.data_adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.samplethesis.R;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>  {

    public int []icons ;
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public DataAdapter(int[] icons, RecyclerViewItemClickListener clickListener) {
        this.icons = icons;
        this.recyclerViewItemClickListener = clickListener;
    }
    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_icon_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolder viewHolder, int i) {
        viewHolder.imgView.setImageResource(icons[i]);
        viewHolder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerViewItemClickListener != null){
                    recyclerViewItemClickListener.onClick(icons[i]);
                }
            }
        });


    }



    @Override
    public int getItemCount() {
        return icons.length;
    }


    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgView = itemView.findViewById(R.id.icon);

        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.onClick(icons[this.getAdapterPosition()]);
        }
    }

    public interface RecyclerViewItemClickListener {
        void onClick(int imgRes);
    }
}