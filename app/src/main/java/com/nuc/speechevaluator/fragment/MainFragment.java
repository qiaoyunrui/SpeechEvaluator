package com.nuc.speechevaluator.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.activity.EvaluatorActivity;
import com.nuc.speechevaluator.activity.UploadActivity;
import com.nuc.speechevaluator.adapter.QuestionAdapter;
import com.nuc.speechevaluator.db.UserService;
import com.nuc.speechevaluator.db.bean.Question;
import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.db.impl.QuestionImpl;
import com.nuc.speechevaluator.db.operation.QuestionOperation;
import com.nuc.speechevaluator.util.Config;

/**
 * 主页面
 * 从数据库加载题目的信息
 */
public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private boolean isAdminer = false;   //是否为管理员

    private FloatingActionButton mFabUpload;
    // Loading 界面
    private ViewGroup mVgLoading;
    private ViewGroup mVgEmpty;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private RecyclerView mRecyclerView;
    private QuestionOperation mQuestionOperation = new QuestionImpl();

    private QuestionAdapter mAdapter;  // 给 RecylerView 提供数据

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        initEvent();
        mVgLoading.setVisibility(View.VISIBLE);
        refresh();
        return mRootView;
    }

    private void initView() {
        mFabUpload = mRootView.findViewById(R.id.fab_main_upload);
        mVgLoading = mRootView.findViewById(R.id.vg_loading);
        mVgEmpty = mRootView.findViewById(R.id.vg_empty);
        mToolbar = mRootView.findViewById(R.id.tb_common);
        mRecyclerView = mRootView.findViewById(R.id.rv_main_question_list);
        mToolbar.setTitle(getString(R.string.question));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new QuestionAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initEvent() {
        // 加号按钮的点击事件
        mFabUpload.setOnClickListener(v -> turn2UploadActivity());
        // RecyclerView 中每个 Item 的点击事件
//        mAdapter.setItemClickListener(this::turn2EvaluatorActivity);
        //            Log.i(TAG, "invoke: " + question);
        mAdapter.setItemClickListener(this::turn2EvaluatorActivity);
        mAdapter.setEmptyClosure(aBoolean -> mVgEmpty.setVisibility(aBoolean ? View.VISIBLE : View.GONE));
        mAdapter.setLongPressClosure((question, position) -> {  // 长按 Item
            Log.i(TAG, "initEvent: 是管理员嘛？ " + isAdminer);
            // 显示删除对话框
            if (!isAdminer) return;
            new AlertDialog.Builder(getContext())
                    .setMessage("是否删除这个题目？")
                    .setTitle("信息")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialog, which) -> {
                        if (question == null) return;
                        mQuestionOperation.remove(question.getId(), tempQuestion -> {
                            mHandler.post(() -> {
                                if (question != null) {
                                    Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                    mAdapter.remove(position);
                                } else {
                                    Toast.makeText(getContext(), "出了点问题", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }, throwable -> {
                            mHandler.post(() -> {
                                Toast.makeText(getContext(), "出了点问题", Toast.LENGTH_SHORT).show();
                            });
                        });
                    })
                    .create()
                    .show();
        });
    }

    private void refresh() {
        UserService.getInstance(getContext()).getCurrentUser(user -> mHandler.post(() -> {
            if (user == null) return;
            isAdminer = user.getType() == User.USER_TYPE_ADMIN;
            //如果是管理员，则显示右下角的加号按钮
            mFabUpload.setVisibility(user.getType() == User.USER_TYPE_ADMIN ? View.VISIBLE : View.GONE);
        }));
        // 从数据库加载所有的题目，并显示
        mQuestionOperation.queryAll(questions -> {
            // 加载成功，question - 获取的数据 List<Question>
            mVgLoading.setVisibility(View.GONE);
            mAdapter.setQuestions(questions);
        }, throwable -> {
            // 加载失败
            mVgLoading.setVisibility(View.GONE);
//            Toast.makeText(this, "加载数据失败", Toast.LENGTH_SHORT).show();
        });
    }

    private void turn2UploadActivity() {
        Intent intent = UploadActivity.newIntent(getContext());
        startActivityForResult(intent, UploadActivity.REQUEST_CODE_UPLOAD);
    }

    private void turn2EvaluatorActivity(Question question) {
        Intent intent = EvaluatorActivity.newIntent(getContext(), question);
        startActivityForResult(intent, Config.MAIN_CODE);
    }

    // 使用 startActivityForResult 启动 Activity 并返回的时候调用
    // 作用就是 UploadActivity 添加了新的数据，返回之后要刷新页面
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UploadActivity.REQUEST_CODE_UPLOAD) {
            if (resultCode == UploadActivity.RESULT_CODE_UPLOAD) {
                mVgLoading.setVisibility(View.VISIBLE);
                refresh();
            }
        } else if (requestCode == Config.MAIN_CODE) {
            if (resultCode == Config.EVALUATOR_CODE) {
                mVgLoading.setVisibility(View.VISIBLE);
                refresh();
            }
        }

    }
}
