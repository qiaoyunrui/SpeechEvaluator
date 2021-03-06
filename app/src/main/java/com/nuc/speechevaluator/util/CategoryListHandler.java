package com.nuc.speechevaluator.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.adapter.CategoryAdapter;
import com.nuc.speechevaluator.db.UserService;
import com.nuc.speechevaluator.db.bean.Category;
import com.nuc.speechevaluator.db.bean.User;

public class CategoryListHandler {

    private static final String TAG = "CategoryListHandler";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CategoryAdapter mAdapter;
    private Button mAdd;    // Header
    private Context mContext;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public CategoryListHandler(Context context, RecyclerView recyclerView) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;
    }

    public void handle() {
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CategoryAdapter();
        mRecyclerView.setAdapter(mAdapter);
        View header = LayoutInflater.from(mContext).inflate(R.layout.view_add_category, null);
        mAdd = header.findViewById(R.id.btn_add_category);
        mAdapter.setHeader(header);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public CategoryAdapter getAdapter() {
        return mAdapter;
    }

    public Button getAdd() {
        return mAdd;
    }
}
