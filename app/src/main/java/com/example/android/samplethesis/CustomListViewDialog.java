package com.example.android.samplethesis;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CustomListViewDialog extends Dialog implements View.OnClickListener {
    public CustomListViewDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    public CustomListViewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public Activity activity;
    public Dialog dialog;
    public Button cancle;
    TextView title;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter adapter;
    public CustomListViewDialog(Activity a, RecyclerView.Adapter adapter) {
        super(a);
        this.activity = a;
        this.adapter = adapter;
        setupLayout();
    }

    private void setupLayout() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_recyclerview);
        cancle = (Button) findViewById(R.id.cBtn);
        title = findViewById(R.id.title);
        recyclerView = findViewById(R.id.dialogRecyclerView);
        mLayoutManager = new GridLayoutManager(activity,4);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        cancle.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.:
//                //Do Something
//                break;
            case R.id.cBtn:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
