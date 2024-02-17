package com.nfl.libraryoflibrary.view.floatwindow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.nfl.libraryoflibrary.utils.PermissionUtil;

/**
 * 为悬浮窗提供一个开启开关，嵌入到应用后可去掉
 *
 * @author nfl
 * @date 2015年10月30日
 */
public class FloatWindowActivity extends Activity {

    private SharedPreferences sp = null;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = new Button(this);
        button.setText("Start Float Window");
        button.setAllCaps(false);
        button.setWidth(LayoutParams.MATCH_PARENT);
        button.setHeight(LayoutParams.WRAP_CONTENT);
        initData();
        button.setOnClickListener(arg0 -> {
            if (!PermissionUtil.checkFloatPermission(this)) {
                new AlertDialog.Builder(this).setMessage("未授予悬浮窗权限，需要先授权")
                        .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUtil.requestSettingCanDrawOverlays(FloatWindowActivity.this);
                            }
                        }).create().show();
                return;
            }
            Intent intent = new Intent(FloatWindowActivity.this,
                    FloatWindowService.class);
            startService(intent);
        });
        setContentView(button);
    }

    private void initData() {
        sp = getSharedPreferences("PersonalSettings", MODE_PRIVATE);
        editor = sp.edit();
        editor.putBoolean("isOnlyShowOnDesktop", false);// 该参数用于配置悬浮窗是否只显示在桌面上，默认为true
        editor.commit();
    }

}
