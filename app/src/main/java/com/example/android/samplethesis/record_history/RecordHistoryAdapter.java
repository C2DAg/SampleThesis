package com.example.android.samplethesis.record_history;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.samplethesis.EditRecordActivity;
import com.example.android.samplethesis.InputActivity;
import com.example.android.samplethesis.R;
import com.example.android.samplethesis.model.DailyRecordWithItem;
import com.example.android.samplethesis.model.Item;

import java.util.List;

public class RecordHistoryAdapter extends RecyclerView.Adapter<RecordHistoryAdapter.ViewHolder> {


    public RecordHistoryAdapter(List<DailyRecordWithItem> records, ClickListener clickListener) {
        this.records = records;
        this.clickListener = clickListener;
    }

    public List<DailyRecordWithItem> getRecords() {
        return records;
    }

    public void setRecords(List<DailyRecordWithItem> records) {
        this.records = records;
    }

    public void removeData(DailyRecordWithItem item){
        int position=records.indexOf(item);
        records.remove(item);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,records.size()-position);
    }

    public List<DailyRecordWithItem> records;

    private ClickListener clickListener;

    public RecordHistoryAdapter(List<Item> items, DateFormat dateFormat) {
        this.items = items;
        this.dateFormat = dateFormat;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public List<Item> items;

    android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View recordHistoryView = layoutInflater.inflate(R.layout.record_view,viewGroup,false);
        ViewHolder recordHistoryVH = new ViewHolder(recordHistoryView);
        return recordHistoryVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.dateTV.setText(dateFormat.format(" dd-MM-yyy ", records.get(i).getDailyRecord().getDate()));
        viewHolder.itemTV.setText(String.valueOf(records.get(i).getItem().getName()));
        viewHolder.catTV.setText(records.get(i).getItem().getCategory());
        viewHolder.valueTV.setText(records.get(i).getDailyRecord().getValue()+"");
        viewHolder.memoTV.setText(records.get(i).getDailyRecord().getMemo()+"");
        viewHolder.left_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewHolder.itemTV.getContext(), InputActivity.class);
                intent.putExtra("record",records.get(i));
                viewHolder.itemTV.getContext().startActivity(intent);

            }
        });


        Log.e("Item",records.get(i).getItem().toString());
        viewHolder.right_view.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(clickListener!=null){
                    clickListener.onClick(records.get(i));
                }
            }
        }));

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView dateTV;
        public TextView itemTV;
        public TextView catTV;
        public TextView valueTV;
        public TextView memoTV;
        public ImageView right_view;
        public ImageView left_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dateTV = itemView.findViewById(R.id.dateTV);
            this.itemTV = itemView.findViewById(R.id.itemTV);
            this.catTV = itemView.findViewById(R.id.catTV);
            this.valueTV = itemView.findViewById(R.id.valueTV);
            this.memoTV = itemView.findViewById(R.id.memoTV);
            this.right_view = itemView.findViewById(R.id.right_view);
            this.left_view = itemView.findViewById(R.id.left_view);

        }
    }
    public interface ClickListener{
        void onClick (DailyRecordWithItem record);
    }
}
