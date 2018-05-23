package com.nuc.speechevaluator.old.sign.signin;

import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.util.Closure;

/**
 * Created by qiaoyunrui on 16-8-26.
 */
public class SignInPresenter implements SignInContract.Presenter {

    private static final String TAG = "SignInPresenter";

    private SignInContract.View mView;

    public SignInPresenter(SignInContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.hideProgressBar();
        mView.hideFabSignIn();
    }

    @Override
    public void signIn(String username, String password, Closure<User> callback) {
        // TODO: 2018/5/23 登录的实现逻辑
    }
}
