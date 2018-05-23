package com.nuc.speechevaluator.old.sign.signup;

import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.db.impl.UserImpl;
import com.nuc.speechevaluator.db.operation.UserOperation;
import com.nuc.speechevaluator.util.Closure;

/**
 * Created by qiaoyunrui on 16-8-26.
 */
public class SignUpPresenter implements SignUpContract.Presenter {

    private static final String TAG = "SignUpPresenter";

    private SignUpContract.View mView;
    private UserOperation mOperation = new UserImpl();

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
    public void signUp(String username, String password, int type, Closure<User> callback) {
        mView.showProgressBar();
        // 首先查询 username 是否存在..
        mOperation.queryByUsername(username, user -> {
            // 用户名已经存在，注册失败
            if (callback != null) {
                callback.invoke(null);
            }
        }, throwable -> {
            User user = User.createUser(type, username, password);
            mOperation.add(user, aVoid -> {
                if (callback != null) {
                    callback.invoke(user);
                }
            }, throwableX -> {
                if (callback != null) {
                    callback.invoke(null);
                }
            });
        });

    }
}
