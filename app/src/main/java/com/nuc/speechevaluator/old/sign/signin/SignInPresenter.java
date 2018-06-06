package com.nuc.speechevaluator.old.sign.signin;

import android.text.TextUtils;

import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.db.impl.UserImpl;
import com.nuc.speechevaluator.db.operation.UserOperation;
import com.nuc.speechevaluator.util.Closure;

public class SignInPresenter implements SignInContract.Presenter {

    private static final String TAG = "SignInPresenter";

    private SignInContract.View mView;
    private UserOperation mOperation = new UserImpl();


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
        mOperation.queryByUsername(username, user -> {
            if (user != null) {
                if (TextUtils.equals(user.getPassword(), password)) {    //密码正确
                    if (callback != null) {
                        callback.invoke(user);
                    }
                } else {
                    if (callback != null) {
                        callback.invoke(null);
                    }
                }
            } else {
                if (callback != null) {
                    callback.invoke(null);
                }
            }
        }, throwable -> {
            if (callback != null) {
                callback.invoke(null);
            }
        });
    }
}
