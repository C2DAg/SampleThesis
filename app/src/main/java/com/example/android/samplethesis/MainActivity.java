package com.example.android.samplethesis;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.Update;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.samplethesis.add_item.AddItemAdapter;
import com.example.android.samplethesis.dao.DailyRecordDAO;
import com.example.android.samplethesis.dao.ItemDAO;
import com.example.android.samplethesis.model.Item;

public class MainActivity extends AppCompatActivity {
    public ImageView addIconView;
    public Button saveBtn;
    public EditText addItemName;
    public RadioGroup defaultFinanceType;
    public String itemDefaultFinanceType = "";
    public int imgSrc;
    public int [] icons;
    public String type ;
    public TextView tV;
    AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addIconView = findViewById(R.id.addItemIcon);
        saveBtn = findViewById(R.id.saveBtn);
        addItemName = findViewById(R.id.addItemName);
        defaultFinanceType = findViewById(R.id.defaultFinaceType);
        tV = findViewById(R.id.textView2);
        type=getIntent().getStringExtra("type");
        addItemName.setVisibility(View.INVISIBLE);

        if(type.equalsIgnoreCase("Income")){
            defaultFinanceType.setVisibility(View.GONE);
            tV.setVisibility(View.GONE);
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

        }else if(type.equalsIgnoreCase("Saving")){
            defaultFinanceType.setVisibility(View.GONE);
            tV.setVisibility(View.GONE);
            icons = new int [] {
                    R.drawable.ic_saving_cash,
                    R.drawable.ic_saving_bank
            };
        }




        RecyclerView recyclerView = findViewById(R.id.addRecyclerView);
        final AddItemAdapter addItemAdapter = new AddItemAdapter( icons);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setAdapter(addItemAdapter);
        addItemAdapter.setClickListener(new AddItemAdapter.ClickListener() {
            @Override
            public void onClick(int imgRes) {
                addIconView.setImageResource(imgRes);
                imgSrc = imgRes;
                addItemName.setVisibility(View.VISIBLE);
                addItemName.setText("");

            }
        });

        defaultFinanceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.needType){
                    itemDefaultFinanceType = "needed";
                }
                else if(checkedId == R.id.wantedType){
                    itemDefaultFinanceType = "wanted";
                }
            }
        });

        database = Room.databaseBuilder(this, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        ItemDAO itemDAO = database.getItemDAO();
        DailyRecordDAO dailyRecordDAO = database.getDailyRecordDAO();


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Item item;
                Log.w("addItem","itemValue"+ addItemName.getText().toString());
                if((addItemName.getText().toString().equalsIgnoreCase(""))) {
                    Toast.makeText(MainActivity.this, "Pls Add Item Name.Item name can not be blank.", Toast.LENGTH_SHORT).show();

                }else if( type.equalsIgnoreCase("Expense")  && itemDefaultFinanceType.equalsIgnoreCase("")){
                    Toast.makeText(MainActivity.this, "Pls Add Default Finance Type for item.(Wanted or Needed)", Toast.LENGTH_SHORT).show();
                }else {
                    item = new Item(addItemName.getText().toString(), type, itemDefaultFinanceType, imgSrc);
                    database.getItemDAO().insert(item);
                    addIconView.setImageResource(R.drawable.ic_add_white);
                    addItemName.setVisibility(View.INVISIBLE);
                    addItemName.setText("");
                    defaultFinanceType.clearCheck();
                    Toast.makeText(MainActivity.this, "Your item is added to the Database", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        addIconView.setImageResource(R.drawable.ic_add_white);
        addItemName.setVisibility(View.INVISIBLE);
        addItemName.setText("");
        defaultFinanceType.clearCheck();
    }



}
