package com.nfl.libraryoflibrary.view.imitationwechat;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.nfl.libraryoflibrary.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 注意：assets目录中有2张图片很重要
 *
 * @author niu
 */
public class ImitatationWeChatActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private String[] mTitles = new String[]{"1", "2", "3", "4"};
    private FragmentPagerAdapter mAdapter;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private ChangColorIconWithText nfl1, nfl2, nfl3, nfl4;
    private List<ChangColorIconWithText> mnfls = new ArrayList<ChangColorIconWithText>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imitation_wechat);
        getActionBar().setDisplayShowHomeEnabled(false);
        setOverflowButtonAlways();

        initView();
        initDatas();
        mViewPager.setAdapter(mAdapter);
        initEvent();
    }

    private void initEvent() {
        // TODO Auto-generated method stub
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                // TODO Auto-generated method stub
                if (positionOffset > 0) {
                    ChangColorIconWithText left = mnfls.get(position);
                    ChangColorIconWithText right = mnfls.get(position + 1);
                    left.setIconAlpha(1 - positionOffset);
                    right.setIconAlpha(positionOffset);
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initDatas() {
        // TODO Auto-generated method stub
        for (String title : mTitles) {
            TabFragment tabFragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString(tabFragment.TITLE, title);
            tabFragment.setArguments(bundle);
            mTabs.add(tabFragment);
        }
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mAdapter = new FragmentPagerAdapter(mFragmentManager) {

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                // TODO Auto-generated method stub
                return mTabs.get(arg0);
            }
        };
    }

    private void initView() {
        // TODO Auto-generated method stub
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        nfl1 = (ChangColorIconWithText) findViewById(R.id.nfl1);
        mnfls.add(nfl1);
        nfl2 = (ChangColorIconWithText) findViewById(R.id.nfl2);
        mnfls.add(nfl2);
        nfl3 = (ChangColorIconWithText) findViewById(R.id.nfl3);
        mnfls.add(nfl3);
        nfl4 = (ChangColorIconWithText) findViewById(R.id.nfl4);
        mnfls.add(nfl4);

        nfl1.setOnClickListener(colorIconWithTextOnClickListener);
        nfl2.setOnClickListener(colorIconWithTextOnClickListener);
        nfl3.setOnClickListener(colorIconWithTextOnClickListener);
        nfl4.setOnClickListener(colorIconWithTextOnClickListener);

        nfl1.setIconAlpha(1.0f);
    }

    private OnClickListener colorIconWithTextOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            clickTab(v);
        }

        private void clickTab(View v) {
            // TODO Auto-generated method stub
            resetOtherTabs();
            int i = v.getId();
            if (i == R.id.nfl1) {
                mnfls.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
            } else if (i == R.id.nfl2) {
                mnfls.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
            } else if (i == R.id.nfl3) {
                mnfls.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
            } else if (i == R.id.nfl4) {
                mnfls.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
            } else {
            }
        }

    };

    private void resetOtherTabs() {
        // TODO Auto-generated method stub
        for (int i = 0; i < mnfls.size(); i++) {
            mnfls.get(i).setIconAlpha(0f);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.imitationwechat, menu);
        return true;
    }

    private void setOverflowButtonAlways() {
        ViewConfiguration config = ViewConfiguration.get(this);
        try {
            Field menuKey = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.set(config, false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        // TODO Auto-generated method stub
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                // menu显示图标
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return super.onMenuOpened(featureId, menu);
    }


}
