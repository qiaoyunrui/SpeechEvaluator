package com.nuc.speechevaluator.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.db.UserService;
import com.nuc.speechevaluator.db.bean.Question;
import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.db.impl.QuestionImpl;
import com.nuc.speechevaluator.db.operation.QuestionOperation;
import com.nuc.speechevaluator.util.Closure;
import com.nuc.speechevaluator.util.Constant;

import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    private static final String TAG = "UploadActivity";

    private static final int DEFAULT_LANGUAGE_TYPE = R.id.rb_english;
    private static final int DEFAULT_QUESTION_TYPE = R.id.rb_word;

    public static final int REQUEST_CODE_UPLOAD = 0x100;
    public static final int RESULT_CODE_UPLOAD = 0x200;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, UploadActivity.class);
        return intent;
    }

    private RadioGroup mRgLanguage;
    private RadioGroup mRgType;
    private EditText mEtContent;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private Button mBtnUpload;
    // Loading 界面
    private ViewGroup mVgLoading;

    private Map<Integer, Integer> mTypeMap = new ArrayMap<>();

    private QuestionOperation mOperation = new QuestionImpl();

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mRgLanguage = findViewById(R.id.rg_upload_language);
        mRgType = findViewById(R.id.rg_upload_type);
        mEtContent = findViewById(R.id.et_upload_content);
        mToolbar = findViewById(R.id.tb_common);
        mBtnUpload = findViewById(R.id.btn_upload_upload);
        mVgLoading = findViewById(R.id.vg_loading);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(getString(R.string.upload_topic));
        // 设置默认选中的 RadioButton
        mRgLanguage.check(DEFAULT_LANGUAGE_TYPE);
        mRgType.check(DEFAULT_QUESTION_TYPE);
        notifyLanguageChange(mRgLanguage.getCheckedRadioButtonId());
    }

    private void initEvent() {
        // 切换语言时调用
        mRgLanguage.setOnCheckedChangeListener((group, checkedId) -> notifyLanguageChange(checkedId));
        // 点击上传按钮
        mBtnUpload.setOnClickListener(v -> upload());
    }

    private void initData() {
        mTypeMap.put(R.id.rb_chinese, Constant.QUESTION_LANG_TYPE_CHINESE);
        mTypeMap.put(R.id.rb_english, Constant.QUESTION_LANG_TYPE_ENGLISH);
        mTypeMap.put(R.id.rb_syllable, Constant.QUESTION_TYPE_SYLLABLE);
        mTypeMap.put(R.id.rb_word, Constant.QUESTION_TYPE_WORD);
        mTypeMap.put(R.id.rb_sentence, Constant.QUESTION_TYPE_SENTENCE);
        mTypeMap.put(R.id.rb_chapter, Constant.QUESTION_TYPE_CHAPTER);
    }

    private void upload() {
        mVgLoading.setVisibility(View.VISIBLE);
        UserService.getInstance(this)
                .getCurrentUser(user -> runOnUiThread(() -> {
//                    Log.i(TAG, "upload: " + user);
//                    Log.i(TAG, "upload: " + Thread.currentThread().getName());
                    if (user == null) {
                        Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
                        mVgLoading.setVisibility(View.GONE);
                        return;
                    }
//                    Log.i(TAG, "upload: 是否有权限？" + (user.getType() == User.USER_TYPE_ADMIN));
                    if (user.getType() != User.USER_TYPE_ADMIN) {
                        Log.i(TAG, "upload: 上传失败");
                        Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                        mVgLoading.setVisibility(View.GONE);
                        return;
                    }
                    Question question = Question.createQuestion(user.getId());
                    question.setLanguageType(mTypeMap.get(mRgLanguage.getCheckedRadioButtonId()))
                            .setQuestionType(mTypeMap.get(mRgType.getCheckedRadioButtonId()))
                            .setContent(mEtContent.getText().toString().trim());
                    if (TextUtils.isEmpty(question.getContent())) {
                        mVgLoading.setVisibility(View.GONE);
                        Toast.makeText(this, "题目内容不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mOperation.add(question, aVoid -> {
                        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
                        mVgLoading.setVisibility(View.GONE);
                        setResult(RESULT_CODE_UPLOAD);
                        finish();
                    }, throwable -> {
                        throwable.printStackTrace();
                        Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
                        mVgLoading.setVisibility(View.GONE);
                    });
                }));

    }

    /**
     * 语言变换的时候调用
     * 用于修改【汉字】的点击性
     */
    private void notifyLanguageChange(int id) {
        // 汉字 RadioButton
        RadioButton radioButton = (RadioButton) mRgType.getChildAt(Constant.QUESTION_TYPE_SYLLABLE);
        switch (id) {
            case R.id.rb_chinese:
                // 切换为中文，设置【汉字】可点击
                radioButton.setEnabled(true);
                break;
            case R.id.rb_english:
                // 切换为英文，设置【汉字】不可点击
                radioButton.setEnabled(false);
                // 当前选择的类型为【汉字】，修改为默认类型
                if (mRgType.getCheckedRadioButtonId() == R.id.rb_syllable) {
                    mRgType.check(DEFAULT_QUESTION_TYPE);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

