package com.nuc.speechevaluator.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.activity.EvaluatorActivity;
import com.nuc.speechevaluator.adapter.QuestionAdapter;
import com.nuc.speechevaluator.db.bean.Question;
import com.nuc.speechevaluator.db.impl.QuestionImpl;
import com.nuc.speechevaluator.db.operation.QuestionOperation;

/**
 * 问题的页面，子页面
 */
public class QuestionFragment extends Fragment {

    private static final String TAG = "QuestionFragment";


    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private View rootView;
    private QuestionOperation mQuestionOperation = new QuestionImpl();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private QuestionAdapter mAdapter;  // 给 RecylerView 提供数据


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_question, container, false);
        mRecyclerView = rootView.findViewById(R.id.rv_question_list);
        mEmptyView = rootView.findViewById(R.id.vg_empty);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new QuestionAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        initEvent();
        return rootView;
    }

    private void initEvent() {
        // Item 单击
        mAdapter.setItemClickListener(question -> {
            Log.i(TAG, "invoke: " + question);
            turn2EvaluatorActivity(question);
        });
        mAdapter.setEmptyClosure(aBoolean -> mEmptyView.setVisibility(aBoolean ? View.VISIBLE : View.GONE));
    }

    private void turn2EvaluatorActivity(Question question) {
        Intent intent = EvaluatorActivity.newIntent(getContext(), question);
        startActivity(intent);
    }

    public void refresh(String categoryId) {
        // 从数据库加载当前分类所有的题目，并显示
        mQuestionOperation.queryByCategoryId(categoryId, questions -> {
            // 加载成功，question - 获取的数据 List<Question>
            mHandler.post(() -> mAdapter.setQuestions(questions));
        }, throwable -> {

            // 加载失败
//            Toast.makeText(this, "加载数据失败", Toast.LENGTH_SHORT).show();
        });
    }


}
