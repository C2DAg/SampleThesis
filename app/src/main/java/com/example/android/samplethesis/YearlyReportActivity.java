package com.example.android.samplethesis;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.android.samplethesis.dao.DailyRecordDAO;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class YearlyReportActivity extends AppCompatActivity {
    Button btn,nBtn,wBtn,sBtn,bBtn;
    static Dialog d;
    String financeType;
    int year = Calendar.getInstance().get(Calendar.YEAR);
    Date date,date1,date2;
    AppDatabase database;
    float wantedTotal,neededTotal,incomeTotal,expenseTotal,savingTotal,withdrawTotal,balance;
    DailyRecordDAO dailyRecordDAO;
    static  Calendar today;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly_report);
        BarChart chart = findViewById(R.id.barchart);
        BarChart chart2 = findViewById(R.id.barchart2);
        today = Calendar.getInstance();
        date = today.getTime();
        today.setTime(date);
        year = today.get(Calendar.YEAR);
        int daysOfMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.w("getActualMaximum", "getActualMaximum" + daysOfMonth);
        today.set(Calendar.DAY_OF_MONTH, daysOfMonth);
        date2 = today.getTime();
        today.set(Calendar.DAY_OF_MONTH, 1);
        date1 = today.getTime();
        Log.e("oncreateview", "date1,date2" + date1 + "and " + date2);
        database = Room.databaseBuilder(this, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        dailyRecordDAO = database.getDailyRecordDAO();
        ArrayList<String> month = new ArrayList<String>();
        month.add("Jan");
        month.add("Feb");
        month.add("March");
        month.add("April");
        month.add("May");
        month.add("June");
        month.add("July");
        month.add("Aug");
        month.add("Sep");
        month.add("Oct");
        month.add("Nov");
        month.add("Dec");
        ArrayList<BarEntry> totalValues = new ArrayList<BarEntry>();
        Calendar c=Calendar.getInstance();
        for(int i=0; i<12;i++){
            c.set(Calendar.YEAR,year);
            c.set(Calendar.MONTH,i);
            c.set(Calendar.DAY_OF_MONTH,0);
            c.set(Calendar.HOUR_OF_DAY,0);
            Date startDate = c.getTime();
            int lastDay =c.getActualMaximum(Calendar.DAY_OF_MONTH);
            c.set(Calendar.YEAR,year);
            c.set(Calendar.DAY_OF_MONTH,lastDay);
            c.set(Calendar.HOUR_OF_DAY,24);
            Date endDate = c.getTime();
            incomeTotal = dailyRecordDAO.getIEMonthTotal("Income",startDate,endDate);
            totalValues.add(new BarEntry(incomeTotal, i));
            Log.e("first bar chart ", "i = "+ i +" ---income total =" +incomeTotal);
        }
        BarDataSet bardataset = new BarDataSet(totalValues, "Monthly Total");
        chart.animateY(2000);
        BarData data = new BarData(month, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);
//For second bar chart
        ArrayList<BarEntry> percentages = new ArrayList<BarEntry>();
        nBtn=findViewById(R.id.nEBtn);
        nBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                financeType="needed";
                for(int i=0; i<12;i++){
                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.MONTH,i);
                    c.set(Calendar.DAY_OF_MONTH,0);
                    c.set(Calendar.HOUR_OF_DAY,0);
                    Date startDate = c.getTime();
                    int lastDay =c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.DAY_OF_MONTH,lastDay);
                    c.set(Calendar.HOUR_OF_DAY,24);
                    Date endDate = c.getTime();
                    neededTotal = dailyRecordDAO.getNWMonthTotal(financeType,startDate,endDate);
                    incomeTotal = dailyRecordDAO.getIEMonthTotal("Income",startDate,endDate);
                    percentages.add(new BarEntry((int)(neededTotal*100/incomeTotal ), i));
                    Log.e("Sec bar chart ", "i = "+ i +" ---need total =" +neededTotal);
                }
                BarDataSet bardataset2 = new BarDataSet(percentages, "Monthly Total");
                chart2.animateY(2000);
                BarData data2 = new BarData(month, bardataset2);
                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                chart2.setData(data2);
            }
        });
        wBtn=findViewById(R.id.wEBtn);
        wBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                financeType="wanted";
                for(int i=0; i<12;i++){
                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.MONTH,i);
                    c.set(Calendar.DAY_OF_MONTH,0);
                    c.set(Calendar.HOUR_OF_DAY,0);
                    Date startDate = c.getTime();
                    int lastDay =c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.DAY_OF_MONTH,lastDay);
                    c.set(Calendar.HOUR_OF_DAY,24);
                    Date endDate = c.getTime();
                    neededTotal = dailyRecordDAO.getNWMonthTotal(financeType,startDate,endDate);
                    incomeTotal = dailyRecordDAO.getIEMonthTotal("Income",startDate,endDate);
                    percentages.add(new BarEntry((int)(neededTotal*100/incomeTotal ), i));
                    Log.e("sec bar chart ", "i = "+ i +" ---want total =" +neededTotal);

                }
                BarDataSet bardataset2 = new BarDataSet(percentages, "Monthly Total");
                chart2.animateY(2000);
                BarData data2 = new BarData(month, bardataset2);
                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                chart2.setData(data2);
            }
        });
        sBtn=findViewById(R.id.sBtn);
        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<12;i++) {
                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.MONTH,i);
                    c.set(Calendar.DAY_OF_MONTH,0);
                    c.set(Calendar.HOUR_OF_DAY,0);
                    Date startDate = c.getTime();
                    int lastDay =c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.DAY_OF_MONTH,lastDay);
                    c.set(Calendar.HOUR_OF_DAY,24);
                    Date endDate = c.getTime();
                    savingTotal = dailyRecordDAO.getSMonthTotal("Saving", startDate, endDate);
                    incomeTotal = dailyRecordDAO.getIEMonthTotal("Income",startDate,endDate);
                    percentages.add(new BarEntry(savingTotal*100/incomeTotal, i));
                    Log.e("sec bar chart ", "i = "+ i +" ---sav total =" +savingTotal);


                }
                BarDataSet bardataset2 = new BarDataSet(percentages, "Monthly Total");
                chart2.animateY(2000);
                BarData data2 = new BarData(month, bardataset2);
                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                chart2.setData(data2);
            }
        });
        bBtn=findViewById(R.id.bBtn);
        bBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<12;i++) {
                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.MONTH,i);
                    c.set(Calendar.DAY_OF_MONTH,0);
                    c.set(Calendar.HOUR_OF_DAY,0);
                    Date startDate = c.getTime();
                    int lastDay =c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.DAY_OF_MONTH,lastDay);
                    c.set(Calendar.HOUR_OF_DAY,24);
                    Date endDate = c.getTime();
                    savingTotal = dailyRecordDAO.getSMonthTotal("Saving", startDate, endDate);
                    expenseTotal=dailyRecordDAO.getIEMonthTotal("Expense",startDate,endDate);
                    withdrawTotal=dailyRecordDAO.getWithdrawMonthTotal("Withdraw",startDate,endDate);
                    incomeTotal = dailyRecordDAO.getIEMonthTotal("Income",startDate,endDate);
                    float bal = (incomeTotal+withdrawTotal-savingTotal-expenseTotal)*100/incomeTotal;
                    percentages.add(new BarEntry(bal, i));
                    Log.e("first bar chart ", "i = "+ i +" ---income total =" +bal);

                }
                BarDataSet bardataset2 = new BarDataSet(percentages, "Monthly Total");
                chart2.animateY(2000);
                BarData data2 = new BarData(month, bardataset2);
                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                chart2.setData(data2);
            }
        });


        (findViewById(R.id.backBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YearlyReportActivity.this, MenuDrawerActivity.class);
                startActivity(intent);
            }
        });
        btn = (Button) findViewById(R.id.yearPick);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearDialog();
            }
        });
    }

    public void showYearDialog() {

        final Dialog d = new Dialog(YearlyReportActivity.this);
        d.setTitle("Year Picker");
        d.setContentView(R.layout.year_picker_dialog);
        Button set = (Button) d.findViewById(R.id.button1);
        Button cancel = (Button) d.findViewById(R.id.button2);
        TextView year_text = (TextView) d.findViewById(R.id.year_text);
        year_text.setText("" + year);
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year);
        nopicker.setMinValue(year - 10);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setText(String.valueOf(nopicker.getValue()));
                year = nopicker.getValue();
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

}
