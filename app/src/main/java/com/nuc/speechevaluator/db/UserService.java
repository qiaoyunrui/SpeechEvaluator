package com.nuc.speechevaluator.db;

import com.nuc.speechevaluator.db.bean.User;

public class UserService {

    private static final class UserServiceHolder {
        private static UserService sInstance = new UserService();
    }

    public static UserService getInstance() {
        return UserServiceHolder.sInstance;
    }

    private User currentUser;   //当前登录的用户

    public int getUserType() {
        if (currentUser == null) {
            return User.USER_TYPE_NONE;
        } else {
            return currentUser.getType();
        }
    }

    /**
     * 获取当前登录用户
     */
    public User getCurrentUser() {
        return currentUser;
    }

    public void signIn(User user) {
        this.currentUser = user;
    }

    public void signOut() {
        currentUser = null;
    }

}