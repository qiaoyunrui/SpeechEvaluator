package com.nuc.speechevaluator.db;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.db.impl.UserImpl;
import com.nuc.speechevaluator.db.operation.UserOperation;
import com.nuc.speechevaluator.old.sign.SignActivity;
import com.nuc.speechevaluator.util.Closure;
import com.nuc.speechevaluator.util.Config;

/**
 * 用户管理的类
 */
public class UserService {

    private static final String TAG = "UserService";

    private static UserService sInstance;

    /**
     * 单例模式，只能创建一个对象
     * @param context
     * @return
     */
    public static UserService getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UserService(context);
        }
        return sInstance;
    }

    private Context mContext;

    private User mCurrentUser;   //当前登录的用户

    private UserOperation mOperation;

    private UserService(Context context) {
        this.mContext = context;
        mOperation = new UserImpl();
    }

    /**
     * 获取当前登录用户
     */
    public void getCurrentUser(Closure<User> closure) {
        if (closure == null) return;
        if (mCurrentUser != null) {
            closure.invoke(mCurrentUser);
            return;
        }
        if (mContext == null) {
            closure.invoke(null);
            return;
        }
        // SharedPreferences 的作用就是序列化对象，将基本数据类型存储在本地
        // 反序列化 UserID，从数据库查询数据
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Config.KEY_USER_SERVICE, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(Config.KEY_USER_ID, "");
        if (TextUtils.isEmpty(id)) {    // id 为空，意味着没有登录。
            closure.invoke(null);
            return;
        }
        // 通过 userId 查询 User 的信息
        mOperation.query(id, closure, throwable -> {
            closure.invoke(null);
            throwable.printStackTrace();
        });
    }

    /**
     * 是否有用户登录
     * @return
     */
    public boolean isOnline() {
        if (mCurrentUser != null) return true;
        if (mContext != null) {
            // 从本地获取存储的userID
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Config.KEY_USER_SERVICE, Context.MODE_PRIVATE);
            String id = sharedPreferences.getString(Config.KEY_USER_ID, "");
            return !TextUtils.isEmpty(id);
        }
        return false;
    }

    // 登录
    public void signIn(User user) {
        this.mCurrentUser = user;
        if (mCurrentUser != null && mContext != null) {
            String id = mCurrentUser.getId();
            // 把当前登录用户的ID存储在本地
            @SuppressLint("CommitPrefEdits")
            SharedPreferences.Editor editor = mContext
                    .getSharedPreferences(Config.KEY_USER_SERVICE, Context.MODE_PRIVATE)
                    .edit();
            editor.putString(Config.KEY_USER_ID, id);
            editor.apply();
        }
    }

    // 退出登录
    public void signOut() {
        mCurrentUser = null;
        if (mContext != null) {
            // 将存储在本地的ID设置为 ""
            @SuppressLint("CommitPrefEdits")
            SharedPreferences.Editor editor = mContext
                    .getSharedPreferences(Config.KEY_USER_SERVICE, Context.MODE_PRIVATE)
                    .edit();
            editor.putString(Config.KEY_USER_ID, "");
            editor.apply();
        }
    }

}