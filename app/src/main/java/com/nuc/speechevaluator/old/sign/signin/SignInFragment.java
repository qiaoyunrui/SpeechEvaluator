package com.nuc.speechevaluator.old.sign.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/*import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.juhezi.citymemory.R;
import com.juhezi.citymemory.map.MapContract;
import com.juhezi.citymemory.other.Config;*/
import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.util.Config;

/**
 * Created by qiaoyunrui on 16-8-26.
 */
public class SignInFragment extends Fragment implements SignInContract.View {

    private static final String TAG = "SignInFragment";

    private SignInContract.Presenter mPresenter;
    private View rootView;
    private TextInputLayout mTILUsername;
    private TextInputLayout mTILPasswd;
    private FloatingActionButton mFabSignin;
    private ProgressBar mPbSignin;

    private boolean usernameOK = false;
    private boolean passwdOK = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.signin_frag, container, false);
        mTILUsername = rootView.findViewById(R.id.til_signin_username);
        mTILPasswd = rootView.findViewById(R.id.til_signin_passwd);
        mFabSignin = rootView.findViewById(R.id.fab_signin);
        mPbSignin = rootView.findViewById(R.id.pb_signin);
        configEdit();
        initEvent();
        return rootView;
    }

    private void configEdit() {
        mTILUsername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mTILUsername.setError("用户名不能为空");
                    usernameOK = false;
                    hideFabSignIn();
                } else {
                    mTILUsername.setError(null);
                    usernameOK = true;
                    showFabSignIn();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTILPasswd.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mTILPasswd.setError("密码不能为空");
                    passwdOK = false;
                    hideFabSignIn();
                } else {
                    mTILPasswd.setError(null);
                    passwdOK = true;
                    showFabSignIn();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initEvent() {
        mFabSignin.setOnClickListener(v -> {
            showProgressBar();
            Log.i(TAG, "onClick: 登录");
            mPresenter.signIn(mTILUsername.getEditText().getText().toString().trim(),
                    mTILPasswd.getEditText().getText().toString().trim(),
                    user -> {
                        hideProgressBar();
                        if (user != null) {
                            showSnackBar("登陆成功");
                            getActivity().setResult(Config.SIGN_CODE, null);
                            getActivity().finish();
                        } else {
                            showSnackBar("用户名和密码不匹配");
                        }
                    });
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    @Override
    public void onDestroy() {
        mPresenter = null;
        super.onDestroy();
    }

    @Override
    public void setPresenter(SignInContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressBar() {
        mPbSignin.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mPbSignin.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showFabSignIn() {
        if (usernameOK && passwdOK) {
            mFabSignin.show();
        }

    }

    @Override
    public void hideFabSignIn() {
        mFabSignin.hide();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

}
