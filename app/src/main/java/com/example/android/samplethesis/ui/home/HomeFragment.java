package com.example.android.samplethesis.ui.home;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.android.samplethesis.AppDatabase;
import com.example.android.samplethesis.InputActivity;
import com.example.android.samplethesis.R;
import com.example.android.samplethesis.dao.DailyRecordDAO;
import com.example.android.samplethesis.dao.ItemDAO;
import com.example.android.samplethesis.model.DailyRecord;
import com.example.android.samplethesis.model.DailyRecordWithItem;
import com.example.android.samplethesis.model.Item;
import com.example.android.samplethesis.record_history.RecordHistoryAdapter;
import com.github.clans.fab.FloatingActionMenu;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private HomeViewModel homeViewModel;
    AppDatabase database;
    List<Item> itemList;
    List<DailyRecordWithItem> dailyRecordWithItemList;
    DailyRecord recordD;
    ItemDAO itemDAO;
    DailyRecordDAO dailyRecordDAO;
    RecordHistoryAdapter recordHistoryAdapter;
    FloatingActionMenu floatingActionMenu ;
    Button dateBtn;
    Date date;
    Date dateE;
    android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.activity_records_history, container, false);

        database = Room.databaseBuilder(getContext(), AppDatabase.class,"mydb").allowMainThreadQueries()
                .build();
        itemDAO = database.getItemDAO();
        dailyRecordDAO = database.getDailyRecordDAO();
        root.findViewById(R.id.recordHisContent).setVisibility(View.VISIBLE);
        floatingActionMenu = root.findViewById(R.id.menu);
        dateBtn=root.findViewById(R.id.dateTVw);
        Calendar calendar = Calendar.getInstance();
        date=calendar.getTime();
        //Date button
        dateBtn.setText(dateFormat.format("dd-MM-yyy",date));
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  Calendar now = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), HomeFragment.this,
                        now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        //Floating menu
        root.findViewById(R.id.expense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(getContext() , InputActivity.class);
                intent.putExtra("type","Expense");
                startActivity(intent);
            }
        });
        root.findViewById(R.id.income).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , InputActivity.class);
                intent.putExtra("type","Income");
                startActivity(intent);
            }
        });
        root.findViewById(R.id.saving).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , InputActivity.class);
                intent.putExtra("type","Saving");
                startActivity(intent);
            }
        }));
        root.findViewById(R.id.withdraw).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , InputActivity.class);
                intent.putExtra("type","Withdraw");
                startActivity(intent);
            }
        }));
        dailyRecordWithItemList =dailyRecordDAO.getDRecordWithItem();
        itemList = itemDAO.getAllItem();
        RecyclerView recyclerView = root.findViewById(R.id.recordHistoryRV);
         recordHistoryAdapter = new RecordHistoryAdapter(dailyRecordWithItemList, new RecordHistoryAdapter.ClickListener() {
            @Override
            public void onClick(DailyRecordWithItem record) {
                recordD = record.getDailyRecord();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                // Set a title for alert dialog
                builder.setTitle("DELETE!!!");
                // Ask the final question
                builder.setMessage("Do You Really Want To Delete The Record?");

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                // User clicked the Yes button
                                dailyRecordDAO.delete(recordD);
//                                dailyRecordWithItemList =dailyRecordDAO.getDRecordWithItem();
//                                recordHistoryAdapter.setRecords(dailyRecordWithItemList);
//                                recordHistoryAdapter.notifyDataSetChanged();
                                recordHistoryAdapter.removeData(record);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
                                break;
                        }
                    }
                };

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Yes", dialogClickListener);

                // Set the alert dialog no button click listener
                builder.setNegativeButton("No",dialogClickListener);

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recordHistoryAdapter);
        return root;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY,0);
        date=c.getTime();
        c.set(Calendar.HOUR_OF_DAY,24);
        dateE=c.getTime();
        dateBtn.setText(dateFormat.format("dd-MM-yyy",date));
        dailyRecordWithItemList =dailyRecordDAO.getDayRecordWithItem(date,dateE);
        recordHistoryAdapter.setRecords(dailyRecordWithItemList);
        recordHistoryAdapter.notifyDataSetChanged();
    }
    @Override
    public void onResume() {
       super.onResume();
       dailyRecordWithItemList = dailyRecordDAO.getDRecordWithItem();
       recordHistoryAdapter.setRecords(dailyRecordWithItemList);
       recordHistoryAdapter.notifyDataSetChanged();
       floatingActionMenu.close(true);
    }

}
