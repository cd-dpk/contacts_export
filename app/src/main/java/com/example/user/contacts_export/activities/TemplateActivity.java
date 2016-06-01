package com.example.user.contacts_export.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public abstract class TemplateActivity extends AppCompatActivity {

    public abstract void  initView();
    public abstract void loadData();
    public abstract void initializeView();
    public abstract void listenView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        loadData();
        initializeView();
        listenView();
    }

}
