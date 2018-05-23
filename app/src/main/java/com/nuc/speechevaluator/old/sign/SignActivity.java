package com.nuc.speechevaluator.old.sign;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.old.sign.signin.SignInFragment;
import com.nuc.speechevaluator.old.sign.signin.SignInPresenter;
import com.nuc.speechevaluator.old.sign.signin.SignInContract;
import com.nuc.speechevaluator.old.sign.signup.SignUpContract;
import com.nuc.speechevaluator.old.sign.signup.SignUpFragment;
import com.nuc.speechevaluator.old.sign.signup.SignUpPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaoyunrui on 16-8-26.
 */
public class SignActivity extends AppCompatActivity {

    private static final String TAG = "SignActivity";

    private List<String> tabNames = new ArrayList<>();

    private List<Fragment> mFragments = new ArrayList<>();

    private SignInContract.View mSigninFragment;
    private SignInPresenter mSignInPresenter;
    private SignUpContract.View mSignupFragment;
    private SignUpPresenter mSignupPresenter;

    private ActionBar mActionBar;
    private Toolbar mToolbar;
    private TabLayout mTLayout;
    private ViewPager mVPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_act);

        initActionBar();

        initView();

        initFragment();

        initTablayout();

    }

    private void initActionBar() {

        mToolbar = findViewById(R.id.tb_sign);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("账户");
    }

    private void initView() {
        mTLayout = findViewById(R.id.tl_layout);
        mVPager = findViewById(R.id.vp_layout);
    }

    private void initFragment() {
        mSigninFragment = new SignInFragment();
        mFragments.add((Fragment) mSigninFragment);
        mSignInPresenter = new SignInPresenter(mSigninFragment);
        mSignupFragment = new SignUpFragment();
        mFragments.add((Fragment) mSignupFragment);
        mSignupPresenter = new SignUpPresenter(mSignupFragment);
    }

    private void initTablayout() {
        tabNames.add("登录");
        tabNames.add("注册");
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(),
                tabNames, mFragments);
        mVPager.setAdapter(vpAdapter);
        mTLayout.setupWithViewPager(mVPager);
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void turnSignIn() {
        mVPager.setCurrentItem(0);
    }

}
