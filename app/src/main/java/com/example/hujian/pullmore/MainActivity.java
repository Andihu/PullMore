package com.example.hujian.pullmore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.hujian.pullmore.wiget.PullListView;

public class MainActivity extends AppCompatActivity {
    private StudentDao studentDao;
    private PullListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentDao = new StudentDao(this);
        studentDao.addstudent();
        listView = findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"huai","hbkadh","hbkadh","hbkadh","hbkadh","hbkadh","hbkadh","hbkadh","hbkadh","hbkadh"}));
    }


}
