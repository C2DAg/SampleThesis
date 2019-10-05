package com.example.android.samplethesis.ui.report;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;



import com.example.android.samplethesis.AppDatabase;
import com.example.android.samplethesis.InputActivity;
import com.example.android.samplethesis.R;
import com.example.android.samplethesis.dao.DailyRecordDAO;
import com.example.android.samplethesis.dao.ItemDAO;
import com.example.android.samplethesis.model.DailyRecordWithItem;
import com.example.android.samplethesis.model.Item;
import com.github.clans.fab.FloatingActionMenu;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportFragment extends Fragment implements MonthPickerDialog.OnDateSetListener {
    AppDatabase database;
    List<Item> itemList;
    List<DailyRecordWithItem> dailyRecordList;
    private ReportViewModel reportViewModel;
    Date date = new Date();
    Date date1, date2;
    int wantedTotal, neededTotal, incomeTotal, expenseTotal, savingTotal;
    FloatingActionMenu floatingActionMenu ;
    TextView nTV, wTV, iTV, eTV, bTV, sTV;
    android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();

    MonthPickerDialog monthPickerDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportViewModel =
                ViewModelProviders.of(this).get(ReportViewModel.class);
        View root = inflater.inflate(R.layout.activity_report, container, false);
        Calendar today = Calendar.getInstance();
        date = today.getTime();
        int daysOfMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.w("getActualMaximum", "getActualMaximum" + daysOfMonth);
        today.set(Calendar.DAY_OF_MONTH, daysOfMonth);
        date2 = today.getTime();
        today.set(Calendar.DAY_OF_MONTH, 1);
        date1 = today.getTime();
        Log.e("oncreateview", "date1,date2" + date1 + "and " + date2);

        nTV = root.findViewById(R.id.neededDTV);
        wTV = root.findViewById(R.id.wantedDTV);
        iTV = root.findViewById(R.id.incomeDTV);
        eTV = root.findViewById(R.id.expenseDTV);
        bTV = root.findViewById(R.id.balanceDTV);
        sTV = root.findViewById(R.id.savingDTV);
        floatingActionMenu = root.findViewById(R.id.menu);


        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(),
                ReportFragment.this, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        monthPickerDialog = builder.setActivatedMonth(today.get(Calendar.MONTH))
                .setMinYear(1990)
                .setMaxYear(today.get(Calendar.YEAR))
                .setMaxMonth(today.get(Calendar.MONTH))
                .build();

        database = Room.databaseBuilder(getContext(), AppDatabase.class, "mydb").allowMainThreadQueries()
                .build();
        final ItemDAO itemDAO = database.getItemDAO();
        DailyRecordDAO dailyRecordDAO = database.getDailyRecordDAO();

        root.findViewById(R.id.dateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthPickerDialog.show();
            }
        });
        wantedTotal = dailyRecordDAO.getNWTotal(date1, date2, "wanted");
        neededTotal = dailyRecordDAO.getNWTotal(date1, date2, "needed");
        incomeTotal = dailyRecordDAO.getIESTotal(date1, date2, "Income");
        expenseTotal = dailyRecordDAO.getIESTotal(date1, date2, "Expense");
        savingTotal = dailyRecordDAO.getIESTotal(date1, date2, "Saving");

        nTV.setText(String.valueOf(neededTotal));
        wTV.setText(String.valueOf(wantedTotal));
        iTV.setText(String.valueOf(incomeTotal));
        eTV.setText(String.valueOf(expenseTotal));
        sTV.setText(String.valueOf(savingTotal));
        bTV.setText(String.valueOf((incomeTotal - expenseTotal)-savingTotal));

        root.findViewById(R.id.expense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getContext(), InputActivity.class);
                intent.putExtra("type", "Expense");
                startActivity(intent);
            }
        });
        root.findViewById(R.id.income).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getContext(), InputActivity.class);

                intent.putExtra("type", "Income");
                startActivity(intent);
            }
        });
        root.findViewById(R.id.saving).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getContext(), InputActivity.class);

                intent.putExtra("type", "Saving");
                startActivity(intent);

            }
        }));

        dailyRecordList = dailyRecordDAO.getDRecordWithItem();
        itemList = itemDAO.getAllItem();
        return root;
    }
    @Override
    public void onDateSet(int selectedMonth, int selectedYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selectedYear);
        calendar.set(Calendar.MONTH, selectedMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        date = calendar.getTime();
        int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.w("getActualMaximum", "getActualMaximum" + daysOfMonth);
        calendar.set(Calendar.DAY_OF_MONTH, daysOfMonth);
        date2 = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        date1 = calendar.getTime();
        Log.w("dates123", "" + date + "_MonthBeginAt" + date1 + "EndIn" + date2);
        DailyRecordDAO dailyrecordDAO = database.getDailyRecordDAO();
        wantedTotal = dailyrecordDAO.getNWTotal(date1, date2, "wanted");
        neededTotal = dailyrecordDAO.getNWTotal(date1, date2, "needed");
        incomeTotal = dailyrecordDAO.getIESTotal(date1, date2, "Income");
        expenseTotal = dailyrecordDAO.getIESTotal(date1, date2, "Expense");
        savingTotal = dailyrecordDAO.getIESTotal(date1, date2, "Saving");

        nTV.setText(String.valueOf(neededTotal));
        wTV.setText(String.valueOf(wantedTotal));
        iTV.setText(String.valueOf(incomeTotal));
        eTV.setText(String.valueOf(expenseTotal));
        sTV.setText(String.valueOf(savingTotal));
        bTV.setText(String.valueOf((incomeTotal - expenseTotal)-savingTotal));

        Log.e("HERE", wantedTotal + "");
        Log.e("HERE", wantedTotal + "");
        Log.e("HERE", neededTotal + "");
        Log.e("HERE", incomeTotal + "");
        Log.e("HERE", expenseTotal + "");
        Log.e("HERE", savingTotal + "");

    }
    @Override
    public void onResume() {
        super.onResume();
        DailyRecordDAO dailyrecordDAO = database.getDailyRecordDAO();
        floatingActionMenu.close(true);
        wantedTotal = dailyrecordDAO.getNWTotal(date1, date2, "wanted");
        neededTotal = dailyrecordDAO.getNWTotal(date1, date2, "needed");
        incomeTotal = dailyrecordDAO.getIESTotal(date1, date2, "Income");
        expenseTotal = dailyrecordDAO.getIESTotal(date1, date2, "Expense");
        savingTotal = dailyrecordDAO.getIESTotal(date1, date2, "Saving");

        nTV.setText(String.valueOf(neededTotal));
        wTV.setText(String.valueOf(wantedTotal));
        iTV.setText(String.valueOf(incomeTotal));
        eTV.setText(String.valueOf(expenseTotal));
        sTV.setText(String.valueOf(savingTotal));
        bTV.setText(String.valueOf((incomeTotal - expenseTotal)-savingTotal));



    }



}
