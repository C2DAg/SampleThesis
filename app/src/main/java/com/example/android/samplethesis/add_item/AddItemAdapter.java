package com.example.android.samplethesis.add_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.samplethesis.R;

public class AddItemAdapter extends RecyclerView.Adapter<AddItemAdapter.ViewHolder> {
    public int []icons ;
    private ClickListener clickListener;

    public AddItemAdapter(int[] icons, ClickListener clickListener) {
        this.icons = icons;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View iconView = layoutInflater.inflate(R.layout.add_icon_view,viewGroup,false);
        ViewHolder iconViewHolder = new ViewHolder(iconView);
        return iconViewHolder;
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.imgView.setImageResource(icons[i]);
        viewHolder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onClick(icons[i]);
                }
            }
        });



    }

    @Override
    public int getItemCount() {

        return icons.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgView = itemView.findViewById(R.id.icon);

        }

        public ImageView imgView;

    }

    public AddItemAdapter(int[] icons) {
        this.icons = icons;
    }

    public int[] getIcons() {
        return icons;
    }

    public void setIcons(int[] icons) {
        this.icons = icons;
    }

    public interface ClickListener {
        void onClick(int imgRes);

    }
}
