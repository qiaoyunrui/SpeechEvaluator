package com.nuc.speechevaluator.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.adapter.QuestionAdapter;
import com.nuc.speechevaluator.db.bean.Question;
import com.nuc.speechevaluator.db.impl.QuestionImpl;
import com.nuc.speechevaluator.db.operation.QuestionOperation;
import com.nuc.speechevaluator.util.Closure;

import java.util.List;

/**
 * 这一页要加载数据
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FloatingActionButton mFabUpload;
    private ViewGroup mVgLoading;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private RecyclerView mRecyclerView;
    private QuestionOperation mQuestionOperation = new QuestionImpl();

    private QuestionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        mVgLoading.setVisibility(View.VISIBLE);
        refresh();
    }

    private void initView() {
        mFabUpload = findViewById(R.id.fab_main_upload);
        mVgLoading = findViewById(R.id.vg_loading);
        mToolbar = findViewById(R.id.tb_common);
        mRecyclerView = findViewById(R.id.rv_main_question_list);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(getString(R.string.app_name));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new QuestionAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initEvent() {
        mFabUpload.setOnClickListener(v -> turn2UploadActivity());
        mAdapter.setItemClickListener(this::turn2EvaluatorActivity);
    }

    private void refresh() {
        mQuestionOperation.queryAll(questions -> {
            mVgLoading.setVisibility(View.GONE);
            mAdapter.setQuestions(questions);
        }, throwable -> {
            mVgLoading.setVisibility(View.GONE);
//            Toast.makeText(this, "加载数据失败", Toast.LENGTH_SHORT).show();
        });
    }

    private void turn2UploadActivity() {
        Intent intent = UploadActivity.newIntent(this);
        startActivityForResult(intent, UploadActivity.REQUEST_CODE_UPLOAD);
    }

    private void turn2EvaluatorActivity(Question question) {
        Intent intent = EvaluatorActivity.newIntent(this, question);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UploadActivity.REQUEST_CODE_UPLOAD) {
            if (resultCode == UploadActivity.RESULT_CODE_UPLOAD) {
                mVgLoading.setVisibility(View.VISIBLE);
                refresh();
            }
        }

    }
}
