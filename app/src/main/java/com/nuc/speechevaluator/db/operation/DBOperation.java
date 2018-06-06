package com.nuc.speechevaluator.db.operation;

import com.nuc.speechevaluator.util.Closure;

import java.util.List;

/**
 * 数据库的增删改查基本操作
 * @param <T>
 */
public interface DBOperation<T> {

    void add(T t, Closure<Void> onSuccess, Closure<Throwable> onError);

    void update(T t, Closure<Void> onSuccess, Closure<Throwable> onError);

    void remove(String id, Closure<T> onSuccess, Closure<Throwable> onError);

    void query(String id, Closure<T> onSuccess, Closure<Throwable> onError);

    void queryAll(Closure<List<T>> onSuccess, Closure<Throwable> onError);

}
