package com.demo.jianjunhuang.jsinterfacedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button jsInterfaceBtn;
    private Button urlBtn;
    private Button interceptBtn;
    private Button libBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jsInterfaceBtn = findViewById(R.id.main_intent_js_interface_btn);
        urlBtn = findViewById(R.id.main_intent_url_btn);
        interceptBtn = findViewById(R.id.main_intercept_btn);
        libBtn = findViewById(R.id.main_lib_btn);

        jsInterfaceBtn.setOnClickListener(this);
        urlBtn.setOnClickListener(this);
        interceptBtn.setOnClickListener(this);
        libBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.main_intent_js_interface_btn: {
                intent.setClass(MainActivity.this, JSInterfaceActivity.class);
                break;
            }
            case R.id.main_intent_url_btn: {
                intent.setClass(MainActivity.this, UrlActivity.class);
                break;
            }
            case R.id.main_intercept_btn: {
                intent.setClass(MainActivity.this, InterceptActivity.class);
                break;
            }
            case R.id.main_lib_btn: {
                intent.setClass(MainActivity.this, LibExampleActivity.class);
                break;
            }
        }
        startActivity(intent);
    }
}
