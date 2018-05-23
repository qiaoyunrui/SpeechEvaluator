package com.nuc.speechevaluator.db.impl;

import android.util.Log;

import com.nuc.speechevaluator.db.bean.Question;
import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.db.operation.UserOperation;
import com.nuc.speechevaluator.util.Closure;
import com.nuc.speechevaluator.util.ErrorUtil;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class UserImpl implements UserOperation {

    private Realm mRealm = Realm.getDefaultInstance();
    private static final String TAG = "UserImpl";

    @Override
    public void add(User user, Closure<Void> onSuccess, Closure<Throwable> onError) {
        mRealm.executeTransactionAsync(realm -> {
            realm.copyToRealm(user);
            Log.i(TAG, "add: " + Thread.currentThread());
        }, () -> {  // 添加成功
            if (onSuccess != null) {
                onSuccess.invoke(null);
            }
        }, error -> ErrorUtil.invokeThrowable(onError, error));
    }

    @Override
    public void update(User user, Closure<Void> onSuccess, Closure<Throwable> onError) {

    }

    @Override
    public void remove(String id, Closure<User> onSuccess, Closure<Throwable> onError) {

    }

    @Override
    public void query(String id, Closure<User> onSuccess, Closure<Throwable> onError) {
        mRealm.executeTransactionAsync(realm -> {
            User user = realm.where(User.class)
                    .equalTo("id", id)
                    .findFirst();
            if (onSuccess != null) {
                onSuccess.invoke(realm.copyFromRealm(user));
            }
        }, error -> ErrorUtil.invokeThrowable(onError, error));
    }

    @Override
    public void queryByUsername(String username, Closure<User> onSuccess, Closure<Throwable> onError) {
        mRealm.executeTransactionAsync(realm -> {
            User user = realm.where(User.class)
                    .equalTo("username", username)
                    .findFirst();
            if (onSuccess != null) {
                onSuccess.invoke(realm.copyFromRealm(user));
            }
        }, error -> ErrorUtil.invokeThrowable(onError, error));
    }

    @Override
    public void queryAll(Closure<List<User>> onSuccess, Closure<Throwable> onError) {

    }

}
