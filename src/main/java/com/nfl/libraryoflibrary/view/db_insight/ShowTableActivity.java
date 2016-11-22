package com.nfl.libraryoflibrary.view.db_insight;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.utils.WebViewHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 显示数据库某一表格中的全部数据
 *
 * @author nfl
 * @date 2015年12月31日
 */
public class ShowTableActivity extends Activity {

    private String filePath; // 存储数据库路径
    private String tableName;
    private DBErgodic dbErgodic ;
    private WebView mWebView;
    private WebViewHelper mWebViewHelper;
    private List<Map<String, String>> data;
    private String[] columnNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtable);
        filePath = getIntent().getStringExtra("filePath");
        tableName = getIntent().getStringExtra("tableName");
        dbErgodic = new DBErgodic(filePath, tableName);
        initView();
        mWebViewHelper = new WebViewHelper(this, mWebView);
        useLoadData();
    }

    private void initView() {
        // TODO Auto-generated method stub
        mWebView = (WebView) findViewById(R.id.webView);
        data = new ArrayList<>();
        data.addAll(dbErgodic.getTableContent());
        columnNames = dbErgodic.getColumnNames();
    }

    // 判断是否能够从该网页返回上一个打开的网页
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            // mWebView.goBackOrForward(steps);
            // mWebView.goForward();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void useLoadData() {
        if(null == columnNames || columnNames.length < 1){
            return;
        }
        StringBuilder dataBuffer = new StringBuilder(
                "<!DOCTYPE HTML><html>"
                        + "<head><style type=\"text/css\">"
                        + "table.gridtable{font-family: verdana,arial,sans-serif;font-size:11px;color:#333333;border-width: 1px;border-color: #666666;border-collapse: collapse;} "
                        + "table.gridtable th{border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #dedede;}"
                        + "table.gridtable td{border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;}"
                        + "</style></head>"
                        + "<body width=\"100%\" height=\"100%\">"
                        + "<table class=\"gridtable\">");

		/*
         * 添加表头
		 */
        dataBuffer.append("<tr>");
        for (int j = 0; j < columnNames.length; j++) {
            dataBuffer.append("<th>");
            dataBuffer.append(columnNames[j]);
            dataBuffer.append("</th>");
        }
        dataBuffer.append("</tr>");
		/*
		 * 添加行数据
		 */
        if(null == data || data.size() < 1){
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            dataBuffer.append("<tr>");
            for (int j = 0; j < columnNames.length; j++) {
                dataBuffer.append("<td>");
                dataBuffer.append(data.get(i).get(columnNames[j]));
                dataBuffer.append("</td>");
            }
            dataBuffer.append("</tr>");
        }

        dataBuffer.append("</table></body></html>");
        mWebViewHelper.loadWithHtmlContent(dataBuffer.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mWebView) {
            mWebView.onPause();
        }
        dbErgodic.closeDB();
    }
}
