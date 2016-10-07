package com.nfl.libraryoflibrary.view.floatwindow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

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
		button.setWidth(LayoutParams.MATCH_PARENT);
		button.setHeight(LayoutParams.WRAP_CONTENT);
		initData();
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(FloatWindowActivity.this,
						FloatWindowService.class);
				startService(intent);
			}
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
