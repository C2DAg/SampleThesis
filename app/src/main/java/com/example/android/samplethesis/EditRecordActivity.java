package com.example.android.samplethesis;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.android.samplethesis.dao.DailyRecordDAO;
import com.example.android.samplethesis.model.DailyRecordWithItem;

public class EditRecordActivity extends AppCompatActivity {
    public EditText nameET;
    public EditText valueET;
    public RadioGroup radioGroup;
    public Button saveBtn;
    DailyRecordWithItem dailyRecordWithItem;
    AppDatabase database;
    String financeType = "";
    RadioButton neededbtn,wantedbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);
        nameET=findViewById(R.id.nameEditV);
        valueET=findViewById(R.id.valueEditV);
        radioGroup =findViewById(R.id.financeType);
        saveBtn = findViewById(R.id.saveBtn);
        neededbtn = findViewById(R.id.neededType);
        wantedbtn = findViewById(R.id.wantedType);
        dailyRecordWithItem = (DailyRecordWithItem) getIntent().getSerializableExtra("record");
        nameET.setText(dailyRecordWithItem.getItem().getName());
        valueET.setText(String.valueOf(dailyRecordWithItem.getDailyRecord().getValue()));
        database=Room.databaseBuilder(this,AppDatabase.class,"mydb").allowMainThreadQueries()
                .build();
        if (dailyRecordWithItem.getItem().getDefaultType().equalsIgnoreCase("needed")) {
            neededbtn.setChecked(true);
        }
        else if (dailyRecordWithItem.getItem().getDefaultType().equalsIgnoreCase("wanted")) {
            wantedbtn.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.needed) {
                    financeType = "needed";
                } else if (checkedId == R.id.wanted) {
                    financeType = "wanted";

                }
            }
        });
        DailyRecordDAO dailyRecordDAO=database.getDailyRecordDAO();
        saveBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });


}


    }



