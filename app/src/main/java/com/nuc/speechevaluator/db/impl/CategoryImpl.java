package com.nuc.speechevaluator.db.impl;

import android.util.Log;

import com.nuc.speechevaluator.db.bean.Category;
import com.nuc.speechevaluator.db.bean.Question;
import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.db.operation.CategoryOperation;
import com.nuc.speechevaluator.util.Closure;
import com.nuc.speechevaluator.util.ErrorUtil;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class CategoryImpl implements CategoryOperation {

    private Realm mRealm = Realm.getDefaultInstance();

    private static final String TAG = "CategoryImpl";

    @Override
    public void add(Category category, Closure<Void> onSuccess, Closure<Throwable> onError) {
        mRealm.executeTransactionAsync(realm -> {
            realm.copyToRealm(category);
        }, () -> {  // 添加成功
            if (onSuccess != null) {
                onSuccess.invoke(null);
            }
        }, error -> ErrorUtil.invokeThrowable(onError, error));
    }

    @Override
    public void update(Category category, Closure<Void> onSuccess, Closure<Throwable> onError) {

    }

    @Override
    public void remove(String id, Closure<Category> onSuccess, Closure<Throwable> onError) {

    }

    @Override
    public void query(String id, Closure<Category> onSuccess, Closure<Throwable> onError) {
        mRealm.executeTransactionAsync(realm -> {
            Category category = realm.where(Category.class)
                    .equalTo("id", id)
                    .findFirst();
            if (onSuccess != null) {
                onSuccess.invoke(realm.copyFromRealm(category));
            }
        }, error -> ErrorUtil.invokeThrowable(onError, error));
    }

    @Override
    public void queryAll(Closure<List<Category>> onSuccess, Closure<Throwable> onError) {
        mRealm.executeTransactionAsync(realm -> {
            Log.i(TAG, "queryAll: " + Thread.currentThread());
            RealmResults<Category> results = realm.where(Category.class)
                    .findAll();
            if (onSuccess != null) {
                onSuccess.invoke(realm.copyFromRealm(results));
            }
        }, error -> ErrorUtil.invokeThrowable(onError, error));
    }

    @Override
    public void queryLikeTitle(String title, Closure<List<Category>> onSuccess, Closure<Throwable> onError) {
        mRealm.executeTransactionAsync(realm -> {
            Log.i(TAG, "queryAll: " + Thread.currentThread());
            RealmResults<Category> results = realm.where(Category.class)
                    .like("title", title)
                    .findAll();
            if (onSuccess != null) {
                onSuccess.invoke(realm.copyFromRealm(results));
            }
        }, error -> ErrorUtil.invokeThrowable(onError, error));
    }
}
