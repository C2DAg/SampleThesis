package com.example.android.samplethesis;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.samplethesis.ExpenseInput.InputExpenseAdapter;
import com.example.android.samplethesis.dao.DailyRecordDAO;
import com.example.android.samplethesis.model.DailyRecord;
import com.example.android.samplethesis.model.DailyRecordWithItem;
import com.example.android.samplethesis.model.Item;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class InputActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    AppDatabase database;
    List<Item> itemList;
    Button[] btn = new Button[16];
    Button memoBtn;
    Button dateBtn;
    TextView userInput;
    TextView itemNameTV;
    ImageView imgV;
    LinearLayout inputExpenseView;
    LinearLayout warninglyt;
    String input;
    String memo;
    String financeType;
    String stack;
    String type = "";
    String catType="";
    long value;
    long lastItemWithdraw;
    long totalItemSaving;
    long lastRecordValue;
    long totalIncome;
    long monthRecord;
    long totalSaving;
    long totalNeededEx;
    long totalWantedEX;
    long itemId;
    double wantedLevel = 0.3;
    double neededLevel = 0.5;
    double savingLevel = 0.2;
    RadioGroup radioGroup;
    RadioButton neededBtn;
    RadioButton wantedBtn;
    Date date = new Date();
    Date date1 = new Date();
    Date date2 = new Date();
    DatePickerDialog datePickerDialog;
    DailyRecordWithItem dailyRecordWithItem = null;
    InputExpenseAdapter inputExpenseAdapter;
    Toolbar toolbar;
    android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        database = Room.databaseBuilder(this, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        DailyRecordDAO dailyRecordDAO = database.getDailyRecordDAO();
        userInput = findViewById(R.id.editNumPad);
        type = getIntent().getStringExtra("type");
        dailyRecordWithItem = (DailyRecordWithItem) getIntent().getSerializableExtra("record");
        Log.w("dailyrecordwithitem", "rwi" + dailyRecordWithItem);
        Calendar calendar = Calendar.getInstance();
        date = calendar.getTime();
        dateBtn=findViewById(R.id.dateBtn);
        dateBtn.setText(dateFormat.format("dd-MM-yyy",date));
        toolbar = (Toolbar) findViewById(R.id.inputToolbar);
        setSupportActionBar(toolbar);
        warninglyt = findViewById(R.id.warningLayout);
        inputExpenseView = findViewById(R.id.inputExpenseView);
        radioGroup = findViewById(R.id.financeType);
        neededBtn = findViewById(R.id.needed);
        wantedBtn = findViewById(R.id.wanted);
        memoBtn = findViewById(R.id.memoBtn);
        btn[0] = findViewById(R.id.btnZero);
        btn[1] = findViewById(R.id.btnAlarm);
        btn[2] = findViewById(R.id.btn2);
        btn[3] = findViewById(R.id.btn3);
        btn[4] = findViewById(R.id.btn4);
        btn[5] = findViewById(R.id.btn5);
        btn[6] = findViewById(R.id.btn6);
        btn[7] = findViewById(R.id.btn7);
        btn[8] = findViewById(R.id.btn8);
        btn[9] = findViewById(R.id.btn9);
        btn[10] = findViewById(R.id.btnSub);
        btn[11] = findViewById(R.id.btnPlus);
        btn[12] = findViewById(R.id.btnMulti);
        btn[13] = findViewById(R.id.btnEqual);
        btn[14] = findViewById(R.id.btnDel);
        btn[15] = findViewById(R.id.btnSave);
        imgV = findViewById(R.id.addItemIcon);
        itemNameTV = findViewById(R.id.itemName);
        if(type=="Withdraw"){
            catType="Saving";
        }else{
            catType = type;
        }
        itemList = database.getItemDAO().getItemByCat(catType);
        RecyclerView recyclerView = findViewById(R.id.inputExpenseRecyclerView);
        inputExpenseAdapter = new InputExpenseAdapter(itemList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(inputExpenseAdapter);
        datePickerDialog = new DatePickerDialog(
                this, InputActivity.this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        if (dailyRecordWithItem == null) {
            // ((TextView) findViewById(R.id.dateToolbar)).setText(dateFormat.format("dd-MM-yyy ", date));
            if (type.equalsIgnoreCase("Income") || type.equalsIgnoreCase("Saving")) {
                Toast.makeText(InputActivity.this, "" + type, Toast.LENGTH_SHORT).show();
                ((TextView) findViewById(R.id.thisMonthPurchase)).setText("This Month Record:");
                ((TextView) findViewById(R.id.lastPurchase)).setText("Last record Of Item:" );
                findViewById(R.id.financeType).setVisibility(View.GONE);
        } else if (type.equalsIgnoreCase("Withdraw") ) {
            Toast.makeText(InputActivity.this, "" + type, Toast.LENGTH_SHORT).show();
            ((TextView) findViewById(R.id.thisMonthPurchase)).setText("Total Saving in Item:");
            ((TextView) findViewById(R.id.lastPurchase)).setText("Last record of Item:" );
            findViewById(R.id.financeType).setVisibility(View.GONE);
        }else{
        Toast.makeText(InputActivity.this, "" + type, Toast.LENGTH_SHORT).show();
            }
    }else if (dailyRecordWithItem != null) {
            type= String.valueOf(dailyRecordWithItem.getItem().getCategory());
            memo = String.valueOf(dailyRecordWithItem.getDailyRecord().getMemo());
            if (dailyRecordWithItem.getItem().getCategory().equalsIgnoreCase("Income")||
                    dailyRecordWithItem.getItem().getCategory().equalsIgnoreCase("Saving")) {
                Toast.makeText(InputActivity.this, "" + dailyRecordWithItem.getItem().getCategory(), Toast.LENGTH_SHORT).show();
                ((TextView) findViewById(R.id.thisMonthPurchase)).setText("This Month Record:" );
                ((TextView) findViewById(R.id.lastPurchase)).setText("Last record of Item:" );
                findViewById(R.id.financeType).setVisibility(View.GONE);
            }else if (dailyRecordWithItem.getItem().getCategory().equalsIgnoreCase("Income")&&
                    dailyRecordWithItem.getDailyRecord().getFinanceType().equalsIgnoreCase("Withdraw")) {
                Toast.makeText(InputActivity.this, "" + dailyRecordWithItem.getDailyRecord().getFinanceType(), Toast.LENGTH_SHORT).show();
                ((TextView) findViewById(R.id.thisMonthPurchase)).setText("Total Saving in Item:" );
                ((TextView) findViewById(R.id.lastPurchase)).setText("Last record of Item:" );
                findViewById(R.id.financeType).setVisibility(View.GONE);
            }
            else {
                Toast.makeText(InputActivity.this, "" + dailyRecordWithItem.getItem().getCategory(), Toast.LENGTH_SHORT).show();
            }
            findViewById(R.id.inputExpenseRecyclerView).setVisibility(View.GONE);
            inputExpenseView.setVisibility(View.VISIBLE);
            itemId = dailyRecordWithItem.getItem().getId();
            date = dailyRecordWithItem.getDailyRecord().getDate();
            dateBtn.setText(dateFormat.format("dd-MM-yyy ", date));
            int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            Log.w("getActualMaximum", "getActualMaximum" + daysOfMonth);
            calendar.set(Calendar.DAY_OF_MONTH, daysOfMonth);
            date2 = calendar.getTime();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            date1 = calendar.getTime();
            monthRecord = dailyRecordDAO.getMonthRecord(date1, date2, itemId);
            totalIncome = dailyRecordDAO.getIESTotal(date1, date2, "Income");
            totalSaving = dailyRecordDAO.getIESTotal(date1, date2, "Saving");
            totalNeededEx = dailyRecordDAO.getNWTotal(date1, date2, "needed");
            totalWantedEX = dailyRecordDAO.getNWTotal(date1, date2, "wanted");
            totalItemSaving = dailyRecordDAO.getSavingItemTotal(itemId,"Income");
            Log.w("totalIncome", "" + totalIncome);
            imgV.setImageResource(dailyRecordWithItem.getItem().getIcon());
            userInput = findViewById(R.id.editNumPad);
            userInput.setText(dailyRecordWithItem.getDailyRecord().getValue() + "");
            if(type=="Withdraw"){
                lastItemWithdraw = dailyRecordDAO.getLastItemWithdraw(itemId,type);
                ((TextView) findViewById(R.id.thisMonthPurchaseDText)).setText("" + totalItemSaving);
                ((TextView) findViewById(R.id.lastPurchaseDText)).setText("" + lastItemWithdraw);
            }else {
                lastRecordValue = dailyRecordDAO.getLastRecord(itemId);
                ((TextView) findViewById(R.id.thisMonthPurchaseDText)).setText("" + monthRecord);
                ((TextView) findViewById(R.id.lastPurchaseDText)).setText("" + lastRecordValue);
            }

            Log.w("lastRecord", "" + lastRecordValue);
            Log.e("lastMonthRecord", "" + monthRecord);
            Log.e("lastMonthsaving", "" + totalSaving);
            Log.e("lastMonthNex", "" + totalNeededEx);
            Log.e("lastMonthWex", "" + totalWantedEX);
            Log.e("total item saving:", "" + totalItemSaving);
//Warning linear layout
            if (dailyRecordWithItem.getItem().getCategory().equalsIgnoreCase("Saving")) {
                if (totalSaving == (totalIncome * savingLevel)) {
                    warninglyt.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.warningInputTV)).setText("Your Saving amount is at " + (100 * savingLevel) + "% of Income.");
                } else if (totalSaving > (totalIncome * savingLevel)) {
                    warninglyt.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.warningInputTV)).setText("Your Saving amount exceeds " + (100 * savingLevel) + "% of Income.");
                } else {
                    warninglyt.setVisibility(View.INVISIBLE);
                }
            }else if (dailyRecordWithItem.getItem().getCategory().equalsIgnoreCase("Income")) {
                if (totalIncome == (totalNeededEx+totalWantedEX+totalSaving)) {
                    warninglyt.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.warningInputTV)).setText("This month income had been used");
                } else if (totalIncome < (totalNeededEx+totalWantedEX+totalSaving)) {
                    warninglyt.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.warningInputTV)).setText("This month expense had exceeded the total income.");
                } else if (totalIncome <= (totalNeededEx+totalWantedEX+totalSaving + 30000)) {
                    warninglyt.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.warningInputTV)).setText("You has low income");
                }else {
                    warninglyt.setVisibility(View.INVISIBLE);
                }
            } else if (dailyRecordWithItem.getDailyRecord().getFinanceType().equalsIgnoreCase("needed")) {
                neededBtn.setChecked(true);
                if (totalNeededEx > (totalIncome * neededLevel)) {
                    warninglyt.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.warningInputTV)).setText("Your Needed Expense exceeds " + (neededLevel * 100) + "% of Income!");
                } else if (totalNeededEx == (totalIncome * neededLevel)) {
                    warninglyt.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.warningInputTV)).setText("Your Needed Expense is at " + (neededLevel * 100) + "% of Income!");
                } else {
                    warninglyt.setVisibility(View.INVISIBLE);
                }
            } else if (dailyRecordWithItem.getDailyRecord().getFinanceType().equalsIgnoreCase("wanted")) {
                wantedBtn.setChecked(true);
                if (totalWantedEX >= (totalIncome * wantedLevel)) {
                    findViewById(R.id.warningLayout).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.warningInputTV)).setText("Your Wanted Expense exceeds " + (wantedLevel * 100) + "% of Income!");

                } else if (totalWantedEX == (totalIncome * wantedLevel)) {
                    findViewById(R.id.warningLayout).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.warningInputTV)).setText("Your Wanted Expense is at " + (wantedLevel * 100) + "% of Income!");
                }
            }else{warninglyt.setVisibility(View.INVISIBLE);}
        }
        for (int i = 0; i < 10; i++) {
            final String finalI = Integer.toString(i);
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(InputActivity.this, "TEXT "+finalI, Toast.LENGTH_SHORT).show();
                    addToArray(finalI);
                }
            });


        }

        for (int j = 10; j < 16; j++) {
            btn[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btnSub:
                            addToArray("-");
                            break;
                        case R.id.btnPlus:
                            addToArray("+");
                            break;
                        case R.id.btnMulti:
                            addToArray("*");
                            break;
                        case R.id.btnEqual:
                            if (userInput.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(InputActivity.this, "Invalid Value", Toast.LENGTH_SHORT).show();
                            } else {
                                calculate(userInput.getText().toString());
                                break;
                            }
                        case R.id.btnSave:
                            calculate(userInput.getText().toString());
                            Log.w("valueone", "s" + value);
                            DailyRecord dailyRecord;
                            if (value <= 0) {
                                Toast.makeText(InputActivity.this, "Invalid Value!! Pls Try again", Toast.LENGTH_SHORT).show();
                            } else {
                                if (dailyRecordWithItem != null) {
                                    if (dailyRecordWithItem.getItem().getCategory().equalsIgnoreCase("Saving")) {
                                        int inval = database.getDailyRecordDAO().getIESTotal(date1, date2, "Income");
                                        int exval = database.getDailyRecordDAO().getIESTotal(date1, date2, "Expense");
                                        int saval = database.getDailyRecordDAO().getIESTotal(date1, date2, type);
                                        Log.w("dailyrecordwithitem", "income" + inval);
                                        Log.w("dailyrecordwithitem", "expense" + exval);
                                        Log.w("dailyrecordwithitem", "saving" + saval);
                                        if (value > (inval - (exval + saval))) {
                                            Toast.makeText(InputActivity.this, "Insufficient Balance! Pls try again.", Toast.LENGTH_LONG).show();
                                        } else {
                                            dailyRecord = dailyRecordWithItem.getDailyRecord();
                                            dailyRecord.setDate(date);
                                            dailyRecord.setFinanceType(financeType);
                                            dailyRecord.setValue(value);
                                            database.getDailyRecordDAO().update(dailyRecord);
                                            userInput.setText("");
                                            itemNameTV.setText("");
                                            Toast.makeText(InputActivity.this, "Your Record is saved.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(InputActivity.this, MenuDrawerActivity.class);
                                            startActivity(intent);

                                        }
                                    }else if (dailyRecordWithItem.getItem().getCategory().equalsIgnoreCase("Saving") &&
                                            dailyRecordWithItem.getDailyRecord().getFinanceType().equalsIgnoreCase("Withdraw")) {
                                        int totalval = database.getDailyRecordDAO().getSavingItemTotal(itemId,"Saving");
                                        Log.w("dailyrecordwithitem", "income" + totalval);
                                        if (value > totalval) {
                                            Toast.makeText(InputActivity.this, "Insufficient Saving! Pls try again.", Toast.LENGTH_LONG).show();
                                        }else {
                                            financeType="Withdraw";
                                            dailyRecord = dailyRecordWithItem.getDailyRecord();
                                            dailyRecord.setDate(date);
                                            dailyRecord.setFinanceType(financeType);
                                            dailyRecord.setValue(value);
                                            database.getDailyRecordDAO().update(dailyRecord);
                                            userInput.setText("");
                                            itemNameTV.setText("");
                                            Toast.makeText(InputActivity.this, "Your Record is saved.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(InputActivity.this, MenuDrawerActivity.class);
                                            startActivity(intent);
                                        }
                                    } else {
                                        dailyRecord = dailyRecordWithItem.getDailyRecord();
                                        dailyRecord.setDate(date);
                                        dailyRecord.setFinanceType(financeType);
                                        dailyRecord.setValue(value);
                                        database.getDailyRecordDAO().update(dailyRecord);
                                        userInput.setText("");
                                        itemNameTV.setText("");
                                        Toast.makeText(InputActivity.this, "Your Record is saved.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(InputActivity.this, MenuDrawerActivity.class);
                                        startActivity(intent);
                                    }
                                } else if (dailyRecordWithItem == null) {
                                    if (type.equalsIgnoreCase("Saving")) {
                                        int inval = database.getDailyRecordDAO().getIESTotal(date1, date, "Income");
                                        int exval = database.getDailyRecordDAO().getIESTotal(date1, date, "Expense");
                                        int saval = database.getDailyRecordDAO().getIESTotal(date1, date, type);
                                        if (value > (inval - (exval + saval))) {
                                            Toast.makeText(InputActivity.this, "Insufficient Balance! Pls try again.", Toast.LENGTH_LONG).show();
                                        } else {
                                            dailyRecord = new DailyRecord(itemId, date, value, financeType, memo);
                                            database.getDailyRecordDAO().insert(dailyRecord);
                                            userInput.setText("");
                                            itemNameTV.setText("");
                                            Toast.makeText(InputActivity.this, "Your Record is saved.", Toast.LENGTH_SHORT).show();
                                        }
                                    }else if (type.equalsIgnoreCase("Withdraw")) {
                                        int inval = database.getDailyRecordDAO().getSavingItemTotal(itemId, "Saving");
                                        if (value > inval ) {
                                            Toast.makeText(InputActivity.this, "Insufficient Saving! Pls try again.", Toast.LENGTH_LONG).show();

                                        } else {
                                            financeType ="Withdraw";
                                            dailyRecord = new DailyRecord(itemId, date, value, financeType, memo);
                                            database.getDailyRecordDAO().insert(dailyRecord);
                                            userInput.setText("");
                                            itemNameTV.setText("");
                                            Toast.makeText(InputActivity.this, "Your Record is saved.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        dailyRecord = new DailyRecord(itemId, date, value, financeType, memo);
                                        Log.w("Insterting recrod", "new record" + dailyRecord.toString());
                                        database.getDailyRecordDAO().insert(dailyRecord);
                                        userInput.setText("");
                                        itemNameTV.setText("");
                                        Toast.makeText(InputActivity.this, "Your Record is saved.", Toast.LENGTH_SHORT).show();
                                    }
                                    inputExpenseView.setVisibility(View.GONE);
                                }
                            }
                            break;
                        case R.id.btnDel:
                            int inputLength = userInput.length();
                            if (inputLength > 0) {
                                String result = userInput.getText().toString().substring(0, inputLength - 1);
                                userInput.setText(result);
                            }
                            break;
                    }

                }
            });
        }

        inputExpenseAdapter.setClickListener(new InputExpenseAdapter.ClickListener() {
            @Override
            public void onClick(Item item) {
                itemId = item.getId();
                memo = "";
                inputExpenseView.setVisibility(View.VISIBLE);
                Calendar calendar = Calendar.getInstance();
                itemNameTV.setText(item.getName() + "");
                int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                Log.w("getActualMaximum", "getActualMaximum" + daysOfMonth);
                calendar.set(Calendar.DAY_OF_MONTH, daysOfMonth);
                date2 = calendar.getTime();
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                date1 = calendar.getTime();
                int monthRecord = dailyRecordDAO.getMonthRecord(date1, date2, itemId);
                imgV.setImageResource(item.getIcon());
                totalIncome = dailyRecordDAO.getIESTotal(date1, date2, "Income");
                totalNeededEx = dailyRecordDAO.getNWTotal(date1, date2, "needed");
                totalWantedEX = dailyRecordDAO.getNWTotal(date1, date2, "wanted");
                totalSaving = dailyRecordDAO.getIESTotal(date1, date2,"Saving");
                totalItemSaving=dailyRecordDAO.getSavingItemTotal(itemId,"Income");
                dateBtn.setText(dateFormat.format("dd-MM-yyy ", date));
                Log.e("total item saving:", "" + totalItemSaving);
                Log.w("date1", "" + date1);
                Log.e("dat2", "" + date2);
                Log.e("lastMonthsaving", "" + totalSaving);
                Log.e("lastMonthNex", "" + totalNeededEx);
                Log.e("lastMonthWex", "" + totalWantedEX);
                Log.e("lastMonthincome", "" + totalIncome);
                if (type.equalsIgnoreCase("Saving")) {
                    Log.w("total Saving ", "" + totalSaving);
                    if (totalSaving == (totalIncome * savingLevel)) {
                        warninglyt.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.warningInputTV)).setText("Your Saving amount is at " + (100 * savingLevel) + "% of Income.");
                    } else if (totalSaving > (totalIncome * savingLevel)) {
                        warninglyt.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.warningInputTV)).setText("Your Saving amount exceeds " + (100 * savingLevel) + "% of Income.");
                    } else {
                        warninglyt.setVisibility(View.INVISIBLE);
                    }
                } else if (type.equalsIgnoreCase("Income")) {
                    if (totalIncome ==(totalNeededEx+totalWantedEX+totalSaving)) {
                        warninglyt.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.warningInputTV)).setText("This month income had been used");
                    } else if (totalIncome < (totalNeededEx + totalWantedEX + totalSaving)) {
                        warninglyt.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.warningInputTV)).setText("This month expense had exceeded the total income.");
                    } else if (totalIncome <= (totalNeededEx + totalWantedEX + totalSaving + 30000)) {
                        warninglyt.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.warningInputTV)).setText("You has low income");
                    } else {
                        warninglyt.setVisibility(View.INVISIBLE);
                    }
                }else if (item.getDefaultType().equalsIgnoreCase("needed")) {
                    neededBtn.setChecked(true);
                    Log.w("total needed expense", "" + totalNeededEx);
                    if (totalNeededEx > (totalIncome * neededLevel)) {
                        warninglyt.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.warningInputTV)).setText("Your Needed Expense exceeds " + (neededLevel * 100) + "% of Income!");
                    } else if (monthRecord == (totalIncome * neededLevel)) {
                        warninglyt.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.warningInputTV)).setText("Your Needed Expense is at " + (neededLevel * 100) + "% of Income!");
                    } else {
                        warninglyt.setVisibility(View.INVISIBLE);
                    }


                } else if (item.getDefaultType().equalsIgnoreCase("wanted")) {
                    wantedBtn.setChecked(true);

                    Log.w("total wanted expense", "" + totalWantedEX);
                    if (totalWantedEX > (totalIncome * wantedLevel)) {
                        warninglyt.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.warningInputTV)).setText("Your Wanted Expense exceeds " + (wantedLevel * 100) + "% of Income!");
                    } else if (totalWantedEX == (totalIncome * wantedLevel)) {
                        warninglyt.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.warningInputTV)).setText("Your Wanted Expense is at " + (wantedLevel * 100) + "% of Income!");
                    } else {
                        warninglyt.setVisibility(View.INVISIBLE);
                    }
                }
                if(type.equalsIgnoreCase("Withdraw")){
                    lastItemWithdraw = dailyRecordDAO.getLastItemWithdraw(itemId,type);
                    ((TextView) findViewById(R.id.thisMonthPurchaseDText)).setText("" + totalItemSaving);
                    Log.w("totalItemSaving", "" + totalItemSaving);
                    Log.e("last Withdraw: ", "" + lastItemWithdraw);
                    ((TextView) findViewById(R.id.lastPurchaseDText)).setText("" + lastItemWithdraw);
                }else {
                    ((TextView) findViewById(R.id.thisMonthPurchaseDText)).setText("" + monthRecord);
                    lastRecordValue = dailyRecordDAO.getLastRecord(itemId);
                    Log.w("lastRecord", "" + lastRecordValue);
                    Log.e("lastMonthRecord", "" + monthRecord);
                    ((TextView) findViewById(R.id.lastPurchaseDText)).setText("" + lastRecordValue);
                }
        }});

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.needed) {
                    financeType = "needed";
                    if (totalNeededEx > (totalIncome * neededLevel)) {
                        warninglyt.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.warningInputTV)).setText("Your Needed Expense exceeds " + (neededLevel * 100) + "% of Income!");
                    } else if (totalNeededEx == (totalIncome * neededLevel)) {
                        warninglyt.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.warningInputTV)).setText("Your Needed Expense is at " + (neededLevel * 100) + "% of Income!");
                    } else {
                        warninglyt.setVisibility(View.INVISIBLE);
                    }
                } else if (checkedId == R.id.wanted) {
                    financeType = "wanted";
                    if (totalWantedEX > (totalIncome * wantedLevel)) {
                        warninglyt.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.warningInputTV)).setText("Your Wanted Expense exceeds " + (wantedLevel * 100) + "% of Income!");
                    } else if (totalWantedEX == (totalIncome * wantedLevel)) {
                        warninglyt.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.warningInputTV)).setText("Your Wanted Expense is at " + (wantedLevel * 100) + "% of Income!");
                    } else {
                        warninglyt.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });


        memoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMemoDialog(InputActivity.this);
            }
        });
    }

    private void showMemoDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setText(memo);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Memo")
                .setMessage("Add your memo here.")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        memo = String.valueOf(taskEditText.getText());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void addToArray(String num) {
        userInput = findViewById(R.id.editNumPad);
        userInput.append(num);
    }

    public void calculate(String str) {
        userInput = findViewById(R.id.editNumPad);
        input = str;
        value = 0;
        long a = 0;
        long b = 0;
        String temp = "0";
        String temp2 = "0";
        stack = "";

        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i)) && stack.equalsIgnoreCase("") && i != input.length() - 1) {
                temp = temp + input.charAt(i);
                Log.w("txt", "first if");
                Log.w("txt", temp + "");
                Log.w("txt", temp2 + "");
                Log.w("txt", a + "");
                Log.w("txt", b + "");
                Log.w("txt", value + "");

            } else if (Character.isDigit(input.charAt(i)) && stack.equalsIgnoreCase("") && i == input.length() - 1) {
                temp = temp + input.charAt(i);
                value = Integer.parseInt(temp);
                Log.w("txt", "first else if ");
                Log.w("txt", temp + "");
                Log.w("txt", temp2 + "");
                Log.w("txt", a + "");
                Log.w("txt", b + "");
                Log.w("txt", value + "");

            } else if (Character.isDigit(input.charAt(i)) && !stack.equalsIgnoreCase("") && i != (input.length() - 1)) {
                temp2 = temp2 + input.charAt(i);

                Log.w("txt", "second else if");
                Log.w("txt", temp + "");
                Log.w("txt", temp2 + "");
                Log.w("txt", a + "");
                Log.w("txt", b + "");
                Log.w("txt", value + "");


            } else if (!Character.isDigit(input.charAt(i)) && stack.equalsIgnoreCase("")) {
                stack = String.valueOf(input.charAt(i));

            } else if (!Character.isDigit(input.charAt(i)) && !stack.equalsIgnoreCase("")) {
                if (!Character.isDigit(input.charAt(i - 1))) {
                    Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    switch (stack) {
                        case "+":
                            a = Integer.valueOf(temp);
                            b = Integer.valueOf(temp2);
                            value = a + b;
                            temp = String.valueOf(value);
                            temp2 = "";
                            stack = String.valueOf(input.charAt(i));

                            Log.w("txt", "6 if sw 1");
                            Log.w("txt", temp + "");
                            Log.w("txt", temp2 + "");
                            Log.w("txt", a + "");
                            Log.w("txt", b + "");
                            Log.w("txt", value + "");
                            break;
                        case "-":
                            a = Integer.valueOf(temp);
                            b = Integer.valueOf(temp2);
                            value = a - b;
                            temp = String.valueOf(value);
                            temp2 = "";
                            stack = String.valueOf(input.charAt(i));

                            Log.w("txt", "6 if sw 2");
                            Log.w("txt", temp + "");
                            Log.w("txt", temp2 + "");
                            Log.w("txt", a + "");
                            Log.w("txt", b + "");
                            Log.w("txt", value + "");
                            break;
                        case "*":
                            a = Integer.valueOf(temp);
                            b = Integer.valueOf(temp2);
                            value = a * b;
                            temp = String.valueOf(value);
                            temp2 = "";
                            stack = String.valueOf(input.charAt(i));

                            Log.w("txt", "6 if sw 3");
                            Log.w("txt", temp + "");
                            Log.w("txt", temp2 + "");
                            Log.w("txt", a + "");
                            Log.w("txt", b + "");
                            Log.w("txt", value + "");
                            break;
                    }
                }
            } else if (Character.isDigit(input.charAt(i)) && i == (input.length() - 1) && !stack.equalsIgnoreCase("")) {
                temp2 = temp2 + input.charAt(i);
                switch (stack) {
                    case "+":
                        a = Integer.valueOf(temp);
                        b = Integer.valueOf(temp2);
                        value = a + b;

                        Log.w("txt", "9if sw 1");
                        Log.w("txt", temp + "");
                        Log.w("txt", temp2 + "");
                        Log.w("txt", a + "");
                        Log.w("txt", b + "");
                        Log.w("txt", value + "");
                        break;
                    case "-":
                        a = Integer.valueOf(temp);
                        b = Integer.valueOf(temp2);
                        value = a - b;
                        Log.w("txt", "9if sw 2");
                        Log.w("txt", temp + "");
                        Log.w("txt", temp2 + "");
                        Log.w("txt", a + "");
                        Log.w("txt", b + "");
                        Log.w("txt", value + "");
                        break;
                    case "*":
                        a = Integer.parseInt(temp);
                        b = Integer.parseInt(temp2);
                        value = a * b;


                        Log.w("txt", "9 if sw 3");
                        Log.w("txt", temp + "");
                        Log.w("txt", temp2 + "");
                        Log.w("txt", a + "");
                        Log.w("txt", b + "");
                        Log.w("txt", value + "");
                        break;
                }
                stack = "";
            } else if (!Character.isDigit(input.charAt(i)) && !Character.isDigit(input.charAt(i - 1))) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        if (!stack.equalsIgnoreCase("")) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        } else {
            input = String.valueOf(value);
            Log.w("calculatedInput", input + "");
            Log.w("calculatedValue", value + "");
            userInput.setText(input);
        }

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DailyRecordDAO dailyRecordDAO = database.getDailyRecordDAO();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        //        YearMonth yearMonth=YearMonth.of(year,month);
        date = calendar.getTime();
        int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.w("getActualMaximum", "getActualMaximum" + daysOfMonth);
        calendar.set(Calendar.DAY_OF_MONTH, daysOfMonth);
        date2 = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        date1 = calendar.getTime();
        dateBtn.setText(dateFormat.format("dd-MM-yyy ", date));
        String value = String.valueOf(dailyRecordDAO.getMonthRecord(date1, date2, itemId));
        ((TextView) findViewById(R.id.thisMonthPurchaseDText)).setText("" + value);
        Log.w("dates123", "" + date + "_MonthBeginAt" + date1 + "EndIn" + date2);
        Log.e("purchse", value + " ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if(item.getItemId()==R.id.addItem){
            intent = new Intent(InputActivity.this, MainActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);

        }else if(item.getItemId()==R.id.editItem){
            intent = new Intent(InputActivity.this, EditItemActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);

        }
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        itemList = database.getItemDAO().getItemByCat(catType);
        inputExpenseAdapter.setExItem(itemList);
        inputExpenseAdapter.notifyDataSetChanged();
        Calendar calendar = Calendar.getInstance();
        date=calendar.getTime();
        dateBtn.setText(dateFormat.format("dd-MM-yyy",date));
    }

}
