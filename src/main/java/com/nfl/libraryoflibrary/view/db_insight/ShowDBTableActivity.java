package com.nfl.libraryoflibrary.view.db_insight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nfl.libraryoflibrary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 展示某一数据库中的所有的表格（除去android系统自动生成的表格）
 *
 * @author nfl
 * @date 2015年12月31日
 */
public class ShowDBTableActivity extends Activity {

    private String filePath; // 存储数据库路径
    private DBErgodic helper;
    private ListView tables;
    private SimpleAdapter adapter;
    private List<Map<String, String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdbtable);
        filePath = getIntent().getStringExtra("filePath");
        helper = new DBErgodic(filePath);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        tables = (ListView) findViewById(R.id.tables);
        data = new ArrayList<>();
        adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_1,
                new String[]{"tableName"}, new int[]{android.R.id.text1});
        tables.setAdapter(adapter);
        data.addAll(helper.getTablesName());
        adapter.notifyDataSetChanged();
        tables.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ShowDBTableActivity.this,
                        ShowTableActivity.class);
                intent.putExtra("filePath", filePath);
                intent.putExtra("tableName", data.get(position)
                        .get("tableName"));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        helper.closeDB();
    }
}
