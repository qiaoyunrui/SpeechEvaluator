package com.nuc.speechevaluator.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.adapter.CategoryAdapter;
import com.nuc.speechevaluator.db.bean.Category;
import com.nuc.speechevaluator.db.impl.CategoryImpl;
import com.nuc.speechevaluator.db.operation.CategoryOperation;
import com.nuc.speechevaluator.util.CategoryListHandler;
import com.nuc.speechevaluator.util.Config;

public class CategoryFragment extends Fragment {

    private View rootView;
    private RecyclerView mRecyclerView;
    private Button mBtnAdd;
    private CategoryAdapter mAdapter;
    private CategoryListHandler mCategoryHandler;
    private CategoryOperation mOperation;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private AlertDialog mDialog;
    private EditText mEtInput;
    private Toolbar mToolBar;
    private QuestionFragment mQuestionFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        initView();
        initEvent();
        initEvent();
        initData();
        initFragment();
        return rootView;
    }

    private void initView() {
        mRecyclerView = rootView.findViewById(R.id.rv_category_fragment_list);
        mCategoryHandler = new CategoryListHandler(getContext(), mRecyclerView);
        mCategoryHandler.handle();
        mBtnAdd = mCategoryHandler.getAdd();
        mBtnAdd.setText("添加");
        mAdapter = mCategoryHandler.getAdapter();
        mToolBar = rootView.findViewById(R.id.tb_common);
        mToolBar.setTitle(R.string.category);
        mOperation = new CategoryImpl();
        View inputWrapper = LayoutInflater.from(getContext()).inflate(R.layout.view_category_input, null, false);
        mDialog = new AlertDialog.Builder(getContext())
                .setView(inputWrapper)
                .setTitle("新的标题")
                .setIcon(R.drawable.ic_category)
                .setPositiveButton("确定", (dialog, which) -> {
                    // 隐藏输入法
                    hideSoftInput();
                    add();  // 向数据库添加新的类别
                })
                .setNegativeButton("取消", (dialog, which) -> hideSoftInput())
                .create();
        mEtInput = inputWrapper.findViewById(R.id.et_category_input);
    }

    private void initEvent() {
        mBtnAdd.setOnClickListener(v -> {
            mDialog.show();
        });
        mAdapter.setClosure(category -> {
            if (category == null) return;
            mQuestionFragment.refresh(category.getId());
        });
    }

    private void initData() {
        refresh();
    }

    private void initFragment() {
        mQuestionFragment = new QuestionFragment();
        getChildFragmentManager().beginTransaction()
                .add(R.id.vg_category_fragment_wrapper, mQuestionFragment)
                .commit();
    }

    /**
     * 添加类别
     * 显示对话框
     */
    private void add() {
        if (mEtInput == null) {
            Toast.makeText(getContext(), "出现了一点问题", Toast.LENGTH_SHORT).show();
            return;
        }
        String content = mEtInput.getText().toString().trim();
        mEtInput.setText("");
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "类别名称不可为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        Category category = Category.newCategory(content);
        mOperation.add(category, aVoid -> {
            Toast.makeText(getContext(), "添加类别成功！", Toast.LENGTH_SHORT).show();
            refresh();
        }, throwable -> Toast.makeText(getContext(), "添加类别失败！", Toast.LENGTH_SHORT).show());
    }

    private void refresh() {
        mOperation.queryAll(categories -> {
            // 加载成功，question - 获取的数据 List<Question>
            mHandler.post(() ->
                    mAdapter.setCategorys(categories));
        }, throwable -> {
            throwable.printStackTrace();
            // 加载失败
//            Toast.makeText(this, "加载类别信息失败", Toast.LENGTH_SHORT).show();
        });
    }

    //隐藏输入法
    private void hideSoftInput() {
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(
                        getActivity().getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
