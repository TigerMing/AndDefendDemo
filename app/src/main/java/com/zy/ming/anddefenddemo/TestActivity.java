package com.zy.ming.anddefenddemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * description ：
 * author : create by qiaoming on 2020/7/27
 * version :
 */
public class TestActivity extends Activity {

    private TextView tvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tvContent = findViewById(R.id.tv_content);
        int a = getIntent().getIntExtra("int",0);
//        IntentUtil.getIntExtra(getIntent(),"Test",0);

        boolean b = getIntent().getBooleanExtra("bool",false);
//        IntentUtil.getBooleanExtra(getIntent(),"bool",false);

        String s = getIntent().getStringExtra("string");
//        IntentUtil.getStringExtra(getIntent(),"String");

        Person o1 = getIntent().getParcelableExtra("object");
//        Person o2 = IntentUtil.getParcelableExtra(getIntent(),"object");

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("a = ").append(a).append("\n");
        stringBuffer.append("b = ").append(b).append("\n");
        stringBuffer.append("s = ").append(s).append("\n");
        stringBuffer.append("person:").append(o1.name).append("年龄是").append(o1.age);
        tvContent.setText(stringBuffer.toString());
    }
}
