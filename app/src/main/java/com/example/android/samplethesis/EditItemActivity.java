package com.example.android.samplethesis;

import android.arch.persistence.room.Room;
import android.content.Intent;
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
    Long itemId;
    String name;
    TextView catEdit;


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
        catEdit=findViewById(R.id.catEdit);
        defaultFinanceType=findViewById(R.id.radioDefaultGp);
        cancelBtn=findViewById(R.id.cancelEditBtn);
        saveBtn=findViewById(R.id.saveEditBtn);
        type=getIntent().getStringExtra("type");
        tv=findViewById(R.id.editDefault);
        itemList = database.getItemDAO().getItemByCat(type);
        RecyclerView recyclerView = findViewById(R.id.editRecycler);
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

        inputExpenseAdapter.setClickListener(new InputExpenseAdapter.ClickListener() {
            @Override
            public void onClick(Item item) {
                itemId = item.getId();
                icons = item.getIcon();
                name = item.getName();
                itemName.setText(name);
                iconEdit.setImageResource(icons);
                catEdit.setText(type);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditItemActivity.this,InputActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item;
                item = new Item(itemId,itemName.getText().toString(), type, itemDefaultFinanceType, icons);
                database.getItemDAO().update(item);

            }
        });

        defaultFinanceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.neededRBtn){
                    itemDefaultFinanceType = "needed";
                }
                else if(checkedId == R.id.wantedRBtn){
                    itemDefaultFinanceType = "wanted";
                }
            }
        });

    }
}
