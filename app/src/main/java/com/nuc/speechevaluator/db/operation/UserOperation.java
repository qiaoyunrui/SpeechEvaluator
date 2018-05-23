package com.nuc.speechevaluator.db.operation;

import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.util.Closure;

public interface UserOperation extends DBOperation<User> {

    /**
     * 通过 用户名进行查询
     */
    void queryByUsername(String username, Closure<User> onSuccess, Closure<Throwable> onError);

}
