package com.zy.ming.moduledemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * description ：
 * author : create by qiaoming on 2020/8/6
 * version :
 */
public class ModuleDemoActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_demo);

        TextView tvContent = findViewById(R.id.tv_content);

        int a = getIntent().getIntExtra("int",0);
//        IntentUtil.getIntExtra(getIntent(),"Test",0);

        boolean b = getIntent().getBooleanExtra("bool",false);
//        IntentUtil.getBooleanExtra(getIntent(),"bool",false);

        String s = getIntent().getStringExtra("string");
//        IntentUtil.getStringExtra(getIntent(),"String");

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("a = ").append(a).append("\n");
        stringBuffer.append("b = ").append(b).append("\n");
        stringBuffer.append("s = ").append(s).append("\n");
        tvContent.setText(stringBuffer.toString());
    }
}
