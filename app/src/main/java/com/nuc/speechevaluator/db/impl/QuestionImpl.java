package com.nuc.speechevaluator.db.impl;

import android.util.Log;

import com.nuc.speechevaluator.db.bean.Question;
import com.nuc.speechevaluator.db.operation.QuestionOperation;
import com.nuc.speechevaluator.util.Closure;
import com.nuc.speechevaluator.util.ErrorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class QuestionImpl implements QuestionOperation {

    private Realm mRealm = Realm.getDefaultInstance();
    private static final String TAG = "QuestionImpl";

    @Override
    public void add(Question question, Closure<Void> onSuccess, Closure<Throwable> onError) {
        mRealm.executeTransactionAsync(realm -> {
            realm.copyToRealm(question);
            Log.i(TAG, "add: " + Thread.currentThread());
        }, () -> {
            if (onSuccess != null) {
                onSuccess.invoke(null);
            }
        }, error -> ErrorUtil.invokeThrowable(onError, error));
    }

    @Override
    public void update(Question question, Closure<Void> onSuccess, Closure<Throwable> onError) {

    }

    @Override
    public void remove(String id, Closure<Question> onSuccess, Closure<Throwable> onError) {

    }

    @Override
    public void query(String id, Closure<Question> onSuccess, Closure<Throwable> onError) {

    }

    @Override
    public void queryAll(Closure<List<Question>> onSuccess, Closure<Throwable> onError) {
        mRealm.executeTransactionAsync(realm -> {
            Log.i(TAG, "queryAll: " + Thread.currentThread());
            RealmResults<Question> results = realm.where(Question.class)
                    .sort("date", Sort.DESCENDING)
                    .findAll();
            if (onSuccess != null) {
                onSuccess.invoke(realm.copyFromRealm(results));
            }
        }, error -> ErrorUtil.invokeThrowable(onError, error));
    }
}
