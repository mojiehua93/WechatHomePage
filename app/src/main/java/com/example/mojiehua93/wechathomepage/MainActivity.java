package com.example.mojiehua93.wechathomepage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragmentList;
    private String[] mTiles = new String[]{
      "First Fragment", "Second Fragment",
            "Third Fragment", "Fourth Fragment",
    };
    private FragmentPagerAdapter mAdapter;
    private List<BottomTabView> mTabsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOverflowButtonAlways();

        mTabsList = new ArrayList<>();
        initView();
        initData();
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(this);
    }

    private void initData() {
        mFragmentList = new ArrayList<>();
        for (String title : mTiles){
            TabFragment tabFragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TabFragment.TITLE, title);
            tabFragment.setArguments(bundle);
            mFragmentList.add(tabFragment);
        }
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        BottomTabView tabWechat = (BottomTabView) findViewById(R.id.id_tab_wechat);
        mTabsList.add(tabWechat);
        BottomTabView tabContact = (BottomTabView) findViewById(R.id.id_tab_contact);
        mTabsList.add(tabContact);
        BottomTabView tabFound = (BottomTabView) findViewById(R.id.id_tab_found);
        mTabsList.add(tabFound);
        BottomTabView tabMe = (BottomTabView) findViewById(R.id.id_tab_me);
        mTabsList.add(tabMe);

        tabWechat.setOnClickListener(this);
        tabContact.setOnClickListener(this);
        tabFound.setOnClickListener(this);
        tabMe.setOnClickListener(this);

        tabWechat.setIconAlpha(1.0F);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    private void setOverflowButtonAlways(){
        ViewConfiguration configuration = ViewConfiguration.get(this);
        try {
            Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(configuration, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null && featureId == AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR){
            if (menu.getClass().getSimpleName().equals("MenuBuilder")){
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",
                            Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onClick(View v) {
        resetOtherTabsColor();
        switch (v.getId()){
            case R.id.id_tab_wechat:
                mTabsList.get(0).setIconAlpha(1.0F);
                mViewPager.setCurrentItem(0, false);
                break;

            case R.id.id_tab_contact:
                mTabsList.get(1).setIconAlpha(1.0F);
                mViewPager.setCurrentItem(1, false);
                break;

            case R.id.id_tab_found:
                mTabsList.get(2).setIconAlpha(1.0F);
                mViewPager.setCurrentItem(2, false);
                break;

            case R.id.id_tab_me:
                mTabsList.get(3).setIconAlpha(1.0F);
                mViewPager.setCurrentItem(3, false);
                break;

            default:break;
        }

    }

    private void resetOtherTabsColor() {

        for (int i=0; i < mTabsList.size(); i++){
            mTabsList.get(i).setIconAlpha(0);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("jiehua", "<onPageScrolled> position = " + position
                + " positionOffset = " + positionOffset);
        if (positionOffset > 0) {
            BottomTabView left = mTabsList.get(position);
            BottomTabView right = mTabsList.get(position + 1);

            left.setIconAlpha(1.0F - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
