package com.airhome.pulltorefresh;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WListView.OnRefreshListener {
    private WListView mListView;
    private List<String> mData;
    private Handler mHandler;
    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (WListView) findViewById(R.id.w_listView);
        mHandler = new Handler();
        setData();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(this);
    }

    @Override
    public void doWhileRefreshing() {
        mData.add(0, "new Item");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                mListView.finishRefresh();
                Toast.makeText(getApplicationContext(), "success!", Toast.LENGTH_SHORT).show();
            }
        }, 3000);
    }

    private void setData() {
        mData = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            mData.add(i, "Item " + i);
        }
    }
}
