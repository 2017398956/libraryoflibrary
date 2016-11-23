package com.nfl.libraryoflibrary.view.db_insight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.utils.LogTool;
import com.nfl.libraryoflibrary.utils.SoftKeyBoardTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBInsightActivity extends Activity {

    private EditText dataBasePath;
    private SimpleAdapter adapter;
    private List<Map<String, String>> data;
    private ListView dbList;
    private String dbPath;
    private String workplacePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbinsight);
        initView();
    }

    private void initView() {
        dataBasePath = (EditText) findViewById(R.id.dataBasePath);
        workplacePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "NFLDBWORKPLACE";
        // + File.separator + "DataBases";
        File file = new File(workplacePath + File.separator + "test.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        data = new ArrayList<>();
        dbList = (ListView) findViewById(R.id.dbList);
        adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_1, new String[]{"dbName"},
                new int[]{android.R.id.text1});
        dbList.setAdapter(adapter);
        dbList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(DBInsightActivity.this,
                        ShowDBTableActivity.class);
                intent.putExtra("filePath", data.get(position).get("filePath"));
                intent.putExtra("dbName", data.get(position).get("dbName"));
                startActivityForResult(intent , 100);// 这里只是为了解决back键不回退到当前Activity的情况
            }
        });
        String selfDataPath = this.getDir("test", MODE_PRIVATE).getParent() + File.separator + "databases" ;
        dataBasePath.setText(selfDataPath);
        LogTool.i(selfDataPath);
        getDataBaseList(selfDataPath);
    }

    public void search(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        dbPath = dataBasePath.getText().toString();
        if (null != dbPath) {
            // openDBRooted(dbPath);
            data.clear();
            getDataBaseList(dbPath);
        }

    }

    private void openDBRooted(String dbURL) {
        ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
        // java.lang.ProcessBuilder: Creates operating system processes.
        pb.directory(new File("/"));// 设置shell的当前目录。
        try {
            Process proc = pb.start();
            // 获取输入流，可以通过它获取SHELL的输出。
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    proc.getInputStream()));
            BufferedReader err = new BufferedReader(new InputStreamReader(
                    proc.getErrorStream()));
            // 获取输出流，可以通过它向SHELL发送命令。
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(proc.getOutputStream())), true);
            out.println("pwd");
            out.println("su root");// 执行这一句时会弹出对话框（以下程序要求授予最高权限...），要求用户确认。
            // out.println("rm -rf " + workplacePath);//
            // 执行这一句时会弹出对话框（以下程序要求授予最高权限...），要求用户确认。
            out.println("cp -rf " + dbURL + " " + workplacePath);
            out.println("exit");
            // proc.waitFor();
            data.clear();
            getDataBaseList(workplacePath);
            in.close();
            out.close();
            proc.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过递归获得所有的数据库文件 void 2015年12月30日
     *
     * @param path
     */
    private void getDataBaseList(String path) {
        // TODO Auto-generated method stub

        File dir = new File(path);
        if (!dir.exists()) {
            LogTool.i("该应用不存在databases目录");
            return;
        }
        File[] files = dir.listFiles();
        if (null == files || files.length < 1) {
            LogTool.i("该应用databases目录下不存在数据库文件");
            return;
        }
        HashMap<String, String> map;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                // LogTool.i(i + " | " + files[i].getAbsolutePath());
                getDataBaseList(files[i].getAbsolutePath());
            } else {
                String fileName = files[i].getName();
                String filePath = files[i].getAbsolutePath();
                // LogTool.i("file[" + i + "]:" + filePath);
                String endString = ".db" ;
                endString = "" ;
                if (fileName.endsWith(endString)) {
                    map = new HashMap<>();
                    map.put("dbName", fileName);
                    map.put("filePath", filePath);
                    data.add(map);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
