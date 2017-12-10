package com.example.administrator.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * TemplateActivity
 * Created by My on 2017/12/10.
 */

public class TemplateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        ListView lv = (ListView) findViewById(R.id.lv);
        final String[] res = {"i love you!", "i miss you!", "i hate you!", "在干嘛?", "在哪里?",
                "吃饭了吗?", "想你了!", "下班了吗?", "刚没看到,不好意思", "请问有什么事吗?", "稍后联系你...",
                "我在吃饭,请稍后联系...", "我在学习,请稍后联系...", "我在睡觉,请稍后联系...",
                "我在开车,请稍后联系...", "我在休假,请稍后联系...", "我在健身,请稍后联系...",
                "我在上课,请稍后联系..."};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, res);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String template = res[position];
                Intent intent = getIntent();
                intent.putExtra("template", template);
                setResult(20, intent);
                finish();
            }
        });

    }
}
