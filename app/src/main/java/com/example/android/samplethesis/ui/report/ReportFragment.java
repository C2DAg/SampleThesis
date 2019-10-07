package com.example.android.samplethesis.ui.report;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.android.samplethesis.YearlyReportActivity;
import com.example.android.samplethesis.dao.DailyRecordDAO;
import com.example.android.samplethesis.dao.ItemDAO;
import com.example.android.samplethesis.model.DailyRecordWithItem;
import com.example.android.samplethesis.model.Item;
import com.github.clans.fab.FloatingActionMenu;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.whiteelephant.monthpicker.MonthPickerDialog;
import java.util.ArrayList;
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
    int wantedTotal, neededTotal, incomeTotal, expenseTotal, savingTotal, withdrawTotal,balance;
    FloatingActionMenu floatingActionMenu ;
    TextView nTV, wTV, iTV, eTV, bTV, sTV,wdTV;
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
        PieChart pieChart = root.findViewById(R.id.piechart);
        nTV = root.findViewById(R.id.neededDTV);
        wTV = root.findViewById(R.id.wantedDTV);
        iTV = root.findViewById(R.id.incomeDTV);
        eTV = root.findViewById(R.id.expenseDTV);
        bTV = root.findViewById(R.id.balanceDTV);
        sTV = root.findViewById(R.id.savingDTV);
        wdTV = root.findViewById(R.id.withdrawDTV);
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
        root.findViewById(R.id.yearBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), YearlyReportActivity.class);
                startActivity(intent);
            }
        });
        wantedTotal = dailyRecordDAO.getNWTotal(date1, date2, "wanted");
        neededTotal = dailyRecordDAO.getNWTotal(date1, date2, "needed");
        incomeTotal = dailyRecordDAO.getIESTotal(date1, date2, "Income");
        expenseTotal = dailyRecordDAO.getIESTotal(date1, date2, "Expense");
        savingTotal = dailyRecordDAO.getIESTotal(date1, date2, "Saving");
        withdrawTotal = dailyRecordDAO.getWithdrawTotal(date1,date2, "Withdraw");
        balance = incomeTotal-(expenseTotal+savingTotal-withdrawTotal);

        nTV.setText(String.valueOf(neededTotal));
        wTV.setText(String.valueOf(wantedTotal));
        iTV.setText(String.valueOf(incomeTotal));
        eTV.setText(String.valueOf(expenseTotal));
        sTV.setText(String.valueOf(savingTotal-withdrawTotal));
        bTV.setText(String.valueOf((incomeTotal - (expenseTotal+savingTotal-withdrawTotal))));
        wTV.setText(String.valueOf(withdrawTotal));

        float[] totals = {neededTotal,wantedTotal,savingTotal-withdrawTotal,withdrawTotal,balance};
        String[] names = {"Needed Expense","Wanted Expense","Saving","Withdraw Saving","Balance"};
        int[] colors = {Color.rgb(152,106,255),Color.rgb(182,2,243),Color.rgb(6,158,207),
                Color.rgb(255,153,10),Color.rgb(84,2,231)};
        ArrayList NoOfEmp = new ArrayList();


//
//        NoOfEmp.add(new Entry((float)(neededTotal*100/incomeTotal), 0));
//        NoOfEmp.add(new Entry((float)(wantedTotal*100/incomeTotal), 1));
//        NoOfEmp.add(new Entry((float)((savingTotal-withdrawTotal)*100/incomeTotal), 2));
//        NoOfEmp.add(new Entry((float)(withdrawTotal*100/incomeTotal), 3));
//        NoOfEmp.add(new Entry((float)(balance*100/incomeTotal), 4));

        PieDataSet dataSet = new PieDataSet(NoOfEmp, "Number Of Employees");

        ArrayList year = new ArrayList();

        year.add("Needed Expense");
        year.add("Wanted Expense");
        year.add("Saving");
        year.add("Withdraw Saving");
        year.add("Balance");

        PieData data = new PieData(year, dataSet);
        pieChart.setData(data);
        dataSet.setColors(colors);
        pieChart.animateXY(3000, 3000);

        root.findViewById(R.id.expense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), InputActivity.class);
                intent.putExtra("type", "Expense");
                startActivity(intent);
            }
        });
        root.findViewById(R.id.income).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(getContext(), InputActivity.class);
                intent.putExtra("type", "Income");
                startActivity(intent);
            }
        });
        root.findViewById(R.id.withdraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), InputActivity.class);
                intent.putExtra("type", "Withdraw");
                startActivity(intent);
            }
        });
        root.findViewById(R.id.saving).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        withdrawTotal = dailyrecordDAO.getWithdrawTotal(date1,date2, "Withdraw");
        balance = incomeTotal-(expenseTotal+savingTotal-withdrawTotal);
        nTV.setText(String.valueOf(neededTotal));
        wTV.setText(String.valueOf(wantedTotal));
        iTV.setText(String.valueOf(incomeTotal));
        eTV.setText(String.valueOf(expenseTotal));
        sTV.setText(String.valueOf(savingTotal));
        bTV.setText(String.valueOf((incomeTotal - expenseTotal)-savingTotal));

    }



}
