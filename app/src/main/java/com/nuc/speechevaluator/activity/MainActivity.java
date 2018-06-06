package com.nuc.speechevaluator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.fragment.CategoryFragment;
import com.nuc.speechevaluator.fragment.MainFragment;
import com.nuc.speechevaluator.fragment.MeFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private BottomNavigationView mBottomView;
    private ViewGroup mWrapper;
    private MeFragment mMeFragment; // 第三个 tab 所对应的页面
    private MainFragment mMainFragment;    // 第一个 tab 对应的页面
    private CategoryFragment mCategoryFragment; // 第二个 Fragment ，分类页面

    private Fragment mShowingFragment;  //当前显示的 fragment

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));   // 修改底部虚拟按键部分的颜色
        initView();
        initFragment();
        initEvent();
    }


    private void initView() {
        mBottomView = findViewById(R.id.bnv_main_bottom);
        mWrapper = findViewById(R.id.vg_main_fragment_wrapper);
    }

    private void initFragment() {
        mMeFragment = new MeFragment();
        mMainFragment = new MainFragment();
        mCategoryFragment = new CategoryFragment();
        mShowingFragment = mMainFragment;
        // 把 3 个Fragment 添加到 Actiivity，并同时隐藏第二个和第三个
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.vg_main_fragment_wrapper, mMainFragment)
                .add(R.id.vg_main_fragment_wrapper, mCategoryFragment)
                .add(R.id.vg_main_fragment_wrapper, mMeFragment)
                .show(mMainFragment)
                .hide(mCategoryFragment)
                .hide(mMeFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void initEvent() {
        // 点击底部 Tab 时会调用
        mBottomView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_question:
                    // 切换到题目列表页面，即第一个页面
                    openFragment(mMainFragment);
                    break;
                case R.id.menu_category:
                    openFragment(mCategoryFragment);
                    // 切换到题目分类页面，即第二个页面
                    break;
                case R.id.menu_me:
                    openFragment(mMeFragment);
                    // 切换到个人页面，即第三个页面
                    break;
            }
            return true;
        });
    }

    private void openFragment(Fragment fragment) {
        if (fragment == null || fragment == mShowingFragment) return;
        getSupportFragmentManager()
                .beginTransaction()
                .hide(mShowingFragment)
                .show(fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        mShowingFragment = fragment;
    }

}
