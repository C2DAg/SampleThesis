package com.example.android.samplethesis;

import android.app.Activity;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.samplethesis.ExpenseInput.InputExpenseAdapter;
import com.example.android.samplethesis.add_item.AddItemAdapter;
import com.example.android.samplethesis.dao.DailyRecordDAO;
import com.example.android.samplethesis.data_adapter.DataAdapter;
import com.example.android.samplethesis.model.Item;

import java.util.List;

public class EditItemActivity extends AppCompatActivity implements DataAdapter.RecyclerViewItemClickListener {
        Button clickButton;

    public ImageView iconEdit;
    public EditText itemName;
    public Button cancelBtn,saveBtn;
    public RadioGroup defaultFinanceType;
    public String itemDefaultFinanceType = "";
    public int  icon;
    public int [] icons;
    public String type ;
    public TextView tv;
    AppDatabase database;
    RecyclerView recyclerView;
    List<Item> itemList;
    InputExpenseAdapter inputExpenseAdapter;
    Long itemId;
    String name;
    TextView catEdit;
    RadioButton needBtn,wantBtn;



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
        needBtn=findViewById(R.id.neededRBtn);
        wantBtn=findViewById(R.id.wantedRBtn);
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
                icon = item.getIcon();
                name = item.getName();
                itemName.setText(name);
                iconEdit.setImageResource(icon);
                catEdit.setText(type);
                if( type.equalsIgnoreCase("Expense")){
                    itemDefaultFinanceType = item.getDefaultType();
                    if (itemDefaultFinanceType.equalsIgnoreCase("needed")) {
                        needBtn.setChecked(true);
                    }
                    else if (itemDefaultFinanceType.equalsIgnoreCase("wanted")) {
                        wantBtn.setChecked(true);
                    }
                }

            }
        });

        iconEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHere(v);
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
                if((itemName.getText().toString().equalsIgnoreCase(""))) {
                    Toast.makeText(EditItemActivity.this, "Pls Add Item Name.Item name can not be blank.", Toast.LENGTH_SHORT).show();

                }else if( type.equalsIgnoreCase("Expense")  && itemDefaultFinanceType.equalsIgnoreCase("")){
                    Toast.makeText(EditItemActivity.this, "Pls Add Default Finance Type for item.(Wanted or Needed)", Toast.LENGTH_SHORT).show();
                }else {
                    item = new Item(itemId,itemName.getText().toString(), type, itemDefaultFinanceType, icon);
                    database.getItemDAO().update(item);
                    iconEdit.setImageResource(R.drawable.ic_add_white);
                    itemName.setText("");
                    defaultFinanceType.clearCheck();
                    Toast.makeText(EditItemActivity.this, "Your item is updated successfully", Toast.LENGTH_SHORT).show();
                }

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
    CustomListViewDialog customDialog;

    public void clickHere(View view) {
        if(type.equalsIgnoreCase("Income")){
            icons = new int [] {
                    R.drawable.ic_income_sale,
                    R.drawable.ic_income_salary,
                    R.drawable.ic_income_lottery,
                    R.drawable.ic_income_grant,
            };
        }else if(type.equalsIgnoreCase("Expense")){
            icons = new int [] {
                    R.drawable.ic_expense_transportation,
                    R.drawable.ic_expense_sport,
                    R.drawable.ic_expense_social,
                    R.drawable.ic_expense_snack,
                    R.drawable.ic_expense_phonebill,
                    R.drawable.ic_expense_housing,
                    R.drawable.ic_expense_health,
                    R.drawable.ic_expense_grocery,
                    R.drawable.ic_expense_food,
                    R.drawable.ic_expense_cosmetic,
                    R.drawable.ic_expense_entertainment,
                    R.drawable.ic_expense_electronic,
                    R.drawable.ic_expense_education,
                    R.drawable.ic_expense_colthing,
                    R.drawable.ic_expense_alcohol,
                    R.drawable.ic_expense_cigarette,
                    R.drawable.ic_expense_grocery,
                    R.drawable.ic_gift,
            };

        }else if(type.equalsIgnoreCase("Saving")) {
            icons = new int[]{
                    R.drawable.ic_saving_cash,
                    R.drawable.ic_saving_bank
            };
        }
            DataAdapter dataAdapter = new DataAdapter(icons, this);
            customDialog = new CustomListViewDialog(EditItemActivity.this, dataAdapter);
            customDialog.show();
            customDialog.setCanceledOnTouchOutside(false);

    }
    @Override
    public void onClick(int imgRes) {
            if (customDialog != null){
                iconEdit.setImageResource(imgRes);
                icon = imgRes;
                customDialog.dismiss();
            }
        }

}
