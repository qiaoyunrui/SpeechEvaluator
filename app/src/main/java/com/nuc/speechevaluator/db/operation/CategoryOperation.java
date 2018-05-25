package com.nuc.speechevaluator.db.operation;

import com.nuc.speechevaluator.db.bean.Category;
import com.nuc.speechevaluator.util.Closure;

import java.util.List;

public interface CategoryOperation extends DBOperation<Category> {

    void queryLikeTitle(String title, Closure<List<Category>> onSuccess, Closure<Throwable> onError);


}
