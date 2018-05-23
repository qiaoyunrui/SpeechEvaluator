package com.nuc.speechevaluator.old.sign.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.old.sign.SignActivity;

/**
 * Created by qiaoyunrui on 16-8-26.
 */
public class SignUpFragment extends Fragment implements SignUpContract.View {

    private static final String TAG = "SignUpFragment";

    private SignUpContract.Presenter mPresenter;

    private TextInputLayout mTILUsername;
    private TextInputLayout mTILPasswd;
    private TextInputLayout mTILPasswdAgain;

    private FloatingActionButton mFabSignup;
    private ProgressBar mPbSignup;

    private View rootView;

    private boolean usernameOK = false;
    private boolean passwdOK = false;
    private boolean passwdAgainOK = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.signup_frag, container, false);
        mTILUsername = rootView.findViewById(R.id.til_signup_username);
        mTILPasswd = rootView.findViewById(R.id.til_signup_passwd);
        mTILPasswdAgain = rootView.findViewById(R.id.til_signup_passwd_again);
        mFabSignup = rootView.findViewById(R.id.fab_signup);
        mPbSignup = rootView.findViewById(R.id.pb_signup);
        configEdit();
        initEvent();
        return rootView;
    }

    private void initEvent() {
        mFabSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.signUp(mTILUsername.getEditText().getText().toString(),
                        mTILPasswd.getEditText().getText().toString(),
                        user -> {
                            hideProgressBar();
                            if (user != null) {
                                showToast("注册成功,欢迎使用");
                                ((SignActivity) getActivity()).turnSignIn();
                            } else {
                                showToast("注册失败");
                            }
                        });
            }
        });
    }

    @Override
    public void onDestroy() {
        mPresenter = null;
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressBar() {
        mPbSignup.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mPbSignup.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showToast(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void enableSignUp() {
        if (usernameOK && passwdOK && passwdAgainOK) {
            mFabSignup.show();
        }

    }

    @Override
    public void unenableSignUp() {
        mFabSignup.hide();
    }

    private void configEdit() {
        mTILUsername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    mTILUsername.setError("用户名长度太短");
                    usernameOK = false;
                    unenableSignUp();
                } else {
                    mTILUsername.setError(null);
                    usernameOK = true;
                    enableSignUp();
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
                if (s.length() < 6) {
                    passwdOK = false;
                    mTILPasswd.setError("密码长度太短");
                    unenableSignUp();
                } else {
                    mTILPasswd.setError(null);
                    passwdOK = true;
                    enableSignUp();
                }
                if (!s.toString().equals(
                        mTILPasswdAgain.getEditText().getText().toString())) {
                    mTILPasswdAgain.setError("密码不一致");
                    unenableSignUp();
                    passwdAgainOK = false;
                } else {
                    mTILPasswdAgain.setError(null);
                    passwdAgainOK = true;
                    enableSignUp();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mTILPasswdAgain.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(mTILPasswd.getEditText().getText().toString())) {
                    mTILPasswdAgain.setError("密码不一致");
                    passwdAgainOK = false;
                    unenableSignUp();
                } else {
                    mTILPasswdAgain.setError(null);
                    passwdAgainOK = true;
                    enableSignUp();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
