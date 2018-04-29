package com.nuc.speechevaluator.util;

import android.content.Context;
import android.util.ArrayMap;

import com.nuc.speechevaluator.R;

public class DataManager {

    private static final ArrayMap<Integer, String> mLanguageMap = new ArrayMap<>();
    private static final ArrayMap<Integer, String> mTypeMap = new ArrayMap<>();

    private static final ArrayMap<Integer, String> mLanguageParam = new ArrayMap<>();
    private static final ArrayMap<Integer, String> mTypeParam = new ArrayMap<>();

    public static void init(Context context) {
        mLanguageMap.put(Constant.QUESTION_LANG_TYPE_CHINESE, context.getString(R.string.chinese));
        mLanguageMap.put(Constant.QUESTION_LANG_TYPE_ENGLISH, context.getString(R.string.english));

        mTypeMap.put(Constant.QUESTION_TYPE_SYLLABLE, context.getString(R.string.syllable));
        mTypeMap.put(Constant.QUESTION_TYPE_WORD, context.getString(R.string.word));
        mTypeMap.put(Constant.QUESTION_TYPE_SENTENCE, context.getString(R.string.sentence));
        mTypeMap.put(Constant.QUESTION_TYPE_CHAPTER, context.getString(R.string.chapter));

        mLanguageParam.put(Constant.QUESTION_LANG_TYPE_CHINESE, Constant.ZH_CN);
        mLanguageParam.put(Constant.QUESTION_LANG_TYPE_ENGLISH, Constant.EN_US);

        mTypeParam.put(Constant.QUESTION_TYPE_SYLLABLE, Constant.READ_SYLLABLE);
        mTypeParam.put(Constant.QUESTION_TYPE_WORD, Constant.READ_WORD);
        mTypeParam.put(Constant.QUESTION_TYPE_SENTENCE, Constant.READ_SENTENCE);
        mTypeParam.put(Constant.QUESTION_TYPE_CHAPTER, Constant.READ_CHAPTER);

    }

    public static String getLanguage(int key) {
        return mLanguageMap.get(key);
    }

    public static String getType(int key) {
        return mTypeMap.get(key);
    }

    public static String getLanguageParam(int key) {
        return mLanguageParam.get(key);
    }

    public static String getTypeParam(int key) {
        return mTypeParam.get(key);
    }
}
