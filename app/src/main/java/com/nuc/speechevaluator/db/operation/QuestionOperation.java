package com.nuc.speechevaluator.db.operation;

import com.nuc.speechevaluator.db.bean.Category;
import com.nuc.speechevaluator.db.bean.Question;
import com.nuc.speechevaluator.util.Closure;

import java.util.List;

public interface QuestionOperation extends DBOperation<Question> {

    void queryByCategoryId(String categoryId, Closure<List<Question>> onSuccess, Closure<Throwable> onError);


}
