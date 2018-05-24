package com.nuc.speechevaluator.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;
import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.db.bean.Question;
import com.nuc.speechevaluator.db.impl.UserImpl;
import com.nuc.speechevaluator.db.operation.UserOperation;
import com.nuc.speechevaluator.fragment.ResultFragment;
import com.nuc.speechevaluator.util.Constant;
import com.nuc.speechevaluator.util.DataManager;

/**
 * 评测页面
 */
public class EvaluatorActivity extends AppCompatActivity {

    private static final String TAG = "EvaluatorActivity";

    private static final String KEY_QUESTION = "key_question";

    public static final String KEY_RESULT = "key_result";

    public static Intent newIntent(Context context, Question question) {
        Intent intent = new Intent(context, EvaluatorActivity.class);
        intent.putExtra(KEY_QUESTION, question);
        return intent;
    }

    private Question mQuestion;

    private TextView mTvLanguage;
    private TextView mTvType;
    private TextView mTvContent;
    private TextView mTvOwner;      //显示出题人的用户名
    private TextView mTvHint;
    private FloatingActionButton mFabEvaluator;

    private UserOperation mUserOperation;

    private Toolbar mToolbar;
    private ActionBar mActionBar;

    private ResultFragment mResultFragment;

    private SpeechEvaluator mIse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluator);
        mUserOperation = new UserImpl();
        mIse = SpeechEvaluator.createEvaluator(EvaluatorActivity.this, null);
        initView();
        initEvent();
        initData();
        initFragment();
        if (mIse == null) {
            showTip("创建对象失败");
        }
        requestPermissions();
    }

    private void initView() {
        mTvLanguage = findViewById(R.id.tv_evaluator_language);
        mTvContent = findViewById(R.id.tv_evaluator_content);
        mTvType = findViewById(R.id.tv_evaluator_type);
        mTvOwner = findViewById(R.id.tv_evaluator_owner_username);
        mFabEvaluator = findViewById(R.id.fab_evaluator_evaluator);
        mTvHint = findViewById(R.id.tv_evaluator_hint);
        mToolbar = findViewById(R.id.tb_common);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        // 显示返回按钮
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(getString(R.string.evaluator));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        mFabEvaluator.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // fab 变大 1.2 倍
                    mFabEvaluator.setScaleX(1.2f);
                    mFabEvaluator.setScaleY(1.2f);
                    startEvaluator();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mFabEvaluator.setScaleX(1.0f);
                    mFabEvaluator.setScaleY(1.0f);
                    endEvaluator();
                    break;
            }
            return false;
        });
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mQuestion = (Question) intent.getSerializableExtra(KEY_QUESTION);
        }
        if (mQuestion != null) {
            mTvType.setText(DataManager.getType(mQuestion.getQuestionType()));
            mTvLanguage.setText(DataManager.getLanguage(mQuestion.getLanguageType()));
            mTvContent.setText(mQuestion.getContent());
            // 根据用户 id 获取用户名
            mUserOperation.query(mQuestion.getOwnerId(), user -> {
                if (user != null) {
                    mTvOwner.setText(user.getUsername());
                }
            }, null);
        }
    }


    private void initFragment() {
        mResultFragment = new ResultFragment();
    }

    // 显示评测结果页面
    // 显示 Fragment
    public void openResultFragment(String result) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_RESULT, result);
            mResultFragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.vg_evaluator_fragment, mResultFragment)
                    .addToBackStack("view")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    // 评测监听接口
    private EvaluatorListener mEvaluatorListener = new EvaluatorListener() {

        @Override
        public void onResult(EvaluatorResult result, boolean isLast) {
            Log.d(TAG, "evaluator result :" + isLast);

            if (isLast) {
                StringBuilder builder = new StringBuilder();
                builder.append(result.getResultString());
                Log.i(TAG, "onResult: 评测结果：" + builder.toString());
//                showTip("评测结束");
                mTvHint.setText("评测结束");
                openResultFragment(builder.toString());
            }
        }

        @Override
        public void onError(SpeechError error) {
            if (error != null) {
                mTvHint.setText("评测失败");
                Log.i(TAG, "onError: " + error.getErrorCode() + "," + error.getErrorDescription());
            } else {
                Log.d(TAG, "evaluator over");
            }
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.d(TAG, "evaluator begin");
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Log.d(TAG, "evaluator stop");
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            showTip("当前音量：" + volume);
            mTvHint.setText("当前音量：" + volume);
            Log.d(TAG, "返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }

    };

    /**
     * 开始测评
     */
    private void startEvaluator() {
        if (mIse == null || mQuestion == null) {
            showTip("初始化失败");
            return;
        }
        mTvHint.setText("评测中...");
        setParams();
        // 开始评测
        int result = mIse.startEvaluating(mQuestion.getContentParam(), null, mEvaluatorListener);
        Log.i(TAG, "startEvaluator: Result Code: " + result);
    }

    /**
     * 结束测评
     */
    private void endEvaluator() {
        if (mIse.isEvaluating()) {
            mTvHint.setHint("评测已停止，等待结果中...");
            mIse.stopEvaluating();
        }
    }

    // 设置评测参数
    private void setParams() {
        // 设置评测语言
        String language = DataManager.getLanguageParam(mQuestion.getLanguageType());    // 0 - zh_ch
        // 设置需要评测的类型
        String category = DataManager.getTypeParam(mQuestion.getQuestionType());
        // 设置结果等级（中文仅支持complete）
        String result_level = Constant.COMPLETE;
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        String vad_bos = "5000";
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        String vad_eos = "1800";
        // 语音输入超时时间，即用户最多可以连续说多长时间；
        String speech_timeout = "-1";

        mIse.setParameter(SpeechConstant.LANGUAGE, language);
        mIse.setParameter(SpeechConstant.ISE_CATEGORY, category);
        mIse.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        mIse.setParameter(SpeechConstant.VAD_BOS, vad_bos);
        mIse.setParameter(SpeechConstant.VAD_EOS, vad_eos);
        mIse.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, speech_timeout);
        mIse.setParameter(SpeechConstant.RESULT_LEVEL, result_level);

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIse.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise.wav";
        Log.i(TAG, "setParams: " + path);
        mIse.setParameter(SpeechConstant.ISE_AUDIO_PATH, path);
        //通过writeaudio方式直接写入音频时才需要此设置
        //mIse.setParameter(SpeechConstant.AUDIO_SOURCE,"-1");
    }

    private void showTip(String string) {
        if (!TextUtils.isEmpty(string)) {
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.LOCATION_HARDWARE, Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS}, 0x0010);
                }

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, 0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 监听返回按钮
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
