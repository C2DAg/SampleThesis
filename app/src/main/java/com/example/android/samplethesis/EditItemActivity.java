package com.example.android.samplethesis;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.samplethesis.ExpenseInput.InputExpenseAdapter;
import com.example.android.samplethesis.dao.DailyRecordDAO;
import com.example.android.samplethesis.model.DailyRecord;
import com.example.android.samplethesis.model.Item;

import java.util.List;

public class EditItemActivity extends AppCompatActivity {
    public ImageButton iconEdit;
    public EditText itemName;
    public Button cancelBtn,saveBtn;
    public RadioGroup defaultFinanceType;
    public String itemDefaultFinanceType = "";
    public int imgSrc;
    public int  icons;
    public String type ;
    public TextView tv;
    AppDatabase database;
    RecyclerView recyclerView;
    List<Item> itemList;
    InputExpenseAdapter inputExpenseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        database = Room.databaseBuilder(this, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        DailyRecordDAO dailyRecordDAO = database.getDailyRecordDAO();
        recyclerView=findViewById(R.id.editRecycler);
        iconEdit=findViewById(R.id.iconEdit);
        itemName=findViewById(R.id.nameEdit);
        defaultFinanceType=findViewById(R.id.radioDefaultGp);
        cancelBtn=findViewById(R.id.cancelEditBtn);
        saveBtn=findViewById(R.id.saveEditBtn);
        type=getIntent().getStringExtra("type");
        tv=findViewById(R.id.editDefault);
        itemList = database.getItemDAO().getItemByCat(type);
        RecyclerView recyclerView = findViewById(R.id.inputExpenseRecyclerView);
        inputExpenseAdapter = new InputExpenseAdapter(itemList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(inputExpenseAdapter);
        if(type.equalsIgnoreCase("Income")){
            tv.setVisibility(View.GONE);
            defaultFinanceType.setVisibility(View.GONE);

        }else if(type.equalsIgnoreCase("Expense")){
            tv.setVisibility(View.VISIBLE);
            defaultFinanceType.setVisibility(View.VISIBLE);

        }else if(type.equalsIgnoreCase("Saving")){
            tv.setVisibility(View.GONE);
            defaultFinanceType.setVisibility(View.GONE);
        }

    }
}
