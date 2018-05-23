package com.nuc.speechevaluator.old.sign.signup;

import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.util.Closure;

/**
 * Created by qiaoyunrui on 16-8-26.
 */
public class SignUpPresenter implements SignUpContract.Presenter {

    private static final String TAG = "SignUpPresenter";

    private SignUpContract.View mView;

    public SignUpPresenter(SignUpContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.hideProgressBar();
        mView.unenableSignUp();
    }

    @Override
    public void signUp(String username, String password, Closure<User> callback) {
        mView.showProgressBar();
        // TODO: 2018/5/23 用户注册的逻辑
    }
}
