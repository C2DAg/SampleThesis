package com.example.android.samplethesis.ExpenseInput;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.samplethesis.R;
import com.example.android.samplethesis.model.Item;

import java.util.List;

public class InputExpenseAdapter extends RecyclerView.Adapter<InputExpenseAdapter.ViewHolder>{
    public List<Item> exItem;
    private ClickListener clickListener;

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }



    public InputExpenseAdapter(List<Item> exItem) {
        this.exItem = exItem;

    }

    public List<Item> getExItem() {
        return exItem;
    }

    public void setExItem(List<Item> exItem) {
        this.exItem = exItem;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View exIconView = layoutInflater.inflate(R.layout.expense_icon_view,viewGroup,false);
        ViewHolder exIconViewHolder = new ViewHolder(exIconView);
        return exIconViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.imgView.setImageResource(exItem.get(i).getIcon());
        viewHolder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onClick(exItem.get(i));
                }
            }
        });
        viewHolder.textView.setText(exItem.get(i).getName());


        Log.e("item",exItem.get(i).toString());
    }


    @Override
    public int getItemCount() {
        return exItem.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgView;
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgView = itemView.findViewById(R.id.exIcon);
            this.textView = itemView.findViewById(R.id.exText);
        }
    }
    public interface ClickListener{
        void onClick (Item item);
    }

}
