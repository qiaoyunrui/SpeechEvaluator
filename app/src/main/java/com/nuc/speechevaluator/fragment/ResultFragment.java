package com.nuc.speechevaluator.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.activity.EvaluatorActivity;
import com.nuc.speechevaluator.ise.result.Result;
import com.nuc.speechevaluator.ise.result.xml.XmlResultParser;

/**
 * 显示评测结果
 */
public class ResultFragment extends Fragment {

    private View mRootView;
    private TextView mTvContent;
    private String mResult; // 返回的结果字符串


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_result, container, false);
        mTvContent = mRootView.findViewById(R.id.tv_result_content);
        initEvent();
        initData();
        return mRootView;
    }

    private void initEvent() {

    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mResult = bundle.getString(EvaluatorActivity.KEY_RESULT, "");
        } else {
            mResult = "";
        }
        showResult();
    }

    public void setResult(String result) {
        this.mResult = result;
        showResult();
    }

    private void showResult() {
        if (TextUtils.isEmpty(mResult)) {
            mTvContent.setText("数据为空");
            return;
        }
        XmlResultParser resultParser = new XmlResultParser();
        Result result = resultParser.parse(mResult);

        if (null != result) {
            mTvContent.setText(result.toString());
        } else {
            Toast.makeText(getContext(), "解析错误", Toast.LENGTH_SHORT).show();
        }
    }

}
