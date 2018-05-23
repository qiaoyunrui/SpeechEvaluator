package com.nuc.speechevaluator.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.db.bean.Question;
import com.nuc.speechevaluator.util.Closure;
import com.nuc.speechevaluator.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> mQuestions = new ArrayList<>();
    private Closure<Question> mClosure;
    private Closure<Boolean> mEmptyClosure;

    public void setQuestions(List<Question> questions) {
        if (questions == null) return;
        mQuestions.clear();
        mQuestions.addAll(questions);
        notifyDataSetChanged();
    }

    public void setItemClickListener(Closure<Question> closure) {
        this.mClosure = closure;
    }

    public void setEmptyClosure(Closure<Boolean> closure) {
        this.mEmptyClosure = closure;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = mQuestions.get(position);
        if (question == null) {
            return;
        }
        holder.mTvContent.setText(question.getContent());
        holder.mTvDate.setText(DateUtil.getFormatDate(question.getDate()));
        holder.itemView.setOnClickListener(v -> {
            if (mClosure != null) {
                mClosure.invoke(question);
            }
        });
    }


    @Override
    public int getItemCount() {
        int size = mQuestions.size();
        if (mEmptyClosure != null) {
            mEmptyClosure.invoke(size == 0);
        }
        return size;
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvContent;
        private TextView mTvDate;

        QuestionViewHolder(View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tv_item_question_content);
            mTvDate = itemView.findViewById(R.id.tv_item_question_date);
        }
    }

}
