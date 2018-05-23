package com.nuc.speechevaluator.old.sign.signup;

import com.nuc.speechevaluator.BasePresenter;
import com.nuc.speechevaluator.BaseView;
import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.util.Closure;

/**
 * Created by qiaoyunrui on 16-8-26.
 */
public interface SignUpContract {

    interface Presenter extends BasePresenter {
        void signUp(String username, String password, Closure<User> callback);
    }

    interface View extends BaseView<Presenter> {

        void showProgressBar();

        void hideProgressBar();

        void showToast(String message);

        void enableSignUp();

        void unenableSignUp();
    }

}
