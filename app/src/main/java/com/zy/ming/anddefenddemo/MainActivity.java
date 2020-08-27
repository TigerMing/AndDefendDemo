package com.zy.ming.anddefenddemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import com.zy.ming.moduledemo.ModuleDemoActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.test_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TestActivity.class);
                intent.putExtra("int",123);
                intent.putExtra("bool",true);
                intent.putExtra("string","测试修改intent数据获取方法");
                Person person = new Person("哈哈哈","100");
                intent.putExtra("object",person);
                startActivity(intent);
            }
        });

        findViewById(R.id.test_module).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ModuleDemoActivity.class);
                intent.putExtra("int",123);
                intent.putExtra("bool",true);
                intent.putExtra("string","测试修改intent数据获取方法");
                startActivity(intent);
            }
        });
    }
}