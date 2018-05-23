package com.nuc.speechevaluator.db;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.db.impl.UserImpl;
import com.nuc.speechevaluator.db.operation.UserOperation;
import com.nuc.speechevaluator.old.sign.SignActivity;
import com.nuc.speechevaluator.util.Closure;
import com.nuc.speechevaluator.util.Config;

public class UserService {

    private static UserService sInstance;

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

    public int getUserType() {
        if (mCurrentUser == null) {
            return User.USER_TYPE_NONE;
        } else {
            return mCurrentUser.getType();
        }
    }

    /**
     * 获取当前登录用户
     */
    public void getCurrentUser(Closure<User> closure) {
        if (closure == null) return;
        if (mCurrentUser != null) {
            closure.invoke(mCurrentUser);
        }
        if (mContext == null) {
            closure.invoke(null);
            return;
        }
        // 反序列化 UserID，从数据库查询数据
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Config.KEY_USER_SERVICE, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(Config.KEY_USER_ID, "");
        if (TextUtils.isEmpty(id)) {    // id 为空，意味着没有登录。
            closure.invoke(null);
            return;
        }
        mOperation.query(id, closure, throwable -> closure.invoke(null));
    }

    public boolean isOnline() {
        if (mCurrentUser != null) return true;
        if (mContext != null) {
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
            @SuppressLint("CommitPrefEdits")
            SharedPreferences.Editor editor = mContext
                    .getSharedPreferences(Config.KEY_USER_SERVICE, Context.MODE_PRIVATE)
                    .edit();
            editor.putString(Config.KEY_USER_ID, "");
        }
    }

    /**
     * 检查并登陆
     *
     * @param context 必须是 Activity 的 context
     */
    public static void checkAndSignIn(Context context, int requestCode) {
        if (context == null) return;
        UserService userService = UserService.getInstance(context);
        if (!userService.isOnline()) {
            Intent intent = new Intent(context, SignActivity.class);
            ((Activity) context).startActivityForResult(intent, requestCode);
        }
    }

}