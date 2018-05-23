package com.nuc.speechevaluator;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nuc.speechevaluator.util.DataManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + getString(R.string.app_id));
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .name("speech_evaluator.realm")
                .schemaVersion(2)
                .build();
        Realm.setDefaultConfiguration(config);
        DataManager.init(this);
    }
}
