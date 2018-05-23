package com.nuc.speechevaluator.old.sign.signin;

import com.nuc.speechevaluator.BasePresenter;
import com.nuc.speechevaluator.BaseView;
import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.util.Closure;

/**
 * Created by qiaoyunrui on 16-8-26.
 */
public interface SignInContract {

    interface Presenter extends BasePresenter {

        void signIn(String username, String password, Closure<User> callback);

    }

    interface View extends BaseView<Presenter> {

        void showProgressBar();

        void hideProgressBar();

        void showFabSignIn();

        void hideFabSignIn();

        void showSnackBar(String message);

    }

}
