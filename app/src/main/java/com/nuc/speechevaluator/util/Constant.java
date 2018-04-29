package com.nuc.speechevaluator.util;

/**
 * 不要修改！！！
 */
public class Constant {

    //---Question Language Type
    // 中文、英文
    public static final int QUESTION_LANG_TYPE_CHINESE = 0;   // 中文试题
    public static final int QUESTION_LANG_TYPE_ENGLISH = 1; // 英文试题

    //---Question Type
    // （单字，汉语专有）、（词语）、（句子）、（篇章）
    public static final int QUESTION_TYPE_SYLLABLE = 0;   // 单个汉字的测评
    public static final int QUESTION_TYPE_WORD = 1;   // 词语
    public static final int QUESTION_TYPE_SENTENCE = 2;   //句子
    public static final int QUESTION_TYPE_CHAPTER = 3;    // 篇章


    //--- Param
    //----- language
    public static final String EN_US = "en_us";
    public static final String ZH_CN = "zh_cn";

    //----- category
    public static final String READ_SYLLABLE = "read_syllable";
    public static final String READ_WORD = "read_word";
    public static final String READ_SENTENCE = "read_sentence";
    public static final String READ_CHAPTER = "read_chapter";

    //----- text_encoding
    public static final String UTF_8 = "utf-8";

    //----- result_level
    public static final String COMPLETE = "complete";

}
