package com.nuc.speechevaluator.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.db.bean.Category;
import com.nuc.speechevaluator.util.Closure;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private static final int ITEM_TYPE_HEADER = 0x01;
    private static final int ITEM_TYPE_NORMAL = 0x02;

    private int mSelectingPosition = -1;

    private List<Category> mList = new ArrayList<>();
    private View mHeaderView;
    private Closure<Category> mClosure;

    public CategoryAdapter() {
        mList.add(new Category());
    }

    public void setCategorys(List<Category> list) {
        if (list == null) return;
        mList.clear();
        mList.add(new Category());
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setClosure(Closure<Category> closure) {
        this.mClosure = closure;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == ITEM_TYPE_HEADER) {
            if (mHeaderView != null) {
                itemView = mHeaderView;
            } else {
                itemView = new View(parent.getContext());
            }
        } else if (viewType == ITEM_TYPE_NORMAL) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent
                    , false);
        } else {
            itemView = new View(parent.getContext());
        }
        return new CategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        if (position == mSelectingPosition) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
        Category category = mList.get(position);
        if (holder.mContent != null && category != null) {
            holder.mContent.setText(category.getTitle());
        }
        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setSelected(true);  //设置为选择状态
            notifyItemChanged(mSelectingPosition);
            mSelectingPosition = position;
            if (mClosure != null) {
                mClosure.invoke(category);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE_HEADER : ITEM_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setHeader(View header) {
        this.mHeaderView = header;
    }

    class CategoryHolder extends RecyclerView.ViewHolder {

        private TextView mContent;

        public CategoryHolder(View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.tv_item_category_content);
        }
    }

}
