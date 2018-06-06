package com.nuc.speechevaluator.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.adapter.CategoryAdapter;
import com.nuc.speechevaluator.db.bean.Category;
import com.nuc.speechevaluator.db.impl.CategoryImpl;
import com.nuc.speechevaluator.db.operation.CategoryOperation;
import com.nuc.speechevaluator.util.CategoryListHandler;
import com.nuc.speechevaluator.util.Config;

/**
 * 分类页面，包括添加新的分类，搜索分类
 */
public class CategoryActivity extends AppCompatActivity {

    private static final String TAG = "CategoryActivity";

    private CategoryOperation mOperation = new CategoryImpl();
    private RecyclerView mRecyclerView;
    private Button mAdd;
    private EditText mEtSearch;
    private ImageView mIvBack;
    private ImageView mIvClose;
    private CategoryListHandler mListHandler;
    private CategoryAdapter mAdapter;
    private AlertDialog mDialog;
    private EditText mEtInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.rv_category_list);
        mListHandler = new CategoryListHandler(this, mRecyclerView);
        mListHandler.handle();
        mAdd = mListHandler.getAdd();
        mAdapter = mListHandler.getAdapter();
        mEtSearch = findViewById(R.id.et_search_content);
        mIvBack = findViewById(R.id.iv_search_back);
        mIvClose = findViewById(R.id.iv_search_close);
        View inputWrapper = LayoutInflater.from(this).inflate(R.layout.view_category_input, null, false);
        // 添加分类的对话框
        mDialog = new AlertDialog.Builder(this)
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

    //隐藏输入法
    private void hideSoftInput() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(
                        getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initEvent() {
        mAdd.setOnClickListener(v -> mDialog.show());
        mIvBack.setOnClickListener(v -> onBackPressed());
        mIvClose.setOnClickListener(v -> {
            mEtSearch.setText("");
            refresh();
        });
        mEtSearch.setOnEditorActionListener((v, actionId, event) -> {   // 点击输入法右下角的搜索按钮时调用
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 先隐藏键盘
                hideSoftInput();
                search();
                return true;
            }
            return false;
        });
        // 点击分类 Item
        mAdapter.setClosure(category -> {
            Intent intent = new Intent();
            intent.putExtra(Config.KEY_CATEGORY, category);
            setResult(Config.CATEGORY_CODE, intent);
            finish();
        });
    }

    private void initData() {
        //加载所有的类别信息
        refresh();
    }

    /**
     * 搜索分类，然后显示在
     */
    private void search() {
        mOperation.queryLikeTitle(mEtSearch.getText().toString().trim(), categories -> {
            // 加载成功，question - 获取的数据 List<Question>
            Log.i(TAG, "refresh: " + categories);
            runOnUiThread(() ->
                    mAdapter.setCategorys(categories));
        }, throwable -> {
            throwable.printStackTrace();
            // 加载失败
//            Toast.makeText(this, "加载类别信息失败", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * 添加类别
     * 显示对话框
     */
    private void add() {
        if (mEtInput == null) {
            Toast.makeText(this, "出现了一点问题", Toast.LENGTH_SHORT).show();
            return;
        }
        String content = mEtInput.getText().toString().trim();
        mEtInput.setText("");
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "类别名称不可为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        Category category = Category.newCategory(content);
        mOperation.add(category, aVoid -> {
            Toast.makeText(this, "添加类别成功！", Toast.LENGTH_SHORT).show();
            refresh();
        }, throwable -> Toast.makeText(this, "添加类别失败！", Toast.LENGTH_SHORT).show());
    }

    /**
     * 获取所有类别数据
     */
    private void refresh() {
        mOperation.queryAll(categories -> {
            // 加载成功，question - 获取的数据 List<Question>
            runOnUiThread(() ->
                    mAdapter.setCategorys(categories));
        }, throwable -> {
            throwable.printStackTrace();
            // 加载失败
//            Toast.makeText(this, "加载类别信息失败", Toast.LENGTH_SHORT).show();
        });
    }

}
